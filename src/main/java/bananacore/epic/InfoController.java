package bananacore.epic;


import bananacore.epic.interfaces.SpeedInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.sql.Timestamp;

public class InfoController implements SpeedInterface{

    private Timestamp lastTime;
    private boolean displayFuel = true;
    private boolean displaySpeed = true;
    private int switchDelay = 2000;

    @FXML
    BorderPane fuel;

    @FXML
    AnchorPane speed;

    @FXML
    BorderPane infoSettingsPane;

    @FXML
    CheckBox displayFuelCheckbox;

    @FXML
    CheckBox displaySpeedCheckbox;

    @FXML
    Button saveSettingsButton;

    public void infoSettings(){
        infoSettingsPane.visibleProperty().set(true);

    }

    public void updateSettings(){
        displayFuel = displayFuelCheckbox.isSelected();
        displaySpeed = displaySpeedCheckbox.isSelected();
        infoSettingsPane.visibleProperty().set(false);
    }

    public void initialize(){
        speed.visibleProperty().set(false);
    }

    public void updateDisplay(boolean displayFuel, boolean displaySpeed, int switchDelay){
        this.displayFuel = displayFuel;
        this.displaySpeed = displaySpeed;
        this.switchDelay = switchDelay;
        if(!displayFuel){
            fuel.visibleProperty().set(false);
        }
        if(!displaySpeed){
            fuel.visibleProperty().set(false);
        }
    }

    @Override
    public void updateVehicleSpeed(int value, Timestamp timestamp) {
        if(lastTime == null){
            lastTime = timestamp;
        } else {
            if(timestamp.getTime() - lastTime.getTime() > switchDelay){
                if(speed.visibleProperty().get() && displayFuel){
                    speed.visibleProperty().set(false);
                    fuel.visibleProperty().set(true);
                } else if (displaySpeed){
                    speed.visibleProperty().set(true);
                    fuel.visibleProperty().set(false);
                }
            }
        }

    }
}
