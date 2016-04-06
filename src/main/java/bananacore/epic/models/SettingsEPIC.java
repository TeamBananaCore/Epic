package bananacore.epic.models;


import bananacore.epic.DatabaseManager;

import javax.persistence.*;

@Entity(name="Settings")

public class SettingsEPIC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "auto")
    private Boolean auto;

    @Column(name = "numberofgears")
    private int getNumberOfGears;

    @Column(name = "weight")
    private int weight;

    @Column(name = "gasoline")
    private Boolean gasoline;

    @Column(name = "fuelsize")
    private int fuelsize;

    @Column(name = "fueldisplay")
    private int fueldisplay;

    @Column(name = "speeddisplay")
    private int speeddisplay;

    @Column(name = "screeninterval")
    private int screeninterval;

    public SettingsEPIC() {
    }

    public SettingsEPIC(Boolean auto, int getNumberOfGears, int weight, Boolean gasoline, int fuelsize, int fueldisplay, int speeddisplay, int screeninterval) {
        this.auto = auto;
        this.getNumberOfGears = getNumberOfGears;
        this.weight = weight;
        this.gasoline = gasoline;
        this.fuelsize = fuelsize;
        this.fueldisplay = fueldisplay;
        this.speeddisplay = speeddisplay;
        this.screeninterval = screeninterval;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getAuto() {
        return auto;
    }

    public void setAuto(Boolean auto) {
        this.auto = auto;
        //DatabaseManager.updateSettings(this);
    }

    public int getGetNumberOfGears() {
        return getNumberOfGears;
    }

    public void setGetNumberOfGears(int getNumberOfGears) {
        this.getNumberOfGears = getNumberOfGears;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
       // DatabaseManager.updateSettings(this);

    }

    public Boolean getGasoline() {
        return gasoline;
    }
//
    public void setGasoline(Boolean gasoline) {
        this.gasoline = gasoline;
        //DatabaseManager.updateSettings(this);

    }

    public int getFuelsize() {
        return fuelsize;
    }

    public void setFuelsize(int fuelsize) {
        this.fuelsize = fuelsize;
       // DatabaseManager.updateSettings(this);

    }

    public int getFueldisplay() {
        return fueldisplay;
    }

    public void setFueldisplay(int fueldisplay) {
        this.fueldisplay = fueldisplay;
    }

    public int getSpeeddisplay() {
        return speeddisplay;
    }

    public void setSpeeddisplay(int speeddisplay) {
        this.speeddisplay = speeddisplay;
    }

    public int getScreeninterval() {
        return screeninterval;
    }

    public void setScreeninterval(int screeninterval) {
        this.screeninterval = screeninterval;
    }

    @Override
    public String toString() {
        return "SettingsEPIC{" +
                "id=" + id +
                ", auto=" + auto +
                ", getNumberOfGears=" + getNumberOfGears +
                ", weight=" + weight +
                ", gasoline=" + gasoline +
                ", fuelsize=" + fuelsize +
                '}';
    }
}
