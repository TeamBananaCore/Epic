package bananacore.epic;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class SettingsController {

    @FXML
    GridPane infoSettingsPane;

    @FXML
    CheckBox displayFuelCheckbox;

    @FXML
    CheckBox displaySpeedCheckbox;

    @FXML
    Button saveSettingsButton;

    @FXML
    Button infoSettingsButton;

    @FXML
    Text intervalText;

    @FXML
    Button decreaseIntervalButton;

    @FXML
    Button increaseIntervalButton;

}
