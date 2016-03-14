package bananacore.epic;

import bananacore.epic.interfaces.SpeedInterface;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.sql.Timestamp;

public class MenuController implements SpeedInterface{

    @FXML private Button graphButton;
    @FXML private Button settingsButton;

    @FXML AnchorPane menuPane;
    private int speed = 0;

    public void initialize(){
        Image graphImage = new Image(String.valueOf(getClass().getClassLoader().getResource("image/graphImage.png")), 65, 65, false, false);
        Image settingsImage = new Image(String.valueOf(getClass().getClassLoader().getResource("image/settingImage.png")), 80, 80, false, false);
        graphButton.setGraphic(new ImageView(graphImage));
        settingsButton.setGraphic(new ImageView(settingsImage));

        update();
    }

    @Override
    public void updateVehicleSpeed(int value, Timestamp timestamp) {
        speed = value;
        update();
    }

    public void update(){
        if(speed == 0){
            final Timeline timeline = new Timeline();
            timeline.setAutoReverse(true);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000),
                    new KeyValue(menuPane.translateYProperty(), 135)));
            timeline.play();
        }else {
            final Timeline timeline = new Timeline();
            timeline.setAutoReverse(true);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000),
                    new KeyValue(menuPane.translateYProperty(), -135)));
            timeline.play();
        }
    }

    @FXML
    public void showGraph(){
        speed = 0;
        update();
    }

    @FXML
    public void showSettings(){
        speed = 1;
        update();
    }
}
