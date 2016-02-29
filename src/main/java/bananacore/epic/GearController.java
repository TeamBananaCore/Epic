package bananacore.epic;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class GearController {

    @FXML
    private ImageView gearImageView;
    @FXML
    private Rectangle rectangle;

    private int thresholdRpmGas = 3000;
    private int thresholdRpmDiesel = 2000;

    private int lowerThresholdRpmGas = 1500;
    private int lowerThresholdRpmDiesel = 1000;

    private double maxRpm = 14000;

    private double rpmStatus;
    private int gearStatus = 4;

    private String carType = "gas";

    private double opacity = 1;

    public void initialize(){
        initGearImageList();
        setGearImage(gearStatus);
    }

    public void updateView(){
        gearImageViewUpdate();
        if(rpmStatus == 0){
            setGearImage(1);
        }
    }

    public void gearImageViewUpdate(){
        updateBgRpm();
        if(carType.equals("gas")){
            if(rpmStatus >= thresholdRpmGas){
                setGearImageUp();
            }else if( rpmStatus > 0 && rpmStatus <= lowerThresholdRpmGas){
                setGearImageDown();
            }else if(gearStatus >= 0 && gearStatus < 7){
                setGearImage(gearStatus);
            }
        }
    }

    private void updateBgRpm() {
        setOpacity();
        System.out.println(opacity);
        Color colorRed;
        if(rpmStatus >= 2500){
            colorRed = new Color(1,0,0,opacity);
        }else{
            colorRed = new Color(1,0,0,0);
        }
        rectangle.setFill(colorRed);
    }

    private void setOpacity(){
        double x = Math.pow(rpmStatus, 2.0) / 2285 / maxRpm;
        if(x <= 0.60 && x > 0.10){
            opacity =x;
        }
    }

    //Bare en test
    @FXML
    private TextField textField;

    @FXML
    private void updateRpm(){
        if(!textField.getText().isEmpty()){
            rpmStatus = Double.parseDouble(textField.getText());
            gearImageViewUpdate();
        }
    }
    //Test slutt

    public int getGearStatus() {
        return gearStatus;
    }

    public void setGearStatus(int gearStatus) {
        this.gearStatus = gearStatus;
    }

    public double getRpmStatus() {
        return rpmStatus;
    }

    public void setRpmStatus(int rpmStatus) {
        this.rpmStatus = rpmStatus;
    }


    private List<Image> gearImageList = new ArrayList<Image>();
    private void initGearImageList() {
        Image gearR = new Image(String.valueOf(getClass().getClassLoader().getResource("image/GearR.png")));
        Image gear1 = new Image(String.valueOf(getClass().getClassLoader().getResource("image/Gear1.png")));
        Image gear2 = new Image(String.valueOf(getClass().getClassLoader().getResource("image/Gear2.png")));
        Image gear3 = new Image(String.valueOf(getClass().getClassLoader().getResource("image/Gear3.png")));
        Image gear4 = new Image(String.valueOf(getClass().getClassLoader().getResource("image/Gear4.png")));
        Image gear5 = new Image(String.valueOf(getClass().getClassLoader().getResource("image/Gear5.png")));
        Image gear6 = new Image(String.valueOf(getClass().getClassLoader().getResource("image/Gear6.png")));
        gearImageList.add(gearR);
        gearImageList.add(gear1);
        gearImageList.add(gear2);
        gearImageList.add(gear3);
        gearImageList.add(gear4);
        gearImageList.add(gear5);
        gearImageList.add(gear6);
    }

    private Image arrowUpImage = new Image(String.valueOf(getClass().getClassLoader().getResource("image/arrow_up.png")));
    private void setGearImageUp(){
        gearImageView.setImage(arrowUpImage);
    }

    private Image arrowDownImage = new Image(String.valueOf(getClass().getClassLoader().getResource("image/arrow_down.png")));
    private void setGearImageDown(){
        gearImageView.setImage(arrowDownImage);
    }


    private void setGearImage(int index){
        gearImageView.setImage(gearImageList.get(index));
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }
}