package bananacore.epic;

import bananacore.epic.interfaces.NumpadInterface;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by marton on 16/03/16.
 */

public class Numpad extends Application {

    @FXML private Text numberview;
    @FXML private Text title;
    @FXML private Text seven;
    @FXML private Text eight;
    @FXML private Text nine;
    @FXML private Text four;
    @FXML private Text five;
    @FXML private Text six;
    @FXML private Text one;
    @FXML private Text two;
    @FXML private Text three;
    @FXML private Text backspace;
    @FXML private Text zero;
    @FXML private Text ok;
    private Stage prevstage;
    NumpadInterface user;

    /*public Numpad(String title, NumpadInterface user) {
        this.title.setText(title);
        this.user = user;
    }*/

    public void initialize() {

    }

    @FXML
    public void onePressed() {
        if (numberview.getText().equals("0")) {
            numberview.setText("1");
        } else {
            numberview.setText(numberview.getText() + 1);
        }
    }
    @FXML
    public void twoPressed() {
        if (numberview.getText().equals("0")) {
            numberview.setText("2");
        } else {
            numberview.setText(numberview.getText() + 2);
        }
    }
    @FXML
    public void threePressed() {
        if (numberview.getText().equals("0")) {
            numberview.setText("3");
        } else {
            numberview.setText(numberview.getText() + 3);
        }
    }
    @FXML
    public void fourPressed() {
        if (numberview.getText().equals("0")) {
            numberview.setText("4");
        } else {
            numberview.setText(numberview.getText() + 4);
        }
    }
    @FXML
    public void fivePressed() {
        if (numberview.getText().equals("0")) {
            numberview.setText("5");
        } else {
            numberview.setText(numberview.getText() + 5);
        }
    }
    @FXML
    public void sixPressed() {
        if (numberview.getText().equals("0")) {
            numberview.setText("6");
        } else {
            numberview.setText(numberview.getText() + 6);
        }
    }
    @FXML
    public void sevenPressed() {
        if (numberview.getText().equals("0")) {
            numberview.setText("7");
        } else {
            numberview.setText(numberview.getText() + 7);
        }
    }
    @FXML
    public void eightPressed() {
        if (numberview.getText().equals("0")) {
            numberview.setText("8");
        } else {
            numberview.setText(numberview.getText() + 8);
        }
    }
    @FXML
    public void ninePressed() {
        if (numberview.getText().equals("0")) {
            numberview.setText("9");
        } else {
            numberview.setText(numberview.getText() + 9);
        }
    }
    @FXML
    public void zeroPressed() {
        if (numberview.getText().equals("0")) {
            numberview.setText("0");
        } else {
            numberview.setText(numberview.getText() + 0);
        }
    }
    @FXML
    public void backspacePressed() {
        String text = numberview.getText();
        if (text.length() > 1) {
            numberview.setText(text.substring(0, text.length() - 1));
        } else if (text.length() == 1) {
            numberview.setText("0");
        }
    }

    @FXML
    public void okPressed() {
        user.getNumber();
    }

    public String getNumber() {
        return numberview.getText();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = (Parent) new FXMLLoader().load(this.getClass().getClassLoader().getResourceAsStream("fxml/numpad.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        launch(Numpad.class, args);
    }
}