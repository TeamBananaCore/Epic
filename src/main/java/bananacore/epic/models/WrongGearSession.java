package bananacore.epic.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "WrongGearSession")
public class WrongGearSession{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "gear")
    private int gear;

    @Column(name = "startTime")
    private Timestamp startTime;

    @Column(name = "duration")
    private int duration;

    public int getId() {
        return id;
    }

    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        this.gear = gear;
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
        return "WrongGearSession{" +
                "id=" + id +
                ", gear=" + gear +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
