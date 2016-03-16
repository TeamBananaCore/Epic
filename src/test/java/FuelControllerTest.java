
import bananacore.epic.FuelController;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class FuelControllerTest {

    /*FuelController controller;

    public void initController(){
        controller = new FuelController();
    }

    @Test
    public void testUpdateOnStart(){
        initController();
        controller.updateFuel(100.0, 0.0);
        assertEquals(100.0, controller.getFuelLevelPercentage(), 0);
        assertEquals(1.0, controller.getFuelUsageInterval(), 0);
    }

    @Test
    public void testValidity(){
        initController();
        try{
            controller.updateFuel(100.0, -1.0);
        } catch (Exception e){
            assertTrue(e.getClass().equals(IllegalArgumentException.class));
        }
        try{
            controller.updateFuel(110, 1.0);
        } catch (Exception e){
            assertTrue(e.getClass().equals(IllegalArgumentException.class));
        }

        try{
            controller.updateOdometer(-500.0);
        } catch (Exception e){
            assertTrue(e.getClass().equals(IllegalArgumentException.class));
        }

    }

    @Test
    public void testComputeKmLeft(){
        initController();
        //controller.updateOdometer(10.0);
        controller.updateFuel(90.0, 10.0);
        assertEquals(90.0, controller.getEstimatedKmLeft(), 0);
    }

    @Test
    public void testComputeFuelUsage1(){
        initController();
        //controller.updateOdometer(10.0);
        for (int i = 0; i < 10; i++){
            controller.updateFuel(10, 10);
        }
        assertEquals(1.0, controller.getFuelUsageInterval(), 0);
    }

    @Test
    public void testComputeFuelUsage2(){
        initController();
        //controller.updateOdometer(5.0);
        for (int i = 0; i < 10; i++){
            controller.updateFuel(10, 10);
        }
        assertEquals(2.0, controller.getFuelUsageInterval(), 0);
    }*/
}
