package bananacore.epic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;


import javax.swing.*;
import java.io.IOException;

/**
 * Created by fonnavn on 09.03.2016.
 */
public class SetupController {

    private Boolean auto;
    private int numberOfGears;
    private int weight;
    private Boolean gassoline;
    private int fuelsize;

    @FXML
    RadioButton auto;
    @FXML
    RadioButton maual;
    @FXML
    RadioButton disel;
    @FXML
    RadioButton gasoline;
    @FXML
    Spinner tankSize;
    @FXML
    Spinner gearSize;

    private void handleGearTypeAction(ActionEvent actionEvent)throws IOException {
        if ( actionEvent.getSource().toString()){

        }

    }


    public void setAuto(Boolean auto) {Constants.auto = auto; }

    public void setWeight(int weight) { Constants.weight = weight;    }

    public void setNumberOfGears(int numberOfGears) {Constants.numberOfGears = numberOfGears;    }

    public void setGassoline(Boolean gassoline) { Constants.gassoline = gassoline;    }

    public void setFuelsize(int fuelsize) { Constants.fuelsize = fuelsize;    }





}
