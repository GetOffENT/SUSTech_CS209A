package linkgame.client.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import linkgame.common.OkHttpUtils;
import lombok.Setter;

import java.util.Map;

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
    private Button startGameButton;
    @FXML
    private Button exitGameButton;

    @FXML
    private VBox usersContainer;

    @Setter
    private MainController mainController;

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
     * @param users 用户信息
     */
    private void displayUsers(JSONArray users) {
        usersContainer.getChildren().clear(); // 清空容器中的用户列表

        for (int i = 0; i < users.size(); i++) {
            JSONObject user = users.getJSONObject(i);
            String nickname = user.getString("nickname");
            String avatarUrl = user.getString("avatar");
            boolean isOnline = user.getBoolean("online");

            // 创建显示用户信息的UI组件
            HBox userBox = createUserBox(nickname, avatarUrl, isOnline);
            usersContainer.getChildren().add(userBox);
        }
    }

    /**
     * 单个用户信息展示
     * @param nickname 用户昵称
     * @param avatarUrl 用户头像
     * @param isOnline 用户在线状态
     * @return 用户信息展示组件
     */
    private HBox createUserBox(String nickname, String avatarUrl, boolean isOnline) {
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

        return userBox;
    }


    @FXML
    private void handleStartGame() {
        mainController.showInputPage();
    }

    @FXML
    private void handleExitGame() {
        Stage stage = (Stage) exitGameButton.getScene().getWindow();
        stage.close();
    }
}
