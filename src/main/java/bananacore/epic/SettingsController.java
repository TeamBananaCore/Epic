package bananacore.epic;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

/**
 * Created by Carlo on 16.03.2016.
 */
public class SettingsController {

    @FXML private CheckBox displayFuelCheckbox;
    @FXML private CheckBox displaySpeedCheckbox;
    @FXML private Button decreaseIntervalButton;
    @FXML private Button increaseIntervalButton;
    @FXML private Label intervalText;
    @FXML private ScrollPane scrollPane;

    private int interval = 2;
    private boolean fuelDisplay = false;
    private boolean speedDisplay = false;

    public void initialize(){
        intervalText.setText(addSpace(interval));
        scrollPane.setFitToWidth(true);

    }

    public String addSpace(int x){
        if(x < 10) {
            return " " + x;
        }
        return "" + x;
    }

    @FXML
    public void increaseInterval(){
        interval++;
        intervalText.setText(addSpace(interval));
    }

    @FXML
    public void decreaseInterval() {
        if(interval - 1 > 1){
            interval--;
        }
        intervalText.setText(addSpace(interval));
    }

    @FXML
    public void save(){

    }
}
