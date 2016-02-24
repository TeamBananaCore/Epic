package bananacore.epic;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class BrakeController implements Initializable {
    private BooleanProperty isBraking;
    private Timestamp start, end;
    private int speed;

    private BrakeThread brakingThread;

    @FXML private PerformanceBar breakBar;
    @FXML private Circle brakeLight;

    public void initialize(URL location, ResourceBundle resources) {
        brakingThread = new BrakeThread();
    }

    public void updateSpeed(int speed){
        this.speed = speed;
    }

    public void updateBrake(boolean isBraking, Timestamp timestamp){
        if (isBraking && !brakingThread.isActive()){
            brakingThread.setValues(timestamp, speed, this);
            brakeLight.getStyleClass().setAll("brake_light_on");
        } else if (isBraking){
            brakingThread.interrupt();
        }
    }

    public void updateView(int startSpeed, int endSpeed, long duration){
        // TODO Create BrakeSession and save to DB
        brakeLight.getStyleClass().setAll("brake_ligt_off");
    }
}
