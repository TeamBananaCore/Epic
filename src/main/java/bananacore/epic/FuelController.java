package bananacore.epic;

import bananacore.epic.interfaces.OdometerInterface;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.sql.Timestamp;


public class FuelController implements OdometerInterface {

    public static final double MAX_FUEL_CONSUMED_VALUE = 4294967295.0;
    public static final double MAX_ODOMETER_VALUE = 16777214.0;
    private double fuelLevel = 100.0;
    private double fuelStart = 0.0;
    private double fuelConsumed = 0.0;
    private double startDistance = 0.0;
    private double fuelUsage = 1.0;
    private double distanceTravelled = 0.0;
    private double estimatedKmLeft = 10.0;
    private double tankSize = 0.0;

    private boolean fuelLevelUpdated = false;
    private boolean fuelConsumedUpdated = false;
    private boolean odoUpdated = false;

    public void updateFuel(double fuelLevel, double fuelConsumed) {
        updateFuelConsumed(fuelConsumed);
        if (tankSize == 0.0){
            tankSize = fuelConsumed * 100 / (this.fuelLevel - fuelLevel);
        }
        updateFuelLevel(fuelLevel);

        if (odoUpdated && fuelLevelUpdated && fuelConsumedUpdated){
            updateEstimatedKmLeft(fuelConsumed);
            odoUpdated = false;
            fuelConsumedUpdated = false;
            fuelLevelUpdated = false;
        }
    }

    @Override
    public void updateOdometer(double odometerReading, Timestamp timestamp) {
        if (validOdometerReading(odometerReading)) {
            if (odometerReading - startDistance != 0){
                odoUpdated = true;
                distanceTravelled = odometerReading - startDistance;
                startDistance = odometerReading;
            }
        }

    }

    private void updateEstimatedKmLeft(double fuelConsumed) {
        estimatedKmLeft = (tankSize - fuelConsumed) / fuelUsage;
        updateEstimatedKmLeftText();
    }

    private void updateFuelConsumed(double fuelConsumed) {
        if (validFuelConsumedValue(fuelConsumed) && fuelConsumed != this.fuelConsumed) {
            this.fuelConsumed = fuelConsumed-fuelStart;
            fuelConsumedUpdated = true;
            fuelStart = fuelConsumed;
            updateFuelUsage();
        }
    }

    private void updateFuelLevel(double fuelLevel) {
        if (validFuelLevelValue(fuelLevel)) {
            if(this.fuelLevel != fuelLevel){
                this.fuelLevel = fuelLevel;
                fuelLevelUpdated = true;
                updateFuelLeftRectangle();
            }
        } else {
            throw new IllegalArgumentException("Invalid fuelLevelValue");
        }
    }

    private void updateFuelUsage() {
        if(distanceTravelled > 0){
            fuelUsage = fuelConsumed / distanceTravelled;
        }
    }

    private boolean validFuelConsumedValue(double fuelConsumed) {
        return fuelConsumed >= 0.0 && fuelConsumed <= MAX_FUEL_CONSUMED_VALUE;
    }

    private boolean validFuelLevelValue(double fuelLevel) {
        // Handles overfilling the tank...
        return fuelLevel <= 105.0 && fuelLevel >= 0.0;
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
        fuelLeftBar.setWidth(fuelLevel * fuelLeftPane.getWidth() / 100);
    }

    private void updateEstimatedKmLeftText() {
        kmLeftText.setText(String.valueOf(estimatedKmLeft) + " km");
    }

}