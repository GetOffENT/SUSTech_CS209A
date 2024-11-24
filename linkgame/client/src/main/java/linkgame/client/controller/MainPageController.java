package linkgame.client.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import linkgame.common.OkHttpUtils;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-24 4:52
 */
public class MainPageController {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label nicknameLabel;

    @FXML
    private ImageView currentUserAvatarImageView;

    @FXML
    private HBox buttonBox;
    @FXML
    private Button startGameButton;
    @FXML
    private Button exitGameButton;
    @FXML
    private Button viewHistoryButton;
    @FXML
    private Button logoutButton;

    @FXML
    private Button refreshButton;
    @FXML
    private VBox usersContainer;

    @Setter
    private MainController mainController;

    @FXML
    public void initialize() {
        viewHistoryButton.setOnMouseEntered(
                _ -> {
                    viewHistoryButton.setCursor(Cursor.HAND);
                    viewHistoryButton.setStyle(
                            "-fx-background-color: #1E88E5; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-border-radius: 5px; " +
                                    "-fx-effect: dropshadow(gaussian, #000, 10, 0, 0, 5); "
                    );
                }
        );
        viewHistoryButton.setOnMouseExited(
                _ -> {
                    viewHistoryButton.setCursor(Cursor.DEFAULT);
                    viewHistoryButton.setStyle(
                            "-fx-background-color: #2196F3; " + // 恢复默认蓝色背景
                                    "-fx-text-fill: white; " +           // 恢复默认字体颜色
                                    "-fx-border-radius: 5px; " +        // 恢复圆角边框
                                    "-fx-effect: none;"                 // 移除阴影效果
                    );
                }
        );
        viewHistoryButton.setStyle(
                "-fx-background-color: #2196F3; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-radius: 5px; "
        );

        startGameButton.setOnMouseEntered(
                _ -> {
                    startGameButton.setCursor(Cursor.HAND);
                    startGameButton.setStyle(
                            "-fx-background-color: #4CAF50; " + // 改变背景色为绿色
                                    "-fx-text-fill: white; " +           // 改变文字颜色为白色
                                    "-fx-border-radius: 5px; " +        // 设置圆角边框
                                    "-fx-effect: dropshadow(gaussian, #000, 10, 0, 0, 5); "  // 添加阴影效果
                    );
                }
        );
        startGameButton.setOnMouseExited(
                _ -> {
                    startGameButton.setCursor(Cursor.DEFAULT);
                    startGameButton.setStyle(
                            "-fx-background-color: #4CAF50; " + // 恢复默认绿色背景
                                    "-fx-text-fill: white; " +           // 恢复默认字体颜色
                                    "-fx-border-radius: 5px; " +        // 恢复圆角边框
                                    "-fx-effect: none;"                 // 移除阴影效果
                    );
                }
        );
        startGameButton.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-radius: 5px; "
        );

        exitGameButton.setOnMouseEntered(
                _ -> {
                    exitGameButton.setCursor(Cursor.HAND);
                    exitGameButton.setStyle(
                            "-fx-background-color: #f44336; " + // 改变背景色为红色
                                    "-fx-text-fill: white; " +           // 改变文字颜色为白色
                                    "-fx-border-radius: 5px; " +        // 设置圆角边框
                                    "-fx-effect: dropshadow(gaussian, #000, 10, 0, 0, 5); "  // 添加阴影效果
                    );
                }
        );

        exitGameButton.setOnMouseExited(
                _ -> {
                    exitGameButton.setCursor(Cursor.DEFAULT);
                    exitGameButton.setStyle(
                            "-fx-background-color: #f44336; " + // 恢复默认红色背景
                                    "-fx-text-fill: white; " +           // 恢复默认字体颜色
                                    "-fx-border-radius: 5px; " +        // 恢复圆角边框
                                    "-fx-effect: none;"                 // 移除阴影效果
                    );
                }
        );
        exitGameButton.setStyle(
                "-fx-background-color: #f44336; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-radius: 5px; "
        );

        logoutButton.setOnMouseEntered(
                _ -> {
                    logoutButton.setCursor(Cursor.HAND);
                    logoutButton.setStyle(
                            "-fx-background-color:" +
                                    "#FFB74D; -fx-text-fill: white;" +
                                    "-fx-border-radius: 5px;" +
                                    "-fx-effect: dropshadow(gaussian, #000, 10, 0, 0, 5); "
                    );
                }
        );

