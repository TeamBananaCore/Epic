package bananacore.epic;

import bananacore.epic.controllers.ContainerController;
import javafx.application.Platform;

public class StyleChooser {
    public static final String DAY = "css/day.css";
    public static final String NIGHT = "css/night.css";
    private static final int LIGHT_THRESH = 100;

    private Thread thread;
    private ContainerController container;
    private boolean adaptable;
    private Lightsensor sensor;

    public StyleChooser(ContainerController container){
        this.container = container;
        adaptable = true;
        setAdaptable(adaptable);
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
                container.setStyle(StyleChooser.DAY);
                break;
            case 2: setAdaptable(false);
                container.setStyle(StyleChooser.NIGHT);
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
                    Platform.runLater(() -> container.setStyle(StyleChooser.NIGHT));
                    current = StyleChooser.NIGHT;
                } else if (light > LIGHT_THRESH && current.equals(StyleChooser.NIGHT)){
                    Platform.runLater(() -> container.setStyle(StyleChooser.DAY));
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

    private boolean stopThread(){
        if (adaptable || thread == null) return false;
        thread.interrupt();
        thread = null;
        // TODO Only for testsensor
        sensor.getStage().close();
        sensor = null;
        return true;
    }
}
