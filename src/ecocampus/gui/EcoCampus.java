package ecocampus.gui;

import ecocampus.UserSerializer;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * The application's main class.
 */
public class EcoCampus extends Application {

    public static void main(String[] args) { launch(args); }
    @Override
    public void start(Stage primaryStage) {
        String tileServerHost = "tile.openstreetmap.org";
        Path cacheBasePath = Path.of("osm-cache");
        TileManager tileManager = new TileManager(cacheBasePath, tileServerHost);
        ObjectProperty<Issue> issueObjectProperty = new SimpleObjectProperty<>();
        Bean bean = new Bean(issueObjectProperty);

        AnnotatedMapManager mapManager =
                new AnnotatedMapManager(tileManager, bean);

        mapManager.getLogout().setOnAction(e -> {
            System.out.println("bloop");
        });
        SplitPane mapAndProfilePane = new SplitPane(mapManager.pane());
        mapAndProfilePane.setOrientation(Orientation.VERTICAL);


        BorderPane finalPane = new BorderPane();
        finalPane.setCenter(mapAndProfilePane);


        bean.getObservedissue().addListener(((observable, oldValue, newValue) -> {
            finalPane.setRight(null);

            if (newValue != null) {
                System.out.println(bean.getObservedissue().get().isEmpty());
                try {
                    finalPane.setRight(new IssueManager(bean,primaryStage).getVBox());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }));

        //insets for stackpane
        StackPane stackPane = new StackPane();


        stackPane.getChildren().add(finalPane);


        WelcomeScreenManager welcomeScreen = new WelcomeScreenManager(bean);
        stackPane.getChildren().add(welcomeScreen.pane());

        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(750);
        primaryStage.setScene(new Scene(stackPane));
        primaryStage.setTitle("EcoCampus");
        primaryStage.getIcons().add(new Image("/iconLeaf.png"));

        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
            try {
                WaypointsSerializer.saveWaypoints(bean.getWaypoints());
                UserSerializer.saveUsers(bean.getUsers());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        primaryStage.show();


    }
}
