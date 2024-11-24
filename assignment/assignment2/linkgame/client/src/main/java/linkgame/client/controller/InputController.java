package linkgame.client.controller;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-22 13:23
 */
@Slf4j
public class InputController {

    @FXML
    private Parent root;

    @FXML
    private TextField rowsInput;

    @FXML
    private TextField colsInput;

    @Setter
    private MainController mainController;

    @FXML
    private Button pickButton;

    @FXML
    private Button randomButton;

    // 检测输入是否合法
    private boolean checkInput(int rows, int cols) {
        if (rows % 2 != 0 || cols % 2 != 0 || rows < 4 || cols < 4) {
            showError("Invalid Input", "Rows and columns must be even and >= 4.");
            log.warn("用户输入了无效的棋盘大小: rows={}, cols={}", rows, cols);
            return false;
        }
        return true;
    }

    @FXML
    public void handlePickStart(){
        try {
            int rows = Integer.parseInt(rowsInput.getText());
            int cols = Integer.parseInt(colsInput.getText());

            if (!checkInput(rows, cols)) {
                return;
            }

            mainController.connectToServer(rows, cols, false);

            // 切换到等待界面
            mainController.showPickPage();
        } catch (NumberFormatException e) {
            showError("Invalid Input", "Please enter valid numbers for rows and cols.");
            log.warn("用户输入了非法的棋盘大小: rowsInput={}, colsInput={}", rowsInput.getText(), colsInput.getText());
        }
    }

    @FXML
    public void handleRandomStart() {
        try {
            int rows = Integer.parseInt(rowsInput.getText());
            int cols = Integer.parseInt(colsInput.getText());

            if (!checkInput(rows, cols)) {
                return;
            }

            mainController.connectToServer(rows, cols, true);

            // 切换到等待界面
            mainController.showWaitingPage();
        } catch (NumberFormatException e) {
            showError("Invalid Input", "Please enter valid numbers for rows and cols.");
            log.warn("用户输入了非法的棋盘大小: rowsInput={}, colsInput={}", rowsInput.getText(), colsInput.getText());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        log.warn("显示错误提示: {} - {}", title, message);
    }

    /**
     * 开始按钮的鼠标进入事件
     */
    @FXML
    private void handleRandomButtonMouseEntered() {
        randomButton.setCursor(Cursor.HAND);
        randomButton.setStyle("-fx-font-size: 16px; -fx-background-color: #45a049; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    }

    /**
     * 开始按钮的鼠标移出事件
     */
    @FXML
    private void handleRandomButtonMouseExited() {
        randomButton.setCursor(Cursor.DEFAULT);
        randomButton.setStyle("-fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    }

    @FXML
    private void handlePickButtonMouseEntered() {
        pickButton.setCursor(Cursor.HAND);
        pickButton.setStyle("-fx-font-size: 16px; -fx-background-color: #45a049; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    }

    @FXML
    private void handlePickButtonMouseExited() {
        pickButton.setCursor(Cursor.DEFAULT);
        pickButton.setStyle("-fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    }

}
