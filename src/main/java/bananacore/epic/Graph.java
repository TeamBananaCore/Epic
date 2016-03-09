package bananacore.epic;

import bananacore.epic.interfaces.Graphable;
import javafx.beans.NamedArg;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class Graph extends Pane {

    private int secsOnScreen = 604800; //One week = 604800
    private double pixelsPerSec;
    private double graphHeight = 250;

    HashMap<String, GraphableList> dataSources = new HashMap<>();
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
        pixelsPerSec = 480 / (double) secsOnScreen;
    }

    public void addDataSource(String name, GraphableList source){
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
        this.getChildren().clear();
        double hue = 0;
        double sat = 1;
        double bright = 1;
        if (dataSources == null || dataSources.size() == 0) return;
        int offset = 360/dataSources.size();
        Timestamp firstDate = Timestamp.valueOf(LocalDateTime.now());
        HBox legends = new HBox(10);
        legends.setLayoutX(10);
        legends.setLayoutY(280);
        this.getChildren().add(legends);

        for(String sourceName : dataSources.keySet()){
            GraphableList source = dataSources.get(sourceName);
            if(source.size() == 0){
                continue;
            }
            Color color = Color.hsb(hue,sat,bright);
            hue = (hue+offset) % 360;

            double position = 0;
            Timestamp lastDate = source.get(0).getDate();
            Polyline line = new Polyline();
            for(Graphable point : source){
                if (point.getDate().before(firstDate)) firstDate = point.getDate();
                double y = graphHeight - (point.getGraphValue()-source.getMin())*graphHeight/(source.getMax()-source.getMin());
                long dateDiff = (point.getDate().getTime()-lastDate.getTime());
                position += (dateDiff/1000.0)*pixelsPerSec;
                line.getPoints().addAll(position, y);
                lastDate = point.getDate();
            }

            Rectangle labelRect = new Rectangle(10, 10, color);
            Label legend = new Label(sourceName, labelRect);
            legend.getStyleClass().add("graph_label");
            legends.getChildren().add(legend);

            line.setStroke(color);
            List<Double> points = line.getPoints();
//            if (points.get(points.size() - 2) > getPrefWidth()) setPrefWidth(Math.max(points.get(points.size() - 2), 480));
            this.getChildren().add(line);
        }

        // Axis and labels
        Line line = new Line(0, graphHeight, getPrefWidth() + 10, graphHeight);
        line.setFill(Color.BLACK);
        this.getChildren().add(line);
        double pixelsPerDay = pixelsPerSec*60*60*24;
        int daysPerLabel = 2;
        double position = 0;
        LocalDateTime date = firstDate.toLocalDateTime();
        while (position < getPrefWidth()){
            Label label = new Label(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            label.setLayoutX(position);
            label.setLayoutY(graphHeight);
            label.getStyleClass().add("graph_label");
            position += daysPerLabel * pixelsPerDay;
            date = date.plusDays(daysPerLabel);
            this.getChildren().add(label);
        }
    }
}
