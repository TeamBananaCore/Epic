package bananacore.epic.models;

import javax.persistence.*;
import bananacore.epic.Constants;
import java.util.Observable;

@Entity(name="Settings")

public class SettingsEPIC extends Observable{

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
    private boolean fueldisplay;

    @Column(name = "displayfuelusage")
    private boolean displayfuelusage;

    @Column(name = "speeddisplay")
    private boolean speeddisplay;

    @Column(name = "screeninterval")
    private int screeninterval;

    @Column(name = "theme")
    private int theme;

    public SettingsEPIC() {
    }

    public SettingsEPIC(Boolean auto, int getNumberOfGears, int weight, Boolean gasoline, int fuelsize, boolean fueldisplay, boolean speeddisplay, int screeninterval, int theme) {
        this.auto = auto;
        this.getNumberOfGears = getNumberOfGears;
        this.weight = weight;
        this.gasoline = gasoline;
        this.fuelsize = fuelsize;
        this.fueldisplay = fueldisplay;
        this.speeddisplay = speeddisplay;
        this.screeninterval = screeninterval;
        this.theme = theme;
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
        valueHasChanged();
    }

    public int getGetNumberOfGears() {
        return getNumberOfGears;
    }

    public void setGetNumberOfGears(int getNumberOfGears) {
        this.getNumberOfGears = getNumberOfGears;
        valueHasChanged();
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
       // DatabaseManager.updateSettings(this);
        valueHasChanged();

    }

    public Boolean getGasoline() {
        return gasoline;
    }
//
    public void setGasoline(Boolean gasoline) {
        this.gasoline = gasoline;
        //DatabaseManager.updateSettings(this);
        valueHasChanged();
    }

    public int getFuelsize() {
        return fuelsize;
    }

    public void setFuelsize(int fuelsize) {
        this.fuelsize = fuelsize;
       // DatabaseManager.updateSettings(this);
        valueHasChanged();
    }

    public boolean getFuelUsagedisplay() {
        return displayfuelusage;
    }

    public void setFuelUsagedisplay(boolean fuelusagedisplay) {
        this.displayfuelusage = fuelusagedisplay;
        valueHasChanged();
    }

    public boolean getFueldisplay() {
        return fueldisplay;
    }

    public void setFueldisplay(boolean fueldisplay) {
        this.fueldisplay = fueldisplay;
        valueHasChanged();
    }

    public boolean getSpeeddisplay() {
        return speeddisplay;
    }

    public void setSpeeddisplay(boolean speeddisplay) {
        this.speeddisplay = speeddisplay;
        valueHasChanged();
    }

    public int getScreeninterval() {
        return screeninterval;
    }

    public void setScreeninterval(int screeninterval) {
        this.screeninterval = screeninterval;
        valueHasChanged();
    }

    public int getTheme() {
        return theme;
    }

    /**
     * Set the theme:
     * 0: adaptable
     * 1: day
     * 2: night
     * @param theme
     */
    public void setTheme(int theme) {
        this.theme = theme;
        Constants.STYLER.setTheme(theme);
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

    public void valueHasChanged(){
        setChanged();
        notifyObservers();
    }

}
