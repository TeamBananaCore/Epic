package bananacore.epic.controllers;

import bananacore.epic.Constants;
import bananacore.epic.DatabaseManager;
import bananacore.epic.interfaces.AutomaticCar;
import bananacore.epic.interfaces.ViewController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class MainController implements ViewController, AutomaticCar {

    public BorderPane info;
    @FXML public AnchorPane gear;


    public void initialize(){
        updateAutomaticView();
    }

    public void updateAutomaticView(){
        if(DatabaseManager.getSettings().getAuto()){
            gear.setVisible(false);
            gear.setMaxWidth(0);
            info.setMinWidth(480);
        }else{
            gear.setVisible(true);
            gear.setMinWidth(150);
            info.setMaxWidth(340);
        }
    }

    @Override
    public void hidden() {

    }

    @Override
    public void shown() {

    }



    @Override
    public void automaticCarSettingsUpdate(Constants constants) {
        updateAutomaticView();
    }
}
