package bananacore.epic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;

public class App extends Application{

    private Logger logger = LoggerFactory.getLogger(getClass());
    public static void main(String[] args) {
        GearController gearController = new GearController;
        BrakeController brakeController = new BrakeController;
        SpeedController speedController = new SpeedController;
        FuelController fuelController = new FuelController;
        OurParser ourParser = new OurParser();
        setupControllers();

        launch(args);
    }
    //connects all controllers to data
    private void setupControllers (OurParser ourParser, GearController gearController, BreakeController breakeController, SpeedController speedController, fuelController fuelController){
                ourParser.addObserver(ourParser.breakObervers,breakeController);
            ourParser.addObserver(ourParser.gearObservers,gearController);
        ourParser.addObserver(ourParser.rpmObservers,gearController);
        ourParser.addObserver(ourParser.speedObservers,speedController);
        ourParser.addObserver(ourParser.fuelObservers,fuelController);





    }
    @Override
    public void start(Stage primaryStage) {
        logger.debug("App started");
        Pane root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/main.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        primaryStage.setHeight(320);
        primaryStage.setWidth(480);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}