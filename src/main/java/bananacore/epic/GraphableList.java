package bananacore.epic;

import bananacore.epic.interfaces.observers.Graphable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class GraphableList extends ArrayList<Graphable> {
    private double min;
    private double max;

    private String unit;

    public GraphableList(int initialCapacity, String unit) {
        super(initialCapacity);
        this.unit = unit;
    }

    public GraphableList(String unit) {
        super();
        this.unit = unit;
    }

    public GraphableList(Collection<? extends Graphable> c, String unit) {
        super(c);
        generateMin();
        generateMax();
        this.unit = unit;
    }

    public void generateMin(){
        min = Double.MAX_VALUE;
        for (Graphable gr : this){
            if (gr.getGraphValue() < min) min = gr.getGraphValue();
        }
    }

    public void generateMax(){
        max = Double.MIN_VALUE;
        for (Graphable gr : this){
            if (gr.getGraphValue() > max) max = gr.getGraphValue();
        }
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    @Override
    public boolean add(Graphable gr){
        boolean ret = super.add(gr);
        generateMax();
        generateMin();
        return ret;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean ret = super.removeAll(c);
        generateMax();
        generateMin();
        return ret;
    }

    @Override
    public Graphable set(int index, Graphable element) {
        Graphable gr = super.set(index, element);
        generateMax();
        generateMin();
        return gr;
    }

    @Override
    public void add(int index, Graphable element) {
        super.add(index, element);
        generateMax();
        generateMin();
    }

    @Override
    public Graphable remove(int index) {
        Graphable gr = super.remove(index);
        generateMax();
        generateMin();
        return gr;
    }

    @Override
    public boolean remove(Object o) {
        boolean ret = super.remove(o);
        generateMax();
        generateMin();
        return ret;
    }

    @Override
    public void clear() {
        super.clear();
        generateMax();
        generateMin();
    }

    @Override
    public boolean addAll(Collection<? extends Graphable> c) {
        boolean ret = super.addAll(c);
        generateMax();
        generateMin();
        return ret;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Graphable> c) {
        boolean ret = super.addAll(index, c);
        generateMax();
        generateMin();
        return ret;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        generateMax();
        generateMin();
    }

    @Override
    public boolean removeIf(Predicate<? super Graphable> filter) {
        boolean ret = super.removeIf(filter);
        generateMax();
        generateMin();
        return ret;
    }

    @Override
    public void replaceAll(UnaryOperator<Graphable> operator) {
        super.replaceAll(operator);
        generateMax();
        generateMin();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
