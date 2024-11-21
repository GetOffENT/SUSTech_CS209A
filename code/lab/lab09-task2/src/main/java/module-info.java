module com.example.broadcastchat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires fastjson;

    opens com.example.broadcastchat to javafx.fxml;
    exports com.example.broadcastchat;
}
