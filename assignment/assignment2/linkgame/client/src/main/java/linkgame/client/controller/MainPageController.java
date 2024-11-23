package linkgame.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Setter;

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
    private Label avatarLabel;

    @FXML
    private Button startGameButton;
    @FXML
    private Button exitGameButton;

    @Setter
    private MainController mainController;

    public void setUserInfo(int userId, String nickname, String avatar) {
        usernameLabel.setText("ID: " + userId);
        nicknameLabel.setText("昵称: " + nickname);
        avatarLabel.setText("头像: " + avatar);
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
