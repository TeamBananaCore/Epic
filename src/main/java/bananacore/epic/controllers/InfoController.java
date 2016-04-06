package bananacore.epic.controllers;


import bananacore.epic.Constants;
import bananacore.epic.interfaces.observers.SpeedInterface;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class InfoController implements SpeedInterface{

    private Timestamp lastTime;
    private boolean displayFuel = true;
    private boolean displaySpeed = false;
    private int switchDelay = Integer.MAX_VALUE;

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
    Text intervalText;

    @FXML
    Button decreaseIntervalButton;

    @FXML
    Button increaseIntervalButton;

    public void saveSettings(){
        fuel.setVisible(false);
        speed.setVisible(false);
        if (displayFuel){
            fuel.setVisible(true);
        } else if (displaySpeed){
            speed.setVisible(true);
        }
        switchDelay = Integer.parseInt(intervalText.getText());
        infoSettingsPane.visibleProperty().set(false);
        infoSettingsButton.visibleProperty().setValue(true);
        saveSettingsButton.setVisible(false);
    }

    public void initialize(){
        Constants.PARSER.addToSpeedObservers(this);
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

    public void increaseInterval(){
        int current = Integer.parseInt(intervalText.getText());
        if (current < 10){
            current++;
            intervalText.setText(String.valueOf(current));
        }
    }

    public void decreaseInterval(){
        int current = Integer.parseInt(intervalText.getText());
        if (current > 2){
            current--;
            intervalText.setText(String.valueOf(current));
        }
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
            if((int)(timestamp.getTime()-lastTime.getTime())/1000 > switchDelay){
                if(speed.visibleProperty().get() && displayFuel){
                    speed.visibleProperty().set(false);
                    fuel.visibleProperty().set(true);
                } else if (displaySpeed){
                    speed.visibleProperty().set(true);
                    fuel.visibleProperty().set(false);
                }
                lastTime = timestamp;
            }
        }

    }
}
