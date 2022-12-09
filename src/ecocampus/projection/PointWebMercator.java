package ecocampus.projection;

import ecocampus.Preconditions;
/**
 * Represents a point in the Web Mercator system
 *
 */
public record PointWebMercator (double x, double y) {
    private static final int STRONG_BIT = 8;
    /**
     * Compact constructor, check if the coordinates are valid (ie between 0 and 1)
     *
     * @param x a coordinate
     * @param y a coordinate
     * @throws IllegalArgumentException if x or y is not between 0 and 1
     */
    public PointWebMercator{
        Preconditions.checkArgument(0 <= x && x <= 1 && 0 <= y && y <= 1);
    }
    /**
     * Convert the coordinates x and y given at a specific zoom level to their value at zoom level 0
     *
     * @param zoomLevel the zoom level in which x, y are given
     * @param x         coordinate x of a point in the WebMercator coordinates system
     * @param y         coordinate y of a point in the WebMercator coordinates system
     * @return the same point at zoom level 0
     */
    public static PointWebMercator of(int zoomLevel, double x, double y){
        double newX = Math.scalb(x, -(STRONG_BIT + zoomLevel));
        double newY = Math.scalb(y, -(STRONG_BIT + zoomLevel));
        return new PointWebMercator(newX, newY);
    }

    /**
     * Converts the coordinates of a point given in Ch1903 to the WebMercator coordinates system
     *
     * @param pointCh, a point which coordinates are given in the Ch1903 coordinates system
     * @return a new Point which coordinates are the one of the given point, converted to WebMercator
     */
    public static PointWebMercator ofPointCh(PointCh pointCh){
        return new PointWebMercator(WebMercator.x(pointCh.lon()),
                WebMercator.y(pointCh.lat()));
    }

    /**
     * Adapt an x coordinates to a given zoom level
     *
     * @param zoomLevel the zoom level
     * @return the coordinate converted to suit the zoom level
     */
    public double xAtZoomLevel(int zoomLevel){ return Math.scalb(x, zoomLevel + 8);}

    /**
     * Adapt an y coordinates to a given zoom level
     *
     * @param zoomLevel the zoom level
     * @return the coordinate converted to suit the zoom level
     */
    public double yAtZoomLevel(int zoomLevel){ return Math.scalb(y, zoomLevel + 8);}

    /**
     * Converts a coordinate x in WebMercator to the longitude in WGS84
     *
     * @return the longitude in radians
     */
    public double lon(){ return WebMercator.lon(x);}

    /**
     * Converts a coordinate y in WebMercator to the latitude in WGS84
     *
     * @return the latitude in radians
     */
    public double lat(){ return WebMercator.lat(y);}

    /**
     * Converts a point given in WebMercator coordinates to a point in the Ch1903 coordinates system
     *
     * @return the converted point in Ch1903 if its coordinates are within the bounds of Switzerland, else return null
     */
    public PointCh toPointCh(){
        double e = Ch1903.e(lon(), lat());
        double n = Ch1903.n(lon(), lat());
        if(SwissBounds.containsEN(e, n)){
            return new PointCh(e,n);
        }
        return null;
    }
}
