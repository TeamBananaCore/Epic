
import bananacore.epic.App;
import bananacore.epic.FuelController;
import org.junit.Test;
import static org.junit.Assert.*;

public class FuelControllerTest {

    FuelController controller;

    public void initController(){
        controller = new FuelController();
    }

    @Test
    public void testUpdateOnStart(){
        initController();
        controller.updateFuel(100.0, 0.0);
        assertEquals(100.0, controller.getFuelLevel(), 0);
        assertEquals(0.0, controller.getFuelUsage(), 0);
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
    }



}
