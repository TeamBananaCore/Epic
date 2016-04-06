package bananacore.epic;

import bananacore.epic.interfaces.ViewController;
import javafx.scene.Node;

public class View {
    public Node node;

    public ViewController controller;

    public View(Node node, ViewController controller) {
        this.node = node;
        this.controller = controller;
    }

    public Node getNode() {
        return node;
    }

    public ViewController getController() {
        return controller;
    }
}
