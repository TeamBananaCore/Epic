package bananacore.epic;

import bananacore.epic.controllers.ContainerController;
import bananacore.epic.controllers.NumpadController;
import bananacore.epic.models.SettingsEPIC;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Constants {

    public static final int BRAKE_POST_TRESHOLD = 1000;
    public static double WEIGHT = 2000;
    public static OurParser PARSER;
    public static SettingsEPIC settingsEPIC;
    public static double OPTIMAL_SPEED_REDUCTION = 10.0 / (50.0 * Constants.WEIGHT);
    public static final long SECONDS_PER_WEEK = 604800;

    public static ContainerController CONTAINER;
    public static StyleChooser STYLER;

    public static Stage PRIMARY_STAGE;
    public static Scene SCENE;
//    public static final GpioController GPIO = GpioFactory.getInstance();

    public static NumpadController numpadController;


    static {
        PARSER = new OurParser();
        try{
            settingsEPIC = DatabaseManager.getSettings();
        } catch (IndexOutOfBoundsException e){
            settingsEPIC = new SettingsEPIC(false,6,900,true,50,true,true,30, 0);
        }
    }

    public static double calculateBrakePerformance(int startSpeed, int endSpeed, long duration){
        return 100*(duration / ((double)(startSpeed - endSpeed) * WEIGHT))/OPTIMAL_SPEED_REDUCTION;
    }

}
