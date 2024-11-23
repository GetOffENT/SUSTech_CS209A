module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires linkgame.common;
    requires lombok;
    requires slf4j.api;

    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}
