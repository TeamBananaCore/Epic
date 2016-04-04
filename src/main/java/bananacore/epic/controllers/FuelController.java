package bananacore.epic.controllers;

import bananacore.epic.Constants;
import bananacore.epic.interfaces.FuelInterface;
import bananacore.epic.interfaces.OdometerInterface;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;


public class FuelController implements OdometerInterface, FuelInterface {


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

    private boolean odoUpdated = false;


    public void initialize(){
        Constants.PARSER.addToFuelObservers(this);
        Constants.PARSER.addToOdometerObservers(this);
    }

    @Override
    public void updateFuelConsumedSinceRestart(double value, Timestamp timestamp) {
        updateFuelConsumed(value);
        updateFuelLevel(value);
        if (odoUpdated){
            updateEstimatedKmLeft();
            odoUpdated = false;
        }
    }

    private void updateFuelConsumed(double fuelConsumed) {
        if (validFuelConsumedValue(fuelConsumed)) {
            this.fuelConsumedInterval = fuelConsumed- fuelIntervalStart;
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
    }

    private void updateFuelLevel(double fuelConsumed) {
        // this part is probably wrong, the rest is ok.
        totalFuelConsumed = tankSize-(tankSize/100*startFuelLevelPercentage) + fuelConsumed;
        this.fuelLevelPercentage = ((startFuelLevelPercentage*tankSize/100)-totalFuelConsumed)*(100/tankSize);
        updateFuelLeftRectangle();
    }

    @Override
    public void updateOdometer(double odometerReading, Timestamp timestamp) {
        double distanceTravelledInterval = odometerReading-distanceIntervalStart;
        if (validOdometerReading(odometerReading)) {
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

    private void updateEstimatedKmLeft() {
        System.out.println("fuelLeft: " + String.valueOf(tankSize-totalFuelConsumed) + " fuelUsageInterval: " + String.valueOf(fuelUsageInterval));
        estimatedKmLeft = round((tankSize - totalFuelConsumed) / fuelUsageInterval, 2);
        updateEstimatedKmLeftText();
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

    public double getFuelLevelPercentage() {
        return fuelLevelPercentage;
    }

    public double getFuelUsageInterval() {
        return fuelUsageInterval;
    }

    public double getEstimatedKmLeft() {
        return estimatedKmLeft;
    }

    @FXML
    private Pane fuelLeftPane;

    @FXML
    private Rectangle fuelLeftBar;

    @FXML
    private Text kmLeftText;


    private void updateFuelLeftRectangle() {
        if (fuelLevelPercentage != 0.0){
            fuelLeftBar.setWidth(fuelLevelPercentage * 320 / 100);
        }
    }

    private void updateEstimatedKmLeftText() {
        kmLeftText.setText(String.valueOf(estimatedKmLeft) + " km");
    }

    public static double round(double value, int places) {
        if (value < Double.POSITIVE_INFINITY){
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
        return 0.0;
    }

}