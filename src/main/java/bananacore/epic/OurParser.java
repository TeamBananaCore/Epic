package bananacore.epic;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class OurParser {
    // input example http://openxcplatform.com.s3.amazonaws.com/traces/nyc/downtown-west.json


    //etc. put in when created


    //last recorded
    JSONObject vehicleSpeed;
    JSONObject engineSpeed;
    JSONObject torqueAtTransmission;
    JSONObject fuelConsumedSinceRestart;
    JSONObject odometer;
    JSONObject brakePedalStatus;
    JSONObject latitude;
    JSONObject acceleratorPedalPosition;
    JSONObject fuelLevel;


    ArrayList<CarController> breakObervers = new ArrayList<CarController>();
    ArrayList<CarController> rpmObservers = new ArrayList<CarController>();
    ArrayList<CarController> gearObservers = new ArrayList<CarController>();
    ArrayList<SpeedInterface> speedObervers = new ArrayList<CarController>();
    ArrayList<FuelInterface> fuelObervers = new ArrayList<FuelInterface>();

    private void updateFuelObservers(double fuelvalue, double fuelSinceRestart){
        for ( CarController (fuelController)carController : fuelObervers){
             carController.updateFuelLevel(fuelvalue);
            carController.updateFuelConsumedSinceRestart(double fuelSinceRestart);
        }
    }
    private void updateSpeedObservers( )


    public void addObserver (ArrayList observer, CarController carController){
        observer.add(carController);
    }


    //takes inn json and update JSONObjects. we dont know how we get the data.....which makes this a little difficult
    //this method do not work :(
    private void testReadsJASJONfromURL(String url2)throws IOException{
        URL url = new URL(url2);
        InputStream urlInputStream = url.openConnection().getInputStream();
        //reads name
        System.out.println(urlInputStream.read());

    }



//------------------------------
    //From jsonObject to int and boolean

    private int extractIntegerValue(JSONObject obj){
        return (Integer) obj.get("value");
    }

    //not sure if breakpedal uses string or boolean
    private boolean extractBoleanValue(JSONObject obj){
        return (Boolean) obj.get("value");
    }

    //
    public int getVehicleSpeed() {
        return extractIntegerValue(vehicleSpeed);
    }    public int getEngineSpeed() {
        return extractIntegerValue(engineSpeed);
    }  public int getTorqueAtTransmission() {
        return extractIntegerValue(torqueAtTransmission);
    }  public int getFuelConsumedSinceRestart() {
        return extractIntegerValue(fuelConsumedSinceRestart);
    }  public int getOdometer() {
        return extractIntegerValue(odometer);
    }  public int getLatitude() {
        return extractIntegerValue(latitude);
    }  public int getFuelLevel() {
        return extractIntegerValue(fuelLevel);
    }public int getAcceleratorPedalPosition() {
        return extractIntegerValue(acceleratorPedalPosition);
    }public boolean getBreakPedalStatus() {
        return extractBoleanValue(brakePedalStatus);
    }


//--------------------------
    //update consols. consols must implement update+"the ting they update" method    //should this be caled set instead of update`?
//
//    private void updateGearController(GearController gearController){
//        gearController.updateFuelConsumedSinceRestart(getFuelConsumedSinceRestart());
//        gearController.updateVehicleSpeed(getVehicleSpeed());
//        gearController.updateEngineSpeedInterface(getEngineSpeed());
//
//    }
//    private void updateBreakController(BreakController breakController){
//        breakController.updateVehicleSpeed(getVehicleSpeed());
//        breakController.updateBreakePedalStatus(getBreakPedalStatus());
//
//    }
//    private void updateFuelController(FuelController fuelController){
//        fuelController.updateFuelLevel(getFuelLevel());
//        fuelController.updateFuelConsumedSinceRestart(getFuelConsumedSinceRestart());
//    }
//    private void updateSpeedController(SpeedContorller speedContorller){
//        speedContorller.updateVehicleSpeed(getVehicleSpeed());
//    }


    private ArrayList<JSONObject> fileToArrayList(String filepath){
        JSONParser parser = new JSONParser();
        try {
            FileReader fileReader= new FileReader(filepath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();

            while (bufferedReader.ready()){
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(bufferedReader.readLine());
                arrayList.add(jsonObject);
            }

            return arrayList;

        }catch (ParseException pe){
            System.err.println("Parse exception");
        }catch (IOException ieo){
            System.err.println("IO exception");
        }
        return null;
    }

    private void shippJSON ( JSONObject jsonObject){
        String name = jsonObject.get("name");
        if (name.equals("engine_speed"){
            for (CarController carController : speedObervers){

            }
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
