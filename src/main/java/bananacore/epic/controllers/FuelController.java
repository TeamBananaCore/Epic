package bananacore.epic.controllers;

import bananacore.epic.Constants;
import bananacore.epic.interfaces.observers.FuelInterface;
import bananacore.epic.interfaces.observers.OdometerInterface;
import bananacore.epic.DatabaseManager;
import bananacore.epic.models.FuelSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    public static final int FUEL_BAR_WIDTH = 276;

    private double fuelLevelPercentage = 0.0;
    private double fuelIntervalStart = 0.0;
    private double fuelConsumedInterval = 0.0;
    private double distanceIntervalStart = 0.0;
    private double fuelUsageInterval = 0.0;
    private double distanceTravelledInterval = 0.0;
    private double estimatedKmLeft = 0.0;
    private double tankSize = 50.0;
    private double totalFuelConsumed = 0.0;
    private double startFuelLevelPercentage = 0.0;

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
    @FXML private ImageView fuelImage;

    public void initialize(){
        Constants.FUEL_IMAGE = fuelImage;
        startOfSwitchInterval = Timestamp.valueOf(LocalDateTime.now());

        Constants.PARSER.addToFuelObservers(this);
        Constants.PARSER.addToOdometerObservers(this);

        displayingKmLeft = Constants.settingsEPIC.getFueldisplay();
        displayingFuelUsage = Constants.settingsEPIC.getFuelUsagedisplay();
        switchFrequency = Constants.settingsEPIC.getScreeninterval();
        tankSize = Constants.settingsEPIC.getFuelsize();

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

        if (odoUpdated){
            updateEstimatedKmLeft(timestamp);
            odoUpdated = false;
        }
    }

    private void updateFuelConsumed(double fuelConsumed) {
        if (validFuelConsumedValue(fuelConsumed)) {
            this.fuelConsumedInterval = fuelConsumed-fuelIntervalStart;

            if (fuelConsumedInterval > 0){
                updateFuelLevel(fuelConsumedInterval);
            }

            if(odoUpdated){
                fuelIntervalStart = fuelConsumed;
                updateFuelUsage();
            }
        }
    }

    @Override
    public void parseInitialFuelLevel(double initialFuelLevel, Timestamp timestamp){
        startFuelLevelPercentage = initialFuelLevel;
        updateFuelLeftRectangle();
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

    private void updateFuelUsage() {
        if(distanceTravelledInterval < 1 && distanceTravelledInterval >= 0.5){
            fuelUsageInterval = fuelConsumedInterval / distanceTravelledInterval;
        }

        if(displayingFuelUsage){
            fuelUsageText.setText(String.format("%.2f", fuelUsageInterval) + " l/km");
        }
    }

    private void updateFuelLevel(double fuelConsumed) {
        if(startFuelLevelPercentage != 0.0){
            // Set total fuel consumed equal to the missing amount from start plus what has been consumed this trip
            if(totalFuelConsumed == 0.0){
                // initial value + consumed this interval
                totalFuelConsumed = (tankSize-tankSize/100*startFuelLevelPercentage) + fuelConsumed;
            } else {
                // else add consumed this interval
                totalFuelConsumed += fuelConsumed;
            }
            // update percentage based on how much has been consumed in total
            this.fuelLevelPercentage = (tankSize-totalFuelConsumed)/tankSize*100;
            updateFuelLeftRectangle();
        }
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
            if (fuelLevelPercentage > 100.0){
                fuelLeftBar.setWidth(FUEL_BAR_WIDTH);
            } else {
                fuelLeftBar.setWidth(fuelLevelPercentage * FUEL_BAR_WIDTH / 100);
            }
            if (fuelLevelPercentage < 50.0) {
                Color c = Color.RED.interpolate(Color.YELLOW, fuelLevelPercentage / 50.0);
                fuelLeftBar.setFill(c);
            }else{
                Color c = Color.YELLOW.interpolate(Color.FORESTGREEN, (fuelLevelPercentage-50.0) / 50.0);
                fuelLeftBar.setFill(c);
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

    public void setFuelImage(Image image){
        fuelImage.setImage(image);
    }
}