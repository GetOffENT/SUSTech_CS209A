package org.example.demo;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import linkgame.common.Message;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-22 14:11
 */
@Slf4j
public class ClientService {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isConnected = false;

    /**
     * 连接服务器
     */
    public void connectToServer(String host, int port, int rows, int cols, Consumer<Message> messageHandler) {
        new Thread(() -> {
            try {
                socket = new Socket(host, port);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                isConnected = true;

                out.writeObject(new int[]{rows, cols});
                out.flush();
                log.info("已发送棋盘大小至服务器: rows={}, cols={}", rows, cols);

                // 接收消息线程
                while (isConnected) {
                    try {
                        Message message = (Message) in.readObject();
                        log.info("收到服务器消息: {}", message);

                        // 使用 JavaFX 线程处理消息
                        Platform.runLater(() -> messageHandler.accept(message));
                    } catch (ClassNotFoundException e) {
                        log.error("收到无法解析的消息: ", e);
                    } catch (IOException e) {
                        log.error("与服务器的连接中断: ", e);
                        handleServerDisconnected();
                        break;
                    }
                }
            } catch (IOException e) {
                log.error("连接服务器失败: ", e);
                handleConnectionFailed();
            }
        }).start();
    }

    /**
     * 发送消息到服务器
     */
    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
            log.info("已发送消息到服务器: {}", message);
        } catch (IOException e) {
            log.error("无法发送消息到服务器: ", e);
            handleServerDisconnected();
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            isConnected = false;
            if (socket != null) socket.close();
            log.info("客户端连接已关闭");
        } catch (IOException e) {
            log.error("关闭客户端连接失败: ", e);
        }
    }

    private void handleServerDisconnected() {
        isConnected = false;
        log.error("服务器已断开连接");

        // 通知用户服务器断开连接
        showDisconnectedDialog();
    }

    private void handleConnectionFailed() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("连接失败");
            alert.setHeaderText("无法连接到服务器");
            alert.setContentText("请检查网络连接或稍后重试");
            alert.getButtonTypes().setAll(new ButtonType("退出游戏", ButtonBar.ButtonData.CANCEL_CLOSE));
            alert.showAndWait();
            System.exit(0);
        });
    }

    private void showDisconnectedDialog() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("连接中断");
            alert.setHeaderText("与服务器的连接已中断");
            alert.setContentText("游戏将退出，请检查网络并重新连接。");
            alert.getButtonTypes().setAll(new ButtonType("退出游戏", ButtonBar.ButtonData.CANCEL_CLOSE));
            alert.showAndWait();
            System.exit(0);
        });
    }
}
