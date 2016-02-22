package bananacore.epic.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name="BrakeSession")
public class BrakeSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "startSpeed")
    private int startSpeed;

    @Column(name = "endSpeed")
    private int endSpeed;

    @Column(name = "startTime")
    private Timestamp startTime;

    @Column(name = "duration")
    private int duration;

    public int getId() {
        return id;
    }

    public int getStartSpeed() {
        return startSpeed;
    }

    public void setStartSpeed(int startSpeed) {
        this.startSpeed = startSpeed;
    }

    public int getEndSpeed() {
        return endSpeed;
    }

    public void setEndSpeed(int endSpeed) {
        this.endSpeed = endSpeed;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
