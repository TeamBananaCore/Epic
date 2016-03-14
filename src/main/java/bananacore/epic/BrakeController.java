package bananacore.epic;

import bananacore.epic.interfaces.BrakeInterface;
import bananacore.epic.interfaces.SpeedInterface;
import bananacore.epic.models.BrakeSession;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
//import javafx.scene.control.Spinner;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class BrakeController implements Initializable, BrakeInterface, SpeedInterface {

    private Timestamp timestamp;
    private int speed;

    private BrakeThread brakingThread;

    @FXML private PerformanceBar brakeBar;
    @FXML private Circle brakeLight;

    public void initialize(URL location, ResourceBundle resources) {
        Constants.PARSER.addToBrakeObserver(this);
        Constants.PARSER.addToSpeedObservers(this);
    }

    public void updateVehicleSpeed(int speed, Timestamp timestamp){
        this.speed = speed;
        this.timestamp = timestamp;

    }

    public void updateBrakePedalStatus(boolean isBraking, Timestamp timestamp){
        if (isBraking && brakingThread == null){
            brakingThread = new BrakeThread();
            brakingThread.setValues(timestamp, speed, this);
            brakeLight.getStyleClass().setAll("brake_light_on");
            brakingThread.start();
        } else if (isBraking){
            brakingThread.interrupt();
        } else if (brakingThread != null && !brakingThread.isActive()){
            brakingThread = null;
        }
    }

    public void updateView(int startSpeed, int endSpeed, long duration){
        brakeBar.setValue(Constants.calculateBrakePerformance(startSpeed,endSpeed,duration));
        brakeLight.getStyleClass().setAll("brake_light_off");
        BrakeSession session = new BrakeSession(startSpeed, endSpeed, timestamp, (int) duration);
        DatabaseManager.insertBrakeSession(session);
    }

    public int getSpeed() {
        return speed;
    }
}
