package bananacore.epic;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

public class OurParser {
    // input example http://openxcplatform.com.s3.amazonaws.com/traces/nyc/downtown-west.json

    //last recorded
    JSONObject vehicleSpeed;
    JSONObject engineSpeed;
    JSONObject fuelConsumedSinceRestart;
    JSONObject odometer;
    JSONObject brakePedalStatus;
    JSONObject acceleratorPedalPosition;
    JSONObject fuelLevel;
    JSONObject transmissionGearPosition;

    ArrayList<BrakeInterface> breakObervers = new ArrayList<BrakeInterface>();
    ArrayList<RPMInterface> rpmObservers = new ArrayList<RPMInterface>();
    ArrayList<GearInterface> gearObservers = new ArrayList<GearInterface>();
    ArrayList<SpeedInterface> speedObervers = new ArrayList<SpeedInterface>();
    ArrayList<FuelInterface> fuelObervers = new ArrayList<FuelInterface>();
    ArrayList<OdometerInterface> odometerObservers = new ArrayList<OdometerInterface>();

    private void updateFuelLevelObservers(double value, Timestamp timestamp) {
        for (FuelInterface carController : fuelObervers) {
            carController.updateFuelLevel(value, timestamp);
        }
    }
    private void updateOdometerObservers(int value, Timestamp timestamp){
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
        for (BrakeInterface carController : breakObervers) {
            carController.updateBreakPedalStatus(value, timestamp);
        }
    }


    public void addObserver(ArrayList observer, Object carController) {
        observer.add(carController);
    }


    public void fileToArrayList(String filepath) {
        JSONParser parser = new JSONParser();
        try {
            FileReader fileReader = new FileReader(filepath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();

            while (bufferedReader.ready()) {
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(bufferedReader.readLine());
                arrayList.add(jsonObject);
                updateControllers(jsonObject);
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
        System.out.println(milliseconds);

        Long time = milliseconds.longValue();

        Timestamp timestamp = new Timestamp(time);

        if (name.equals("engine_speed")) {
            updateRPMObservers(Integer.parseInt(jsonObject.get("value").toString()), timestamp);
        }else if(name.equals("fuel_level"))    {
            updateFuelLevelObservers((Double) jsonObject.get("value"), timestamp);
        }else if (name.equals("fuel_consumed_since_restart")){
            updateFuelSinceRestartObservers((Double) jsonObject.get("value"),timestamp);
        }else if (name.equals("vehicle_speed")){
            updateSpeedObservers((Integer)jsonObject.get("value"),timestamp);
        }else if (name.equals("brake_pedal_status")){
            updateBrakeObservers((Boolean)jsonObject.get("value"),timestamp);
        }else if (name.equals("transmission_gear_position")){
            updateGearObservers((Integer)jsonObject.get("value"),timestamp);
        }else if (name.equals("odometer")){
            updateOdometerObservers((Integer)jsonObject.get("value"),timestamp);
        }

}

    public static void main(String[] args) throws IOException {
//  eksempler på    http://www.tutorialspoint.com/json/json_java_example.htm

        //just learning jason. ignore all the stuff under under this point
        JSONParser parser = new JSONParser();

try {
    FileReader fileReader= new FileReader("src/main/resources/downtown-west.json");
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    Scanner in = new Scanner("src/main/resources/downtown-west.txt");
    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();

    while (bufferedReader.ready()){
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(bufferedReader.readLine());
        arrayList.add(jsonObject);
    }
    for (JSONObject jsonObject : arrayList){
        System.out.println(jsonObject.get("name"));
    }


}catch (ParseException pe){
    System.out.println("feil");
}
    }



}
