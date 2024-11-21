package com.example.broadcastchat;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ChatClientController {
    @FXML
    private TextField inputField;

    @FXML
    private TextArea displayArea;

    private String userName;

    private MessageSender messageSender;


    public void setUserName(String userName) {
        this.userName = userName;
        displayArea.appendText("Welcome, " + userName + "!\n");

    }


    @FXML
    public void initialize() {
        // this method is automatically invoked
        // once everything is loaded


        // display message when user presses Enter key
        inputField.setOnAction(e -> {
            String userInput = inputField.getText();
            if (!userInput.isEmpty()) {
                sendMessage(userInput);
                inputField.clear();
            }
        });
    }

    public void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 1234);
            messageSender = new MessageSender(socket, userName);

            // separate threads for read and write
            // because read doesn't need to wait for write
            new Thread(messageSender).start();
            new Thread(new MessageReceiver(socket, this)).start();
        } catch (IOException e) {
            displayArea.appendText("Disconnected from server");
        }
    }

    private void sendMessage(String message) {
        if (messageSender != null) {
            messageSender.sendMessage(message);
        }
    }

    public void displayMessage(String message) {
        Platform.runLater(() -> displayArea.appendText(message + "\n"));
    }

}



class MessageSender implements Runnable {
    private final PrintWriter out;
//    private final TextField inputField;
//    private final TextArea displayArea;
//    private final String userName;

    public MessageSender(Socket socket, String name/*, TextField inputField, TextArea displayArea, String userName*/) throws IOException {
        this.out = new PrintWriter(socket.getOutputStream(), true);
//        this.inputField = inputField;
//        this.displayArea = displayArea;
//        this.userName = userName;
        // Send name immediately
        out.println(name);
    }

    @Override
    public void run() {

    }

    public void sendMessage(String message) {
        out.println(message);
    }
}

class MessageReceiver implements Runnable {
    private final BufferedReader in;
    private final ChatClientController controller;

    public MessageReceiver(Socket socket, ChatClientController controller) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                controller.displayMessage(message); // 显示消息
            }
        } catch (IOException e) {
            System.out.println("Disconnected from server");
        }
    }
}
