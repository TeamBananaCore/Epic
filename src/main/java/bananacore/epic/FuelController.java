package bananacore.epic;

import bananacore.epic.interfaces.FuelInterface;
import bananacore.epic.interfaces.OdometerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;


public class FuelController implements OdometerInterface, FuelInterface {


    public static final double MAX_FUEL_CONSUMED_VALUE = 4294967295.0;
    public static final double MAX_ODOMETER_VALUE = 16777214.0;
    private double fuelLevel = 0.0;
    private double fuelStart = 0.0;
    private double fuelConsumed = 0.0;
    private double startDistance = 0.0;
    private double fuelUsage = 1.0;
    private double distanceTravelled = 0.0;
    private double estimatedKmLeft = 10.0;
    private double tankSize = 2.0;
    private double totalFuelConsumed = 0.0;
    private double startFuelLevel = 100.0;

    private boolean fuelLevelUpdated = false;
    private boolean fuelConsumedUpdated = false;
    private boolean odoUpdated = false;


    @Override
    public void updateFuelConsumedSinceRestart(double value, Timestamp timestamp) {
        updateFuelConsumed(value);
        updateFuelLevel(value);
        System.out.println("read value from JSON: " + String.valueOf(value));
        if (odoUpdated && fuelLevelUpdated && fuelConsumedUpdated){
            updateEstimatedKmLeft(value);
            odoUpdated = false;
            fuelConsumedUpdated = false;
            fuelLevelUpdated = false;
        }
    }

    public void initialize(){
        Constants.PARSER.addToFuelObserver(this);
        Constants.PARSER.addToOdometerObservers(this);
    }

    @Override
    public void updateOdometer(double odometerReading, Timestamp timestamp) {
        System.out.println("dist: " + String.valueOf(odometerReading-startDistance));
        if (validOdometerReading(odometerReading) && odometerReading - startDistance > 0.05) {
            if (odometerReading - startDistance != 0){
                odoUpdated = true;

                distanceTravelled = odometerReading - startDistance;
                startDistance = odometerReading;
            }
        }

    }

    private void updateEstimatedKmLeft(double fuelConsumed) {
        estimatedKmLeft = round((tankSize - fuelConsumed) / fuelUsage, 2);
        updateEstimatedKmLeftText();
    }

    private void updateFuelConsumed(double fuelConsumed) {
        if (validFuelConsumedValue(fuelConsumed)) {
            this.fuelConsumed = fuelConsumed-fuelStart;
            fuelConsumedUpdated = true;
            if(odoUpdated){
                fuelStart = fuelConsumed;
            }
            updateFuelUsage();
        }
    }

    private void updateFuelLevel(double fuelConsumed) {
        totalFuelConsumed = tankSize-(tankSize/100*startFuelLevel) + fuelConsumed;
        double fuelLevel = ((startFuelLevel*tankSize/100)-totalFuelConsumed)*(100/tankSize);
        this.fuelLevel = fuelLevel;
        fuelLevelUpdated = true;
        updateFuelLeftRectangle();
    }

    private void updateFuelUsage() {
        if(distanceTravelled > 0){
            fuelUsage = fuelConsumed / distanceTravelled;
        }
    }

    private boolean validFuelConsumedValue(double fuelConsumed) {
        return fuelConsumed >= 0.0 && fuelConsumed <= MAX_FUEL_CONSUMED_VALUE;
    }

    private boolean validOdometerReading(double odometerReading) {
        return odometerReading >= startDistance && validDistanceValue(odometerReading);
    }

    private boolean validDistanceValue(double startDistance) {
        return startDistance >= 0.0 && startDistance <= MAX_ODOMETER_VALUE;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public double getFuelUsage() {
        return fuelUsage;
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
        if (fuelLevel != 0.0){
            fuelLeftBar.setWidth(fuelLevel * fuelLeftPane.getWidth() / 100);
        }
    }

    private void updateEstimatedKmLeftText() {;
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