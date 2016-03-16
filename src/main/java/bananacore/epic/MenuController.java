package bananacore.epic;

import bananacore.epic.interfaces.SpeedInterface;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
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
        Constants.PARSER.addToSpeedObservers(this);
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
        try {
            BorderPane root = new BorderPane();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/graphView.fxml"));
            loader.setController(new GraphController());
            loader.setRoot(root);
            loader.load();
            Constants.SCENE.setRoot(root);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void showSettings(){
        try {
            Parent root = (Parent) FXMLLoader.load(getClass().getClassLoader().getResource("fxml/settings.fxml"));
            Constants.SCENE.setRoot(root);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
