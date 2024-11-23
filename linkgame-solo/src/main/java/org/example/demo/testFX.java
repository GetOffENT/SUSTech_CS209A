package org.example.demo;

import javafx.application.Application;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * @author Yuxian Wu
 * @version 1.0
 * @Description:
 * @Create: 2024-10-28 20:24
 */
public class testFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Label label = new Label("Hello, JavaFX!");
        BorderPane pane = new BorderPane(label);
//        MFXButton button = new MFXButton("Click me!");
//        button.setRippleColor(Color.RED);
//        button.setDepthLevel(DepthLevel.LEVEL1);
//        pane.setCenter(button);


        Scene scene = new Scene(pane, 640, 480);
//        button.setOnAction(e ->{
//            getHostServices().showDocument("https://www.google.com");
//        });

        // 设置光标
        scene.setCursor(new ImageCursor(new Image(Objects.requireNonNull(testFX.class.getResource("/org/example/demo/cursor.png")).toExternalForm())));

//        button.layoutXProperty().bind(scene.widthProperty().subtract(button.widthProperty()).divide(2));
//        button.layoutYProperty().bind(scene.heightProperty().subtract(button.heightProperty()).divide(2));
        stage.setScene(scene);
        stage.setTitle("test");
        stage.getIcons().add(new Image("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"));
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
