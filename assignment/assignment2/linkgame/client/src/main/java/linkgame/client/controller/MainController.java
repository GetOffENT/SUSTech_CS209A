package linkgame.client.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import linkgame.client.ClientService;
import linkgame.common.OkHttpUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-22 13:14
 */
@Slf4j
public class MainController {
    @Getter
    private Integer userId;
    @Getter
    private String nickname;
    @Getter
    private String avatar;

    private Stage stage;

    private Parent inputRoot;
    private Parent waitingRoot;
    private Parent gameRoot;
    private Parent mainPageRoot;

    private InputController inputController;
    private GameController gameController;
    private MainPageController mainPageController;
    private LoginController loginController;


    public MainController(Stage stage) {
        this.stage = stage;
        preloadControllers();

        stage.setOnCloseRequest(event -> {
            log.info("程序正在退出...");
            if (gameController != null && gameController.getClientService() != null) {
                gameController.getClientService().close();
            }
            if (userId != null) {
                OkHttpUtils.postForm("http://localhost:8080/user/logout",
                        Map.of("userId", userId.toString()), null);
            }
            System.exit(0);
        });
    }

    private Scene inputScene;
    private Scene waitingScene;
    private Scene gameScene;
    private Scene mainPageScene;
    private Scene loginScene;

    private void preloadControllers() {
        try {

            // 预加载 LoginController
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
            Parent loginRoot = loginLoader.load();
            loginController = loginLoader.getController();
            loginController.setMainController(this);
            loginScene = new Scene(loginRoot);

            // 预加载 MainPageController
            FXMLLoader mainPageLoader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
            mainPageRoot = mainPageLoader.load();
            mainPageController = mainPageLoader.getController();
            mainPageController.setMainController(this);
            mainPageScene = new Scene(mainPageRoot);


            // 预加载 InputController
            FXMLLoader inputLoader = new FXMLLoader(getClass().getResource("InputPage.fxml"));
            inputRoot = inputLoader.load();
            inputController = inputLoader.getController();
            inputController.setMainController(this);
            inputScene = new Scene(inputRoot);

            // 预加载 WaitingPage
            FXMLLoader waitingLoader = new FXMLLoader(getClass().getResource("WaitingPage.fxml"));
            waitingRoot = waitingLoader.load();
            waitingScene = new Scene(waitingRoot);

            // 预加载 ClientController
            FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("GamePage.fxml"));
            gameRoot = gameLoader.load();
            gameController = gameLoader.getController();
            gameController.setMainController(this);
            gameController.setClientService(new ClientService());
            gameScene = new Scene(gameRoot);

            // 设置初始页面为输入页面
//            stage.setScene(inputScene);
//            stage.setTitle("Input Page");
//            stage.show();
            // 设置初始页面为登录页面
            stage.setScene(loginScene);
            stage.setTitle("登录");
            stage.show();

            log.info("所有页面预加载完成");
        } catch (IOException e) {
            log.error("预加载页面时发生错误：", e);
            throw new RuntimeException("页面预加载失败");
        }
    }

    public void showLoginPage() {
        loginController.init();
        stage.setScene(loginScene);
        stage.setTitle("登录");
    }

    public void showMainPage(int userId, String nickname, String avatar) {
        this.userId = userId;
        this.nickname = nickname;
        this.avatar = avatar;
        mainPageController.setUserInfo(userId, nickname, avatar);
        mainPageController.loadAllUsers();
        stage.setScene(mainPageScene);
        stage.setTitle("主页");
    }

    public void showInputPage() {
        stage.setScene(inputScene);
        stage.setTitle("棋盘大小");
    }

    public void showWaitingPage() {
        stage.setScene(waitingScene);
        stage.setTitle("匹配中");
    }

    public void showGamePage(int[][] board) {
        gameController.loadGameScene(board);
        stage.setScene(gameScene);
        stage.setTitle("连连看");
    }

    public void connectToServer(int rows, int cols) {
        gameController.connectToServer(rows, cols);
    }
}
