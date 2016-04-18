package bananacore.epic.controllers;

import bananacore.epic.Constants;
import bananacore.epic.interfaces.observers.FuelInterface;
import bananacore.epic.interfaces.observers.OdometerInterface;
import bananacore.epic.DatabaseManager;
import bananacore.epic.models.FuelSession;
import bananacore.epic.models.SettingsEPIC;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;


public class FuelController implements OdometerInterface, FuelInterface, Observer{


    public static final double MAX_FUEL_CONSUMED_VALUE = 4294967295.0;
    public static final double MAX_ODOMETER_VALUE = 16777214.0;

    private double fuelLevelPercentage = 0.0;
    private double fuelIntervalStart = 0.0;
    private double fuelConsumedInterval = 0.0;
    private double distanceIntervalStart = 0.0;
    private double fuelUsageInterval = 0.0;
    private double distanceTravelledInterval = 0.0;
    private double estimatedKmLeft = 0.0;
    private double tankSize = 50.0;
    private double totalFuelConsumed = 0.0;
    private double startFuelLevelPercentage = 100.0;

    private Timestamp startOfInterval = null;

    private boolean odoUpdated = false;
    private boolean displayingFuelUsage = false;
    private boolean displayingKmLeft = true;
    private int switchFrequency;
    private Timestamp startOfSwitchInterval;

    @FXML private Pane fuelLeftPane;
    @FXML private Rectangle fuelLeftBar;
    @FXML private Label kmLeftText;
    @FXML private Label fuelUsageText;

    public void initialize(){
        startOfSwitchInterval = Timestamp.valueOf(LocalDateTime.now());

        Constants.PARSER.addToFuelObservers(this);
        Constants.PARSER.addToOdometerObservers(this);

        SettingsEPIC settings = DatabaseManager.getSettings();
        displayingKmLeft = settings.getFueldisplay();
        displayingFuelUsage = settings.getFuelUsagedisplay();
        switchFrequency = settings.getScreeninterval();
        tankSize = settings.getFuelsize();

        kmLeftText.setVisible(displayingKmLeft);
        if(!displayingKmLeft){
            fuelUsageText.setVisible(displayingFuelUsage);
        }

        Constants.settingsEPIC.addObserver(this);
    }

    @Override
    public void updateFuelConsumedSinceRestart(double value, Timestamp timestamp) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        if(now.getTime()-startOfSwitchInterval.getTime() > switchFrequency*1000L) {
            switchDisplayedConsoleInformation(now);
        }

        updateFuelConsumed(value);
        updateFuelLevel(value);

        if (odoUpdated){
            updateEstimatedKmLeft(timestamp);
            odoUpdated = false;
        }
    }

    @Override
    public void updateOdometer(double odometerReading, Timestamp timestamp) {
        if (validOdometerReading(odometerReading)) {
            double distanceTravelledInterval = odometerReading-distanceIntervalStart;

            if(distanceTravelledInterval > 0.5){
                odoUpdated = true;
                this.distanceTravelledInterval = odometerReading - distanceIntervalStart;
                distanceIntervalStart = odometerReading;
                if(distanceTravelledInterval > 1){
                    fuelUsageInterval = 0.112;
                }
            }
        }

    }

    private void updateFuelConsumed(double fuelConsumed) {
        if (validFuelConsumedValue(fuelConsumed)) {
            this.fuelConsumedInterval = fuelConsumed-fuelIntervalStart;

            if(odoUpdated){
                fuelIntervalStart = fuelConsumed;
                updateFuelUsage();
            }
        }
    }

    private void updateFuelUsage() {
        if(distanceTravelledInterval < 1 && distanceTravelledInterval >= 0.5){
            fuelUsageInterval = fuelConsumedInterval / distanceTravelledInterval;
        }

        if(displayingFuelUsage){
            fuelUsageText.setText(String.format("%.2f", fuelUsageInterval) + " l/km");
        }
    }

    private void updateFuelLevel(double fuelConsumed) {
        totalFuelConsumed = tankSize-(tankSize/100*startFuelLevelPercentage) + fuelConsumed;
        this.fuelLevelPercentage = ((startFuelLevelPercentage*tankSize/100)-totalFuelConsumed)*(100/tankSize);
        updateFuelLeftRectangle();
    }

    private void updateEstimatedKmLeft(Timestamp endOfInterval) {
        estimatedKmLeft = (tankSize - totalFuelConsumed) / fuelUsageInterval;

        if (startOfInterval != null){
            FuelSession session = new FuelSession((float) fuelUsageInterval, startOfInterval, (int)(endOfInterval.getTime()-startOfInterval.getTime())/1000);
            DatabaseManager.insertFuelSession(session);
            startOfInterval = endOfInterval;
        } else {
            startOfInterval = endOfInterval;
        }


        updateEstimatedKmLeftText();
    }

    public void switchDisplayedConsoleInformation(Timestamp now){
        if(displayingFuelUsage){
            if(fuelUsageText.isVisible()){
                kmLeftText.setVisible(false);
                if(displayingKmLeft){
                    fuelUsageText.setVisible(false);
                    kmLeftText.setVisible(true);
                }
            } else {
                kmLeftText.setVisible(false);
                fuelUsageText.setVisible(true);
            }
        } else if(displayingKmLeft){
            if(kmLeftText.isVisible()){
                fuelUsageText.setVisible(false);
                if(displayingFuelUsage){
                    fuelUsageText.setVisible(true);
                    kmLeftText.setVisible(false);
                }
            } else {
                kmLeftText.setVisible(true);
                fuelUsageText.setVisible(false);
            }
        } else {
            fuelUsageText.setVisible(false);
            kmLeftText.setVisible(false);
        }
        startOfSwitchInterval = now;
    }

    private boolean validFuelConsumedValue(double fuelConsumed) {
        return fuelConsumed >= 0.0 && fuelConsumed <= MAX_FUEL_CONSUMED_VALUE;
    }

    private boolean validOdometerReading(double odometerReading) {
        return odometerReading >= distanceIntervalStart && validDistanceValue(odometerReading);
    }

    private boolean validDistanceValue(double startDistance) {
        return startDistance >= 0.0 && startDistance <= MAX_ODOMETER_VALUE;
    }

    private void updateFuelLeftRectangle() {
        if (fuelLevelPercentage != 0.0) {
            fuelLeftBar.setWidth(fuelLevelPercentage * 276 / 100);
            if (fuelLevelPercentage < 75) {
                if (fuelLevelPercentage > 50) {
                    fuelLeftBar.setFill(Color.YELLOW);
                } else if (fuelLevelPercentage > 25) {
                    fuelLeftBar.setFill(Color.ORANGE);
                } else {
                    fuelLeftBar.setFill(Color.RED);
                }
            }
        }
    }

    private void updateEstimatedKmLeftText() {
        if(displayingKmLeft){
            kmLeftText.setText(String.format("%.2f", estimatedKmLeft) + " km");
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        displayingFuelUsage = Constants.settingsEPIC.getFuelUsagedisplay();
        displayingKmLeft = Constants.settingsEPIC.getFueldisplay();
        switchFrequency = Constants.settingsEPIC.getScreeninterval();
        tankSize = Constants.settingsEPIC.getFuelsize();

        if(displayingKmLeft){
            kmLeftText.setVisible(true);
            fuelUsageText.setVisible(false);
        } else if (displayingFuelUsage){
            fuelUsageText.setVisible(true);
            kmLeftText.setVisible(false);
        } else {
            fuelUsageText.setVisible(false);
            kmLeftText.setVisible(false);
        }
    }
}