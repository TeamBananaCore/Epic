package bananacore.epic;


import bananacore.epic.interfaces.SpeedInterface;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class InfoController implements SpeedInterface{

    private Timestamp lastTime;
    private boolean displayFuel = true;
    private boolean displaySpeed = false;
    private int switchDelay = 2000;

    @FXML
    BorderPane fuel;

    @FXML
    AnchorPane speed;

    @FXML
    GridPane infoSettingsPane;

    @FXML
    CheckBox displayFuelCheckbox;

    @FXML
    CheckBox displaySpeedCheckbox;

    @FXML
    Button saveSettingsButton;

    @FXML
    Button infoSettingsButton;

    @FXML
    Spinner<Integer> intervalSpinner;

    public void saveSettings(){
        if (displayFuel){
            fuel.visibleProperty().set(displayFuel);
        } else if (displaySpeed){
            speed.visibleProperty().set(displaySpeed);
        }
        switchDelay = intervalSpinner.getValue();
        infoSettingsPane.visibleProperty().set(false);
        infoSettingsButton.visibleProperty().setValue(true);
    }

    public void initialize(){
        intervalSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2,10));
        displayFuelCheckbox.selectedProperty().set(true);
        displaySpeedCheckbox.selectedProperty().set(false);
        infoSettingsButton.visibleProperty().set(false);
        fuel.visibleProperty().set(false);
        speed.visibleProperty().set(false);
        displayFuelCheckbox.selectedProperty().addListener(observable -> {
            displayFuel = displayFuelCheckbox.selectedProperty().get();
        });
        displaySpeedCheckbox.selectedProperty().addListener(observable -> {
            displaySpeed = displaySpeedCheckbox.selectedProperty().get();
        });

    }

    public void infoSettings(){
        fuel.visibleProperty().set(false);
        speed.visibleProperty().set(false);
        infoSettingsButton.visibleProperty().set(false);
        infoSettingsPane.visibleProperty().set(true);
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
