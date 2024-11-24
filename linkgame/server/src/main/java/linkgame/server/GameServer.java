package linkgame.server;

import linkgame.common.Message;
import linkgame.common.MessageType;
import linkgame.common.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-22 7:21
 */
@Slf4j
public class GameServer {
    private static final int PORT = 12345;
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    private static final Queue<ClientHandler> waitingClients = new LinkedList<>();
    private static final Map<String, ClientHandler> pickingClients = new HashMap<>();
    private static final String DISCONNECTION_LOG_FILE = "disconnection.log";

    public static void main(String[] args) {
        log.info("游戏服务器正在运行，监听端口：{}", PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                log.info("新客户端连接：{}:{}", clientSocket.getInetAddress(), clientSocket.getPort());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                threadPool.submit(clientHandler);
            }
        } catch (IOException e) {
            log.error("服务器发生错误：", e);
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket socket;
        private ClientHandler opponent;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        private Game sharedGame;

        private String clientId;

        private Integer score = 0;

        private boolean gameOver = false;

        private boolean normalClose = false;

        private String userId, nickname, avatar;

        int[] boardSize;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            this.clientId = "Client-" + socket.getPort();// 客户端标识id
        }

        @Override
        public void run() {
            try {
                log.info("开始处理客户端：{}", clientId);
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());

                // 接收棋初始化信息
                Map<?, ?> initMessage = (Map<?, ?>) in.readObject();
                userId = (String) initMessage.get("userId");
                boardSize = (int[]) initMessage.get("boardSize");
                nickname = (String) initMessage.get("nickname");
                avatar = (String) initMessage.get("avatar");
                boolean isRandom = (boolean) initMessage.get("isRandom");
                log.info("{} 提供了棋盘大小：{}", clientId, Arrays.toString(boardSize));

                if (isRandom) {
                    handleRandomStart();
                } else {
                    handlePickStart();
                }

                // 游戏交互逻辑
                while (true) {
                    Message message = (Message) in.readObject();
                    log.info("{} 发送消息：{}", clientId, message);
                    if (message.getType() == MessageType.PICK_OPPONENT) {
                        String opponentId = (String) message.getData().get("opponentId");
                        opponent = pickingClients.get(opponentId);
                        pickingClients.remove(opponentId);
                        informPickList();
                        opponent.opponent = this;
                        handleInit();
                    } else if (opponent != null) {
                        if (message.getType() == MessageType.PICK) {
                            opponent.sendMessage(message);
                            log.info("消息已转发给 {}", opponent.clientId);
                        } else if (message.getType() == MessageType.JUDGE) {
                            handleJudge(message);
                        } else if (message.getType() == MessageType.TIMEOUT) {
                            Message m = new Message(MessageType.TIMEOUT, null);
                            sendMessage(m);
                            opponent.sendMessage(m);
                            log.info("已向 {} 和 {} 发送超时消息", clientId, opponent.clientId);
                        }
                    } else {
                        log.warn("{} 尚未匹配到对手", clientId);
                    }
                }
            } catch (IOException e) {
                log.error("与 {} 通信时发生错误：", clientId, e);
                notifyOpponentDisconnected(); // 通知对手断开连接
            } catch (ClassNotFoundException e) {
                log.error("无法识别的消息类型：", e);
            } catch (InterruptedException e) {
                log.error("线程被中断：", e);
                notifyOpponentDisconnected(); // 通知对手断开连接
            } finally {
                close();
            }
        }

        private void handleInit() throws IOException {
            log.info("{} 与 {} 匹配成功", clientId, opponent.clientId);

            // 随机决定棋盘大小
            int[] finalBoardSize = new Random().nextBoolean() ? boardSize : opponent.boardSize;

            int[][] board;
            do {
                board = Game.SetupBoard(finalBoardSize[0], finalBoardSize[1], false);
                sharedGame = new Game(board);
            } while (sharedGame.isGameOver());

            opponent.sharedGame = sharedGame;

            sendMessage(new Message(
                    MessageType.INIT,
                    Map.of("board", board,
                            "turn", false,
                            "opponentNickname", opponent.nickname,
                            "opponentAvatar", opponent.avatar)
            ));
            opponent.sendMessage(new Message(
                    MessageType.INIT,
                    Map.of("board", board,
                            "turn", true,
                            "opponentNickname", nickname,
                            "opponentAvatar", avatar)
            ));
            log.info("已向 {} 和 {} 发送初始化消息", clientId, opponent.clientId);
        }

        private void handleRandomStart() throws IOException {
            // 客户端匹配逻辑
            synchronized (waitingClients) {
                if (!waitingClients.isEmpty()) {
                    // 从等待队列中取出一个对手
                    opponent = waitingClients.poll();
                    opponent.opponent = this;
                    handleInit();
                } else {
                    waitingClients.add(this);
                    log.info("{} 正在等待匹配...", clientId);
                    sendMessage(new Message(MessageType.WAIT, null));
                }
            }
        }

        private void handlePickStart() throws IOException {
            synchronized (pickingClients) {
                pickingClients.put(this.userId, this);
                List<String> userIds = pickingClients.keySet().stream().toList();
                for (ClientHandler client : pickingClients.values()) {
                    client.sendMessage(new Message(MessageType.LIST,
                            Map.of("userIds", userIds.stream().filter(id -> !id.equals(client.userId)).toList())
                    ));
                }
                log.info("{} 正在等待匹配...", clientId);
            }
        }

        private void handleJudge(Message message) throws IOException, InterruptedException {
            int[] pair = (int[]) message.getData().get("pair");
            boolean result = sharedGame.judge(pair[0], pair[1], pair[2], pair[3]);
            // 更新服务器棋盘
            int[] path = sharedGame.getPath();
            if (result) {
                if (path.length == 8) {
                    score += 15;
                } else if (path.length == 6) {
                    score += 10;
                } else {
                    score += 5;
                }

                sharedGame.board[path[0]][path[1]] = 0;
                sharedGame.board[path[path.length - 2]][path[path.length - 1]] = 0;

                // 画线
                Message lineMessage = new Message(MessageType.LINE_SHOW, Map.of("path", path));
                sendMessage(lineMessage);
                opponent.sendMessage(lineMessage);
                log.info("已向 {} 和 {} 发送连线消息：{}", clientId, opponent.clientId, lineMessage);
                // 500ms后连线消失且更新客户端棋盘
                TimeUnit.MILLISECONDS.sleep(500);
                sendMessage(new Message(MessageType.LINE_DISAPPEAR, Map.of("turn", false, "score", score)));
                opponent.sendMessage(new Message(MessageType.LINE_DISAPPEAR, Map.of("turn", true, "score", score)));
                log.info("已向 {} 和 {} 发送连线消失消息以及更新分数：{}", clientId, opponent.clientId, score);
            } else {
                sendMessage(new Message(MessageType.FAIL, Map.of("path", path)));
                opponent.sendMessage(new Message(MessageType.FAIL, Map.of("path", path)));
                log.info("已向 {} 和 {} 发送交换回合消息", clientId, opponent.clientId);
            }

            // 判断游戏是否结束
            if (sharedGame.isGameOver()) {
                sendMessage(new Message(MessageType.END, Map.of("yourScore", score, "opponentScore", opponent.score)));
                opponent.sendMessage(new Message(MessageType.END, Map.of("yourScore", opponent.score, "opponentScore", score)));
                log.info("已向 {} 和 {} 发送游戏结束消息", clientId, opponent.clientId);
                gameOver = true;
                opponent.gameOver = true;
                sendRecord();
            }
        }

        private void sendRecord() {

            Map<String, String> record = Map.of(
                    "userId", userId,
                    "opponentId", opponent.userId,
                    "score", score.toString(),
                    "opponentScore", opponent.score.toString(),
                    "createAt", LocalDateTime.now().toString()
            );

            OkHttpUtils.postForm(
                    "http://localhost:8080/record",
                    record,
                    null
            );
            log.info("已向发送游戏记录 {}", record);
        }

        private void notifyOpponentDisconnected() {
            if (opponent != null && !gameOver) {
                try {
                    opponent.sendMessage(new Message(MessageType.DISCONNECTED, Map.of("message", "您的对手掉线啦!")));
                    log.info("已通知 {} 的对手掉线", opponent.clientId);
                } catch (IOException e) {
                    log.error("通知 {} 的对手掉线时发生错误：", opponent.clientId, e);
                }
            }
        }


        /**
         * 向客户端发送消息
         */
        public void sendMessage(Message message) throws IOException {
            try {
                out.writeObject(message);
                out.flush();
                log.info("已向 {} 发送消息：{}", clientId, message);
            } catch (IOException e) {
                log.error("无法向 {} 发送消息：", clientId, e);
            }
        }

        private void informPickList() throws IOException {
            synchronized (pickingClients) {
                List<String> userIds = pickingClients.keySet().stream().toList();
                for (ClientHandler client : pickingClients.values()) {
                    client.sendMessage(new Message(MessageType.LIST,
                            Map.of("userIds", userIds.stream().filter(id -> !id.equals(client.userId)).toList())
                    ));
                }
            }
        }

        /**
         * 关闭客户端连接并清理资源
         */
        private void close() {
            try {
                synchronized (waitingClients) {
                    waitingClients.remove(this);
                }
                synchronized (pickingClients) {
                    pickingClients.remove(userId);
                    informPickList();
                }

                if (socket != null) {
                    socket.close();
                }

                if (opponent.socket.isClosed()) {
                    assert socket != null;
                    if (socket.isClosed()) {
                        logBothClientsDisconnected(); // 记录双方都已断开连接的日志
                    }
                }
                log.info("已关闭与 {} 的连接", clientId);
            } catch (IOException e) {
                log.error("关闭与 {} 的连接时出错：", clientId, e);
            }
        }

        private void logBothClientsDisconnected() {
            String logMessage = String.format("[%s] 游戏中双方客户端断开连接：%s 和 %s",
                    LocalDateTime.now(),
                    clientId,
                    opponent != null ? opponent.clientId : "对手未知");

            try (FileWriter fileWriter = new FileWriter(DISCONNECTION_LOG_FILE, true);
                 PrintWriter printWriter = new PrintWriter(fileWriter)) {
                printWriter.println(logMessage);
            } catch (IOException e) {
                log.error("记录断开日志到文件时出错：", e);
            }

            log.info(logMessage);
        }
    }
}
