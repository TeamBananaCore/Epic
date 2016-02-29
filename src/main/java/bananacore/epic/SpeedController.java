package bananacore.epic;

import java.sql.Timestamp;

/**
 * Created by marton on 29/02/16.
 */
public class SpeedController implements SpeedInterface{


    int vehicleSpeed = 0;

    public void updateVehicleSpeed(int value, Timestamp timestamp) {
        vehicleSpeed = value;
        System.out.println("Speed: " + vehicleSpeed);
    }
}
