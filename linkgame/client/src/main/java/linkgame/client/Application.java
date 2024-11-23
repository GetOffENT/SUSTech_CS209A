package linkgame.client;

import javafx.stage.Stage;
import linkgame.client.controller.MainController;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
        new MainController(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
