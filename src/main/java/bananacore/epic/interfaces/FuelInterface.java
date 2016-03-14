package bananacore.epic.interfaces;


import java.sql.Timestamp;

public interface FuelInterface {
    // public void updateFuelLevel(double value, Timestamp timestamp);
    // These values are never updated, we dont want them anymore.

    public void updateFuelConsumedSinceRestart(double value, Timestamp timestamp);

}
