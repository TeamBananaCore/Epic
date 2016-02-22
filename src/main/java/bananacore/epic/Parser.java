package bananacore.epic;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class Parser {
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
    }public boolean getBreakPedalStatus() {
        return extractBoleanValue(brakePedalStatus);
    }

    public static void main(String[] args) {
//  eksempler på    http://www.tutorialspoint.com/json/json_java_example.htm

        //just learning jason. ignore all the stuff under under this point

        String teststring = "{'name':'vehicle_speed','value':0,'timestamp':1364310855.031000}";
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


    }



}
