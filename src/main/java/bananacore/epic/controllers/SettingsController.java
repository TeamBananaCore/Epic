package bananacore.epic.controllers;

import bananacore.epic.Constants;
import bananacore.epic.DatabaseManager;
import bananacore.epic.interfaces.ViewController;
import bananacore.epic.interfaces.observers.SpeedInterface;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.Timestamp;

public class SettingsController implements SpeedInterface, ViewController {

    static String weightString="asfd";

    @FXML private CheckBox displayFuelCheckbox;
    @FXML private CheckBox displaySpeedCheckbox;
    @FXML private Button decreaseIntervalButton;
    @FXML private Button increaseIntervalButton;
    @FXML private Label intervalLabel;
    @FXML private ScrollPane scrollPane;

    @FXML private NumpadController numpadController;

    @FXML private AnchorPane numpadPane;

    private int interval = 2;
    private boolean fuelDisplay = false;
    private boolean speedDisplay = false;
//
    public void initialize(){
        //numpadController.getNumber();
        intervalLabel.setText(addSpace(interval));
        scrollPane.setFitToWidth(true);
        Constants.PARSER.addToSpeedObservers(this);
        numpadController=Constants.numpadController;
        numpadController.addSettingsController(this);
        tanksizeLabel.setText(Integer.toString(Constants.settingsEPIC.getFuelsize()));
        weightLabel.setText(Integer.toString(Constants.settingsEPIC.getWeight()));
    }

    //updates Tank or wightlabel. is called from th eNumpadcontrolller.
    public void setWeightOrSize(String value){
        if (toggleNumpad) {
            tanksizeLabel.setText(value);
            Constants.settingsEPIC.setFuelsize(Integer.valueOf(value));
        } else {
            weightLabel.setText(value);
            Constants.settingsEPIC.setWeight(Integer.valueOf(value));

        }
    }

    public AnchorPane getNumpadPane() {
        return numpadPane;
    }

    public String addSpace(int x){
        if(x < 10) {
            return " " + x;
        }
        return "" + x;
    }

    @FXML
    public void increaseInterval(){
        interval++;
        intervalLabel.setText(addSpace(interval));
    }

    @FXML
    public void decreaseInterval() {
        if(interval - 1 > 1){
            interval--;
        }
        intervalLabel.setText(addSpace(interval));
    }

    @FXML
    public void save(){
        fuelDisplay = displayFuelCheckbox.isSelected();
        speedDisplay = displaySpeedCheckbox.isSelected();
        interval = Integer.parseInt(intervalLabel.getText().trim());

        //updates local version. (the rest is updated on press.)
        Constants.settingsEPIC.setFueldisplay(fuelDisplay);
        Constants.settingsEPIC.setSpeeddisplay(speedDisplay);
        Constants.settingsEPIC.setScreeninterval(interval);

        //local to db
        DatabaseManager.updateSettings(Constants.settingsEPIC);
        showMain();


    }

    @FXML
    public void cancel() {
        showMain();
    }

    private void showMain() {
        Constants.CONTAINER.setView(ContainerController.MAIN);
    }

    @Override
    public void updateVehicleSpeed(int value, Timestamp timestamp) {
        if(value != 0){
            showMain();
        }
    }

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
    Label tanksizeLabel;
    @FXML
    Label weightLabel;
    @FXML
    Label gearTitel;

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
    private void setTanksizeLabel(String value) {
        tanksizeLabel.setText(value + " L");
    }
    @FXML
    private void setWeightLabel(String value) {
        weightLabel.setText(value + " kg");
    }

    @FXML
    private void handleTankButton()  {
        toggleNumpad = true;
        numpadPane.setVisible(true);
        numpadController.setNumberview(Integer.toString(Constants.settingsEPIC.getFuelsize()));

    }
    @FXML
    private void handleWeightButton() {
        toggleNumpad = false;
        numpadPane.setVisible(true);
        numpadController.setNumberview(Integer.toString(Constants.settingsEPIC.getWeight()));

    }
    //skal slettes
    public void setGear(Boolean auto) {
        Constants.settingsEPIC.setAuto(auto); }

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


    @Override
    public void hidden() {

    }

    @Override
    public void shown() {

    }
}
