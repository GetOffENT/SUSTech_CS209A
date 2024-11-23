package org.example.demo;

import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws Exception {
        MainController mainController = new MainController(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
