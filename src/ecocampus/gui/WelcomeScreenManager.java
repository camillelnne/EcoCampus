package ecocampus.gui;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import java.io.FileInputStream;

import static javafx.scene.layout.StackPane.setAlignment;


public class WelcomeScreenManager {
    private final StackPane pane;


    public static final int FADE_OUT_TRANSITION_DURATION = 1000;


    private final SequentialTransition transition;

    private final static double MAX_OPACITY = 0.8;
    private final static double MIN_OPACITY = 0;
    private int currentUserId;
    /**
     * Constructor
     *
     */
    public WelcomeScreenManager(Bean bean) {
        pane = new StackPane();
        //padding
        Insets insets = new Insets(10,10,10,10);
        pane.setPadding(insets);
        pane.setStyle("-fx-background-color: #fdfcee;");

        VBox vBox = new VBox();
        //image:
        //Creating the image view
        ImageView imageView = new ImageView();
        try{
            FileInputStream stream = new FileInputStream("resources/EcoCampus.png");
            Image image = new Image(stream);
            //Setting image to the image view
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(500);
            //Setting the image view parameters
            imageView.setPreserveRatio(true);
            vBox.getChildren().add(imageView);
        }catch (Exception e){
            System.out.println(e);
        }
        //center vBox
        vBox.setAlignment(Pos.TOP_CENTER);

        ComboBox<String> comboBox = new ComboBox<>();

        ObservableList<String> userNames = bean.getUserNames();

        comboBox.setItems(userNames);
        comboBox.valueProperty().set(userNames.get(0));

        comboBox.setStyle("-fx-background-radius: 100; -fx-font: 20px \"Helvetica\"; -fx-background-color: #bfdcb2; -fx-text-fill: #000000;");
        vBox.getChildren().add(comboBox);
        vBox.setSpacing(20);


        comboBox.valueProperty().addListener((observable, oldValue, newValue) ->{
            bean.setCurrentUserId(comboBox.getItems().indexOf(comboBox.getValue()));
            System.out.println("User id: " + bean.getCurrentUserId());
        });


        transition = new SequentialTransition();

        //Fade out Transition
        FadeTransition fadeTransition2 = new FadeTransition();
        fadeTransition2.setDuration(Duration.millis(FADE_OUT_TRANSITION_DURATION));
        fadeTransition2.setFromValue(MAX_OPACITY);
        fadeTransition2.setToValue(MIN_OPACITY);

        transition.getChildren().addAll(fadeTransition2);

        transition.setOnFinished(event -> pane.setVisible(false));

        transition.setNode(pane);


        //button start
        Button start = new Button("Start");
        start.setStyle("-fx-background-radius: 100; -fx-font: 20px \"Helvetica\"; -fx-background-color: #bfdcb2; -fx-text-fill: #000000;");
        start.setTextAlignment(TextAlignment.CENTER);
        //lower the button
        start.setPrefWidth(200);
        start.setAlignment(Pos.TOP_CENTER);
        //put button in the middle of the screen
        vBox.getChildren().add(start);
        start.setOnAction(e -> {
            transition.play();

        });
        pane.getChildren().add(vBox);
    }

    public StackPane pane() {
        return pane;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }
}
