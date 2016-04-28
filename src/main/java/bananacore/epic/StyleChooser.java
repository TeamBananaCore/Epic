package bananacore.epic;

import bananacore.epic.controllers.ContainerController;
import bananacore.epic.interfaces.Lightsensor;
import javafx.application.Platform;
import javafx.scene.image.Image;
import org.slf4j.Logger;

public class StyleChooser {
    public static final String DAY = "css/day.css";
    public static final String NIGHT = "css/night.css";

    private Thread thread;
    private ContainerController container;
    private boolean adaptable;
    private Image dayImage;
    private Image nightImage;

    private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    public StyleChooser(ContainerController container){
        this.container = container;
        dayImage = new Image(getClass().getClassLoader().getResource("image/fuelpump_day.png").toExternalForm());
        nightImage = new Image(getClass().getClassLoader().getResource("image/fuelpump_night.png").toExternalForm());
        setTheme(Constants.settingsEPIC.getTheme());
        logger.info("THEME FROM START: " + Constants.settingsEPIC.getTheme());
    }

    public void setAdaptable(boolean adaptable){
        this.adaptable = adaptable;
        if (adaptable) startThread();
        else stopThread();
    }

    public boolean isAdaptable(){
        return adaptable;
    }

    public void setTheme(int theme){
        switch (theme){
            case 0: setAdaptable(true);
                break;
            case 1: setAdaptable(false);
                setDay();
                break;
            case 2: setAdaptable(false);
                setNight();
                break;
            default: setTheme(0);
        }
    }

    private boolean startThread(){
        if (!adaptable || thread != null) return false;
        Lightsensor sensor;
        if(Constants.ON_PI){
            sensor = new PiLightsensor();
        }else{
            sensor = new MockLightsensor();
        }
        thread = new Thread(() -> {
            String current = StyleChooser.DAY;
            setDay();

            while (true){
                int light = sensor.readAdc();
                logger.debug("Light sensor reading: " + light);
                if (light < Constants.LIGHT_THRESH && current.equals(StyleChooser.DAY)){
                    Platform.runLater(() -> setNight());
                    current = StyleChooser.NIGHT;
                } else if (light > Constants.LIGHT_THRESH && current.equals(StyleChooser.NIGHT)){
                    Platform.runLater(() -> setDay());
                    current = StyleChooser.DAY;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    sensor.close();
                    break;
                }
                if (Thread.interrupted()) {
                    sensor.close();
                    break;
                }
            }
        });
        thread.start();
        return true;
    }

    private boolean stopThread() {
        if (adaptable || thread == null) return false;
        thread.interrupt();
        thread = null;
        return true;
    }

    private void setDay(){
        Platform.runLater(() -> container.setStyle(StyleChooser.DAY));
        Constants.FUEL_IMAGE.setImage(dayImage);
    }

    private void setNight() {
        Platform.runLater(() -> container.setStyle(StyleChooser.NIGHT));
        Constants.FUEL_IMAGE.setImage(nightImage);
    }
}
