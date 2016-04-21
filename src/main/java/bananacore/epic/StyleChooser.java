package bananacore.epic;

import bananacore.epic.controllers.ContainerController;
import javafx.application.Platform;
import javafx.scene.image.Image;

public class StyleChooser {
    public static final String DAY = "css/day.css";
    public static final String NIGHT = "css/night.css";
    private static final int LIGHT_THRESH = 100;

    private Thread thread;
    private ContainerController container;
    private boolean adaptable;
    private Lightsensor sensor;
    private Image dayImage;
    private Image nightImage;

    public StyleChooser(ContainerController container){
        this.container = container;
        dayImage = new Image(getClass().getClassLoader().getResource("image/fuelpump_day.png").toExternalForm());
        nightImage = new Image(getClass().getClassLoader().getResource("image/fuelpump_night.png").toExternalForm());
        setTheme(Constants.settingsEPIC.getTheme());
        System.out.println("THEME FROM START: " + Constants.settingsEPIC.getTheme());
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
        sensor = new Lightsensor();
        thread = new Thread(() -> {
            String current = StyleChooser.NIGHT;

            while (true){
                int light = sensor.readAdc();
                if (light < LIGHT_THRESH && current.equals(StyleChooser.DAY)){
                    Platform.runLater(() -> setNight());
                    current = StyleChooser.NIGHT;
                } else if (light > LIGHT_THRESH && current.equals(StyleChooser.NIGHT)){
                    Platform.runLater(() -> setDay());
                    current = StyleChooser.DAY;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    break;
                }
                if (Thread.interrupted()) break;
            }
        });
        thread.start();
        return true;
    }

    private boolean stopThread() {
        if (adaptable || thread == null) return false;
        thread.interrupt();
        thread = null;
        // TODO Only for testsensor
//        sensor.getStage().close();
        sensor = null;
        return true;
    }

    private void setDay(){
        container.setStyle(StyleChooser.DAY);
        Constants.FUEL_IMAGE.setImage(dayImage);
    }

    private void setNight() {
        container.setStyle(StyleChooser.NIGHT);
        Constants.FUEL_IMAGE.setImage(nightImage);
    }
}
