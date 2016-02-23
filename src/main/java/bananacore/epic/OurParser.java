package bananacore.epic;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class OurParser {
    // input example http://openxcplatform.com.s3.amazonaws.com/traces/nyc/downtown-west.json

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
//---------------------------
    //Listeners listen to when JSONObjects get changed



//--------------------------
    //update consols. consols must implement update+"the ting they update" method    //should this be caled set instead of update`?

    private void updateGearController(GearController gearController){
        gearController.updateFuelConsumedSinceRestart(getFuelConsumedSinceRestart());
        gearController.updateVehicleSpeed(getVehicleSpeed());
        gearController.updateEngineSpeedInterface(getEngineSpeed());

    }
    private void updateBreakController(BreakController breakController){
        breakController.updateVehicleSpeed(getVehicleSpeed());
        breakController.updateBreakePedalStatus(getBreakPedalStatus());

    }
    private void updateFuelController(FuelController fuelController){
        fuelController.updateFuelLevel(getFuelLevel());
        fuelController.updateFuelConsumedSinceRestart(getFuelConsumedSinceRestart());
    }
    private void updateSpeedController(SpeedContorller speedContorller){
        speedContorller.updateVehicleSpeed(getVehicleSpeed());
    }


    public static void main(String[] args) throws IOException {
//  eksempler på    http://www.tutorialspoint.com/json/json_java_example.htm

        //just learning jason. ignore all the stuff under under this point

        String teststring = "{'name':'vehicle_speed','value':0,'timestamp':1364310855.031110}";
        JSONObject jasonObjekt= new JSONObject();
        jasonObjekt.put("name", "vehicle_speed");
        jasonObjekt.put("value", new Integer(100));

       // String name= jasonObjekt.get();
        System.out.println(jasonObjekt);

        System.out.println(jasonObjekt.get("name"));
        System.out.println(jasonObjekt.get("name").getClass());
        Object vechile_speed= jasonObjekt.get("name");
        System.out.println(vechile_speed);
        JSONParser parser = new JSONParser();

        String booleantest = "{\"name\":\"brake_pedal_status\",\"value\":true,\"timestamp\":1364312260.599000}";
        JSONObject test2 = new JSONObject();
        test2.put("value", new Boolean(true));
        System.out.println(test2);

        OurParser ourParser = new OurParser();
        ourParser.testReadsJASJONfromURL("http://openxcplatform.com.s3.amazonaws.com/traces/nyc/uptown-west.json");

    }



}
