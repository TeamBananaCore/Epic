package bananacore.epic;

import bananacore.epic.interfaces.BrakeInterface;
import bananacore.epic.interfaces.SpeedInterface;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class BrakeController implements Initializable, BrakeInterface, SpeedInterface {
    private BooleanProperty isBraking;
    private Timestamp start, end;
    private int speed;

    private BrakeThread brakingThread;

    @FXML private PerformanceBar brakeBar;
    @FXML private Circle brakeLight;

    private double optimalSpeedReduction;

    public void initialize(URL location, ResourceBundle resources) {
        Constants.PARSER.addToBrakeObserver(this);
        Constants.PARSER.addToSpeedObservers(this);
        optimalSpeedReduction = 10.0 / (50.0 * Constants.WEIGHT);
    }

    public void updateVehicleSpeed(int speed, Timestamp timestamp){
        this.speed = speed;
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
        double performance = duration / ((double)(startSpeed - endSpeed) * Constants.WEIGHT);
        System.out.println("Optimal: " + optimalSpeedReduction);
        System.out.println("Actual: " + performance);
        brakeBar.setValue(100* performance/optimalSpeedReduction);
        brakeLight.getStyleClass().setAll("brake_light_off");
        // TODO Create BrakeSession and save to DB
    }

    public int getSpeed() {
        return speed;
    }
}
