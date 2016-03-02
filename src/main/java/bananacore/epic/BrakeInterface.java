package bananacore.epic;


import java.sql.Timestamp;

public interface BrakeInterface {
    void updateBrakePedalStatus(boolean value, Timestamp timestamp);
}
