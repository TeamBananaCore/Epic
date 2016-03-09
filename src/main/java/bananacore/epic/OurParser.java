package bananacore.epic;


import bananacore.epic.interfaces.*;
import javafx.application.Platform;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;


public class OurParser implements Runnable {


    // input example http://openxcplatform.com.s3.amazonaws.com/traces/nyc/downtown-west.json

        volatile  ArrayList<BrakeInterface> brakeObervers = new ArrayList<BrakeInterface>();
    volatile ArrayList<RPMInterface> rpmObservers = new ArrayList<RPMInterface>();
    volatile ArrayList<GearInterface> gearObservers = new ArrayList<GearInterface>();
    volatile ArrayList<SpeedInterface> speedObervers = new ArrayList<SpeedInterface>();
    volatile ArrayList<FuelInterface> fuelObervers = new ArrayList<FuelInterface>();
    volatile ArrayList<OdometerInterface> odometerObservers = new ArrayList<OdometerInterface>();

    private void updateFuelLevelObservers(double value, Timestamp timestamp) {
        for (FuelInterface carController : fuelObervers) {
            carController.updateFuelLevel(value, timestamp);
        }
    }
    private void updateOdometerObservers(double value, Timestamp timestamp){
        for (OdometerInterface carController : odometerObservers) {
            carController.updateOdometer(value, timestamp);
        }
    }

    private void updateFuelSinceRestartObservers(double value, Timestamp timestamp) {
        for (FuelInterface carController : fuelObervers) {
            carController.updateFuelConsumedSinceRestart(value, timestamp);
        }
    }

    private void updateSpeedObservers(int vehiclespeed, Timestamp timestamp) {
        for (SpeedInterface carController : speedObervers) {
            carController.updateVehicleSpeed(vehiclespeed, timestamp);
        }
    }

    private void updateGearObservers(int value, Timestamp timestamp) {
        for (GearInterface carController : gearObservers) {
            carController.updateGear(value, timestamp);
        }
    }

    private void updateRPMObservers(int value, Timestamp timestamp) {
        for (RPMInterface carController : rpmObservers) {
            carController.updateRPM(value, timestamp);
        }
    }

    private void updateBrakeObservers(Boolean value, Timestamp timestamp) {
        for (BrakeInterface carController : brakeObervers) {
            carController.updateBrakePedalStatus(value, timestamp);
        }
    }

//can be used in App to add observers
    public void addObserver(ArrayList observer, Object carController) {
        observer.add(carController);
    }
  //these are used by the Consoles to add themself.
    public void addToBrakeObserver(BrakeInterface controller){
        brakeObervers.add(controller);
    }

    public void addToFuelObserver (FuelInterface controller){
        fuelObervers.add(controller);
    }
    public void addToGearObservers (GearInterface controller){
        gearObservers.add(controller);
    }
    public void addToOdometerObservers (OdometerInterface controller){
        odometerObservers.add(controller);
    }
    public void addToRPMObservers (RPMInterface controller){
        rpmObservers.add(controller);
    }
    public void addToSpeedObservers (SpeedInterface controller){
        speedObervers.add(controller);
    }


    public void updateFromFile(String filepath) {
        JSONParser parser = new JSONParser();
        try {
            FileReader fileReader = new FileReader(filepath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            JSONObject jsonObject = (JSONObject) new JSONParser().parse(bufferedReader.readLine());
            Long diffTime = ( System.currentTimeMillis())- new Double((Double)jsonObject.get("timestamp")*1000).longValue();
            while (bufferedReader.ready()) {
                JSONObject jsonObject2 = (JSONObject) new JSONParser().parse(bufferedReader.readLine());
                Long milliseconds = new Double((Double) jsonObject2.get("timestamp") * 1000).longValue();
                Long currentTime = System.currentTimeMillis();
                Long compareTime = milliseconds- (currentTime-diffTime);
                while ( compareTime>0){
                    currentTime=System.currentTimeMillis();
                    compareTime=milliseconds- (currentTime-diffTime);
                }

                Platform.runLater(() -> updateControllers(jsonObject2));
            }
        } catch (ParseException pe) {
            System.err.println("Parse exception");
        } catch (IOException ieo) {
            System.err.println("IO exception");
        }
    }

    private void updateControllers(JSONObject jsonObject) {
        String name = (String) jsonObject.get("name");
        Double milliseconds = (Double) jsonObject.get("timestamp") * 1000;

        Long time = milliseconds.longValue();

        Timestamp timestamp = new Timestamp(time);



        if (name.equals("engine_speed")) {
            updateRPMObservers(Integer.parseInt(jsonObject.get("value").toString()), timestamp);
        }else if(name.equals("fuel_level"))    {
            updateFuelLevelObservers((Double) jsonObject.get("value"), timestamp);
        }else if (name.equals("fuel_consumed_since_restart")){
            updateFuelSinceRestartObservers((Double) jsonObject.get("value"),timestamp);
        }else if (name.equals("vehicle_speed")){
            updateSpeedObservers((int) Double.parseDouble(jsonObject.get("value").toString()),timestamp);
        }else if (name.equals("brake_pedal_status")){
            updateBrakeObservers((Boolean)jsonObject.get("value"),timestamp);
        }else if (name.equals("transmission_gear_position")){
            updateGearObservers(numericToInt((String) jsonObject.get("value")),timestamp);
        }else if (name.equals("odometer")){
            updateOdometerObservers( Double.parseDouble(jsonObject.get("value").toString()),timestamp);
        }
    }

    private int numericToInt(String numeric) {
        if (numeric.equals("first")) {
            return 1;
        } else if (numeric.equals("second")) {
            return 2;
        } else if (numeric.equals("third")) {
            return 3;
        } else if (numeric.equals("fourth")) {
            return 4;
        } else if (numeric.equals("fifth")) {
            return 5;
        } else if (numeric.equals("sixth")) {
            return 6;
        } else if (numeric.equals("neutral")) {
            return 0;
        } else if (numeric.equals("reverse")) {
            return 7;
        } else {return -1;}
    }

    @Override
    public void run() {

        updateFromFile(getClass().getClassLoader().getResource("downtown-west.txt").getPath());


    }
}
