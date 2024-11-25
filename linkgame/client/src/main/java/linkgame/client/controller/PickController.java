package linkgame.client.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import linkgame.common.OkHttpUtils;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-24 9:49
 */
public class PickController {
    @Setter
    private MainController mainController;

    @FXML
    VBox usersContainer;

    @FXML
    public void backToMainPage() {
        mainController.showMainPage();
    }

    public void loadUsers(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            usersContainer.getChildren().clear();
        } else if (userIds != null && !userIds.isEmpty()) {
            String response = OkHttpUtils.postForm(
                    "http://localhost:8080/user/listByIds",
                    Map.of("userIds", String.join(",", userIds)),
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

            // 创建显示用户信息的UI组件
            HBox userBox = createUserBox(userId, nickname, avatarUrl);
            usersContainer.getChildren().add(userBox);
        }

//        if (usersContainer.getChildren().size() > 1) {
//            adjustWindowSize(usersContainer.getChildren().size());
//        }
    }

    /**
     * 单个用户信息展示
     *
     * @param nickname  用户昵称
     * @param avatarUrl 用户头像
     * @return 用户信息展示组件
     */
    private HBox createUserBox(String userId, String nickname, String avatarUrl) {
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


        userBox.getChildren().addAll(avatarImageView, nicknameLabel);

        userBox.setOnMouseClicked(_ -> {
            connectToUser(userId);
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

//    private void adjustWindowSize(int count) {
//        Stage stage = (Stage) usersContainer.getScene().getWindow();
//        stage.setHeight(200 + 40 * count);
//    }

    private void connectToUser(String userId) {
        mainController.connectToUser(userId);
    }
}
