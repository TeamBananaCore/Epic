package bananacore.epic;

import bananacore.epic.interfaces.Graphable;
import bananacore.epic.models.BrakeSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GraphController implements Initializable{

    private List<BrakeSession> brakeSessions;

    @FXML private Graph graph;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DatabaseManager.update();
        brakeSessions = DatabaseManager.getBrakeSessions();

        List<Graphable> graphables = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now();
        for(int i = 0; i < 100;i++){
            time = time.plusSeconds((long) (Math.random()*1000));
            Graphable thing = new GraphableTester(Timestamp.valueOf(time), Math.random()*100);
            graphables.add(thing);
        }

        graph.addDataSource("random", graphables);

        List<Graphable> graphables2 = new ArrayList<>();
        time = LocalDateTime.now();
        for(int i = 0; i < 100;i++){
            time = time.plusSeconds((long) (Math.random()*1000));
            Graphable thing = new GraphableTester(Timestamp.valueOf(time), Math.random()*50);
            graphables2.add(thing);
        }

        graph.addDataSource("random2", graphables2);
    }
}
