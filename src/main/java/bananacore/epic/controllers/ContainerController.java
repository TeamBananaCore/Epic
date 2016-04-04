package bananacore.epic.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ContainerController implements Initializable {


    @FXML private BorderPane main;
    @FXML private ScrollPane settings;
    @FXML private BorderPane graph;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main.setVisible(true);
        settings.setVisible(false);
        graph.setVisible(false);
    }
}
