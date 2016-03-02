package bananacore.epic.interfaces;


import java.sql.Timestamp;

public interface FuelInterface {
    public void updateFuelLevel(double value, Timestamp timestamp);
    public void updateFuelConsumedSinceRestart(double value, Timestamp timestamp);

}
