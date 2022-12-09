package ecocampus.gui;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.util.ArrayList;

/**
 * The annotated map
 */
public final class AnnotatedMapManager {
    private final StackPane pane;
    ObjectProperty<MapViewParameters> property;
    private final Bean bean;
    private ObjectProperty<Point2D> point;
    private final DoubleProperty mousePositionProperty;
    private final Button logout;
    /**
     * Constructor
     * @param tileManager a tile manager
     * @param bean the bean
     */
    public AnnotatedMapManager(TileManager tileManager, Bean bean){
        this.bean = bean;
        int zoomLevel = 16;
        double xParam = 8694212;
        double yParam = 5933305;
        property = new SimpleObjectProperty<>(new MapViewParameters(zoomLevel, xParam, yParam));
        WaypointsManager waypointsManager = new WaypointsManager(property, bean.getWaypoints(), bean);
        BaseMapManager baseMapManager = new BaseMapManager(tileManager, waypointsManager, property);
        pane = new StackPane(baseMapManager.pane(), waypointsManager.pane());
        pane.getStylesheets().add("map.css");

        point = new SimpleObjectProperty<>(new Point2D(0,0));
        mousePositionProperty = new SimpleDoubleProperty(Double.NaN);

        pane.setOnMouseMoved(e -> {
            point = new SimpleObjectProperty<>(new Point2D(e.getX(), e.getY()));
        });
        pane.setOnMouseExited(e -> mousePositionProperty.set(Double.NaN));
        ArrayList<String> al = new ArrayList<String>();
        al.add("Show All");
        al.add("Show Resolved");
        al.add("Show Unresolved");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(FXCollections.observableArrayList(al));
        comboBox.valueProperty().addListener((observable, oldValue, newValue) ->{ bean.setSelectedMode(comboBox.getItems().indexOf(comboBox.getValue()));
            bean.drawProperty().set(!bean.drawProperty().get());});
        comboBox.valueProperty().set(al.get(2));



        Pane p = new Pane(comboBox);
        p.setPickOnBounds(false);
        pane.getChildren().add(p);

        HBox buttonBox = new HBox();
        logout = new Button("Logout");

        //put logout in bottom left corner
        //set logout button in the top right corner
        buttonBox.getChildren().add(logout);
        buttonBox.setPadding(new Insets(10, 10, 10, 10));
        buttonBox.setAlignment(Pos.BOTTOM_LEFT);
        buttonBox.setPickOnBounds(false);
        pane.getChildren().add(buttonBox);


    }


    /**
     * Return the pane containing the annotated map
     *
     * @return the pane containing the annotated map
     */
    public Pane pane(){return pane;}

    public Button getLogout() {
        return logout;
    }
}
