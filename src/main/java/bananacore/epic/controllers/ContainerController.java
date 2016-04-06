package bananacore.epic.controllers;

import bananacore.epic.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ContainerController implements Initializable {

    public static final String MAIN = "main";
    public static final String MAIN_FXML = "fxml/main.fxml";

    public static final String GRAPH = "graph";
    public static final String GRAPH_FXML = "fxml/graphView.fxml";

    // TODO Change to settings when ready
    public static final String SETTINGS = "setup";
    public static final String SETTINGS_FXML = "fxml/setup.fxml";

    @FXML private StackPane pane;

    Map<String, View> views = new HashMap<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadView(MAIN, MAIN_FXML);
        loadView(GRAPH, GRAPH_FXML);
        loadView(SETTINGS, SETTINGS_FXML);

        setView(MAIN);
    }

    public void loadView(String name, String fxml){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxml));
            Parent node = loader.load();
            node.setUserData(name);
            views.put(name, new View(node, loader.getController()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setView(String name){
        for(Node node : pane.getChildren()){
            views.get(node.getUserData()).getController().hidden();
        }
        pane.getChildren().clear();
        View view = views.get(name);
        pane.getChildren().add(view.getNode());
        view.getController().shown();
    }
}
