package ecocampus.projection;

import ecocampus.Math2;

/**
 *  Class containing static methods to convert coordinates between the WGS 84 and the Web Mercator systems
 *
 */

public final class WebMercator {
    /**
     * Default constructor
     * Private because the class is non-instantiable
     */
    private WebMercator(){}

    /**
     * Converts the longitude in the WGS84 coordinate system
     * to the x coordinates given in the WebMercator coordinate system
     *
     * @param lon the longitude in WGS84
     * @return x in WebMercator
     */
    public static double x(double lon){
        return 1 / (2 * Math.PI) * (lon + Math.PI);
    }

    /**
     * Converts the latitude in the WGS84 coordinate system
     * to the y coordinates given in the WebMercator coordinate system
     *
     * @param lat the latitude in WGS84
     * @return y in WebMercator
     */
    public static double y(double lat){
        return 1 / (2 * Math.PI) * (Math.PI - Math2.asinh(Math.tan(lat)));
    }

    /**
     * Converts the x coordinates given in the WebMercator coordinate system to the longitude in the WGS84 coordinate system
     *
     * @param x in WebMercator
     * @return lon, the longitude in WGS84
     */
    public static double lon(double x){
        return 2 * Math.PI * x - Math.PI;
    }

    /**
     * Converts the y coordinates given in the WebMercator coordinate system to the longitude in the WGS84 coordinate system
     *
     * @param y in WebMercator
     * @return lat, the latitude in WGS84
     */
    public static double lat(double y){
        return Math.atan(Math.sinh(Math.PI - 2 * Math.PI * y));
    }
}
