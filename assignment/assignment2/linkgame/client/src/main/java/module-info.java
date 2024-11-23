module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires linkgame.common;
    requires lombok;
    requires slf4j.api;

    opens linkgame.client to javafx.fxml;
    exports linkgame.client;
    exports linkgame.client.controller;
    opens linkgame.client.controller to javafx.fxml;
}
