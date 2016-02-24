package bananacore.epic;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class FuelController implements OdometerInterface{

    public static final double MAX_FUEL_CONSUMED_VALUE = 4294967295.0;
    public static final double MAX_ODOMETER_VALUE = 16777214.0;
    private double fuelLevel = 100.0;
    private double fuelConsumed = 0.0;
    private double startDistance = 0.0;
    private double fuelUsage = 1.0;
    private double distanceTravelled = 0.0;
    private double estimatedKmLeft = 10.0;
    private double tankSize = 100;
    private int fuelUpdateCounter = 0;

    public void updateFuel(double fuelLevel, double fuelConsumed){
        updateTankSize(fuelLevel, fuelConsumed);
        updateFuelConsumed(fuelConsumed);
        updateEstimatedKmLeft(fuelConsumed);
        updateFuelLevel(fuelLevel);
    }

    public void updateOdometer(double odometerReading){
        if (validOdometerReading(odometerReading)){
            distanceTravelled = computeDistanceTravelled(odometerReading);
        }
    }

    private void updateTankSize(double fuelLevel, double fuelConsumed){
        tankSize = fuelConsumed*100/(this.fuelLevel-fuelLevel);
    }

    private void updateEstimatedKmLeft(double fuelConsumed){
        estimatedKmLeft = (tankSize - fuelConsumed)/fuelUsage;
        //updateEstimatedKmLeftText();
    }

    private void updateFuelConsumed(double fuelConsumed){
        if (validFuelConsumedValue(fuelConsumed)){
            this.fuelConsumed = fuelConsumed;
        }
        updateFuelUsage();
    }

    private void updateFuelLevel(double fuelLevel){
        if(validFuelLevelValue(fuelLevel)){
            this.fuelLevel = fuelLevel;
            //updateFuelLeftRectangle();
        } else {
            throw new IllegalArgumentException("Invalid fuelLevelValue");
        }
    }

    private void updateFuelUsage(){
        fuelUpdateCounter++;
        if(fuelUpdateCounter == 10){
            /* After 10 readings, reset counter
                Also set new start distance to get proper measuring interval for fuel usage and estimateKmLeft
            */
            fuelUsage = computeFuelUsed()/distanceTravelled;
            fuelUpdateCounter = 0;
            startDistance = distanceTravelled;
        }
    }

    private double computeDistanceTravelled(double currentOdometerReading){
        return currentOdometerReading - startDistance;
    }

    private double computeFuelUsed(){
        double consumed = fuelConsumed;
        // Reset fuel consumed since last computation
        fuelConsumed = 0;
        return consumed;
    }

    private boolean validFuelConsumedValue(double fuelConsumed) {
        return fuelConsumed >= 0.0 && fuelConsumed <= MAX_FUEL_CONSUMED_VALUE;
    }

    private boolean validFuelLevelValue(double fuelLevel) {
        // Handles overfilling the tank...
        return fuelLevel <= 105.0 && fuelLevel >= 0.0;
    }

    private boolean validOdometerReading(double odometerReading){
        return odometerReading >= distanceTravelled + startDistance && validDistanceValue(odometerReading);
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

    public double getEstimatedKmLeft(){
        return estimatedKmLeft;
    }

    @FXML private Pane fuelLeftPane;

    @FXML private Rectangle fuelLeftBar;

    @FXML private Text kmLeftText;

    private void updateFuelLeftRectangle(){
        fuelLeftBar.setWidth(fuelLevel*fuelLeftPane.getWidth()/100);
    }

    private void updateEstimatedKmLeftText(){
        kmLeftText.setText(String.valueOf(estimatedKmLeft) + " km");
    }

}