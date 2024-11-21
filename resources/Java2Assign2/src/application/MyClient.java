package application;

import application.controller.Page1Controller;
import application.controller.Page2Controller;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class MyClient extends Application {
//            Stage secondStage=new Stage();

//    public static Stage secondStage=new Stage();
//    public static Scene scene1,scene2,scene3;
//    static {
//        try {
//            scene1=switchScene("page1.fxml");
//            scene2=switchScene("page2.fxml");
//            scene3=switchScene("page3.fxml");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public Scene switchScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setLocation(MyClient.class.getClassLoader().getResource(fxml));
        fxmlLoader.setLocation(getClass().getClassLoader().getResource(fxml));
        Pane root = fxmlLoader.load();
        return new Scene(root);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getClassLoader().getResource("Page1.fxml"));
//            Pane root = fxmlLoader.load();
//            primaryStage.setTitle("Tic Tac Toe");
//            primaryStage.setScene(new Scene(root));
//            primaryStage.setResizable(false);
//            primaryStage.show();

            Stage secondStage=new Stage();

            FXMLLoader fxmlLoader1=new FXMLLoader();
            fxmlLoader1.setLocation(getClass().getClassLoader().getResource("page1.fxml"));
            Pane root1=fxmlLoader1.load();
            Scene scene1=new Scene(root1);
            Page1Controller controller1=fxmlLoader1.getController();
//
            FXMLLoader fxmlLoader2=new FXMLLoader();
            fxmlLoader2.setLocation(getClass().getClassLoader().getResource("page2.fxml"));
            Pane root2=fxmlLoader2.load();
            Scene scene2=new Scene(root2);
            Page2Controller controller2=fxmlLoader2.getController();

//            FXMLLoader fxmlLoader3=new FXMLLoader();
//            fxmlLoader3.setLocation(getClass().getClassLoader().getResource("page3.fxml"));
//            Pane root3=fxmlLoader3.load();
//            Scene scene3=new Scene(root3);
//            Page3Controller controller3=fxmlLoader3.getController();
//

            Scene scene=scene1;
            scene.addEventHandler(Event.ANY, new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    EventType<? extends Event> type = event.getEventType();
                    String name = type.getName();

//                KeyEvent.KEY_PRESSED
//                    switch (name) {
//                        case "MOUSE_CLICKED":
//                            break;
//                        case "KEY_PRESSED":
//                            // 切换到第二个场景
//                            primaryStage.setScene(scene2);
//                            break;
//                    }
                    Scene ascene=scene1;
                    if(name=="KEY_PRESSED"){
                        switch (controller1.changeScene()) {
                            case 1:
                                ascene=scene1;
                                break;
                            case 2:
                                ascene=scene2;
                                break;
//                            case 3:
//                                ascene=scene3;
//                                break;
                        }
                        secondStage.setScene(ascene);
                    }
                    event.consume();
                }
            });

            secondStage.setTitle("Tic Tac Toe");
            secondStage.setScene(scene1);
            secondStage.setResizable(false);
            secondStage.show();
//
//
//            Socket s=new Socket("localhost",8886);
//            InputStream inputStream=s.getInputStream();
//            OutputStream outputStream=s.getOutputStream();
//            Scanner in=new Scanner(inputStream);
//            PrintWriter out=new PrintWriter(outputStream);
//.......................................
//            SimpleStringProperty commandProperty=new SimpleStringProperty();
//            while (true){
////                while (controller1.)
//                Thread.sleep(1000);
//                if(currentScene==1){
//                    commandProperty.bind(controller1.getCommand());
//                    out.println(commandProperty.get());
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        launch(args);

//        InputStream inputStream;
//        OutputStream outputStream;
//        Scanner in=null;
//        PrintWriter out = null;
//        try(Socket s=new Socket("localhost",8886)){
//            inputStream=s.getInputStream();
//            outputStream=s.getOutputStream();
//            in=new Scanner(inputStream);
//            out=new PrintWriter(outputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        ServerSocket scene1Server=new ServerSocket(8881);
//        Socket scene1Socket=scene1Server.accept();
//        System.out.println("Connected to scene1");
//        InputStream inputStream1= scene1Socket.getInputStream();
//        OutputStream outputStream1=scene1Socket.getOutputStream();
//        Scanner in1=new Scanner(inputStream1);
//        PrintWriter out1=new PrintWriter(outputStream1);
//        while (in1.hasNext()){
//            String command1=in1.nextLine();
//            out.println(command1);//转发给MyServer
//            out.flush();
//            String ans1=in.next();//收到MyServer回复
//            out1.println(ans1);//将回复转发给Scene1
//            out1.flush();
//        }
    }
}
