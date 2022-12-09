package ecocampus.projection;

import ecocampus.Preconditions;

/**
 * Represents a point in swiss coordinates
 *
 */

public record PointCh(double e, double n) {
    /**
     * Compact constructor of the class that creates a point with the CH1903 coordinates
     *
     * @param e east coordinate
     * @param n north coordinate
     * @throws IllegalArgumentException if the given coordinates are not in Switzerland
     */
    public PointCh { Preconditions.checkArgument(SwissBounds.containsEN(e, n));}

    /**
     * Converts a position (e, n) given in the Ch1903 coordinate system to the longitude in the WGS84 coordinate system
     *
     * @return lon, the longitude in WGS84
     */
    public double lon(){
        return Ch1903.lon(e,n);
    }

    /**
     * Converts a position(e, n) given in the Ch1903 coordinate system to the latitude in the WGS84 coordinate system
     *
     * @return lat, the longitude in WGS84
     */
    public double lat(){
        return Ch1903.lat(e,n);
    }
}
