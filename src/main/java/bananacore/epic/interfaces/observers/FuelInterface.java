package bananacore.epic.interfaces.observers;


import java.sql.Timestamp;

public interface FuelInterface {

    public void updateFuelConsumedSinceRestart(double value, Timestamp timestamp);

}
