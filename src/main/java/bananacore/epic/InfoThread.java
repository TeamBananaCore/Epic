package bananacore.epic;

import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class InfoThread extends Thread {

    private long displayFuel, displaySpeed;
    private boolean onlyFuel;
    private AnchorPane speed;
    private BorderPane fuel;


    public InfoThread(){
        this.setDaemon(true);
    }

    public void setSettings(long displayFuel, long displaySpeed, boolean onlyFuel, AnchorPane speed, BorderPane fuel){
        this.displayFuel = displayFuel;
        this.displaySpeed = displaySpeed;
        this.onlyFuel = onlyFuel;
        this.speed = speed;
        this.fuel = fuel;
    }

    public void run(){
            if(onlyFuel){
                speed.visibleProperty().set(false);
                fuel.visibleProperty().set(true);
            } else {
                if (!speed.visibleProperty().get()){
                    try{
                        Thread.sleep(displaySpeed);
                    }catch(InterruptedException e){
                        run();
                        return;
                    }
                    Platform.runLater(() -> speed.visibleProperty().set(false));
                    //fuel.visibleProperty().set(true);
                } else {
                    try{
                        Thread.sleep(displayFuel);
                    }catch(InterruptedException e){
                        run();
                        return;
                    }
                    speed.visibleProperty().set(true);
                    fuel.visibleProperty().set(false);
                }
            }
        }
}
