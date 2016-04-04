package bananacore.epic;


import bananacore.epic.interfaces.*;
import javafx.application.Platform;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;


public class OurParser implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private volatile  ArrayList<BrakeInterface> brakeObservers = new ArrayList<>();
    private volatile ArrayList<RPMInterface> rpmObservers = new ArrayList<>();
    private volatile ArrayList<GearInterface> gearObservers = new ArrayList<>();
    private volatile ArrayList<SpeedInterface> speedObservers = new ArrayList<>();
    private volatile ArrayList<FuelInterface> fuelObservers = new ArrayList<>();
    private volatile ArrayList<OdometerInterface> odometerObservers = new ArrayList<>();

    //List of data we are interested in
    private final List<String> dataTypes = Arrays.asList("engine_speed", "fuel_consumed_since_restart", "vehicle_speed", "brake_pedal_status", "transmission_gear_position", "odometer");

    //Data: The last data that was sent of that type. Diff: The difference from last data required to send new update
    private int lastRPMData = 0;
    private int RPMDataDiff = 100;

    private int lastSpeedData = 0;
    private int speedDataDiff = 1;

    private double lastFuelData = 0;
    private double fuelDataDiff = 0.001;

    private boolean lastBrakeData = false;

    private int lastGearData = -1;

    private double lastOdometerData = 0;
    private double odometerDataDiff = 0.01;

    private boolean debug;

    public OurParser(){
        this(false);
    }

    public OurParser(boolean debug){
        this.debug = debug;
    }

    private void updateOdometerObservers(double value, Timestamp timestamp){
        if(debug) logger.debug(timestamp + " - updating odometer: " + value);
        for (OdometerInterface odometerObserver : odometerObservers) {
            odometerObserver.updateOdometer(value, timestamp);
        }
    }

    private void updateFuelObservers(double value, Timestamp timestamp) {
        if(debug) logger.debug(timestamp + " - updating fuel: " + value);
        for (FuelInterface fuelObserver : fuelObservers) {
            fuelObserver.updateFuelConsumedSinceRestart(value, timestamp);
        }
    }

    private void updateSpeedObservers(int value, Timestamp timestamp) {
        if(debug) logger.debug(timestamp + " - updating speed: " + value);
        for (SpeedInterface speedObserver : speedObservers) {
            speedObserver.updateVehicleSpeed(value, timestamp);
        }
    }

    private void updateGearObservers(int value, Timestamp timestamp) {
        if(debug) logger.debug(timestamp + " - updating gear: " + value);
        for (GearInterface gearObserver : gearObservers) {
            gearObserver.updateGear(value, timestamp);
        }
    }

    private void updateRPMObservers(int value, Timestamp timestamp) {
        if(debug) logger.debug(timestamp + " - updating rpm: " + value);
        for (RPMInterface rpmObserver : rpmObservers) {
            rpmObserver.updateRPM(value, timestamp);
        }
    }

    private void updateBrakeObservers(Boolean value, Timestamp timestamp) {
        if(debug) logger.debug(timestamp + " - updating brake: " + value);
        for (BrakeInterface brakeObserver : brakeObservers) {
            brakeObserver.updateBrakePedalStatus(value, timestamp);
        }
    }
    //these are used by the Controllers to add themselves.
    public void addToBrakeObservers(BrakeInterface controller){
        brakeObservers.add(controller);
    }
    public void addToFuelObservers(FuelInterface controller){
        fuelObservers.add(controller);
    }
    public void addToGearObservers(GearInterface controller){
        gearObservers.add(controller);
    }
    public void addToOdometerObservers(OdometerInterface controller){
        odometerObservers.add(controller);
    }
    public void addToRPMObservers(RPMInterface controller){
        rpmObservers.add(controller);
    }
    public void addToSpeedObservers(SpeedInterface controller){
        speedObservers.add(controller);
    }

    private void updateFromFile(String filepath) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filepath)));

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(bufferedReader.readLine());
            Timestamp time = new Timestamp(new Double((Double) jsonObject.get("timestamp")*1000).longValue());
            while (bufferedReader.ready() && time != null) {
                time = findNext(bufferedReader,parser,time);
            }
        } catch (ParseException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private Timestamp findNext(BufferedReader reader, JSONParser parser, Timestamp current) throws IOException, ParseException, InterruptedException {
        while(reader.ready()){
            JSONObject json = (JSONObject) parser.parse(reader.readLine());
            String dataType = json.get("name").toString();
            if(dataTypes.contains(dataType)){
                Timestamp time = new Timestamp(new Double((Double) json.get("timestamp")*1000).longValue());
                String value = json.get("value").toString();
                switch (dataType){
                    case "engine_speed":
                        int rpm = Integer.parseInt(value);
                        if(Math.abs(lastRPMData-rpm)>RPMDataDiff){
                            sleep(time.getTime()-current.getTime());
                            lastRPMData = rpm;
                            Platform.runLater(()->updateRPMObservers(rpm, time));
                            return time;
                        }
                        break;
                    case "fuel_consumed_since_restart":
                        double fuel = Double.parseDouble(value);
                        if(Math.abs(lastFuelData-fuel)>fuelDataDiff){
                            sleep(time.getTime()-current.getTime());
                            lastFuelData = fuel;
                            Platform.runLater(()->updateFuelObservers(fuel,time));
                            return time;
                        }
                        break;
                    case "vehicle_speed":
                        int speed = (int) Double.parseDouble(value);
                        if(Math.abs(lastSpeedData-speed)>=speedDataDiff){
                            sleep(time.getTime()-current.getTime());
                            lastSpeedData = speed;
                            Platform.runLater(()->updateSpeedObservers(speed,time));
                            return time;
                        }
                        break;
                    case "brake_pedal_status":
                        boolean braking = Boolean.parseBoolean(value);
                        if(braking != lastBrakeData){
                            sleep(time.getTime()-current.getTime());
                            lastBrakeData = braking;
                            Platform.runLater(()->updateBrakeObservers(braking, time));
                            return time;
                        }
                        break;
                    case "transmission_gear_position":
                        int gear = numericToInt(value);
                        if(gear != lastGearData){
                            sleep(time.getTime()-current.getTime());
                            lastGearData = gear;
                            Platform.runLater(()->updateGearObservers(gear, time));
                            return time;
                        }
                        break;
                    case "odometer":
                        double odo = Double.parseDouble(value);
                        if(Math.abs(lastOdometerData-odo) > odometerDataDiff){
                            sleep(time.getTime()-current.getTime());
                            lastOdometerData = odo;
                            Platform.runLater(()->updateOdometerObservers(odo, time));
                            return time;
                        }
                        break;
                }
            }
        }
        return null;
    }

    private void sleep(long millis) throws InterruptedException {
        if(millis > 0){
            Thread.sleep(millis);
        }
    }

    private int numericToInt(String numeric) {
        switch (numeric) {
            case "first": return 1;
            case "second": return 2;
            case "third": return 3;
            case "fourth": return 4;
            case "fifth": return 5;
            case "sixth": return 6;
            case "neutral": return 0;
            case "reverse": return 7;
            default: return -1;
        }
    }

    @Override
    public void run() {
        updateFromFile("data/downtown-west.txt");
    }
}
