package bananacore.epic;

import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class EpicPreloader extends Preloader {
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ImageView img = new ImageView(getClass().getClassLoader().getResource("image/epic.png").toExternalForm());
        img.setFitWidth(450);
        img.setPreserveRatio(true);
        Label label = new Label("Starting Application");
        label.setFont(Font.font(24));
        label.setTextFill(Paint.valueOf("#49caf5"));
        VBox box = new VBox(20, img, label);
        box.setAlignment(Pos.CENTER);
        stage = new Stage();
        stage.setScene(new Scene(box, 480, 320));
        box.setStyle("-fx-background-color: lightgrey");
        stage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }
}
