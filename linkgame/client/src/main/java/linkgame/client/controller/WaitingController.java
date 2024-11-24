package linkgame.client.controller;

import javafx.fxml.FXML;
import lombok.Setter;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-24 12:17
 */
@Setter
public class WaitingController {
    private MainController mainController;

    @FXML
    public void backToMainPage() {
        mainController.closeConnection();
        mainController.showMainPage();
    }
}
