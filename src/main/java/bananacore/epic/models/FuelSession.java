package bananacore.epic.models;

import bananacore.epic.interfaces.Graphable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "FuelSession")
public class FuelSession implements Graphable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "fuelUsage")
    private float fuelUsage;

    @Column(name = "startTime")
    private Timestamp startTime;

    @Column(name = "duration")
    private int duration;

    public int getId() {
        return id;
    }

    public float getFuelUsage() {
        return fuelUsage;
    }

    public void setFuelUsage(float fuelUsage) {
        this.fuelUsage = fuelUsage;
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
        return "FuelSession{" +
                "id=" + id +
                ", fuelUsage=" + fuelUsage +
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
        return fuelUsage;
    }
}
