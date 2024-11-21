package org.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import static org.example.demo.Game.SetupBoard;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {

        int[] size = getBoardSizeFromUser();
        Controller.game = new Game(SetupBoard(size[0], size[1], false));

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("board.fxml"));
        VBox root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.createGameBoard();

        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        // TODO: handle the game logic

    }

    private int[] getBoardSizeFromUser() {
        // 创建一个自定义对话框
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Choose Board Size");
        dialog.setHeaderText("Enter the number of rows and columns");

        // 设置按钮类型
        ButtonType confirmButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // 创建布局和输入控件
        GridPane grid = new GridPane();
        grid.setHgap(10); // 列间距
        grid.setVgap(10); // 行间距
        grid.setPadding(new Insets(20, 150, 10, 10));

        Label rowLabel = new Label("Rows:");
        rowLabel.setMinWidth(60); // 设置标签最小宽度
        TextField rowInput = new TextField();
        rowInput.setPromptText("Enter rows (even >= 4)");
        rowInput.setMinWidth(200); // 限制输入框宽度

        Label colLabel = new Label("Columns:");
        colLabel.setMinWidth(60); // 设置标签最小宽度
        TextField colInput = new TextField();
        colInput.setPromptText("Enter columns (even >= 4)");
        colInput.setMinWidth(200); // 限制输入框宽度

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setWrapText(true); // 启用多行显示
        errorLabel.setPrefWidth(200);
        errorLabel.setVisible(false);

        grid.add(rowLabel, 0, 0);
        grid.add(rowInput, 1, 0);
        grid.add(colLabel, 0, 1);
        grid.add(colInput, 1, 1);
        grid.add(errorLabel, 0, 2, 3, 1); // 跨两列显示错误信息
        GridPane.setMargin(errorLabel, new Insets(0, 0, 0, 50)); // 设置错误信息的左边距

        dialog.getDialogPane().setContent(grid);

        // 禁用 "OK" 按钮直到输入合法
        Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

        // 实时验证输入
        rowInput.textProperty().addListener((observable, oldValue, newValue) -> validateInput(rowInput, colInput, errorLabel, confirmButton));
        colInput.textProperty().addListener((observable, oldValue, newValue) -> validateInput(rowInput, colInput, errorLabel, confirmButton));

        // 处理对话框的结果
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new int[]{
                        Integer.parseInt(rowInput.getText().trim()),
                        Integer.parseInt(colInput.getText().trim())
                };
            }
            return null;
        });

        // 显示对话框并获取结果
        Optional<int[]> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        } else {
            System.exit(0); // 用户取消或关闭对话框，退出游戏
            return null; // 这一行实际上不会被执行
        }
    }

    // 输入验证逻辑
    private void validateInput(TextField rowInput, TextField colInput, Label errorLabel, Node confirmButton) {
        try {
            String rowText = rowInput.getText().trim();
            String colText = colInput.getText().trim();
            if (rowText.isEmpty() && colText.isEmpty()) {
                errorLabel.setVisible(false);
                confirmButton.setDisable(true);
            } else {
                boolean rowValid = false;
                if (!rowText.isEmpty()) {
                    int rows = Integer.parseInt(rowText);
                    if (rows < 4) {
                        errorLabel.setText("Rows must be >= 4.");
                        errorLabel.setVisible(true);
                        confirmButton.setDisable(true);
                        return ;
                    } else if (rows % 2 == 0) {
                        rowValid = true;
                    } else {
                        errorLabel.setText("Rows must be even numbers >= 4.");
                        errorLabel.setVisible(true);
                        confirmButton.setDisable(true);
                        return ;
                    }
                }


                if (!colText.isEmpty()) {
                    int cols = Integer.parseInt(colText);
                    if (cols < 4) {
                        errorLabel.setText("Columns must be >= 4.");
                        errorLabel.setVisible(true);
                        confirmButton.setDisable(true);
                    } else if (cols % 2 == 0) {
                        if (rowValid) {
                            errorLabel.setVisible(false);
                            confirmButton.setDisable(false);
                        } else {
                            errorLabel.setText("Please enter rows.");
                            errorLabel.setVisible(true);
                            confirmButton.setDisable(true);
                        }
                    } else {
                        errorLabel.setText("Columns must be even numbers >= 4.");
                        errorLabel.setVisible(true);
                        confirmButton.setDisable(true);
                    }
                }
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Please enter valid integers for rows and columns.");
            errorLabel.setVisible(true);
            confirmButton.setDisable(true);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
