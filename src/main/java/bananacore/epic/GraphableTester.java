package bananacore.epic;

import bananacore.epic.interfaces.Graphable;

import java.sql.Timestamp;

public class GraphableTester implements Graphable{

    private Timestamp date;
    private double value;

    public GraphableTester(Timestamp date, double value) {
        this.date = date;
        this.value = value;
    }

    @Override
    public Timestamp getDate() {
        return date;
    }

    @Override
    public double getGraphValue() {
        return value;
    }
}
