package bananacore.epic;

import bananacore.epic.interfaces.Graphable;
import bananacore.epic.models.BrakeSession;
import bananacore.epic.models.WrongGearSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GraphController implements Initializable{

    private List<BrakeSession> brakeSessions;

    @FXML private Graph graph;
    @FXML private Button lastWeek;
    @FXML private Button nextWeek;

    private Timestamp startDate, endDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        endDate = Timestamp.valueOf(LocalDateTime.now());
        startDate = new Timestamp(endDate.getTime() - Constants.SECONDS_PER_WEEK*1000);
        DatabaseManager.update(startDate, endDate);
        brakeSessions = DatabaseManager.getBrakeSessions();
        setData();
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

    private void setData(){
        graph.clearDataSources();
        graph.addDataSource("Brakes", new GraphableList(DatabaseManager.getBrakeSessions(),"%"));
        graph.addDataSource("Fuel", new GraphableList(DatabaseManager.getFuelSessions(),"l/km"));
        if (!DatabaseManager.brakeDataExistsBefore(startDate)) lastWeek.setDisable(true);
        else lastWeek.setDisable(false);
        if (!DatabaseManager.brakeDataExistsAfter(endDate)) nextWeek.setDisable(true);
        else nextWeek.setDisable(false);
    }
}
