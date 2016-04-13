package bananacore.epic.controllers;

import bananacore.epic.Constants;
import bananacore.epic.DatabaseManager;
import bananacore.epic.models.WrongGearSession;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import bananacore.epic.interfaces.observers.GearInterface;
import bananacore.epic.interfaces.observers.RPMInterface;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GearController implements GearInterface, RPMInterface, Observer {

    @FXML
    private ImageView gearImageView;
    @FXML
    private Rectangle rectangle;

    private int thresholdRpmGas = 2500;
    private int thresholdRpmDiesel = 2000;

    private int lowerThresholdRpmGas = 1500;
    private int lowerThresholdRpmDiesel = 1000;

    private double maxRpm = 14000;

    private Timestamp rpmTimestamp;
    private Timestamp gearTimestamp;

    private double rpmStatus;
    private int gearStatus = 1;
    private int maxGear = 6;

    private String carType = "gas";
    private double opacity = 1;

    private Boolean isAuto;

    public void initialize() {
        Constants.PARSER.addToGearObservers(this);
        Constants.PARSER.addToRPMObservers(this);
        initGearImageList();
        setGearImage(gearStatus);
        Constants.settingsEPIC.addObserver(this);
        this.isAuto = Constants.settingsEPIC.getAuto();
    }

    @Override
    public void updateGear(int value, Timestamp timestamp) {
        setGearStatus(value);
        this.gearTimestamp = timestamp;
        updateView();
    }

    @Override
    public void updateRPM(int value, Timestamp timestamp) {
        setRpmStatus(value);
        this.rpmTimestamp = timestamp;
        updateView();
    }

    public void updateView() {
        gearImageViewUpdate();
        if (rpmStatus == 0) {
            setGearImage(1);
        }
    }

    public void gearImageViewUpdate() {
        updateBgRpm();
        if (carType.equals("gas")) {
            if (!(gearStatus == maxGear) && rpmStatus >= thresholdRpmGas) {
                setGearImageUp();
                if (session == null && !isAuto) {
                    startWrongGearSession();
                }
            } else if (!(gearStatus == 1) && rpmStatus > 0 && rpmStatus <= lowerThresholdRpmGas) {
                setGearImageDown();
                if (session == null && !isAuto) {
                    startWrongGearSession();
                }
            } else if (gearStatus >= 0 && gearStatus < 7) {
                setGearImage(gearStatus);
                if (session != null) {
                    endWrongGearSession();
                }
            }
        }
    }

    private void updateBgRpm() {
        setOpacity();
        Color colorRed;
        if (rpmStatus >= 2500) {
            colorRed = new Color(1, 0, 0, opacity);
        } else {
            colorRed = new Color(1, 0, 0, 0);
        }
        rectangle.setFill(colorRed);
    }

    private void setOpacity() {
        double x = Math.pow(rpmStatus, 2.0) / 2285 / maxRpm;
        if (x <= 0.60 && x > 0.10) {
            opacity = x;
        }
    }

    public void setGearStatus(int gearStatus) {
        this.gearStatus = gearStatus;
    }

    public void setRpmStatus(int rpmStatus) {
        this.rpmStatus = rpmStatus;
    }

    private List<Image> gearImageList = new ArrayList<Image>();

    private void initGearImageList() {
        Image gearFree = new Image(String.valueOf(getClass().getClassLoader().getResource("image/GearFree.png")));
        Image gear1 = new Image(String.valueOf(getClass().getClassLoader().getResource("image/Gear1.png")));
        Image gear2 = new Image(String.valueOf(getClass().getClassLoader().getResource("image/Gear2.png")));
        Image gear3 = new Image(String.valueOf(getClass().getClassLoader().getResource("image/Gear3.png")));
        Image gear4 = new Image(String.valueOf(getClass().getClassLoader().getResource("image/Gear4.png")));
        Image gear5 = new Image(String.valueOf(getClass().getClassLoader().getResource("image/Gear5.png")));
        Image gear6 = new Image(String.valueOf(getClass().getClassLoader().getResource("image/Gear6.png")));
        Image gearR = new Image(String.valueOf(getClass().getClassLoader().getResource("image/GearR.png")));
        gearImageList.add(gearFree);
        gearImageList.add(gear1);
        gearImageList.add(gear2);
        gearImageList.add(gear3);
        gearImageList.add(gear4);
        gearImageList.add(gear5);
        gearImageList.add(gear6);
        gearImageList.add(gearR);
    }

    private Image arrowUpImage = new Image(String.valueOf(getClass().getClassLoader().getResource("image/arrow_up.png")));

    private void setGearImageUp() {
        gearImageView.setImage(arrowUpImage);
    }

    private Image arrowDownImage = new Image(String.valueOf(getClass().getClassLoader().getResource("image/arrow_down.png")));

    private void setGearImageDown() {
        gearImageView.setImage(arrowDownImage);
    }

    private void setGearImage(int index) {
        gearImageView.setImage(gearImageList.get(index));
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public void setMaxGear(int maxGear) {
        this.maxGear = maxGear;
    }


    private WrongGearSession session;

    private void startWrongGearSession() {
        session = new WrongGearSession();
        session.setStartTime(rpmTimestamp);
        session.setGear(gearStatus);
    }

    private void endWrongGearSession() {
        long diff = rpmTimestamp.getTime() - session.getStartTime().getTime();
        session.setDuration((int) diff);
        if(diff >= 2000){
            DatabaseManager.insertWrongGearSession(session);
        }
        session = null;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.isAuto = Constants.settingsEPIC.getAuto();
        this.maxGear = Constants.settingsEPIC.getGetNumberOfGears();
    }
}