        logoutButton.setOnMouseExited(
                _ -> {
                    logoutButton.setCursor(Cursor.DEFAULT);
                    logoutButton.setStyle(
                            "-fx-background-color: #FF9800; " +
                                    "-fx-text-fill: white;" +
                                    "-fx-border-radius: 5px;" +
                                    "-fx-effect: none;"
                    );
                }
        );
        logoutButton.setStyle(
                "-fx-background-color: #FF9800; " +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 5px;"
        );

        refreshButton.setOnMouseEntered(
                _ -> {
                    refreshButton.setCursor(Cursor.HAND);
                }
        );
        refreshButton.setOnMouseExited(
                _ -> {
                    refreshButton.setCursor(Cursor.DEFAULT);
                }
        );
        refreshButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-effect: none; "
        );
        ImageView imageView = new ImageView(
                new Image(Objects.requireNonNull(GameController.class.getResource("/linkgame/client/refresh.png")).toExternalForm())
        );
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        refreshButton.setGraphic(imageView);

    }

    @FXML
    public void handleRefresh() {
        loadAllUsers();
    }

    public void setUserInfo(int userId, String nickname, String avatar) {
        usernameLabel.setText("ID: " + userId);
        nicknameLabel.setText("昵称: " + nickname);

        if (avatar != null && !avatar.isEmpty()) {
            Image avatarImage = new Image(avatar, true);
            currentUserAvatarImageView.setImage(avatarImage);
            currentUserAvatarImageView.setFitHeight(50);
            currentUserAvatarImageView.setFitWidth(50);
        }
    }

    // 发送请求，获取所有用户的信息并展示
    public void loadAllUsers() {
        // 发送查询请求
        String response = OkHttpUtils.postForm(
                "http://localhost:8080/user/list",
                Map.of("userId", mainController.getUserId().toString()),
                null
        );

        // 解析响应数据
        JSONObject jsonObject = JSON.parseObject(response);
        int code = jsonObject.getInteger("code");
        if (code == 200) {
            JSONArray userData = jsonObject.getJSONArray("data");
            displayUsers(userData);
        }
    }

    /**
     * 展示用户信息
     *
     * @param users 用户信息
     */
    private void displayUsers(JSONArray users) {
        usersContainer.getChildren().clear(); // 清空容器中的用户列表

        for (int i = 0; i < users.size(); i++) {
            JSONObject user = users.getJSONObject(i);
            String userId = user.getString("id");
            String nickname = user.getString("nickname");
            String avatarUrl = user.getString("avatar");
            boolean isOnline = user.getBoolean("online");

            // 创建显示用户信息的UI组件
            HBox userBox = createUserBox(userId, nickname, avatarUrl, isOnline);
            usersContainer.getChildren().add(userBox);
        }
    }

    /**
     * 单个用户信息展示
     *
     * @param nickname  用户昵称
     * @param avatarUrl 用户头像
     * @param isOnline  用户在线状态
     * @return 用户信息展示组件
     */
    private HBox createUserBox(String userId, String nickname, String avatarUrl, boolean isOnline) {
        HBox userBox = new HBox(10); // HBox 布局，用户头像和信息水平排列

        // 用户头像
        ImageView avatarImageView = new ImageView();
        avatarImageView.setFitHeight(40); // 头像大小
        avatarImageView.setFitWidth(40);
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Image avatarImage = new Image(avatarUrl, true); // 使用头像 URL 创建 Image 对象
            avatarImageView.setImage(avatarImage);
        }

        // 用户昵称
        Label nicknameLabel = new Label(nickname);

        // 在线状态
        Label onlineLabel = new Label(isOnline ? "在线" : "离线");
        onlineLabel.setStyle(isOnline ? "-fx-text-fill: green;" : "-fx-text-fill: #988f8f;");

        userBox.getChildren().addAll(avatarImageView, nicknameLabel, onlineLabel);

        userBox.setOnMouseClicked(_ -> {
            showHistory(userId);
        });

        userBox.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-effect: none; "
        );

        userBox.setOnMouseEntered(
                _ -> {
                    userBox.setCursor(Cursor.HAND);
                    userBox.setStyle(
                            "-fx-background-color: #f0f0f0; " +
                                    "-fx-border-radius: 5px; " +
                                    "-fx-effect: dropshadow(gaussian, #000, 10, 0, 0, 5); "
                    );
                }
        );

        userBox.setOnMouseExited(
                _ -> {
                    userBox.setCursor(Cursor.DEFAULT);
                    userBox.setStyle(
                            "-fx-background-color: transparent; " +
                                    "-fx-border-radius: 5px; " +
                                    "-fx-effect: none; "
                    );
                }
        );

        return userBox;
    }


    @FXML
    private void handleStartGame() {
        mainController.showInputPage();
    }

    @FXML
    private void handleExitGame() {
        OkHttpUtils.postForm("http://localhost:8080/user/logout",
                Map.of("userId", mainController.getUserId().toString()), null);
        mainController.getStage().close();
    }

    @FXML
    private void handleViewHistory() {
        showHistory(mainController.getUserId().toString());
    }

    @FXML
    private void handleLogout() {
        OkHttpUtils.postForm("http://localhost:8080/user/logout",
                Map.of("userId", mainController.getUserId().toString()), null);
        mainController.showLoginPage();
    }

    public void showHistory(String userId) {
        String response = OkHttpUtils.get("http://localhost:8080/record?userId=" + userId, null);
        JSONObject jsonObject = JSON.parseObject(response);
        int code = jsonObject.getInteger("code");

        if (code == 200) {
            JSONArray records = jsonObject.getJSONArray("data");
            showHistoryDialog(records);
        } else {
            showAlert(Alert.AlertType.ERROR, "无法加载历史记录", "无法加载游戏历史记录，请稍后再试");
        }
    }

    // 显示历史记录的对话框
    private void showHistoryDialog(JSONArray records) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("历史记录");

        VBox vbox = new VBox(10);
        vbox.setPrefWidth(400);
        vbox.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");

        for (int i = 0; i < records.size(); i++) {
            JSONObject record = records.getJSONObject(i);

            HBox recordBox = createRecordBox(record);
            vbox.getChildren().add(recordBox);
        }

        Scene dialogScene = new Scene(vbox);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    // 创建每条记录的显示框
    private HBox createRecordBox(JSONObject record) {
        HBox recordBox = new HBox(15);
        recordBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5;");

        // 当前用户信息
        JSONObject user = record.getJSONObject("user");
        String userNickname = user.getString("nickname");
        String userAvatar = user.getString("avatar");
        int userScore = record.getInteger("score");

        // 对手信息
        JSONObject opponent = record.getJSONObject("opponent");
        String opponentNickname = opponent.getString("nickname");
        String opponentAvatar = opponent.getString("avatar");
        int opponentScore = record.getInteger("opponentScore");

        String createAt = record.getString("createAt");
        String formattedDate = formatDate(createAt);


        // 当前用户头像和分数
        ImageView userImageView = new ImageView(userAvatar);
        userImageView.setFitHeight(40);
        userImageView.setFitWidth(40);
        Label userNicknameLabel = new Label(userNickname);
        userNicknameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");
        Label userScoreLabel = new Label("得分: " + userScore);
        userScoreLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
        VBox infoBox1 = new VBox(5, userNicknameLabel, userScoreLabel);

        // 对手头像和分数
        ImageView opponentImageView = new ImageView(opponentAvatar);
        opponentImageView.setFitHeight(40);
        opponentImageView.setFitWidth(40);
        Label opponentNicknameLabel = new Label(opponentNickname);
        opponentNicknameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");
        Label opponentScoreLabel = new Label("得分: " + opponentScore);
        opponentScoreLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #f44336; -fx-font-weight: bold;");
        VBox infoBox2 = new VBox(5, opponentNicknameLabel, opponentScoreLabel);

        // VS 标签
        Label vsLabel = new Label("VS");
        vsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333;");

        // 时间标签
        Label timeLabel = new Label(formattedDate);
        timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888;");

        VBox middleBox = new VBox(5, vsLabel, timeLabel);
        middleBox.setAlignment(Pos.CENTER);

        // 布局：左侧为当前用户，右侧为对手
        HBox userBox = new HBox(5, userImageView, infoBox1);
        HBox opponentBox = new HBox(5, infoBox2, opponentImageView);

        recordBox.getChildren().addAll(userBox, middleBox, opponentBox);

        return recordBox;
    }

    private String formatDate(String createAt) {
        try {
            DateTimeFormatter isoFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(createAt, isoFormat);

            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));

            DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MM月dd日 HH:mm");
            return zonedDateTime.format(outputFormat);
        } catch (Exception e) {
            e.printStackTrace();
            return "未知时间";
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
