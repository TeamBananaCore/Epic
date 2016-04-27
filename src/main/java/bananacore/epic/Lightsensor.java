package bananacore.epic;
// Start Real Code

//import com.pi4j.io.gpio.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Class for communicating with GPIO
// *
// * Connect ADC in this way:
// * CLK: PIN 12, GPIO 1
// * DO: PIN 13, GPIO 2
// * DI: PIN 15, GPIO 3
// * CS: PIN 11, GPIO 0
// *
// * Above is wrong
// *
// * Use 1kΩ resistor, and connect all of the above connections
// */
//public class Lightsensor {
//    private Logger logger;
//
//    private final GpioPinDigitalInput dataIn = Constants.GPIO.provisionDigitalInputPin(RaspiPin.GPIO_28);
//    private final GpioPinDigitalOutput dataOut = Constants.GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_25);
//    private final GpioPinDigitalOutput chipSelect = Constants.GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_22);
//    private final GpioPinDigitalOutput clk = Constants.GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_26);
//    private final GpioPinDigitalOutput pwr = Constants.GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_27);
//
//    public Lightsensor(){
//        logger = LoggerFactory.getLogger(this.getClass());
//
//        pwr.setState(PinState.HIGH);
//    }
//
//    public int readAdc(){
//        chipSelect.setState(PinState.HIGH);
//        chipSelect.setState(PinState.LOW);
//        clk.setState(PinState.LOW);
//
//        for (int i : new int[]{1,1,0}){
//            if(i == 1){
//                dataOut.setState(PinState.HIGH);
//            }else{
//                dataOut.setState(PinState.LOW);
//            }
//            clk.setState(PinState.HIGH);
//            clk.setState(PinState.LOW);
//        }
//
//        int ad = 0;
//        for (int i = 0; i < 8; i++){
//            clk.setState(PinState.HIGH);
//            clk.setState(PinState.LOW);
//            ad <<= 1;
//            if (dataIn.getState().isHigh()){
//                ad |= 0x1;
//            }
//        }
//
//        chipSelect.setState(PinState.HIGH);
//        return ad;
//    }
//
//    public void close(){
//        Constants.GPIO.unprovisionPin(dataIn, dataOut, clk, chipSelect, pwr);
//    }
//}
//End real code

import javafx.application.Platform;
        import javafx.scene.Scene;
        import javafx.scene.control.Label;
        import javafx.scene.control.Slider;
        import javafx.scene.layout.HBox;
        import javafx.scene.layout.Pane;
        import javafx.stage.Stage;

public class Lightsensor{
    private Slider slider;

    private Stage stage;

    public static Lightsensor createLightsenssor(){
        return new Lightsensor();
    }

    public Lightsensor(){
        stage = new Stage();
        Pane pane = new Pane();
        slider = new Slider(0, 255, 150);
        Label label = new Label();
        label.textProperty().bind(slider.valueProperty().asString());
        HBox hbox = new HBox(slider, label);
        pane.getChildren().add(hbox);
        stage.setScene(new Scene(pane));
        stage.show();
    }

    public int readAdc(){
        return (int)slider.getValue();
    }

    public void close(){
        System.out.println("Before");
        Platform.runLater(()  -> stage.close());
    }
}