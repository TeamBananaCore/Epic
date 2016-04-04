package bananacore.epic;

import bananacore.epic.interfaces.NumpadInterface;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;


import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SetupController extends Application implements NumpadInterface {


 

    @FXML
    RadioButton auto;
    @FXML
    RadioButton manual;
    @FXML
    RadioButton diesel;
    @FXML
    RadioButton gasoline;
    @FXML
    RadioButton gear6;
    @FXML
    RadioButton gear5;
    @FXML
    RadioButton gear4;
    @FXML
    Label tankSizeLabel;
    @FXML
    Label weightLabel;
    @FXML
    Label gearTitel;

    private Numpad numpad;
    private boolean toggleNumpad; //true means tankSize, false means carWeight

  

    @FXML
    private void handleManualButton() {
        manual.setSelected(true);
        auto.setSelected(false);
        setAuto(false);
        gear4.setVisible(true);
        gear5.setVisible(true);
        gear6.setVisible(true);
        gearTitel.setVisible(true);
    }
    @FXML
    private void handleAutoButton() {
        manual.setSelected(false);
        auto.setSelected(true);
        setAuto(true);
        gear4.setVisible(false);
        gear5.setVisible(false);
        gear6.setVisible(false);
        gearTitel.setVisible(false);
    }

    @FXML
    private void handleGearfour() {
        gear4.setSelected(true);
        gear5.setSelected(false);
        gear6.setSelected(false);
        setNumberOfGears(4);
    }
    @FXML
    private void handleGearfive() {
        gear4.setSelected(false);
        gear5.setSelected(true);
        gear6.setSelected(false);
        setNumberOfGears(5);
    }
    @FXML
    private void handleGearsix() {
        gear4.setSelected(false);
        gear5.setSelected(false);
        gear6.setSelected(true);
        setNumberOfGears(6);
    }
//lower canvas
    @FXML
    private void handleDieselButton() {
        diesel.setSelected(true);
        gasoline.setSelected(false);
        setGasoline(false);
}
    @FXML
    private void handleGasolineButton() {
        diesel.setSelected(false);
        gasoline.setSelected(true);
        setGasoline(true);
    }
    @FXML
    private void setTankSizeLabel(String value) {
        tankSizeLabel.setText(value + " L");
    }
    @FXML
    private void setWeightLabel(String value) {
        weightLabel.setText(value +" kg");
    }
    @FXML
    private void handleTankButton()  {
        toggleNumpad = true;
        numpad("Tank Size");
    }
    @FXML
    private void handleWeightButton() {
        toggleNumpad = false;
        numpad("Weight");
    }
//skal slettes
    public void setGear(Boolean auto) {Constants.settingsEPIC.setAuto(auto); }

    public void setAuto(Boolean auto) {
        Constants.settingsEPIC.setAuto(auto);

    }

    public void setWeight(int weight) {
        Constants.settingsEPIC.setWeight(weight);
    }

    public void setNumberOfGears(int numberOfGears) {
        Constants.settingsEPIC.setGetNumberOfGears(numberOfGears);
    }
    public void setGasoline(Boolean gasoline) { Constants.settingsEPIC.setGasoline(gasoline);    }

    

    public void setFuelsize(int fuelsize) {
        Constants.settingsEPIC.setFuelsize(fuelsize);
    }

    public void numpad(String title) {
        //numpad = new Numpad(title, this);
        try {
            Stage stage = (Stage) auto.getScene().getWindow();
            Parent root = (Parent) FXMLLoader.load(getClass().getClassLoader().getResource("fxml/numpad.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNumber() {
        String text = numpad.getNumber();
        if (toggleNumpad) {
            tankSizeLabel.setText(text);
        } else {
            weightLabel.setText(text);
        }
        System.out.println(text);
        return text;
    }


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/setup.fxml"));

        stage.setTitle("test");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(SetupController.class, args);
    }


}