package bananacore.epic;

import bananacore.epic.interfaces.OdometerInterface;
import bananacore.epic.interfaces.SpeedInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.sql.Timestamp;

public class SettingsController extends SetupController implements SpeedInterface {

    @FXML private CheckBox displayFuelCheckbox;
    @FXML private CheckBox displaySpeedCheckbox;
    @FXML private Button decreaseIntervalButton;
    @FXML private Button increaseIntervalButton;
    @FXML private Label intervalLabel;
    @FXML private ScrollPane scrollPane;

    private int interval = 2;
    private boolean fuelDisplay = false;
    private boolean speedDisplay = false;

    public void initialize(){
        intervalLabel.setText(addSpace(interval));
        scrollPane.setFitToWidth(true);
        Constants.PARSER.addToSpeedObservers(this);
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
        intervalLabel.setText(addSpace(interval));
    }

    @FXML
    public void decreaseInterval() {
        if(interval - 1 > 1){
            interval--;
        }
        intervalLabel.setText(addSpace(interval));
    }

    @FXML
    public void save(){
        fuelDisplay = displayFuelCheckbox.isSelected();
        speedDisplay = displaySpeedCheckbox.isSelected();
        interval = Integer.parseInt(intervalLabel.getText().trim());

        //updates local version. (the rest is updated on press.)
        Constants.settingsEPIC.setFueldisplay(fuelDisplay);
        Constants.settingsEPIC.setSpeeddisplay(speedDisplay);
        Constants.settingsEPIC.setScreeninterval(interval);

        //local to db
        DatabaseManager.updateSettings(Constants.settingsEPIC);
        showMain();


    }

    @FXML
    public void cancel() {
        showMain();
    }

    private void showMain() {
        try {
            BorderPane root = new BorderPane();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main.fxml"));
            loader.setRoot(root);
            loader.load();
            Constants.SCENE.setRoot(root);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateVehicleSpeed(int value, Timestamp timestamp) {
        if(value != 0){
            //showMain();
        }
    }
}
