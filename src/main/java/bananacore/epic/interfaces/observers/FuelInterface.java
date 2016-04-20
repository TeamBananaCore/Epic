package bananacore.epic.interfaces.observers;


import java.sql.Timestamp;

public interface FuelInterface {

    void parseInitialFuelLevel(double value, Timestamp timestamp);

    void updateFuelConsumedSinceRestart(double value, Timestamp timestamp);

}
