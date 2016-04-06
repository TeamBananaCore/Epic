package bananacore.epic.controllers;

import bananacore.epic.Constants;
import bananacore.epic.DatabaseManager;
import bananacore.epic.interfaces.observers.SpeedInterface;
import bananacore.epic.models.SpeedSession;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.sql.Timestamp;

public class SpeedController implements SpeedInterface {

    @FXML
    AnchorPane speedPane;

    @FXML
    Text speedText;

    public Timestamp startOfSpeedSession;
    public int totalSpeedForComputation = 0;
    public int amountOfReadingsForComputation = 0;
    public int avgSpeed;
    public final int logInterval = 60;
    public int lastSpeed = 0;

    public void initialize(){
        Constants.PARSER.addToSpeedObservers(this);
    }

    public void updateVehicleSpeed(int value, Timestamp timestamp) {
        if(lastSpeed != value) {
            speedText.setText(String.valueOf(value));
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
                DatabaseManager.update();
                startOfSpeedSession = timestamp;
                totalSpeedForComputation = 0;
                amountOfReadingsForComputation = 0;
            }
        }
    }
}
