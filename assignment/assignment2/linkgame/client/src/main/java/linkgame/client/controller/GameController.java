package linkgame.client.controller;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import linkgame.client.ClientService;
import linkgame.common.Message;
import linkgame.common.MessageType;
import linkgame.common.OkHttpUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class GameController {

    // 头部信息
    @FXML
    private Label yourScoreLabel, opponentScoreLabel, yourNickname, opponentNickname;

    @FXML
    private ImageView yourAvatar, opponentAvatar;

    @FXML
    private ProgressBar yourProgressBar, opponentProgressBar;


    private Timeline timeline;
    private static final int TURN_DURATION = 15; // 每回合时长（秒）
    private static final int UPDATE_INTERVAL = 100; // 每100ms更新一次


    /**
     * 更新玩家得分
     */
    public void updateYourScore(int score) {
        yourScoreLabel.setText(String.valueOf(score));
    }

    public void updateOpponentScore(int score) {
        opponentScoreLabel.setText(String.valueOf(score));
    }

    /**
     * 设置用户头像
     */
    public void setYourAvatar(Image image) {
        yourAvatar.setImage(image);
    }

    public void setOpponentAvatar(Image image) {
        opponentAvatar.setImage(image);
    }

    /**
     * 设置用户昵称
     */
    public void setYourNickname(String nickname) {
        yourNickname.setText(nickname);
    }

    public void setOpponentNickname(String nickname) {
        opponentNickname.setText(nickname);
    }

    /**
     * 显示回合进度条
     */
    public void showYourProgress(boolean isVisible) {
        yourProgressBar.setVisible(isVisible);
        opponentProgressBar.setVisible(!isVisible);
    }

    public void updateYourProgress(double progress) {
        yourProgressBar.setProgress(progress);
    }

    public void updateOpponentProgress(double progress) {
        opponentProgressBar.setProgress(progress);
    }

    private void startTurnCountdown() {
        // 清除之前的倒计时
        if (timeline != null) {
            timeline.stop();
        }

        // 显示当前玩家的进度条
        showYourProgress(isYourTurn);

        // 初始化倒计时
        if (isYourTurn) {
            updateYourProgress(1); // 起始进度为1
        } else {
            updateOpponentProgress(1);
        }

        // 手动记录已过去的时间
        AtomicReference<Double> elapsed = new AtomicReference<>((double) 0);

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        // 每秒触发一次更新逻辑
        KeyFrame keyFrame = new KeyFrame(Duration.millis(UPDATE_INTERVAL), event -> {
            elapsed.updateAndGet(v -> (v + UPDATE_INTERVAL / 1000.0)); // 累积时间
//            log.info("倒计时: {}", elapsed);

            // 计算进度条的剩余进度
            double progress = Math.max(0, 1 - (elapsed.get() / TURN_DURATION));
            if (isYourTurn) {
                updateYourProgress(progress);
            } else {
                updateOpponentProgress(progress);
            }

            // 倒计时结束
            if (elapsed.get() >= TURN_DURATION) {
                timeline.stop(); // 停止计时器
                endTurnDueToTimeout(isYourTurn); // 触发回合超时逻辑
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void endTurnDueToTimeout(boolean isYourTurn) {
        if (isYourTurn) {
            clientService.sendMessage(new Message(MessageType.TIMEOUT, Map.of("timeout", true)));
        }
    }


    // 游戏区域
    @FXML
    private GridPane gameBoard;

    @FXML
    private StackPane gamePane;

    @Setter
    @Getter
    private ClientService clientService;
    @Setter
    private MainController mainController;

    private Button[][] buttons;

    private boolean isYourTurn = true;

    private final Polyline connectionLine = new Polyline(); // 连线

    private final List<Button> selectedButtons = new ArrayList<>();

    private int lastRow = -1;

    private int lastCol = -1;

    private final List<Button> opponentSelectedButtons = new ArrayList<>();

    int[][] board; // 棋盘(图片对应的数字)
    int[] path; // 连线路径

    Image cursorImage = new Image(String.valueOf(Objects.requireNonNull(getClass().getResource("/linkgame/client/cursor.png"))), 10, 10, true, true);

    @FXML
    public void initialize() {
        log.info("初始化游戏界面");
        // 初始化进度条状态
        yourProgressBar.setVisible(false);
        opponentProgressBar.setVisible(false);

        // 初始化用户信息（仅为占位，可动态更新）
        yourNickname.setText("Your Name");
        opponentNickname.setText("Opponent Name");
        yourScoreLabel.setText("0");
        opponentScoreLabel.setText("0");


        connectionLine.setStroke(Color.RED);
        connectionLine.setStrokeWidth(2);
        connectionLine.getStrokeDashArray().addAll(10.0, 5.0);
        connectionLine.setManaged(false);
        connectionLine.setMouseTransparent(true);
        gamePane.getChildren().add(connectionLine);
        connectionLine.setVisible(false);
    }

    // 显示连接线
    private void showConnectionLine(int[] path) {
        connectionLine.getPoints().clear();

        for (int i = 0; i < path.length; i += 2) {
            int row = path[i], col = path[i + 1];
            Button button = buttons[row][col];
            double x = button.getLayoutX() + button.getWidth() / 2;
            double y = button.getLayoutY() + button.getHeight() / 2;
            connectionLine.getPoints().addAll(x, y);
        }

        connectionLine.setVisible(true);
    }

    public void connectToUser(String userId) {
        log.info("连接到用户: {}", userId);
        clientService.sendMessage(
                new Message(MessageType.PICK_OPPONENT,
                        Map.of("opponentId", userId)
                )
        );
    }

    public void connectToServer(int rows, int cols, boolean isRandom, boolean isReconnect) {
        log.info("连接到服务器: {}行 {}列", rows, cols);
        clientService.connectToServer("localhost", 12345, rows, cols, isRandom, isReconnect, mainController.getUserId().toString(), mainController.getNickname(), mainController.getAvatar(), message -> {
            if (message.getType() == MessageType.LIST) {
                List<?> uncheckedUserIds = (List<?>) message.getData().get("userIds");
                List<String> userIds = new ArrayList<>();
                for (Object item : uncheckedUserIds) {
                    if (item instanceof String) {
                        userIds.add((String) item);
                    } else {
                        log.warn("非法的用户ID: {}", item);
                    }
                }
                mainController.loadUsers(userIds);
            } else if (message.getType() == MessageType.RECONNECT) {
                isReconnecting = false;
                if (message.getData() == null) {
                    isYourTurn = true;
                    reconnectDialog.close();
                    showInformMessage("对手已重连!");
                } else {
                    isYourTurn = false;
                    int[][] board = (int[][]) message.getData().get("board");
                    updateOpponentScore((Integer) (message.getData().get("opponentScore")));
                    setOpponentNickname((String) message.getData().get("opponentNickname"));
                    setOpponentAvatar(new Image((String) message.getData().get("opponentAvatar")));

                    updateYourScore((Integer) (message.getData().get("score")));
                    setYourNickname(mainController.getNickname());
                    setYourAvatar(new Image(mainController.getAvatar()));

                    loadGameScene(board);
                    mainController.showGamePage();
                    showInformMessage("重连成功，对手回合!");
                }
                startTurnCountdown();
            } else if (message.getType() == MessageType.RECONNECT_FAIL) {
                mainController.closeConnection();
                mainController.showMainPage();
            } else if (message.getType() == MessageType.INIT) {
                int[][] board = (int[][]) message.getData().get("board");

                // 匹配成功，初始化游戏界面
                String opponentNickname = (String) message.getData().get("opponentNickname");
                String opponentAvatar = (String) message.getData().get("opponentAvatar");
                updateYourScore(0);
                updateOpponentScore(0);
                setYourNickname(mainController.getNickname());
                setOpponentNickname(opponentNickname);
                setYourAvatar(new Image(mainController.getAvatar()));
                setOpponentAvatar(new Image(opponentAvatar));

                loadGameScene(board);
                mainController.showGamePage();
                isYourTurn = (boolean) message.getData().get("turn");
                showInformMessage(isYourTurn ? "匹配成功! 你先手" : "匹配成功! 对手先手");
                startTurnCountdown();
            } else if (message.getType() == MessageType.PICK) {
                int[] picked = (int[]) message.getData().get("pick");
                handleOpponentSelectedButton(buttons[picked[0]][picked[1]]);
            } else if (message.getType() == MessageType.LINE_SHOW) {
                path = (int[]) message.getData().get("path");
                showConnectionLine(path);
            } else if (message.getType() == MessageType.LINE_DISAPPEAR) {
                connectionLine.setVisible(false);

                if (isYourTurn) {
                    updateYourScore((Integer) message.getData().get("score"));
                } else {
                    updateOpponentScore((Integer) message.getData().get("score"));
                }
                isYourTurn = (boolean) message.getData().get("turn");
                // 更新棋盘
                board[path[0]][path[1]] = 0;
                board[path[path.length - 2]][path[path.length - 1]] = 0;
                loadGameScene(board);
                selectedButtons.clear();
                opponentSelectedButtons.clear();
                if (isYourTurn) {
                    showInformMessage("轮到你了!");
                } else {
                    showInformMessage("消除成功!");
                }
                startTurnCountdown();
            } else if (message.getType() == MessageType.FAIL) {
                path = (int[]) message.getData().get("path");
                if (isYourTurn) {
                    showMessage("消除失败! 交换回合");
                }
                isYourTurn = !isYourTurn;
                loadGameScene(board);
                selectedButtons.clear();
                opponentSelectedButtons.clear();
                startTurnCountdown();
            } else if (message.getType() == MessageType.TIMEOUT) {
                isYourTurn = !isYourTurn;
                if (isYourTurn) {
                    showInformMessage("对手超时，轮到你啦!");
                } else {
                    showMessage("超时啦! 交换回合!");
                }
                loadGameScene(board);
                selectedButtons.clear();
                opponentSelectedButtons.clear();
                startTurnCountdown();
            } else if (message.getType() == MessageType.END) {
                int yourScore = (Integer) message.getData().get("yourScore");
                int opponentScore = (Integer) message.getData().get("opponentScore");

                showGameOverDialog(yourScore, opponentScore);
            } else if (message.getType() == MessageType.WAIT_RECONNECT) {
                showReconnectDialog();
            }
        });
    }

    private void handleOpponentSelectedButton(Button button) {
        if (opponentSelectedButtons.contains(button)) {
            opponentSelectedButtons.remove(button);
            button.setStyle("-fx-background-color: #ffffff; -fx-border-color: transparent;");
            button.setEffect(null);
            button.setTranslateY(0);
        } else {
            opponentSelectedButtons.add(button);
            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.GRAY);
            shadow.setRadius(10);
            shadow.setOffsetX(5);
            shadow.setOffsetY(5);

            button.setEffect(shadow);
            button.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #FFB6C1, #FF69B4);" + // 渐变背景
                            "-fx-border-color: #FF1493;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 10px;" +
                            "-fx-border-insets: -2;" +
                            "-fx-background-radius: 10px;"

            );
            button.setTranslateY(-4);
        }
    }

    public void loadGameScene(int[][] board) {
        this.board = board;
        gameBoard.getChildren().clear();
        buttons = new Button[board.length][board[0].length];

        double buttonSize = 40;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Button button = new Button();
                button.setPrefSize(buttonSize, buttonSize);
                button.setStyle("-fx-background-color: #ffffff; -fx-border-color: transparent;");

                ImageView imageView = addContent(board[row][col]);
                if (imageView != null) {
                    imageView.setFitWidth(buttonSize * 0.8);
                    imageView.setFitHeight(buttonSize * 0.8);
                    imageView.setPreserveRatio(true);
                    button.setGraphic(imageView);
                }

                if (board[row][col] == 0) {
                    button.setDisable(true);
                } else {
                    addHoverEffect(button);
                }

                int finalRow = row;
                int finalCol = col;
                button.setOnAction(event -> handleButtonPress(button, finalRow, finalCol));

                buttons[row][col] = button;
                gameBoard.add(button, col, row);
            }
        }
        log.info("游戏页面加载完成: {}行 {}列", board.length, board[0].length);
    }

    private void handleButtonPress(Button button, int row, int col) {
        if (isYourTurn) {
            if (selectedButtons.contains(button)) {
                selectedButtons.remove(button);
                button.setStyle("-fx-background-color: #ffffff; -fx-border-color: transparent;");
                button.setEffect(null);
                button.setTranslateY(0);
                clientService.sendMessage(new Message(MessageType.PICK, Map.of("pick", new int[]{row, col})));
            } else if (selectedButtons.size() < 2) {
                selectedButtons.add(button);
                DropShadow shadow = new DropShadow();
                shadow.setColor(Color.GRAY);
                shadow.setRadius(10);
                shadow.setOffsetX(5);
                shadow.setOffsetY(5);

                button.setEffect(shadow);
                button.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #87CEEB, #4682B4);" + // 渐变背景
                                "-fx-border-color: #1E90FF;" +
                                "-fx-border-width: 2px;" +
                                "-fx-border-radius: 10px;" +
                                "-fx-border-insets: -2;" +
                                "-fx-background-radius: 10px;"

                );
                button.setTranslateY(-4);
                clientService.sendMessage(new Message(MessageType.PICK, Map.of("pick", new int[]{row, col})));
            }
            if (selectedButtons.size() == 1) {
                lastRow = row;
                lastCol = col;
            } else if (selectedButtons.size() == 2) {
                clientService.sendMessage(new Message(
                        MessageType.JUDGE,
                        Map.of("pair", new int[]{lastRow, lastCol, row, col})
                ));
            }

        } else {
            showMessage("现在不是你的回合");
        }
    }

    private void addHoverEffect(Button button) {
        DropShadow hoverShadow = new DropShadow();
        hoverShadow.setColor(Color.GRAY);
        hoverShadow.setRadius(10);
        hoverShadow.setOffsetX(3);
        hoverShadow.setOffsetY(3);

        button.setOnMouseEntered(event -> {
            if (isYourTurn) {
                if (selectedButtons.size() < 2) {
                    if (!selectedButtons.contains(button)) {
                        button.setStyle("-fx-background-color: white;");
                        button.setEffect(hoverShadow);
                        button.setTranslateY(-2);
                    }
                    button.setCursor(Cursor.HAND);
                } else {
                    if (selectedButtons.contains(button)) {
                        button.setCursor(Cursor.HAND);
                    }
                }
            } else {
                button.setCursor(Cursor.cursor(cursorImage.getUrl()));
            }
        });

        button.setOnMouseExited(event -> {
            if (isYourTurn) {
                if (!selectedButtons.contains(button) && selectedButtons.size() < 2) {
                    button.setStyle("-fx-background-color: white;");
                    button.setEffect(null);
                    button.setTranslateY(0);
                }
            }
            button.setCursor(Cursor.DEFAULT);
        });
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        log.warn("显示错误提示: {} - {}", title, message);
    }

    private void showInformMessage(String message) {
        Label messageLabel = new Label(message);
        messageLabel.setStyle("""
                -fx-background-color: linear-gradient(to bottom, #D4EDDA, #155724);
                -fx-text-fill: white;
                -fx-padding: 10px;
                -fx-border-color: #C3E6CB;
                -fx-border-width: 2px;
                -fx-border-radius: 10px;
                -fx-background-radius: 10px;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                """);
        messageLabel.setOpacity(0);

        StackPane root = (StackPane) gameBoard.getParent();
        root.getChildren().add(messageLabel);

        StackPane.setAlignment(messageLabel, Pos.TOP_CENTER);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), messageLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // 停留后渐隐动画
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), messageLabel);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(1)); // 停留1.5秒后消失

        // 消失后移除消息框
        fadeOut.setOnFinished(event -> root.getChildren().remove(messageLabel));

        // 顺序播放动画
        fadeIn.setOnFinished(event -> fadeOut.play());
        fadeIn.play();
    }

    private void showMessage(String message) {
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-background-color: linear-gradient(to bottom, #F8D7DA, #721C24); " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10px; " +
                "-fx-border-color: #F5C6CB; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 10px; " +
                "-fx-background-radius: 10px; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold;");
        messageLabel.setOpacity(0);

        StackPane root = (StackPane) gameBoard.getParent();
        root.getChildren().add(messageLabel);

        StackPane.setAlignment(messageLabel, Pos.TOP_CENTER);

        addShakeEffect(messageLabel);
        addWindowShakeEffect((Stage) root.getScene().getWindow());

        // 渐显动画
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), messageLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // 停留后渐隐动画
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), messageLabel);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(1)); // 停留1秒后消失

        // 消失后移除消息框
        fadeOut.setOnFinished(event -> root.getChildren().remove(messageLabel));

        // 顺序播放动画
        fadeIn.setOnFinished(event -> fadeOut.play());
        fadeIn.play();
    }

    private void fadeInAndOut(Label messageLabel) {

    }

    private void addShakeEffect(Node node) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), node);
        shake.setFromX(0); // 起始水平位置
        shake.setByX(10); // 向右偏移10像素
        shake.setCycleCount(6); // 反复6次（3次来回）
        shake.setAutoReverse(true); // 自动反向

        shake.play(); // 播放抖动动画
    }

    private void addWindowShakeEffect(Stage stage) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), stage.getScene().getRoot());
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6); // 反复6次
        shake.setAutoReverse(true);

        shake.play();
    }

    public ImageView addContent(int content) {
        return switch (content) {
            case 0 -> new ImageView(imageCarambola);
            case 1 -> new ImageView(imageApple);
            case 2 -> new ImageView(imageMango);
            case 3 -> new ImageView(imageBlueberry);
            case 4 -> new ImageView(imageCherry);
            case 5 -> new ImageView(imageGrape);
            case 6 -> new ImageView(imageKiwi);
            case 7 -> new ImageView(imageOrange);
            case 8 -> new ImageView(imagePeach);
            case 9 -> new ImageView(imagePear);
            case 10 -> new ImageView(imagePineapple);
            case 11 -> new ImageView(imageWatermelon);
            default -> null;
        };
    }

    public static Image imageApple = new Image(Objects.requireNonNull(GameController.class.getResource("/linkgame/client/apple.png")).toExternalForm());
    public static Image imageMango = new Image(Objects.requireNonNull(GameController.class.getResource("/linkgame/client/mango.png")).toExternalForm());
    public static Image imageBlueberry = new Image(Objects.requireNonNull(GameController.class.getResource("/linkgame/client/blueberry.png")).toExternalForm());
    public static Image imageCherry = new Image(Objects.requireNonNull(GameController.class.getResource("/linkgame/client/cherry.png")).toExternalForm());
    public static Image imageGrape = new Image(Objects.requireNonNull(GameController.class.getResource("/linkgame/client/grape.png")).toExternalForm());
    public static Image imageCarambola = new Image(Objects.requireNonNull(GameController.class.getResource("/linkgame/client/carambola.png")).toExternalForm());
    public static Image imageKiwi = new Image(Objects.requireNonNull(GameController.class.getResource("/linkgame/client/kiwi.png")).toExternalForm());
    public static Image imageOrange = new Image(Objects.requireNonNull(GameController.class.getResource("/linkgame/client/orange.png")).toExternalForm());
    public static Image imagePeach = new Image(Objects.requireNonNull(GameController.class.getResource("/linkgame/client/peach.png")).toExternalForm());
    public static Image imagePear = new Image(Objects.requireNonNull(GameController.class.getResource("/linkgame/client/pear.png")).toExternalForm());
    public static Image imagePineapple = new Image(Objects.requireNonNull(GameController.class.getResource("/linkgame/client/pineapple.png")).toExternalForm());
    public static Image imageWatermelon = new Image(Objects.requireNonNull(GameController.class.getResource("/linkgame/client/watermelon.png")).toExternalForm());

    public void handleReset(ActionEvent actionEvent) {
        System.out.println("Reset button clicked");
    }

    private void showGameOverDialog(int yourScore, int opponentScore) {
        if (timeline != null) {
            timeline.stop();
        }

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("游戏结束");

        String resultMessage;
        if (yourScore > opponentScore) {
            resultMessage = "恭喜，你赢了！🎉";
        } else if (yourScore < opponentScore) {
            resultMessage = "很遗憾，你输了... 😔";
        } else {
            resultMessage = "平局！🤝";
        }


        Label resultLabel = new Label(resultMessage);
        resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-alignment: center;");
        Label scoreLabel = new Label("你的得分: " + yourScore + " 对手得分: " + opponentScore);
        scoreLabel.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");

        VBox content = new VBox(10, resultLabel, scoreLabel);
        content.setAlignment(Pos.CENTER);

        alert.getDialogPane().setContent(content);

        ButtonType playAgainButton = new ButtonType("再来一局", ButtonBar.ButtonData.OK_DONE);
        ButtonType returnButton = new ButtonType("返回主页", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType exitButton = new ButtonType("退出游戏", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(playAgainButton, returnButton, exitButton);

        alert.setOnCloseRequest(dialogEvent -> {
            ButtonType result = alert.getResult();
            if (result == playAgainButton) {
                mainController.closeConnection();
                mainController.showInputPage();
            } else if (result == returnButton) {
                mainController.closeConnection();
                mainController.showMainPage();
            } else if (result == exitButton) {
                mainController.closeConnection();
                if (mainController.getUserId() != null) {
                    OkHttpUtils.postForm("http://localhost:8080/user/logout",
                            Map.of("userId", mainController.getUserId().toString()), null);
                }
                System.exit(0);
            }
        });

        alert.showAndWait();
    }

    /**
     * 对手掉线提示框
     */
    private void showDisconnectedDialog() {
        if (timeline != null) {
            timeline.stop();
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("对手已离线");
        alert.setHeaderText("对手已离线，游戏结束！");
        alert.setContentText("请选择下一步操作：");

        ButtonType restartButton = new ButtonType("重新开始");
        ButtonType returnButton = new ButtonType("返回主页");
        ButtonType exitButton = new ButtonType("退出游戏", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(restartButton, returnButton, exitButton);

        // 处理用户选择
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == restartButton) {
            // 用户选择重新开始
            mainController.closeConnection();
            mainController.showInputPage();
        } else if (result.isPresent() && result.get() == returnButton) {
            mainController.closeConnection();
            mainController.showMainPage();
        } else {
            mainController.closeConnection();
            if (mainController.getUserId() != null) {
                OkHttpUtils.postForm("http://localhost:8080/user/logout",
                        Map.of("userId", mainController.getUserId().toString()), null);
            }
            // 用户选择退出
            System.exit(0);
        }
    }

    private Alert reconnectDialog;
    private Label countdownLabel;
    private int countdownSeconds;
    private boolean isReconnecting = false;

    public void showReconnectDialog() {
        if (timeline != null) {
            timeline.stop();
        }

        // 创建一个警告类型的弹窗
        reconnectDialog = new Alert(Alert.AlertType.INFORMATION);
        reconnectDialog.setTitle("等待重连");
        reconnectDialog.setHeaderText(null);
        countdownSeconds = 30;
        isReconnecting = true;

        // 设置弹窗的内容：一个文本标签显示倒计时和重连提示
        VBox vbox = new VBox();
        Text reconnectText = new Text("对手掉线，正在等待重连...");
        countdownLabel = new Label("剩余时间: " + countdownSeconds + "秒");
        vbox.getChildren().addAll(reconnectText, countdownLabel);

        reconnectDialog.getDialogPane().setContent(vbox);

        reconnectDialog.getDialogPane().setHeaderText(null);  // 关闭掉默认的标题区域
        reconnectDialog.getDialogPane().getScene().getWindow().setOpacity(1); // 保证正常透明度
        reconnectDialog.getDialogPane().setStyle("-fx-background-color: white;");

        startCountdown();

        ButtonType restartButton = new ButtonType("重新开始");
        ButtonType returnButton = new ButtonType("返回主页");
        ButtonType exitButton = new ButtonType("退出游戏", ButtonBar.ButtonData.CANCEL_CLOSE);

        reconnectDialog.getButtonTypes().setAll(restartButton, returnButton, exitButton);

        if (!reconnectDialog.isShowing()) {
            Optional<ButtonType> result = reconnectDialog.showAndWait();
            if (result.isPresent() && result.get() == restartButton) {
                mainController.closeConnection();
                mainController.showInputPage();
                clientService.sendMessage(new Message(MessageType.WAIT_RECONNECT_FAIL, null));
            } else if (result.isPresent() && result.get() == returnButton) {
                clientService.sendMessage(new Message(MessageType.WAIT_RECONNECT_FAIL, null));
                isReconnecting = false;
                mainController.closeConnection();
                mainController.showMainPage();
            } else {
//                mainController.closeConnection();
//                clientService.sendMessage(new Message(MessageType.WAIT_RECONNECT_FAIL, null));
//                if (mainController.getUserId() != null) {
//                    OkHttpUtils.postForm("http://localhost:8080/user/logout",
//                            Map.of("userId", mainController.getUserId().toString()), null);
//                }
//                System.exit(0);
            }
        }
    }

    // 启动倒计时
    private void startCountdown() {
        Thread countdownThread = new Thread(() -> {
            try {
                while (countdownSeconds > 0) {
                    TimeUnit.SECONDS.sleep(1);
                    countdownSeconds--;
                    Platform.runLater(() -> countdownLabel.setText("剩余时间: " + countdownSeconds + "秒"));
                }
                if (isReconnecting) {
                    isReconnecting = false;
                    clientService.sendMessage(new Message(MessageType.WAIT_RECONNECT_FAIL, null));
                    Platform.runLater(() -> {
                        {
                            closeReconnectingDialog();
                            showDisconnectedDialog();
                        }
                    });
                }
            } catch (InterruptedException e) {
                log.error("倒计时线程被中断", e);
            }
        });

        countdownThread.setDaemon(true);
        countdownThread.start();
    }

    // 关闭等待重连弹窗
    public void closeReconnectingDialog() {
        reconnectDialog.close();
    }
}
