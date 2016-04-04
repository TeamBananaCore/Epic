package bananacore.epic.interfaces.observers;

import java.sql.Timestamp;

public interface OdometerInterface {//
    public void updateOdometer(double value, Timestamp timestamp);
}
