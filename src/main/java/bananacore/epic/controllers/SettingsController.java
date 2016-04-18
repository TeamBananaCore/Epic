package bananacore.epic.controllers;

import bananacore.epic.Constants;
import bananacore.epic.DatabaseManager;
import bananacore.epic.interfaces.ViewController;
import bananacore.epic.interfaces.observers.SpeedInterface;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.Timestamp;
import java.util.HashMap;

public class SettingsController implements SpeedInterface, ViewController {

    @FXML private CheckBox displayFuelCheckbox;
    @FXML private CheckBox displaySpeedCheckbox;
    @FXML private CheckBox displayFuelUsageCheckbox;
    @FXML private Button decreaseIntervalButton;
    @FXML private Button increaseIntervalButton;
    @FXML private Label intervalLabel;
    @FXML private ScrollPane scrollPane;
    @FXML private NumpadController numpadController;
    @FXML private AnchorPane numpadPane;

    @FXML private ToggleGroup themeGroup;
    private HashMap<String, Integer> themeValues;

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
    @FXML
    RadioButton adaptableTheme;
    @FXML
    RadioButton dayTheme;
    @FXML
    RadioButton nightTheme;

    private boolean toggleNumpad; //true means tankSize, false means carWeight

    //these two are needed in case cancel is pressed
    private int weightFromNumpad;
    private int fuelsizeFromNumpad;

    private int interval = 2;

    private int oldTheme;
//
    public void initialize(){
        themeValues = new HashMap<>();
        themeValues.put("adaptable", 0);
        themeValues.put("day", 1);
        themeValues.put("night", 2);
        intervalLabel.setText(addSpace(interval));
        scrollPane.setFitToWidth(true);
        Constants.PARSER.addToSpeedObservers(this);
        numpadController=Constants.numpadController;
        numpadController.addSettingsController(this);

        fuelsizeFromNumpad=Constants.settingsEPIC.getFuelsize();
        setTanksizeLabel(Integer.toString(fuelsizeFromNumpad));
        weightFromNumpad=Constants.settingsEPIC.getWeight();
        setWeightLabel(Integer.toString(weightFromNumpad));
        markGear();
        initializeFuel();
        initializeTheme();
        displayFuelCheckbox.setSelected(Constants.settingsEPIC.getFueldisplay());
        displaySpeedCheckbox.setSelected(Constants.settingsEPIC.getSpeeddisplay());
        displayFuelUsageCheckbox.setSelected(Constants.settingsEPIC.getFuelUsagedisplay());
    }

    //assumes all false. used in initilize
    private void initializeTheme(){
        int theme=Constants.settingsEPIC.getTheme();
        oldTheme = theme;
        markTheme(theme);
    }
    private void initializeGearNumber(){
        int gear = Constants.settingsEPIC.getGetNumberOfGears();
        //setts one true value in 4,5,6
        markGearNumber(gear);
    }
    private void initializeFuel(){
        boolean gasolinefuel=  Constants.settingsEPIC.getGasoline();
        if (gasolinefuel){
            gasoline.setSelected(true);
        }else{
            diesel.setSelected(true);
        }
    }
    //marks one fxml object to true
    private void markTheme(int theme){
        if (theme==0){
            adaptableTheme.setSelected(true);
        }
        else if (theme==1){
            dayTheme.setSelected(true);
        }
        else{
            nightTheme.setSelected(true);
        }

    }

    private void markGear(){
        boolean autogear= Constants.settingsEPIC.getAuto();
        if (autogear){
            auto.setSelected(true);
        }else{
            manual.setSelected(true);
            initializeGearNumber();
        }
    }
    private void markGearNumber(int gear){
        if (gear==4){
            gear4.setSelected(true);
        }
        if(gear==5){
            gear5.setSelected(true);
        }
        if(gear==6){
            gear6.setSelected(true);
        }
    }

    //used when some is uncorrectlly marked true. aka. cancel pressed
    private void markAndUnmarkGearNumber(){
        int gear = Constants.settingsEPIC.getGetNumberOfGears();
        //setts one true value in 4,5,6
        gear4.setSelected(false);
        gear5.setSelected(false);
        gear6.setSelected(false);
        markGearNumber(gear);
}
    private void markAndUnmarkFuel(){
        diesel.setSelected(false);
        gasoline.setSelected(false);
        initializeFuel();
    }
    private void markAndUnmarkTheme(){
        int theme = oldTheme;
        dayTheme.setSelected(false);
        adaptableTheme.setSelected(false);
        nightTheme.setSelected(false);
        markTheme(theme);
    }
    private void markAndUnmarkGear(){
        boolean autogear= Constants.settingsEPIC.getAuto();
        manual.setSelected(false);
        auto.setSelected(false);
        if (autogear){
            auto.setSelected(true);
        }else{
            manual.setSelected(true);
            markAndUnmarkGearNumber();
        }
    }

