package bananacore.epic;


import bananacore.epic.interfaces.SpeedInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.sql.Timestamp;

public class InfoController implements SpeedInterface{

    private Timestamp lastTime;

    @FXML
    BorderPane fuel;

    @FXML
    AnchorPane speed;

    public void initialize(){
        speed.visibleProperty().set(false);
    }

    @Override
    public void updateVehicleSpeed(int value, Timestamp timestamp) {
        if(lastTime == null){
            lastTime = timestamp;
        } else {
            if(timestamp.getTime() - lastTime.getTime() > 2000){
                if(speed.visibleProperty().get()){
                    speed.visibleProperty().set(false);
                    fuel.visibleProperty().set(true);
                } else {
                    speed.visibleProperty().set(true);
                    fuel.visibleProperty().set(false);
                }
            }
        }

    }
}
