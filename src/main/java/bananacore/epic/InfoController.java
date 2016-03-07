package bananacore.epic;


import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class InfoController{

    @FXML
    BorderPane fuel;

    @FXML
    AnchorPane speed;

    InfoThread infoThread;

    public void initialize(){
        speed.visibleProperty().set(false);
        infoThread = new InfoThread();
        infoThread.setSettings(5000, 1000, false, speed, fuel);
        //startRunning();
    }

    public void startRunning(){
        while(true){
            infoThread.run();
        }
    }

}
