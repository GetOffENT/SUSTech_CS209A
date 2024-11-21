package application.controller;

import application.MyClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class Page2Controller implements Initializable {


    @FXML
    private Button StartGame;

    @FXML
    private ListView<String> PlayerList;

    @FXML
    private Label Greeting;

    @FXML
    void StartGame(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Button getStartGame(){
        return StartGame;
    }
}
