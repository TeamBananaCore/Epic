package bananacore.epic;


import java.sql.Timestamp;

public interface BrakeInterface {
    public void updateBreakPedalStatus(boolean value, Timestamp timestamp);
}
