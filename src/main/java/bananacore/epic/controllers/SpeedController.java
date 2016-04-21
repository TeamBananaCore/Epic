package bananacore.epic.controllers;

import bananacore.epic.Constants;
import bananacore.epic.DatabaseManager;
import bananacore.epic.interfaces.observers.SpeedInterface;
import bananacore.epic.models.SettingsEPIC;
import bananacore.epic.models.SpeedSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.sql.Timestamp;
import java.util.Observable;
import java.util.Observer;

public class SpeedController implements SpeedInterface, Observer {

    @FXML
    Pane speedPane;

    @FXML
    Label speedText;

    private Timestamp startOfSpeedSession;
    private int totalSpeedForComputation = 0;
    private int amountOfReadingsForComputation = 0;
    private int avgSpeed;
    private final int logInterval = 60;
    private int lastSpeed = 0;
    private boolean displayingSpeed = true;

    public void initialize(){
        Constants.PARSER.addToSpeedObservers(this);
        displayingSpeed = Constants.settingsEPIC.getSpeeddisplay();
        speedText.setVisible(displayingSpeed);
        Constants.settingsEPIC.addObserver(this);
    }

    public void updateVehicleSpeed(int value, Timestamp timestamp) {
        if(lastSpeed != value) {
            if (displayingSpeed){
                speedText.setText(String.valueOf(value) + " km/t");
            }
            lastSpeed = value;
            totalSpeedForComputation += value;
            amountOfReadingsForComputation++;

            if (startOfSpeedSession == null) {
                startOfSpeedSession = timestamp;
                return;
            }

            int duration = (int) (timestamp.getTime() - startOfSpeedSession.getTime()) / 1000;

            if (duration > logInterval) {
                avgSpeed = totalSpeedForComputation / amountOfReadingsForComputation;
                SpeedSession session = new SpeedSession(avgSpeed, startOfSpeedSession, duration);
                DatabaseManager.insertSpeedSession(session);
                startOfSpeedSession = timestamp;
                totalSpeedForComputation = 0;
                amountOfReadingsForComputation = 0;
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        displayingSpeed = Constants.settingsEPIC.getSpeeddisplay();
        speedText.setVisible(displayingSpeed);
    }
}
