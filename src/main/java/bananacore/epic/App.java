package bananacore.epic;

import bananacore.epic.controllers.ContainerController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends Application{

    private Logger logger = LoggerFactory.getLogger(getClass());
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.connectToDB();
        Platform.setImplicitExit(true);

        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/container.fxml"));
            root = loader.load();
            Constants.CONTAINER = loader.getController();
            Constants.STYLER = new StyleChooser(Constants.CONTAINER);
        } catch (Exception e) {
            e.printStackTrace();
            Platform.exit();
            System.exit(-1);
        }

        Scene scene = new Scene(root, 480, 320);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        Constants.SCENE = scene;
        logger.debug("App started");
        new Thread(Constants.PARSER, "parserThread").start();
    }
}