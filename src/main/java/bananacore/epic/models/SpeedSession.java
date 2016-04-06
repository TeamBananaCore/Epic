package bananacore.epic.models;

import bananacore.epic.interfaces.observers.Graphable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "SpeedSession")
public class SpeedSession implements Graphable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "avgSpeed")
    private int avgSpeed;

    @Column(name = "startTime")
    private Timestamp startTime;

    @Column(name = "duration")
    private int duration;

    public int getId() {
        return id;
    }

    public int getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(int avgSpeed) {
        this.avgSpeed = avgSpeed;
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

    public SpeedSession(){}

    public SpeedSession(int avgSpeed, Timestamp startTime, int duration){
        this.avgSpeed = avgSpeed;
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "SpeedSession{" +
                "id=" + id +
                ", avgSpeed=" + avgSpeed +
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
        return avgSpeed;
    }
}
