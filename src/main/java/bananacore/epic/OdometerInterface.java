package bananacore.epic;

import java.sql.Timestamp;

public interface OdometerInterface {
    public void updateOdometer(int value, Timestamp timestamp);
}
