package bananacore.epic.controllers;

import bananacore.epic.Constants;
import bananacore.epic.DatabaseManager;
import bananacore.epic.customcontrols.Graph;
import bananacore.epic.GraphableList;
import bananacore.epic.interfaces.ViewController;
import bananacore.epic.interfaces.observers.SpeedInterface;
import bananacore.epic.models.BrakeSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;

import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class GraphController implements Initializable, SpeedInterface, ViewController{

    Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    private List<BrakeSession> brakeSessions;

    @FXML private Graph graph;
    @FXML private Button lastWeek;
    @FXML private Button nextWeek;
    @FXML private Button back;

    private Timestamp startDate, endDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        endDate = DatabaseManager.getLatestData();
        startDate = new Timestamp(endDate.getTime() - Constants.SECONDS_PER_WEEK*1000);
        DatabaseManager.update(startDate, endDate);
        brakeSessions = DatabaseManager.getBrakeSessions();
        setData();
        Image homeImage = new Image(String.valueOf(getClass().getClassLoader().getResource("image/homebutton.png")), 23, 25, true, false);
        back.setGraphic(new ImageView(homeImage));
    }

    @FXML
    public void lastWeek(){
        startDate = new Timestamp(startDate.getTime() - Constants.SECONDS_PER_WEEK*1000);
        endDate = new Timestamp(endDate.getTime() - Constants.SECONDS_PER_WEEK*1000);
        DatabaseManager.update(startDate, endDate);
        setData();
    }

    @FXML
    public void nextWeek(){
        startDate = new Timestamp(startDate.getTime() + Constants.SECONDS_PER_WEEK*1000);
        endDate = new Timestamp(endDate.getTime() + Constants.SECONDS_PER_WEEK*1000);
        DatabaseManager.update(startDate, endDate);
        setData();
    }

    @FXML
    public void back(){
        Constants.CONTAINER.setView(ContainerController.MAIN);
    }

    private void setData(){
        graph.clearDataSources();
        graph.addDataSource("Brakes", new GraphableList(DatabaseManager.getBrakeSessions(),"%"));
        graph.addDataSource("Fuel", new GraphableList(DatabaseManager.getFuelSessions(),"l/km"));
        graph.addDataSource("Speed", new GraphableList(DatabaseManager.getSpeedSessions(), "km/h"));
        if (!DatabaseManager.brakeDataExistsBefore(startDate)) lastWeek.setDisable(true);
        else lastWeek.setDisable(false);
        if (!DatabaseManager.brakeDataExistsAfter(endDate)) nextWeek.setDisable(true);
        else nextWeek.setDisable(false);
    }

    @Override
    public void updateVehicleSpeed(int value, Timestamp timestamp) {
        if(value != 0){
            back();
        }
    }

    @Override
    public void hidden() {
        Platform.runLater(()->Constants.PARSER.removeSpeedObserver(this));
    }

    @Override
    public void shown() {
        Platform.runLater(()->Constants.PARSER.addToSpeedObservers(this));
    }
}
