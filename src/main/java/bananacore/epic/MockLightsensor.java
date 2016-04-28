package bananacore.epic;

import bananacore.epic.interfaces.*;
import javafx.application.Platform;
        import javafx.scene.Scene;
        import javafx.scene.control.Label;
        import javafx.scene.control.Slider;
        import javafx.scene.layout.HBox;
        import javafx.scene.layout.Pane;
        import javafx.stage.Stage;

public class MockLightsensor implements Lightsensor{
    private Slider slider;

    private Stage stage;

    public MockLightsensor(){
        stage = new Stage();
        Pane pane = new Pane();
        slider = new Slider(0, 255, 150);
        Label label = new Label();
        label.textProperty().bind(slider.valueProperty().asString());
        HBox hbox = new HBox(slider, label);
        pane.getChildren().add(hbox);
        stage.setScene(new Scene(pane));
        stage.show();
    }

    public int readAdc(){
        return (int)slider.getValue();
    }

    public void close(){
        System.out.println("Before");
        Platform.runLater(()  -> stage.close());
    }
}
