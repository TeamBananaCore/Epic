package bananacore.epic;

import bananacore.epic.interfaces.SpeedInterface;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.sql.Timestamp;

public class SpeedController implements SpeedInterface {

    @FXML
    Text speedText;

    public void updateVehicleSpeed(int value, Timestamp timestamp) {
        speedText.setText(String.valueOf(value));
    }
}
