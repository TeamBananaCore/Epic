package bananacore.epic;
// Start Real Code
//
//import com.pi4j.io.gpio.GpioPinDigitalInput;
//import com.pi4j.io.gpio.GpioPinDigitalOutput;
//import com.pi4j.io.gpio.PinState;
//import com.pi4j.io.gpio.RaspiPin;
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
// * Use 1kΩ resistor, and connect all of the above connections
// */
//public class Lightsensor {
//    private Logger logger;
//
//    private final GpioPinDigitalInput dataIn = Constants.GPIO.provisionDigitalInputPin(RaspiPin.GPIO_02);
//    private final GpioPinDigitalOutput dataOut = Constants.GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_03);
//    private final GpioPinDigitalOutput chipSelect = Constants.GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_00);
//    private final GpioPinDigitalOutput clk = Constants.GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_01);
//
//    public Lightsensor(){
//        logger = LoggerFactory.getLogger(this.getClass());
//
//        while (true){
//            System.out.println(readAdc());
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private int readAdc(){
//        chipSelect.setState(PinState.HIGH);
//        chipSelect.setState(PinState.LOW);
//        clk.setState(PinState.LOW);
//
//        for (int i = 0; i < 3; i++){
//            dataOut.setState(PinState.HIGH);
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
//}
// End real code
