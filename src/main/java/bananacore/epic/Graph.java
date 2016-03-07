package bananacore.epic;

import bananacore.epic.interfaces.Graphable;
import javafx.beans.NamedArg;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class Graph extends Pane {

    private int secsOnScreen = 10000; //One week = 604800

    private double pixelsPerSec;

    HashMap<String, List<Graphable>> dataSources = new HashMap<>();
    public Graph(@NamedArg("prefWidth") double prefWidth, @NamedArg("prefHeight") double prefHeight){
        super();
        setPrefHeight(prefHeight);
        setPrefWidth(prefWidth);
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/graph.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        pixelsPerSec = this.getPrefWidth() / (double) secsOnScreen;
    }

    public void addDataSource(String name, List<Graphable> source){
        dataSources.put(name, source);
        updateGraph();
    }

    public void clearDataSources(){
        dataSources.clear();
        updateGraph();
    }

    public void deleteDataSource(String name){
        dataSources.remove(name);
        updateGraph();
    }

    public void updateGraph(){
        double hue = 0;
        double sat = 1;
        double bright = 1;
        int offset = 360/dataSources.size();
        for(String sourceName : dataSources.keySet()){
            List<Graphable> source = dataSources.get(sourceName);
            if(source.size() == 0){
                continue;
            }
            Color color = Color.hsb(hue,sat,bright);
            hue = (hue+offset) % 360;

            double position = 0;
            Timestamp lastDate = source.get(0).getDate();
            Polyline line = new Polyline();
            for(Graphable point : source){
                double y = point.getGraphValue()*getPrefHeight()/100.0;
                long dateDiff = (point.getDate().getTime()-lastDate.getTime());
                position += (dateDiff/1000.0)*pixelsPerSec;
                line.getPoints().addAll(new Double[]{position,y});
                lastDate = point.getDate();
            }
            line.setStroke(color);

            this.getChildren().addAll(line);
        }
    }
}
