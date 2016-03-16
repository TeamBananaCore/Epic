package bananacore.epic;

import bananacore.epic.interfaces.NumpadInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;

import java.io.IOException;

/**
 * Created by fonnavn on 09.03.2016.
 */
public class SetupController implements NumpadInterface {

    private Boolean gear;
    private int numberOfGears;
    private int weight;
    private Boolean gasoline;
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
        //if ( actionEvent.getSource().toString()){

        //}

    }


    public void setGear(Boolean auto) {Constants.auto = auto; }

    public void setWeight(int weight) { Constants.weight = weight;    }

    public void setNumberOfGears(int numberOfGears) {Constants.numberOfGears = numberOfGears;    }

    public void setGasoline(Boolean gasoline) { Constants.gasoline = gasoline;    }

    public void setFuelsize(int fuelsize) { Constants.fuelsize = fuelsize;    }


    @Override
    public String getNumber() {
        return null;
    }
}
