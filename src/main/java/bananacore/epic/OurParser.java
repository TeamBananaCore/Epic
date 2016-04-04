package bananacore.epic;


import bananacore.epic.interfaces.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;


public class OurParser implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private volatile  ArrayList<BrakeInterface> brakeObervers = new ArrayList<>();
    private volatile ArrayList<RPMInterface> rpmObservers = new ArrayList<>();
    private volatile ArrayList<GearInterface> gearObservers = new ArrayList<>();
    private volatile ArrayList<SpeedInterface> speedObervers = new ArrayList<>();
    private volatile ArrayList<FuelInterface> fuelObervers = new ArrayList<>();
    private volatile ArrayList<OdometerInterface> odometerObservers = new ArrayList<>();

    //List of data we are interested in
    private final List<String> dataTypes = Arrays.asList("engine_speed", "fuel_consumed_since_restart", "vehicle_speed", "brake_pedal_status", "transmission_gear_position", "odometer");

    //Data: The last data that was sent of that type. Diff: The difference from last data required to send new update
    int lastRPMData = 0;
    int RPMDataDiff = 50;

    int lastSpeedData = 0;
    int speedDataDiff = 1;

    double lastFuelData = 0;
    double fuelDataDiff = 0.01;

    boolean lastBrakeData = false;

    int lastGearData = -1;

    double lastOdometerData = 0;
    double odometerDataDiff = 1;

    private void updateOdometerObservers(double value, Timestamp timestamp){
        logger.debug(timestamp + " - updating odometer: " + value);
        for (OdometerInterface odometerObserver : odometerObservers) {
            odometerObserver.updateOdometer(value, timestamp);
        }
    }

    private void updateFuelObservers(double value, Timestamp timestamp) {
        logger.debug(timestamp + " - updating fuel: " + value);
        for (FuelInterface fuelObserver : fuelObervers) {
            fuelObserver.updateFuelConsumedSinceRestart(value, timestamp);
        }
    }

    private void updateSpeedObservers(int value, Timestamp timestamp) {
        logger.debug(timestamp + " - updating speed: " + value);
        for (SpeedInterface speedObserver : speedObervers) {
            speedObserver.updateVehicleSpeed(value, timestamp);
        }
    }

    private void updateGearObservers(int value, Timestamp timestamp) {
        logger.debug(timestamp + " - updating gear: " + value);
        for (GearInterface gearObserver : gearObservers) {
            gearObserver.updateGear(value, timestamp);
        }
    }

    private void updateRPMObservers(int value, Timestamp timestamp) {
        logger.debug(timestamp + " - updating rpm: " + value);
        for (RPMInterface rpmObserver : rpmObservers) {
            rpmObserver.updateRPM(value, timestamp);
        }
    }

    private void updateBrakeObservers(Boolean value, Timestamp timestamp) {
        logger.debug(timestamp + " - updating brake: " + value);
        for (BrakeInterface brakeObserver : brakeObervers) {
            brakeObserver.updateBrakePedalStatus(value, timestamp);
        }
    }
    //these are used by the Controllers to add themselves.
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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filepath)));

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
                            if(time.getTime()>current.getTime()){
                                Thread.sleep(time.getTime()-current.getTime());
                            }
                            lastRPMData = rpm;
                            Platform.runLater(()->updateRPMObservers(rpm, time));
                            return time;
                        }
                        break;
                    case "fuel_consumed_since_restart":
                        double fuel = Double.parseDouble(value);
                        if(Math.abs(lastFuelData-fuel)>fuelDataDiff){
                            if(time.getTime()>current.getTime()){
                                Thread.sleep(time.getTime()-current.getTime());
                            }
                            lastFuelData = fuel;
                            Platform.runLater(()->updateFuelObservers(fuel,time));
                            return time;
                        }
                        break;
                    case "vehicle_speed":
                        int speed = (int) Double.parseDouble(value);
                        if(Math.abs(lastSpeedData-speed)>=speedDataDiff){
                            if(time.getTime()>current.getTime()){
                                Thread.sleep(time.getTime()-current.getTime());
                            }
                            lastSpeedData = speed;
                            Platform.runLater(()->updateSpeedObservers(speed,time));
                            return time;
                        }
                        break;
                    case "brake_pedal_status":
                        boolean braking = Boolean.parseBoolean(value);
                        if(braking != lastBrakeData){
                            if(time.getTime()>current.getTime()){
                                Thread.sleep(time.getTime()-current.getTime());
                            }
                            lastBrakeData = braking;
                            Platform.runLater(()->updateBrakeObservers(braking, time));
                            return time;
                        }
                        break;
                    case "transmission_gear_position":
                        int gear = numericToInt(value);
                        if(gear != lastGearData){
                            if(time.getTime()>current.getTime()){
                                Thread.sleep(time.getTime()-current.getTime());
                            }
                            lastGearData = gear;
                            Platform.runLater(()->updateGearObservers(gear, time));
                            return time;
                        }
                        break;
                    case "odometer":
                        double odo = Double.parseDouble(value);
                        if(Math.abs(lastOdometerData-odo) > odometerDataDiff){
                            if(time.getTime()>current.getTime()){
                                Thread.sleep(time.getTime()-current.getTime());
                            }
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
