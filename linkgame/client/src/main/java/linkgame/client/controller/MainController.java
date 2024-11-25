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
import java.util.List;
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

    @Getter
    private Stage stage;

    private Parent inputRoot;
    private Parent waitingRoot;
    private Parent gameRoot;
    private Parent mainPageRoot;

    private InputController inputController;
    private PickController pickController;
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
    private Scene pickScene;

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

            // 预加载 PickController
            FXMLLoader pickLoader = new FXMLLoader(getClass().getResource("PickPage.fxml"));
            Parent pickRoot = pickLoader.load();
            pickController = pickLoader.getController();
            pickController.setMainController(this);
            pickScene = new Scene(pickRoot);

            // 预加载 WaitingController
            FXMLLoader waitingLoader = new FXMLLoader(getClass().getResource("WaitingPage.fxml"));
            waitingRoot = waitingLoader.load();
            WaitingController waitingController = waitingLoader.getController();
            waitingController.setMainController(this);
            waitingScene = new Scene(waitingRoot);

            // 预加载 GameController
            FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("GamePage.fxml"));
            gameRoot = gameLoader.load();
            gameController = gameLoader.getController();
            gameController.setMainController(this);
            gameController.setClientService(new ClientService(this));
            gameScene = new Scene(gameRoot);

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

    public void showMainPage() {
        closeConnection();
        mainPageController.loadAllUsers();
        stage.setScene(mainPageScene);
        stage.setTitle("主页");
    }

    public void showMainPage(int userId, String nickname, String avatar) {
        gameController.getClientService().setUserId(userId);
        this.userId = userId;
        this.nickname = nickname;
        this.avatar = avatar;
        mainPageController.setUserInfo(userId, nickname, avatar);

        // 切换到主页面之前尝试重连
        reconnectToServer();
    }

    public void showInputPage() {
        closeConnection();
        stage.setScene(inputScene);
        stage.setTitle("棋盘大小");
    }

    public void showPickPage() {
        stage.setScene(pickScene);
        stage.setTitle("匹配中");
    }

    public void showWaitingPage() {
        stage.setScene(waitingScene);
        stage.setTitle("匹配中");
    }

    public void showGamePage() {
        stage.setScene(gameScene);
        stage.setTitle("连连看");
    }

    public void connectToServer(int rows, int cols, boolean isRandom) {
        gameController.connectToServer(rows, cols, isRandom, false);
    }

    public void reconnectToServer() {
        gameController.connectToServer(0, 0, true, true);
    }

    public void connectToUser(String userId) {
        gameController.connectToUser(userId);
    }

    public void loadUsers(List<String> userIds) {
        pickController.loadUsers(userIds);
    }

    public void closeConnection() {
        if (gameController != null && gameController.getClientService() != null) {
            gameController.getClientService().setNormalClose(true);
            gameController.getClientService().close();
        }
    }
}
