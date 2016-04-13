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
    @FXML private Button decreaseIntervalButton;
    @FXML private Button increaseIntervalButton;
    @FXML private Label intervalLabel;
    @FXML private ScrollPane scrollPane;

    @FXML private NumpadController numpadController;

    @FXML private AnchorPane numpadPane;

    @FXML private ToggleGroup themeGroup;
    private HashMap<String, Integer> themeValues;

    //these are needed in case chanel changes and not saved.
    private int weightFromNumpad;
    private int fuelsizeFromNumpad;

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


    private int interval = 2;
    private boolean fuelDisplay = false;
    private boolean speedDisplay = false;
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
        markfuel();
        markTheme();
        displayFuelCheckbox.setSelected(Constants.settingsEPIC.getFueldisplay());
        displaySpeedCheckbox.setSelected(Constants.settingsEPIC.getSpeeddisplay());
    }

    //assumes all false
    private void markTheme(){
        int theme=Constants.settingsEPIC.getTheme();
        oldTheme = Constants.settingsEPIC.getTheme();
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
    private void markAndUnmarkTheme(){
        int theme = oldTheme;
        dayTheme.setSelected(false);
        adaptableTheme.setSelected(false);
        nightTheme.setSelected(false);
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
            markGearNumber();
        }
    }
    private void markAndUnmarkGear(){
        boolean autogear= Constants.settingsEPIC.getAuto();
        if (autogear){
            auto.setSelected(true);
            manual.setSelected(false);
        }else{
            manual.setSelected(true);
            auto.setSelected(false);

            markGearNumber();
        }
    }
    //used when all is default set false
    private void markGearNumber(){
        int gear = Constants.settingsEPIC.getGetNumberOfGears();
        //setts one true value in 4,5,6
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
    //used when some is uncorrectlly marked true
    private void markAndUnmarkGearNumber(){
        int gear = Constants.settingsEPIC.getGetNumberOfGears();
        //setts one true value in 4,5,6
        if (gear==4){
            gear4.setSelected(true);
            gear5.setSelected(false);
            gear6.setSelected(false);

        }
        if(gear==5){
            gear5.setSelected(true);
            gear4.setSelected(false);
            gear6.setSelected(false);

        }
        if(gear==6){
            gear6.setSelected(true);
            gear4.setSelected(false);
            gear5.setSelected(false);
        }
    }

    private void markfuel(){
        boolean gasolinefuel=  Constants.settingsEPIC.getGasoline();
        if (gasolinefuel){
            gasoline.setSelected(true);
        }else{
            diesel.setSelected(true);
        }
    }
    private void markAndUnmarkFuel(){
        boolean gasolinefuel=  Constants.settingsEPIC.getGasoline();
        if (gasolinefuel){
            gasoline.setSelected(true);
            diesel.setSelected(false);
        }else{
            diesel.setSelected(true);
            gasoline.setSelected(false);
        }
    }


    //updates Tank or wightlabel. is called from th eNumpadcontrolller.
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

    private int getNumberOfGearsSelected(){
        int gear = Constants.settingsEPIC.getGetNumberOfGears();
        //setts one true value in 4,5,6
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

    @FXML
    public void changeTheme(){
        Constants.settingsEPIC.setTheme(themeValues.getOrDefault(((RadioButton) themeGroup.getSelectedToggle()).getText().toLowerCase(), 0));
    }

    @FXML
    public void cancel() {
        updateButtonsFromSettingsEpic();
        Constants.settingsEPIC.setTheme(oldTheme);
        numpadPane.setVisible(false);
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
        markAndUnmarkTheme();

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
    //lower canvas
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
