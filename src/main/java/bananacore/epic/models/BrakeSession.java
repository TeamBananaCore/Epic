package bananacore.epic.models;

import bananacore.epic.Constants;
import bananacore.epic.interfaces.Graphable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name="BrakeSession")
public class BrakeSession implements Graphable{
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

    public BrakeSession(){}

    public BrakeSession(int startSpeed, int endSpeed, Timestamp startTime, int duration) {
        this.startSpeed = startSpeed;
        this.endSpeed = endSpeed;
        this.startTime = startTime;
        this.duration = duration;
    }

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

    @Override
    public String toString() {
        return "BrakeSession{" +
                "id=" + id +
                ", startSpeed=" + startSpeed +
                ", endSpeed=" + endSpeed +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    @Override
    public Timestamp getDate() {
        return startTime;
    }

    @Override
    public double getGraphValue() {
        return Constants.calculateBrakePerformance(startSpeed,endSpeed,duration);
    }
}
