package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.Objects;

public class Controller {

    @FXML
    private Pane gamePane;

    @FXML
    private Label scoreLabel;

    @FXML
    private GridPane gameBoard;

    private Button[][] buttons;

    private final Polyline connectionLine = new Polyline();

    public static Game game;

    int[] position = new int[3];

    Button lastButton = null;

    @FXML
    public void initialize() {
        connectionLine.setStroke(Color.RED);
        connectionLine.setStrokeWidth(2);
        connectionLine.getStrokeDashArray().addAll(10.0, 5.0);
        connectionLine.setManaged(false);
        connectionLine.setMouseTransparent(true);
        gamePane.getChildren().add(connectionLine);
        connectionLine.setVisible(false);
    }

    private void showConnectionLine(Button[] path, Button startButton, Button endButton) {
        connectionLine.getPoints().clear();

        for (Button button : path) {
            double x = button.getLayoutX() + button.getWidth() / 2;
            double y = button.getLayoutY() + button.getHeight() / 2;
            connectionLine.getPoints().addAll(x, y);
        }

        connectionLine.setVisible(true);
    }

    public void createGameBoard() {

        gameBoard.getChildren().clear();
        buttons = new Button[game.row][game.col];

        for (int row = 0; row < game.row; row++) {
            for (int col = 0; col < game.col; col++) {
                Button button = new Button();
                buttons[row][col] = button;
                button.setPrefSize(40, 40);
                ImageView imageView = addContent(game.board[row][col]);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                imageView.setPreserveRatio(true);
                button.setGraphic(imageView);
                if (game.board[row][col] == 0) {
                    button.setDisable(true);
                }
                int finalRow = row;
                int finalCol = col;
                button.setOnAction(_ -> handleButtonPress(finalRow, finalCol, button));
                gameBoard.add(button, col, row);
            }
        }
    }

    private void handleButtonPress(int row, int col, Button button) {
        System.out.println("Button pressed at: " + row + ", " + col);
        if (position[0] == 0) {
            // 第一次点击，记录选中位置
            position[1] = row;
            position[2] = col;
            position[0] = 1;
            lastButton = button;
        } else {
            boolean change = game.judge(position[1], position[2], row, col, buttons);
            if (change) {

                Button[] path = game.path;
                showConnectionLine(path, lastButton, button);

                new Thread(() -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // 在 UI 线程中移除格子和连线
                    javafx.application.Platform.runLater(() -> {
                        connectionLine.setVisible(false);
                        game.board[position[1]][position[2]] = 0;
                        game.board[row][col] = 0;
                        createGameBoard(); // 更新 UI
                    });
                }).start();
            } else {
                showError("Cannot connect these tiles!");
            }
            position[0] = 0;
            lastButton = null;
        }
    }

    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleReset() {
        game.board = Game.SetupBoard(game.row, game.col, true);
        createGameBoard(); // Update the game board UI
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

    public static Image imageApple = new Image(Objects.requireNonNull(Game.class.getResource("/org/example/demo/apple.png")).toExternalForm());
    public static Image imageMango = new Image(Objects.requireNonNull(Game.class.getResource("/org/example/demo/mango.png")).toExternalForm());
    public static Image imageBlueberry = new Image(Objects.requireNonNull(Game.class.getResource("/org/example/demo/blueberry.png")).toExternalForm());
    public static Image imageCherry = new Image(Objects.requireNonNull(Game.class.getResource("/org/example/demo/cherry.png")).toExternalForm());
    public static Image imageGrape = new Image(Objects.requireNonNull(Game.class.getResource("/org/example/demo/grape.png")).toExternalForm());
    public static Image imageCarambola = new Image(Objects.requireNonNull(Game.class.getResource("/org/example/demo/carambola.png")).toExternalForm());
    public static Image imageKiwi = new Image(Objects.requireNonNull(Game.class.getResource("/org/example/demo/kiwi.png")).toExternalForm());
    public static Image imageOrange = new Image(Objects.requireNonNull(Game.class.getResource("/org/example/demo/orange.png")).toExternalForm());
    public static Image imagePeach = new Image(Objects.requireNonNull(Game.class.getResource("/org/example/demo/peach.png")).toExternalForm());
    public static Image imagePear = new Image(Objects.requireNonNull(Game.class.getResource("/org/example/demo/pear.png")).toExternalForm());
    public static Image imagePineapple = new Image(Objects.requireNonNull(Game.class.getResource("/org/example/demo/pineapple.png")).toExternalForm());
    public static Image imageWatermelon = new Image(Objects.requireNonNull(Game.class.getResource("/org/example/demo/watermelon.png")).toExternalForm());

}
