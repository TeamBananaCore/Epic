package bananacore.epic.controllers;

import bananacore.epic.Constants;
import bananacore.epic.DatabaseManager;
import bananacore.epic.interfaces.observers.SpeedInterface;
import bananacore.epic.models.SpeedSession;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.sql.Timestamp;

public class SpeedController implements SpeedInterface {

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

    public void initialize(){
        Constants.PARSER.addToSpeedObservers(this);
    }

    public void updateVehicleSpeed(int value, Timestamp timestamp) {
        if(lastSpeed != value) {
            speedText.setText(String.valueOf(value) + " km/t");
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
}
