package bananacore.epic.controllers;

import bananacore.epic.Constants;
import bananacore.epic.interfaces.NumpadInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class NumpadController  {

    private String numberview;
    // private Text numberview;
    @FXML private Text title;
    @FXML private Button seven;
    @FXML private Button eight;
    @FXML private Button nine;
    @FXML private Button four;
    @FXML private Button five;
    @FXML private Button six;
    @FXML private Button one;
    @FXML private Button two;
    @FXML private Button three;
    @FXML private Button backspace;
    @FXML private Button zero;
    @FXML private Button ok;
    NumpadInterface user;
    @FXML private AnchorPane numberpane;

    /*public Numpad(String title, NumpadInterface user) {
        this.title.setText(title);
        this.user = user;
    }*/
//connects numpad and settings controllers
    SettingsController settingsController;
    public void initialize() {
        Constants.numpadController=this;
        numberview="0";
    }

    public void addSettingsController(SettingsController settingsController){
        this.settingsController=settingsController;
    }

    public void changeSettingsControllerText(){
        settingsController.setWeightOrSize(numberview);
    }

    public String getNumberview() {
        return numberview;
    }

    public void setNumberview(String numberview) {
        this.numberview = numberview;
    }

    @FXML
    public void onePressed() {
        if (numberview.equals("0")) {
            numberview="1";
        } else {
            numberview=(numberview + 1);
        }
        changeSettingsControllerText();
    }
    @FXML
    public void twoPressed() {
        if (numberview.equals("0")) {
            numberview=("2");
        } else {
            numberview=(numberview + 2);
        }
        changeSettingsControllerText();
    }
    @FXML
    public void threePressed() {
        if (numberview.equals("0")) {
            numberview=("3");
        } else {
            numberview=(numberview + 3);
        }
        changeSettingsControllerText();

    }
    @FXML
    public void fourPressed() {
        if (numberview.equals("0")) {
            numberview=("4");
        } else {
            numberview=(numberview + 4);
        }
        changeSettingsControllerText();

    }
    @FXML
    public void fivePressed() {
        if (numberview.equals("0")) {
            numberview=("5");
        } else {
            numberview=(numberview + 5);
        }
        changeSettingsControllerText();

    }
    @FXML
    public void sixPressed() {
        if (numberview.equals("0")) {
            numberview=("6");
        } else {
            numberview=(numberview + 6);
        }
    }
    @FXML
    public void sevenPressed() {
        if (numberview.equals("0")) {
            numberview=("7");
        } else {
            numberview=(numberview + 7);
        }
        changeSettingsControllerText();

    }
    @FXML
    public void eightPressed() {
        if (numberview.equals("0")) {
            numberview=("8");
        } else {
            numberview=(numberview + 8);
        }
        changeSettingsControllerText();

    }
    @FXML
    public void ninePressed() {
        if (numberview.equals("0")) {
            numberview=("9");
        } else {
            numberview=(numberview + 9);
        }
    }
    @FXML
    public void zeroPressed() {
        if (numberview.equals("0")) {
            numberview=("0");
        } else {
            numberview=(numberview + 0);
        }
        changeSettingsControllerText();

    }
    @FXML
    public void backspacePressed() {
        String text = numberview;
        if (text.length() > 1) {
            numberview=(text.substring(0, text.length() - 1));
        } else if (text.length() == 1) {
            numberview=("0");
        }
        changeSettingsControllerText();

    }

    //this removes the numpad for the Settings
    @FXML
    public void okPressed() {
        settingsController.getNumpadPane().setVisible(false);

    }

    public String getNumber() {
        return numberview;
    }



}