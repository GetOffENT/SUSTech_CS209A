package linkgame.client.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import linkgame.common.OkHttpUtils;
import lombok.Setter;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-24 4:19
 */
public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextField avatarUrlField;
    @FXML
    private HBox registerFields;
    @FXML
    private Button loginOrRegisterButton;
    @FXML
    private Button toggleButton;

    @Setter
    private MainController mainController;

    @FXML
    public void initialize() {
        registerFields.setVisible(false);
    }

    /**
     * 切换登录和注册
     */
    @FXML
    private void toggle() {
        if (!registerFields.isVisible()) {
            registerFields.setVisible(true);
            toggleButton.setText("切换到登录");
            loginOrRegisterButton.setText("注册");
        } else {
            registerFields.setVisible(false);
            toggleButton.setText("切换到注册");
            loginOrRegisterButton.setText("登录");
        }
    }

    /**
     * 登录或注册
     */
    @FXML
    private void handleLoginOrRegister() {
        if (!registerFields.isVisible()) {
            handleLogin();
        }else {
            handleRegister();
        }
    }

    private void handleLogin(){
        String username = usernameField.getText();
        String password = passwordField.getText();

        String response = OkHttpUtils.postForm(
                "http://localhost:8080/user/login",
                Map.of("username", username, "password", password),
                null);

        JSONObject jsonObject = JSON.parseObject(response);
        int code = jsonObject.getInteger("code");
        String message = jsonObject.getString("message");

        if (code == 200) {
            JSONObject data = jsonObject.getJSONObject("data");
            int userId = data.getInteger("id");
            String nickname = data.getString("nickname");
            String avatar = data.getString("avatar");
            mainController.showMainPage(userId, nickname, avatar);
        } else {
            showAlert(Alert.AlertType.ERROR, "登录失败", message);
        }
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String nickname = nicknameField.getText();
        String avatarUrl = avatarUrlField.getText();

        if (username.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "输入错误", "用户名、密码和昵称不能为空！");
            return;
        }

        if (!avatarUrl.isEmpty() && !isValidUrl(avatarUrl)) {
            showAlert(Alert.AlertType.ERROR, "输入错误", "头像URL格式不正确！");
            return;
        }

        String response = OkHttpUtils.postForm(
                "http://localhost:8080/user/register",
                Map.of("username", username,
                        "password", password,
                        "nickname", nickname,
                        "avatarUrl", avatarUrl),
                null);

        JSONObject jsonObject = JSON.parseObject(response);
        int code = jsonObject.getInteger("code");
        String message = jsonObject.getString("message");

        if (code == 200) {
            JSONObject data = jsonObject.getJSONObject("data");
            int userId = data.getInteger("id");
            String avatar = data.getString("avatar");
            mainController.showMainPage(userId, nickname, avatar);
        } else {
            showAlert(Alert.AlertType.ERROR, "注册失败", message);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // 验证URL格式
    private boolean isValidUrl(String url) {
        String regex = "^(https?|ftp)://[^\s/$.?#].[^\s]*$";
        return Pattern.matches(regex, url);
    }
}
