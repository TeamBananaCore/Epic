package bananacore.epic;

import bananacore.epic.controllers.ContainerController;
import bananacore.epic.controllers.NumpadController;
import bananacore.epic.models.SettingsEPIC;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Constants {

    public static final int BRAKE_POST_TRESHOLD = 1000;
    public static OurParser PARSER;
    public static SettingsEPIC settingsEPIC;
    public static final long SECONDS_PER_WEEK = 604800;

    public static ContainerController CONTAINER;
    public static StyleChooser STYLER;
    public static boolean firstTimeUse=false;
    public static ImageView FUEL_IMAGE;

    //From config file
    public static String DATASET;
    public static int LIGHT_THRESH;
    public static boolean DATASET_REPETITION;
    public static int TIME_SPEED;


    public static Scene SCENE;

    public static GpioController GPIO;

    //Set to true when building to Pi!
    public static boolean ON_PI = false;

    public static NumpadController numpadController;

    private static Logger logger = LoggerFactory.getLogger(Constants.class);

    public static double calculateBrakePerformance(int startSpeed, int endSpeed, long duration){
        return 100*(duration / ((double)(startSpeed - endSpeed) * settingsEPIC.getWeight()))/optimalSpeedReduction();
    }

    private static double optimalSpeedReduction(){
        return 30.0 / (50.0 * settingsEPIC.getWeight());
    }

    public static void setUp(){
        if(ON_PI){
            rasPiSetup();
            logger.info("Running with Pi config");
            try {
                loadConfig(new BufferedReader(new InputStreamReader(new FileInputStream("/home/pi/epic.config"))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            logger.info("Not running with Pi config");
            loadConfig(new BufferedReader(new InputStreamReader(Constants.class.getClassLoader().getResourceAsStream("epic.config"))));
        }
        DatabaseManager.connectToDB();


        PARSER = new OurParser();
        try{
            settingsEPIC = DatabaseManager.getSettings();
            PARSER.setSendData(true);
        } catch (IndexOutOfBoundsException e){
            settingsEPIC = new SettingsEPIC(false,6,900,true,50,true,true,30, 0);
            firstTimeUse=true;
            PARSER.setSendData(false);
        }
    }

    private static void loadConfig(BufferedReader reader){
        try {
            JSONObject config = new JSONObject((JSONObject) new JSONParser().parse(reader.readLine()));
            DATASET = config.get("dataset").toString();
            LIGHT_THRESH = Integer.parseInt(config.get("light_thresh").toString());
            DATASET_REPETITION = Boolean.parseBoolean(config.get("dataset_repetition").toString());
            TIME_SPEED = Integer.parseInt(config.get("time_speed").toString());
            logger.info("Using dataset: " + DATASET);
            logger.info("Using light threshold: " + LIGHT_THRESH);
            logger.info("Dataset repetition: " + DATASET_REPETITION);
            logger.info("Using time speed: x" + TIME_SPEED);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void rasPiSetup(){
        GPIO = GpioFactory.getInstance();
        GPIO.unprovisionPin(Constants.GPIO.provisionDigitalInputPin(RaspiPin.GPIO_28),
                Constants.GPIO.provisionDigitalInputPin(RaspiPin.GPIO_25),
                Constants.GPIO.provisionDigitalInputPin(RaspiPin.GPIO_22),
                Constants.GPIO.provisionDigitalInputPin(RaspiPin.GPIO_26),
                Constants.GPIO.provisionDigitalInputPin(RaspiPin.GPIO_27));
    }

}
