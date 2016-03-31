package bananacore.epic.models;


import bananacore.epic.DatabaseManager;

import javax.persistence.*;

@Entity(name="BrakeSession")

public class SettingsEPIC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private int id;

    @Column(name = "auto")
    private Boolean auto;

    @Column(name = "numberOfGears")
    private int getNumberOfGears;

    @Column(name = "weight")
    private int weight;

    @Column(name = "gasoline")
    private Boolean gasoline;

    @Column(name = "fuelsize")
    private int fuelsize;

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
        DatabaseManager.updateSettings(this);
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
        DatabaseManager.updateSettings(this);

    }

    public Boolean getGasoline() {
        return gasoline;
    }
//
    public void setGasoline(Boolean gasoline) {
        this.gasoline = gasoline;
        DatabaseManager.updateSettings(this);

    }

    public int getFuelsize() {
        return fuelsize;
    }

    public void setFuelsize(int fuelsize) {
        this.fuelsize = fuelsize;
        DatabaseManager.updateSettings(this);

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