    //updates Tank or wightlabel. is called from the Numpadcontrolller.
    public void setWeightOrSize(String value){
        if (toggleNumpad) {
            setTanksizeLabel(value);
            fuelsizeFromNumpad=Integer.valueOf(value);
        } else {
            setWeightLabel(value);
            weightFromNumpad=Integer.valueOf(value);

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

    //FXML mothods
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
    public void changeTheme(){
        Constants.settingsEPIC.setTheme(themeValues.getOrDefault(((RadioButton) themeGroup.getSelectedToggle()).getText().toLowerCase(), 0));
    }
    @FXML
    private void handleManualButton() {
        manual.setSelected(true);
        auto.setSelected(false);
        gear4.setVisible(true);
        gear5.setVisible(true);
        gear6.setVisible(true);
        gearTitel.setVisible(true);
    }
    @FXML
    private void handleAutoButton() {
        manual.setSelected(false);
        auto.setSelected(true);
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
    }
    @FXML
    private void handleGearfive() {
        gear4.setSelected(false);
        gear5.setSelected(true);
        gear6.setSelected(false);
    }
    @FXML
    private void handleGearsix() {
        gear4.setSelected(false);
        gear5.setSelected(false);
        gear6.setSelected(true);
    }
    @FXML
    private void handleDieselButton() {
        diesel.setSelected(true);
        gasoline.setSelected(false);
    }
    @FXML
    private void handleGasolineButton() {
        diesel.setSelected(false);
        gasoline.setSelected(true);
    }
    @FXML
    private void setTanksizeLabel(String value) {
        tanksizeLabel.setText(value + " l");
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


    @FXML
    public void cancel() {
        updateButtonsFromSettingsEpic();
        Constants.settingsEPIC.setTheme(oldTheme);
        numpadPane.setVisible(false);
        showMain();
    }
    @FXML
    public void save(){
        boolean fuelDisplay = displayFuelCheckbox.isSelected();
        boolean fuelUsageDisplay = displayFuelUsageCheckbox.isSelected();
        boolean speedDisplay = displaySpeedCheckbox.isSelected();
        interval = Integer.parseInt(intervalLabel.getText().trim());

        //updates local version. (the rest is updated on press.)
        Constants.settingsEPIC.setFueldisplay(fuelDisplay);
        Constants.settingsEPIC.setFuelUsagedisplay(fuelUsageDisplay);
        Constants.settingsEPIC.setSpeeddisplay(speedDisplay);
        Constants.settingsEPIC.setScreeninterval(interval);
        Constants.settingsEPIC.setTheme(themeValues.getOrDefault(((RadioButton) themeGroup.getSelectedToggle()).getText().toLowerCase(), 0));
        oldTheme = Constants.settingsEPIC.getTheme();

        Constants.settingsEPIC.setAuto(auto.isSelected());
        Constants.settingsEPIC.setGasoline(gasoline.isSelected());
        Constants.settingsEPIC.setFuelsize(fuelsizeFromNumpad);
        Constants.settingsEPIC.setWeight(weightFromNumpad);
        Constants.settingsEPIC.setGetNumberOfGears(getNumberOfGearsSelected());

        //local settings to db
        DatabaseManager.updateSettings(Constants.settingsEPIC);
        showMain();


    }

    ///called when cancel()
    private void updateButtonsFromSettingsEpic(){
        fuelsizeFromNumpad=Constants.settingsEPIC.getFuelsize();
        weightFromNumpad=Constants.settingsEPIC.getWeight();
        setTanksizeLabel(Integer.toString(fuelsizeFromNumpad));
        setWeightLabel(Integer.toString(weightFromNumpad));
        markAndUnmarkFuel();
        markAndUnmarkGear();
        displayFuelCheckbox.setSelected(Constants.settingsEPIC.getFueldisplay());
        displaySpeedCheckbox.setSelected(Constants.settingsEPIC.getSpeeddisplay());
        displayFuelUsageCheckbox.setSelected(Constants.settingsEPIC.getFuelUsagedisplay());
        markAndUnmarkTheme();
    }

    private int getNumberOfGearsSelected(){
        //return the selected gear
        if ( gear4.isSelected()  ){
            return 4;
        }
        if(gear5.isSelected()){
            return 5;
        }
        else{
            return 6;
        }
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

    @Override
    public void hidden() {

    }

    @Override
    public void shown() {

    }
}
