package ecocampus.gui;

import ecocampus.projection.PointWebMercator;


/**
 * Represents the map view's background parameters
 * Constructor
 * @param zoomLevel zoom level of the map
 * @param x Web Mercator x coordinate of the top left corner
 * @param y Web Mercator y coordinate of the top left corner
 */
public record MapViewParameters(int zoomLevel, double x, double y) {
    /**
     * Return a MapViewParameters with the coordinates of the top left corner
     * @param x the x coordinate of the top left corner
     * @param y the y coordinate of the top left corner
     * @return a MapViewParameters with the coordinates of the top left corner
     */
    public MapViewParameters withMinXY(double x, double y){
        return new MapViewParameters(zoomLevel,x,y);
    }

    /**
     * Takes the coordinates of a point according to the top left corner of the displayed map
     * and returns the same point in the Web Mercator coordinate system
     * @param x the x coordinate of the point according to the top left corner of the displayed map
     * @param y the y coordinate of the point according to the top left corner of the displayed map
     * @return the corresponding point in Web Mercator
     */
    public PointWebMercator pointAt(double x, double y){
        return PointWebMercator.of(zoomLevel, x+this.x,y+this.y);
    }

    /**
     * Return the x coordinate of a point Web Mercator according to the top left corner of the displayed map
     * @param pointWebMercator a point in the Web Mercator coordinate system
     * @return Return the x coordinate of a point Web Mercator according to the top left corner of the displayed map
     */
    public double viewX(PointWebMercator pointWebMercator){
        return pointWebMercator.xAtZoomLevel(zoomLevel)-this.x;
    }

    /**
     * Return the y coordinate of a point Web Mercator according to the top left corner of the displayed map
     * @param pointWebMercator a point in the Web Mercator coordinate system
     * @return Return the y coordinate of a point Web Mercator according to the top left corner of the displayed map
     */
    public double viewY(PointWebMercator pointWebMercator){
        return pointWebMercator.yAtZoomLevel(zoomLevel)-this.y;
    }
}
