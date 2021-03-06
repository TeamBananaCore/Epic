package bananacore.epic.customcontrols;

import bananacore.epic.GraphableList;
import bananacore.epic.interfaces.Graphable;
import javafx.beans.NamedArg;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class Graph extends Pane {

    private int secsOnScreen = 604800; //One week = 604800
    private double pixelsPerSec;
    private double xOffset = 50;
    private double yOffset = 10;
    private double graphHeight = 200-yOffset;
    private double graphOffset = 5;

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
        pixelsPerSec = (480-xOffset-graphOffset) / (double) secsOnScreen;
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
        DecimalFormat df = new DecimalFormat("#.##");

        if (dataSources == null || dataSources.size() == 0) return;
        int offset = 360/dataSources.size();
        Timestamp firstDate = Timestamp.valueOf(LocalDateTime.now());
        HBox legends = new HBox(10);
        legends.setLayoutX(xOffset);
        legends.setLayoutY(graphHeight+yOffset+20);
        this.getChildren().add(legends);

        VBox valueLabels = new VBox();
        valueLabels.setAlignment(Pos.CENTER_RIGHT);
        valueLabels.getStyleClass().add("graph_value_labels");

        for(String sourceName : dataSources.keySet()){
            GraphableList source = dataSources.get(sourceName);
            if(source.size() == 0){
                continue;
            }
            Color color = Color.hsb(hue,sat,bright);
            hue = (hue+offset) % 360;

            double position = xOffset + graphOffset;
            Timestamp lastDate = source.get(0).getDate();
            Polyline line = new Polyline();
            for(Graphable point : source){
                if (point.getDate().before(firstDate)) firstDate = point.getDate();
                double y = yOffset + (graphHeight - (point.getGraphValue()-source.getMin())*graphHeight/(source.getMax()-source.getMin()));
                long dateDiff = (point.getDate().getTime()-lastDate.getTime());
                position += (dateDiff/1000.0)*pixelsPerSec;
                line.getPoints().addAll(position, y);
                lastDate = point.getDate();
            }

            Rectangle labelRect = new Rectangle(10, 10, color);
            Label legend = new Label(sourceName, labelRect);
            legend.getStyleClass().add("graph_label");
            legends.getChildren().add(legend);

            List<Double> points = line.getPoints();
            this.getChildren().add(line);
            line.setStyle("-fx-stroke: " + getColorRGBString(color));

            Label valueLabel = new Label(df.format(source.getMax()) + " " + source.getUnit());
            valueLabel.setStyle("-fx-text-fill: " + getColorRGBString(color));
            valueLabels.getChildren().add(valueLabel);
        }
        valueLabels.layoutXProperty().bind(Bindings.subtract(xOffset-5, valueLabels.widthProperty()));
        valueLabels.setLayoutY(yOffset-8);

        // Axis and labels
        Line xAxis = new Line(xOffset, graphHeight+yOffset, getPrefWidth() + 10, graphHeight+yOffset);
        xAxis.getStyleClass().add("axis");

        Line yAxis = new Line(xOffset, graphHeight+yOffset, xOffset, yOffset);
        yAxis.getStyleClass().add("axis");

        Line topDash = new Line(xOffset-2,yOffset,xOffset+2,yOffset);
        topDash.getStyleClass().add("axis");

        this.getChildren().addAll(valueLabels, xAxis, yAxis, topDash);

        double pixelsPerDay = pixelsPerSec*60*60*24;
        int daysPerLabel = 2;
        double position = xOffset;
        LocalDateTime date = firstDate.toLocalDateTime();
        while (position < getPrefWidth()){
            Label label = new Label(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            label.setLayoutX(position);
            label.setLayoutY(graphHeight+yOffset);
            label.getStyleClass().add("graph_label");
            position += daysPerLabel * pixelsPerDay;
            date = date.plusDays(daysPerLabel);
            this.getChildren().add(label);
        }
    }

    private String getColorRGBString(Color color){
        return "rgb(" + color.getRed()*255 + ", " + color.getGreen()*255 + ", " + color.getBlue()*255 + ")";
    }
}
