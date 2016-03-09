package bananacore.epic;

import bananacore.epic.interfaces.SpeedInterface;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.sql.Timestamp;

public class SpeedController implements SpeedInterface {

    @FXML
    AnchorPane speedPane;

    @FXML
    Text speedText;

    public void initialize(){
        Constants.PARSER.addToSpeedObservers(this);
    }

    public void updateVehicleSpeed(int value, Timestamp timestamp) {
        speedText.setText(String.valueOf(value) + " km/h");
    }
}
