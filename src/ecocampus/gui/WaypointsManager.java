package ecocampus.gui;

import ecocampus.Status;
import ecocampus.projection.PointCh;
import ecocampus.projection.PointWebMercator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;


/**
 * Manages the waypoints display
 */
public class WaypointsManager {
    private final String LaLigne = "M 0 -23 L 0 0 Z";
    private final String LaBoule = "M 0 -13 A 1 1 0 0 0 0 -25 A 1 1 0 0 0 0 -13";

    private final ObjectProperty<MapViewParameters> property;
    private final ObservableList<Waypoint> waypointsList;
    private final Pane pane;
    private final Bean bean;

    /**
     * Constructor
     *
     * @param mapViewParametersObjectProperty  the map parameters
     * @param waypointsList the list of waypoints
     * @param bean the bean
     */
    public WaypointsManager(ObjectProperty<MapViewParameters> mapViewParametersObjectProperty,
                            ObservableList<Waypoint> waypointsList, Bean bean) {
        this.bean = bean;
        this.property = mapViewParametersObjectProperty;
        this.waypointsList = waypointsList;
        this.pane = new Pane();
        pane.setPickOnBounds(false);
        this.property.addListener(e -> movePins());
        this.waypointsList.addListener((ListChangeListener<? super Waypoint>) l -> drawAllPins());

        drawAllPins();
        bean.drawProperty().addListener((observable, oldValue, newValue) -> drawAllPins());
    }


    /**
     * Return the pane containing the waypoints
     *
     * @return the pane
     */
    public Pane pane() {
        return pane;
    }

    /**
     * Add a new waypoint of coordinates x, y to the graph
     *
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     */
    public void addWaypoint(double x, double y) {
        if (bean.getObservedissue().get() == null) {
            MapViewParameters mVP = property.get();
            PointCh point = mVP.pointAt(x, y).toPointCh();
            //create a new waypoint with issue
            Issue issue = new Issue("", "", bean.getCurrentUserId(), waypointsList.size(), "");
            waypointsList.add(new Waypoint(point, issue));
            bean.getObservedissue().set(issue);
        } else {
            if (bean.noObservedIssue()) {
                bean.removeLastWaypoint();
            }

            bean.getObservedissue().set(null);

        }
    }

    /**
     * Draw the pins
     */
    private void drawAllPins() {
        pane.getChildren().clear();
        int maxUpvotes = bean.getMaxUpvotes();
        for (int i = 0; i < waypointsList.size(); i++) {
            Waypoint point = waypointsList.get(i);
            Group pinGroup = new Group();

            SVGPath laLigne = new SVGPath();
            laLigne.setContent(LaLigne);
            laLigne.getStyleClass().add("ligne");

            SVGPath laBoule = new SVGPath();
            laBoule.setContent(LaBoule);
            if (point.issue().getStatus() == Status.RESOLVED){
                laBoule.getStyleClass().add("boule_Resolved");

            } else {
                laBoule.getStyleClass().add("boule_Unresolved");
                double angle = 65 - 65* point.issue().getUpVotes()/(double)maxUpvotes;
                laBoule.setFill(Color.hsb(angle,0.95,0.95));
            }


            pinGroup.getChildren().addAll(laLigne, laBoule);

            pinGroup.getStyleClass().add("pin");


            PointWebMercator pointWebMercator = PointWebMercator.ofPointCh(point.position());
            pinGroup.setLayoutX(property.get().viewX(pointWebMercator));
            pinGroup.setLayoutY(property.get().viewY(pointWebMercator));

            addEventListeners(pinGroup, i);
            if (point.issue().getStatus() == Status.RESOLVED && (bean.getSelectedMode()== 0 || bean.getSelectedMode() == 1)) {
                pane.getChildren().add(pinGroup);
            } else if (!(point.issue().getStatus() == Status.RESOLVED) && (bean.getSelectedMode()== 0 || bean.getSelectedMode() == 2)){
                pane.getChildren().add(pinGroup);
            }


            pinGroup.setOnMouseEntered(event -> {
                Label label = new Label(point.issue().getTitle() + "\n" + "Click to see more");
                label.setBackground(Background.fill(Color.color(0.9, 0.9, 0.9)));

                VBox vBox = new VBox();
                vBox.getChildren().addAll(label);

                pinGroup.getChildren().add(vBox);

            });

            pinGroup.setOnMouseExited(event -> {

                Node svgPath1 = pinGroup.getChildren().get(0);
                Node svgPath2 = pinGroup.getChildren().get(1);

                pinGroup.getChildren().clear();
                pinGroup.getChildren().add(svgPath1);
                pinGroup.getChildren().add(svgPath2);

            });
        }
    }

    /**
     * Move the pins
     */
    private void movePins() {
        for (int i = 0; i < waypointsList.size(); i++) {
            Waypoint point = waypointsList.get(i);
            Group pinGroup = (Group) pane.getChildren().get(i);
            PointWebMercator pointWebMercator = PointWebMercator.ofPointCh(point.position());
            pinGroup.setLayoutX(property.get().viewX(pointWebMercator));
            pinGroup.setLayoutY(property.get().viewY(pointWebMercator));
        }
    }

    /**
     * Add event listeners to the pins
     *
     * @param group the pin
     * @param waypointIndex the index of the pin
     */
    private void addEventListeners(Group group, int waypointIndex) {
        ObjectProperty<Point2D> initMousePos = new SimpleObjectProperty<>();
        ObjectProperty<Point2D> mousePath = new SimpleObjectProperty<>();

        group.setOnMousePressed(e -> initMousePos.set(new Point2D(e.getSceneX(), e.getSceneY())));

        group.setOnMouseReleased(e -> {
            PointWebMercator init = PointWebMercator.ofPointCh(waypointsList.get(waypointIndex).position());
            Point2D initGroupPos = new Point2D(property.get().viewX(init), property.get().viewY(init));

            if (e.isStillSincePress()) {
                if (e.getButton() == MouseButton.SECONDARY && bean.isUserAdmin()){
                    if (waypointsList.get(waypointIndex).issue().getStatus() == Status.RESOLVED)
                        waypointsList.get(waypointIndex).issue().setStatus(Status.UNRESOLVED);
                    else {
                        waypointsList.get(waypointIndex).issue().setStatus(Status.RESOLVED);
                    }
                    bean.drawProperty().set(!bean.drawProperty().get());
                } else
                    bean.getObservedissue().set(waypointsList.get(waypointIndex).issue());
            } else {
                double newX = initGroupPos.subtract(mousePath.getValue()).getX();
                double newY = initGroupPos.subtract(mousePath.getValue()).getY();
                MapViewParameters mVP = property.get();
                PointCh point = mVP.pointAt(newX, newY).toPointCh();

                //get wayPoint in list at waypointIndex
                Waypoint wayPoint = waypointsList.get(waypointIndex);
                //get issue of waypoint
                Issue issue = wayPoint.issue();
                waypointsList.remove(waypointIndex);
                waypointsList.add(waypointIndex, new Waypoint(point, issue));
                group.setLayoutX(newX);
                group.setLayoutY(newY);

            }
        });
    }

}
