package application.controller;

import application.MyClient;
import application.MyServer;
import application.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;


//import static application.MyClient.login;

public class Page1Controller implements Initializable{
//    SimpleStringProperty usernameProperty=new SimpleStringProperty();
//    SimpleStringProperty passwordProperty=new SimpleStringProperty();
//    SimpleStringProperty command=new SimpleStringProperty();


    private static final int PLAY_1 = 1;
    private static final int PLAY_2 = 2;
    private static final int EMPTY = 0;
    private static final int BOUND = 90;
    private static final int OFFSET = 15;

    private static boolean TURN = false;

    private static final int[][] chessBoard = new int[3][3];
    private static final boolean[][] flag = new boolean[3][3];

    private static boolean gameOver=false;
    private static boolean tie=false;
    private AnchorPane baseSquare;

    Socket s;
    InputStream inputStream;
    OutputStream outputStream;
    Scanner in;
    PrintWriter out;
    int sceneNum=1;

    @FXML
    private Button SignUp;

    @FXML
    private Button LogIn;

    @FXML
    void LogIn(ActionEvent event) {
        Stage loginStage=new Stage();
        loginStage.setResizable(false);
        loginStage.setHeight(300);
        loginStage.setWidth(500);
        loginStage.initModality(Modality.APPLICATION_MODAL);
        loginStage.initStyle(StageStyle.UTILITY);
        loginStage.setTitle("Log In");

        Label label1=new Label("Username");
        label1.setLayoutX(80);
        label1.setLayoutY(50);
        Label label2=new Label("Password");
        label2.setLayoutX(80);
        label2.setLayoutY(100);
        TextField textField1=new TextField();
        textField1.setLayoutX(200);
        textField1.setLayoutY(50);
        TextField textField2=new TextField();
        textField2.setLayoutX(200);
        textField2.setLayoutY(100);
        Button ok=new Button("Log In");
        ok.setLayoutX(150);
        ok.setLayoutY(200);

//        usernameProperty.bind(textField1.textProperty());
//        passwordProperty.bind(textField2.textProperty());
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String userName=textField1.getText();
                String password=textField2.getText();
                System.out.println(userName+password);
                out.println("Login "+userName+" "+password);
                out.flush();
                String ans=in.next();
                System.out.println(ans);
                if("fail".equals(ans)){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.titleProperty().set("Error");
                    alert.headerTextProperty().set("The username or password is incorrect.");
                    alert.showAndWait();
                }else {
                    System.out.println("登录成功!");
                    int total=Integer.parseInt(in.next());
                    int won=Integer.parseInt(in.next());
                    System.out.println("你好，"+userName+"！你的战绩："+won+"胜 "+total+"负");
                    //todo: 换场景！！
                    loginStage.close();
                    sceneNum=2;

                    out.println("Prepared");
                    out.flush();
                    String ans2="";
                    in.nextLine();
                    if(in.hasNext())
                        ans2=in.nextLine();
                    if("GameBegin".equals(ans2)){
                        //棋盘出现，开始下棋
                        System.out.println("对局开始");
                        Stage board=new Stage();
                        board.setWidth(600);
                        board.setHeight(400);
                        Rectangle game_panel=new Rectangle();
                        baseSquare=new AnchorPane();
                        baseSquare.setPrefWidth(300);
                        baseSquare.setPrefHeight(300);
                        game_panel.setWidth(270);
                        game_panel.setHeight(270);
//                    game_panel.setStroke(Paint.valueOf("BLACK"));
                        game_panel.setStrokeType(StrokeType.INSIDE);
                        Line line1=new Line();
                        line1.setEndX(170);
                        line1.setStartX(-100);
                        line1.setLayoutX(115);
                        line1.setLayoutY(105);
                        Line line2=new Line();
                        line2.setEndX(170);
                        line2.setStartX(-100);
                        line2.setLayoutX(115);
                        line2.setLayoutY(195);
                        Line line3=new Line();
                        line3.setEndX(170);
                        line3.setStartX(-100);
                        line3.setLayoutX(70);
                        line3.setLayoutY(150);
                        line3.setRotate(270.0);
                        Line line4=new Line();
                        line4.setEndX(170);
                        line4.setStartX(-100);
                        line4.setLayoutX(160);
                        line4.setLayoutY(151);
                        line4.setRotate(90.0);
                        baseSquare.getChildren().addAll(game_panel,line1,line2,line3,line4);
                        Scene boardScene=new Scene(baseSquare);
                        board.setScene(boardScene);
                        board.show();

                        game_panel.setOnMouseClicked(aevent -> {
                            int x = (int) (aevent.getX() / BOUND);
                            int y = (int) (aevent.getY() / BOUND);
                            if (refreshBoard(x, y)) {
                                out.println(TURN+" "+x+" "+y);
                                out.flush();

                                if(gameOver){
                                    //gameover
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.titleProperty().set("Win");
                                    alert.headerTextProperty().set("You Win.");
                                    alert.showAndWait();
                                    board.close();
                                }
                                TURN = !TURN;
                            }
                        });
                    }else{
                        System.out.println("Please wait for another player...请等待对手上线...");
                        if(in.hasNext())
                            ans=in.next();
                        if("GameBegin".equals(ans)){
                            System.out.println("对局开始");
                            TURN=!TURN;

                            Stage board=new Stage();
                            board.setWidth(600);
                            board.setHeight(400);
                            Rectangle game_panel=new Rectangle();
                            baseSquare=new AnchorPane();
                            baseSquare.setPrefWidth(300);
                            baseSquare.setPrefHeight(300);
                            game_panel.setWidth(270);
                            game_panel.setHeight(270);
//                    game_panel.setStroke(Paint.valueOf("BLACK"));
                            game_panel.setStrokeType(StrokeType.INSIDE);
                            Line line1=new Line();
                            line1.setEndX(170);
                            line1.setStartX(-100);
                            line1.setLayoutX(115);
                            line1.setLayoutY(105);
                            Line line2=new Line();
                            line2.setEndX(170);
                            line2.setStartX(-100);
                            line2.setLayoutX(115);
                            line2.setLayoutY(195);
                            Line line3=new Line();
                            line3.setEndX(170);
                            line3.setStartX(-100);
                            line3.setLayoutX(70);
                            line3.setLayoutY(150);
                            line3.setRotate(270.0);
                            Line line4=new Line();
                            line4.setEndX(170);
                            line4.setStartX(-100);
                            line4.setLayoutX(160);
                            line4.setLayoutY(151);
                            line4.setRotate(90.0);
                            baseSquare.getChildren().addAll(game_panel,line1,line2,line3,line4);
                            Scene boardScene=new Scene(baseSquare);
                            board.setScene(boardScene);
                            board.show();

                            game_panel.setOnMouseClicked(aevent -> {
                                int x = (int) (aevent.getX() / BOUND);
                                int y = (int) (aevent.getY() / BOUND);
                                if (refreshBoard(x, y)) {
                                    if(gameOver){
                                        //gameover
                                        if(!tie) {
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                            alert.titleProperty().set("Win");
                                            alert.headerTextProperty().set("You Win.");
                                            alert.showAndWait();
                                            board.close();
                                        }
                                        else {
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                            alert.titleProperty().set("Tie");
                                            alert.headerTextProperty().set("A Tie!");
                                            alert.showAndWait();
                                            board.close();
                                        }
                                    }
                                    TURN = !TURN;
                                }
                            });
                        }

                    }

                }
                loginStage.close();
            }
        });

        AnchorPane root=new AnchorPane();
        root.getChildren().addAll(ok,label1,label2,textField1,textField2);
        Scene scene=new Scene(root);
        loginStage.setScene(scene);
        loginStage.show();
    }

    @FXML
    void SignUp(ActionEvent event) {
        Stage signupStage=new Stage();
        signupStage.setResizable(false);
        signupStage.setHeight(300);
        signupStage.setWidth(500);
        signupStage.initStyle(StageStyle.UTILITY);
        signupStage.initModality(Modality.APPLICATION_MODAL);
        signupStage.setTitle("Sign Up");
        signupStage.show();

        Label label1=new Label("Username");
        label1.setLayoutX(80);
        label1.setLayoutY(50);
        Label label2=new Label("Password");
        label2.setLayoutX(80);
        label2.setLayoutY(100);
        TextField textField1=new TextField();
        textField1.setLayoutX(200);
        textField1.setLayoutY(50);
        TextField textField2=new TextField();
        textField2.setLayoutX(200);
        textField2.setLayoutY(100);
        Button ok=new Button("Sign Up");
        ok.setLayoutX(150);
        ok.setLayoutY(200);

        ok.setOnAction(event1 -> {
            String userName=textField1.getText();
            String password=textField2.getText();
            System.out.println(userName+password);
            out.println("Signup "+userName+" "+password);
            out.flush();
            String ans=in.next();
            System.out.println(ans);
            if("fail".equals(ans)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.titleProperty().set("Error");
                alert.headerTextProperty().set("This username already exists.");
                alert.showAndWait();
            }else {
                System.out.println("注册成功!");
                //todo: 换场景！！
                sceneNum=2;

                out.println("Prepared");
                out.flush();
                String ans2=in.nextLine();

                if("Game begin".equals(ans2)){
                    //棋盘出现，开始下棋
                    System.out.println("对局开始");
                    Stage board=new Stage();
                    board.setWidth(600);
                    board.setHeight(400);
                    Rectangle game_panel=new Rectangle();
                    baseSquare=new AnchorPane();
                    baseSquare.setPrefWidth(300);
                    baseSquare.setPrefHeight(300);
                    game_panel.setWidth(270);
                    game_panel.setHeight(270);
//                    game_panel.setStroke(Paint.valueOf("BLACK"));
                    game_panel.setStrokeType(StrokeType.INSIDE);
                    Line line1=new Line();
                    line1.setEndX(170);
                    line1.setStartX(-100);
                    line1.setLayoutX(115);
                    line1.setLayoutY(105);
                    Line line2=new Line();
                    line2.setEndX(170);
                    line2.setStartX(-100);
                    line2.setLayoutX(115);
                    line2.setLayoutY(195);
                    Line line3=new Line();
                    line3.setEndX(170);
                    line3.setStartX(-100);
                    line3.setLayoutX(70);
                    line3.setLayoutY(150);
                    line3.setRotate(270.0);
                    Line line4=new Line();
                    line4.setEndX(170);
                    line4.setStartX(-100);
                    line4.setLayoutX(160);
                    line4.setLayoutY(151);
                    line4.setRotate(90.0);
                    baseSquare.getChildren().addAll(game_panel,line1,line2,line3,line4);
                    Scene boardScene=new Scene(baseSquare);
                    board.setScene(boardScene);
                    board.show();

                    game_panel.setOnMouseClicked(aevent -> {
                        int x = (int) (aevent.getX() / BOUND);
                        int y = (int) (aevent.getY() / BOUND);
                        if (refreshBoard(x, y)) {
                            if(gameOver){
                                //gameover
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.titleProperty().set("Win");
                                alert.headerTextProperty().set("You Win.");
                                alert.showAndWait();
                                board.close();
                            }
                            TURN = !TURN;
                        }
                    });
                }else System.out.println("Please wait for another player...请等待对手上线...");

            }
            signupStage.close();
        });

        AnchorPane root=new AnchorPane();
        root.getChildren().addAll(ok,label1,label2,textField1,textField2);
        Scene scene=new Scene(root);
        signupStage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            s=new Socket("localhost",8886);
            inputStream=s.getInputStream();
            outputStream=s.getOutputStream();
            in=new Scanner(inputStream);
            out=new PrintWriter(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int changeScene(){
        return sceneNum;
    }

    private boolean refreshBoard (int x, int y) {
        if (chessBoard[x][y] == EMPTY) {
            int tmp=chessBoard[x][y] = TURN ? PLAY_1 : PLAY_2;


            judgeOver(x,y,tmp);//judge gameover
            drawChess();
            return true;
        }
        return false;
    }

    public void judgeOver(int x,int y,int tmp){
        tie=true;
        for(int i=0;i<3;++i)
            for (int j=0;j<3;++j)
                if(chessBoard[i][j]==EMPTY)
                    tie=false;

        if(tie) {
            gameOver=true;
            return;
        }
        if(x==1 && y==1){
            if(chessBoard[x+1][y-1]==tmp && chessBoard[x-1][y+1]==tmp)
                gameOver=true;
            else if(chessBoard[x+1][y+1]==tmp && chessBoard[x-1][y-1]==tmp)
                gameOver=true;
            else if(chessBoard[x][y+1]==tmp && chessBoard[x][y-1]==tmp)
                gameOver=true;
            else if(chessBoard[x+1][y]==tmp && chessBoard[x-1][y]==tmp)
                gameOver=true;
        }
        else if(x==0 && y==0){
            if(chessBoard[x+1][y+1]==tmp && chessBoard[x+2][y+2]==tmp)
                gameOver=true;
            else if(chessBoard[x+1][y]==tmp && chessBoard[x+2][y]==tmp)
                gameOver=true;
            else if(chessBoard[x][y+1]==tmp && chessBoard[x][y+2]==tmp)
                gameOver=true;
        }
        else if(x==2 && y==0){
            if(chessBoard[x-1][y+1]==tmp && chessBoard[x-2][y+2]==tmp)
                gameOver=true;
            else if(chessBoard[x-1][y]==tmp && chessBoard[x-2][y]==tmp)
                gameOver=true;
            else if(chessBoard[x][y+1]==tmp && chessBoard[x][y+2]==tmp)
                gameOver=true;
        }
        else if(x==0 && y==2){
            if(chessBoard[x+1][y-1]==tmp && chessBoard[x+2][y-2]==tmp)
                gameOver=true;
            else if(chessBoard[x+1][y]==tmp && chessBoard[x+2][y]==tmp)
                gameOver=true;
            else if(chessBoard[x][y-1]==tmp && chessBoard[x][y-2]==tmp)
                gameOver=true;
        }
        else if(x==2 && y==2){
            if(chessBoard[x-1][y-1]==tmp && chessBoard[x-2][y-2]==tmp)
                gameOver=true;
            else if(chessBoard[x-1][y]==tmp && chessBoard[x-2][y]==tmp)
                gameOver=true;
            else if(chessBoard[x][y-1]==tmp && chessBoard[x][y-2]==tmp)
                gameOver=true;
        }
        else if(x==0 && y==1){
            if(chessBoard[x][y-1]==tmp && chessBoard[x][y+1]==tmp)
                gameOver=true;
            else if(chessBoard[x+1][y]==tmp && chessBoard[x+2][y]==tmp)
                gameOver=true;
        }
        else if(x==2 && y==1){
            if(chessBoard[x][y-1]==tmp && chessBoard[x][y+1]==tmp)
                gameOver=true;
            else if(chessBoard[x-1][y]==tmp && chessBoard[x-2][y]==tmp)
                gameOver=true;
        }
        else if(x==1 && y==0){
            if(chessBoard[x-1][y]==tmp && chessBoard[x+1][y]==tmp)
                gameOver=true;
            else if(chessBoard[x][y+1]==tmp && chessBoard[x][y+2]==tmp)
                gameOver=true;
        }
        else if(x==1 && y==2){
            if(chessBoard[x-1][y]==tmp && chessBoard[x+1][y]==tmp)
                gameOver=true;
            else if(chessBoard[x][y-1]==tmp && chessBoard[x][y-2]==tmp)
                gameOver=true;
        }
    }

    private void drawChess () {
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[0].length; j++) {
                if (flag[i][j]) {
                    // This square has been drawing, ignore.
                    continue;
                }
                switch (chessBoard[i][j]) {
                    case PLAY_1:
                        drawCircle(i, j);
                        break;
                    case PLAY_2:
                        drawLine(i, j);
                        break;
                    case EMPTY:
                        // do nothing
                        break;
                    default:
                        System.err.println("Invalid value!");
                }
            }
        }
    }

    private void drawCircle (int i, int j) {
        Circle circle = new Circle();
        baseSquare.getChildren().add(circle);
        circle.setCenterX(i * BOUND + BOUND / 2.0 + OFFSET);
        circle.setCenterY(j * BOUND + BOUND / 2.0 + OFFSET);
        circle.setRadius(BOUND / 2.0 - OFFSET / 2.0);
        circle.setStroke(Color.RED);
        circle.setFill(Color.TRANSPARENT);
        flag[i][j] = true;
    }

    private void drawLine (int i, int j) {
        Line line_a = new Line();
        Line line_b = new Line();
        baseSquare.getChildren().add(line_a);
        baseSquare.getChildren().add(line_b);
        line_a.setStartX(i * BOUND + OFFSET * 1.5);
        line_a.setStartY(j * BOUND + OFFSET * 1.5);
        line_a.setEndX((i + 1) * BOUND + OFFSET * 0.5);
        line_a.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_a.setStroke(Color.BLUE);

        line_b.setStartX((i + 1) * BOUND + OFFSET * 0.5);
        line_b.setStartY(j * BOUND + OFFSET * 1.5);
        line_b.setEndX(i * BOUND + OFFSET * 1.5);
        line_b.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_b.setStroke(Color.BLUE);
        flag[i][j] = true;
    }
}