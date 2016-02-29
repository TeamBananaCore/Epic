package bananacore.epic;

import javafx.application.Platform;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class BrakeThread extends Thread{

    private Timestamp startTime;
    private int startSpeed;
    private BrakeController brakeController;
    private boolean active = false;

    public void setValues(Timestamp startTime, int startSpeed, BrakeController brakeController){
        this.startTime = startTime;
        this.startSpeed = startSpeed;
        this.brakeController = brakeController;
    }

    public void run(){
        active = true;
        try{
            Thread.sleep(Constants.BRAKE_POST_TRESHOLD);
        } catch (InterruptedException e) {
            run();
            return;
        }
        long duration = (Timestamp.valueOf(LocalDateTime.now()).getTime() - startTime.getTime())/1000;
        Platform.runLater(() -> brakeController.updateView(startSpeed, brakeController.getSpeed(), duration));
        active = false;
    }

    public boolean isActive(){
        return active;
    }
}
