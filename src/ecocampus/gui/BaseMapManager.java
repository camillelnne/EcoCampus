package ecocampus.gui;

import ecocampus.Math2;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import java.io.IOException;

/**
 * Handle the display of the base map.
 */
public final class BaseMapManager {
    private final TileManager tileManager;
    private final ObjectProperty<MapViewParameters> property;
    private boolean redrawNeeded;
    private final Pane pane;
    private final Canvas canvas;
    private final static int TILE_LENGTH = 256;
    private final ObjectProperty<Point2D> point = new SimpleObjectProperty<>();
    /**
     * @param tileManager      a given tile manager
     * @param waypointsManager
     * @param mapViewParametersObjectProperty
     */
    public BaseMapManager(TileManager tileManager, WaypointsManager waypointsManager,
                          ObjectProperty<MapViewParameters> mapViewParametersObjectProperty){
        redrawNeeded = true;
        this.tileManager = tileManager;
        this.property = mapViewParametersObjectProperty;

        pane = new Pane();
        canvas = new Canvas();
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());
        pane.getChildren().add(canvas);
        point.set(new Point2D(0,0));

        canvas.heightProperty().addListener((l -> redrawOnNextPulse()));
        canvas.widthProperty().addListener((l -> redrawOnNextPulse()));
        canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;
            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
        });
        pane.setOnMouseClicked(e -> {
            if(e.isStillSincePress()) {
                waypointsManager.addWaypoint(e.getX(),e.getY());
                redrawOnNextPulse();
            }
        });
        SimpleLongProperty minScrollTime = new SimpleLongProperty();
        pane.setOnScroll(e -> {
            if (e.getDeltaY() == 0d) return;
            long currentTime = System.currentTimeMillis();
            if (currentTime < minScrollTime.get()) return;
            minScrollTime.set(currentTime + 200);
            int zoomDelta = (int) Math.signum(e.getDeltaY());
            int newZoom = Math2.clamp(16, mapViewParametersObjectProperty.get().zoomLevel() +
                    zoomDelta, 19);
            double scale = Math.scalb(1, newZoom - mapViewParametersObjectProperty.get().zoomLevel());
            Point2D newPoint = new Point2D(mapViewParametersObjectProperty.get().x() + e.getX(),
                    mapViewParametersObjectProperty.get().y() + e.getY());
            newPoint = newPoint.multiply(scale);
            mapViewParametersObjectProperty.set(new MapViewParameters(newZoom, newPoint.getX() - e.getX(),
                    newPoint.getY() - e.getY()));
            redrawOnNextPulse();
        });
        pane.setOnMousePressed(e -> point.set(new Point2D(e.getX(), e.getY())));
        pane.setOnMouseDragged(e -> {
            double diffX = point.get().getX() - e.getX();
            double diffY = point.get().getY() - e.getY();
            mapViewParametersObjectProperty.set(mapViewParametersObjectProperty.get().withMinXY(mapViewParametersObjectProperty.get().x() + diffX, mapViewParametersObjectProperty.get().y() + diffY));
            point.set(new Point2D(e.getX(), e.getY()));
            redrawOnNextPulse();
        });
    }

    /**
     * Return the pane containing the base map.
     * @return the pane containing the base map
     */
    public Pane pane(){return pane;}

    /**
     * If a redraw is needed, calls draw
     */
    private void redrawIfNeeded() {
        if (!redrawNeeded) return;
        redrawNeeded = false;
        draw();
    }

    /**
     * Draw every tile on the canvas
     */
    private void draw(){
        GraphicsContext context = canvas.getGraphicsContext2D();
        int tileX = (int) Math.floor(property.get().x()/TILE_LENGTH);
        int tileY = (int) Math.floor(property.get().y()/TILE_LENGTH);

        int x0 = TILE_LENGTH + (int) (tileX * TILE_LENGTH - property.get().x());
        int y0 = TILE_LENGTH + (int) (tileY * TILE_LENGTH - property.get().y());

        int tileXMax = tileX + (int) Math.ceil((x0 + pane.getWidth())/TILE_LENGTH);
        int tileYMax = tileY + (int) Math.ceil((y0 + pane.getHeight())/TILE_LENGTH);

        for(int y = tileY; y <= tileYMax; y++){
            for(int x = tileX; x <= tileXMax; x++){
                try{
                    TileManager.TileId tileId = new TileManager.TileId(property.get().zoomLevel(), x, y);
                    int newX = (int) (tileId.X() * TILE_LENGTH - property.get().x());
                    int newY = (int) (tileId.Y() * TILE_LENGTH - property.get().y());
                    context.drawImage(tileManager.imageForTileAt(tileId), newX, newY);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Request a redrawing on the next pulse
     */
    private void redrawOnNextPulse() {
        redrawNeeded = true;
        Platform.requestNextPulse();
    }
}
