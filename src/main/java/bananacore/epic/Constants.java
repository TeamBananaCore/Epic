package bananacore.epic;

import bananacore.epic.controllers.ContainerController;
import bananacore.epic.controllers.NumpadController;
import bananacore.epic.models.SettingsEPIC;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;

public class Constants {

    public static final int BRAKE_POST_TRESHOLD = 1000;
    public static OurParser PARSER;
    public static SettingsEPIC settingsEPIC;
    public static final long SECONDS_PER_WEEK = 604800;

    public static ContainerController CONTAINER;
    public static StyleChooser STYLER;
    public static boolean firstTimeUse=false;
    public static ImageView FUEL_IMAGE;

    public static Scene SCENE;

    // TODO Uncomment when on pi
//    public static final GpioController GPIO = GpioFactory.getInstance();

    public static NumpadController numpadController;

    static {
        PARSER = new OurParser();
        try{
            settingsEPIC = DatabaseManager.getSettings();
        } catch (IndexOutOfBoundsException e){
            settingsEPIC = new SettingsEPIC(false,6,900,true,50,true,true,30, 0);
            firstTimeUse=true;
        }
    }

    public static double calculateBrakePerformance(int startSpeed, int endSpeed, long duration){
        return 100*(duration / ((double)(startSpeed - endSpeed) * settingsEPIC.getWeight()))/optimalSpeedReduction();
    }

    private static double optimalSpeedReduction(){
        return 30.0 / (50.0 * settingsEPIC.getWeight());
    }

}
