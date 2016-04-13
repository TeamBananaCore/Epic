package bananacore.epic.controllers;

import bananacore.epic.Constants;
import bananacore.epic.DatabaseManager;
import bananacore.epic.interfaces.ViewController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.util.Observable;
import java.util.Observer;

public class MainController implements ViewController, Observer {

    public BorderPane info;
    @FXML public AnchorPane gear;


    public void initialize(){
        Constants.settingsEPIC.addObserver(this);
        updateAutomaticView();
    }

    public void updateAutomaticView(){
        if(Constants.settingsEPIC.getAuto()){
            gear.setVisible(false);
            gear.setMaxWidth(0);
            info.setMinWidth(480);
        }else{
            gear.setVisible(true);
            gear.setMinWidth(150);
            info.setMaxWidth(340);
            info.setMinWidth(340);
        }
    }

    @Override
    public void hidden() {

    }

    @Override
    public void shown() {

    }

    @Override
    public void update(Observable o, Object arg) {
        updateAutomaticView();
    }
}
