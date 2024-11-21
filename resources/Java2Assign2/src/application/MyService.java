package application;


import application.Player;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MyService implements Runnable{
//    ArrayList<Player> playerList=new ArrayList<>();
    private Scanner in1,in2;
    private PrintWriter out1,out2;
    Socket s1,s2;


    public MyService(Socket a1,Socket a2){
        s1=a1;
        s2=a2;
    }
    @Override
    public void run() {


        try {
            in1=new Scanner(s1.getInputStream());
            out1=new PrintWriter(s1.getOutputStream());
            in2=new Scanner(s2.getInputStream());
            out2=new PrintWriter(s2.getOutputStream());
                //todo: do the service


        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
