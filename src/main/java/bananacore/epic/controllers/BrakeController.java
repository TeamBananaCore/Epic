package bananacore.epic.controllers;

import bananacore.epic.BrakeThread;
import bananacore.epic.Constants;
import bananacore.epic.DatabaseManager;
import bananacore.epic.customcontrols.PerformanceBar;
import bananacore.epic.interfaces.observers.BrakeInterface;
import bananacore.epic.interfaces.observers.SpeedInterface;
import bananacore.epic.models.BrakeSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
//import javafx.scene.control.Spinner;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class BrakeController implements Initializable, BrakeInterface, SpeedInterface {

    private Timestamp timestamp;
    private int speed;
    private int startSpeed;
    private boolean braking = false;

    private BrakeThread brakingThread;

    @FXML private PerformanceBar brakeBar;
    @FXML private Circle brakeLight;

    public void initialize(URL location, ResourceBundle resources) {
        Constants.PARSER.addToBrakeObservers(this);
        Constants.PARSER.addToSpeedObservers(this);
    }

    public void updateVehicleSpeed(int speed, Timestamp timestamp){
        this.speed = speed;
        if(speed == 0) updateBrakePedalStatus(false, timestamp);
    }

//    public void updateBrakePedalStatus(boolean isBraking, Timestamp timestamp){
//        if (isBraking && brakingThread == null){
//            brakingThread = new BrakeThread();
//            brakingThread.setValues(timestamp, speed, this);
//            brakeLight.getStyleClass().setAll("brake_light_on");
//            brakingThread.start();
//        } else if (isBraking){
//            brakingThread.interrupt();
//        } else if (brakingThread != null && !brakingThread.isActive()){
//            brakingThread = null;
//        }
//    }

    public void updateBrakePedalStatus(boolean isBraking, Timestamp timestamp){
        // Started braking
        if (!braking && isBraking && brakingThread == null){
            this.timestamp = timestamp;
            braking = true;
            startSpeed = speed;
            brakeLight.getStyleClass().setAll("brake_light_on");
        }
        // Stopped braking
        else if(braking && !isBraking && brakingThread == null){
            long duration = (timestamp.getTime() - this.timestamp.getTime()) / 1000;
            braking = false;
            brakeLight.getStyleClass().setAll("brake_light_off");
            brakingThread = new BrakeThread(this.timestamp, timestamp, startSpeed, speed, this);
            brakingThread.start();
        }
        // Started braking after pause
        if (!braking && isBraking && brakingThread != null && brakingThread.isActive()){
            braking = true;
            brakingThread.interrupt();
            brakingThread = null;
            brakeLight.getStyleClass().setAll("brake_light_on");
        }
    }

    public void updateView(int startSpeed, int endSpeed, long duration){
        if (brakingThread == null || !brakingThread.isActive()) {
            brakingThread = null;
            brakeBar.setValue(Constants.calculateBrakePerformance(startSpeed, endSpeed, duration));
            BrakeSession session = new BrakeSession(startSpeed, endSpeed, timestamp, (int) duration);
            DatabaseManager.insertBrakeSession(session);
        }
    }

    public int getSpeed() {
        return speed;
    }
}
