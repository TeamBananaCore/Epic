package bananacore.epic;

import bananacore.epic.controllers.BrakeController;
import javafx.application.Platform;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class BrakeThread extends Thread{

    private Timestamp startTime, endTime;
    private int startSpeed, endSpeed;
    private BrakeController brakeController;
    private boolean active = false;

    public BrakeThread(Timestamp startTime, Timestamp endTime, int startSpeed, int endSpeed, BrakeController brakeController){
        setDaemon(true);
        this.startTime = startTime;
        this.endTime = endTime;
        this.startSpeed = startSpeed;
        this.endSpeed = endSpeed;
        this.brakeController = brakeController;

    }

    public void run(){
        active = true;
        try{
            Thread.sleep(Constants.BRAKE_POST_TRESHOLD);
        } catch (InterruptedException e) {
            return;
        }
        long duration = (endTime.getTime() - startTime.getTime())/1000;
        active = false;
        Platform.runLater(() -> brakeController.updateView(startSpeed, endSpeed, duration));
    }

    public boolean isActive(){
        return active;
    }
}
