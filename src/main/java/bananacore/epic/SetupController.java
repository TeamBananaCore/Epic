package bananacore.epic;

import bananacore.epic.interfaces.NumpadInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;


import javax.swing.*;
import java.io.IOException;

/**
 * Created by fonnavn on 09.03.2016.
 */
public class SetupController implements NumpadInterface {
public class SetupController extends Application {


 

    @FXML
    RadioButton auto;
    @FXML
    RadioButton maual;
    @FXML
    RadioButton disel;
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

   
    @FXML
    private void handleManualButton() {
        maual.setSelected(true);
        auto.setSelected(false);
        setAuto(false);
        gear4.setVisible(true);
        gear5.setVisible(true);
        gear6.setVisible(true);
        gearTitel.setVisible(true);
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
        disel.setSelected(true);
        gasoline.setSelected(false);
        setGassoline(false);
}
    @FXML
    private void handleGasolineButton() {
        disel.setSelected(false);
        gasoline.setSelected(true);
        setGassoline(true);
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
        //inntasting
    }
    @FXML
    private void handleWeightButton() {

    }

    public void setGear(Boolean auto) {Constants.auto = auto; }

    public void setAuto(Boolean auto) {
        Constants.auto = auto;
    }

    public void setWeight(int weight) {
        Constants.weight = weight;
    }

    public void setNumberOfGears(int numberOfGears) {
        Constants.numberOfGears = numberOfGears;
    }
    public void setGasoline(Boolean gasoline) { Constants.gasoline = gasoline;    }

    

    public void setFuelsize(int fuelsize) {
        Constants.fuelsize = fuelsize;
    }

    @Override
    public String getNumber() {
        return null;
    }
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