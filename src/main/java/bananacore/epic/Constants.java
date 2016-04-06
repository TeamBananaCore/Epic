package bananacore.epic;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Constants {

    public static final int BRAKE_POST_TRESHOLD = 1000;
    public static double WEIGHT = 2000;
    public static OurParser PARSER;
    public static SettingsEPIC settingsEPIC;//= DatabaseManager.getSettings();
    public static double OPTIMAL_SPEED_REDUCTION = 10.0 / (50.0 * Constants.WEIGHT);
    public static final long SECONDS_PER_WEEK = 604800;

    public static Stage PRIMARY_STAGE;
    public static Scene SCENE;


    static {
        PARSER = new OurParser();
        try{
            settingsEPIC = DatabaseManager.getSettings();
        } catch (IndexOutOfBoundsException e){
            settingsEPIC = new SettingsEPIC(false,6,900,true,50,1,1,30);
        }
    }

}
