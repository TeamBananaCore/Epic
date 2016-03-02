package bananacore.epic;

import javafx.animation.*;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PerformanceBar extends Pane {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final int ACTIVE_TIME = 2000;
    private double min, max, current;
    private Rectangle hand;
    private BooleanProperty activeProperty = new SimpleBooleanProperty(false);

    @FXML
    private Pane overlay;

    public PerformanceBar(@NamedArg(value = "min", defaultValue = "0") double min, @NamedArg(value = "max", defaultValue = "100") double max){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/performanceBar.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        setMinHeight(getPrefHeight());
        setMaxHeight(getPrefHeight());
        setMinWidth(getPrefWidth());
        setMaxWidth(getPrefWidth());
        this.min = min;
        this.max = max;

        overlay.prefHeightProperty().bind(this.prefHeightProperty());
        overlay.prefWidthProperty().bind(this.prefWidthProperty());
        overlay.visibleProperty().bind(activeProperty.not());

        if (min >= max){
            throw new IllegalArgumentException("Min value must be less than max value");
        }

        hand = new Rectangle(5, getPrefHeight());
        hand.setFill(Color.BLACK);
        hand.setVisible(false);
        hand.setLayoutY(0);
        hand.visibleProperty().bind(activeProperty);
        getChildren().add(hand);
    }

    public void setValue(double current) {
        logger.debug("" + current);

        if (current > max) current = max;
        else if (current < min) current = min;
        this.current = current;

        double pos = current/(max-min) * getPrefWidth();
        hand.xProperty().setValue(0.5*getPrefWidth());
        activeProperty.setValue(true);
        Timeline moveHand = new Timeline();
        moveHand.setCycleCount(1);
        moveHand.setAutoReverse(false);
        KeyValue kv = new KeyValue(hand.xProperty(), pos, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.millis(2000), kv);
        moveHand.getKeyFrames().add(kf);
        moveHand.setOnFinished(ae -> {
            Timeline tl = new Timeline(new KeyFrame(Duration.millis(ACTIVE_TIME), e -> setInactive()));
            tl.play();
        });
        moveHand.play();
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max){
        this.max = max;

    }

    private void setInactive(){
        activeProperty.setValue(false);
    }
}
