package ecocampus.projection;


/**
 * Contains static methods to perform conversions between swiss coordinates and WGS 84 coordinates
 */
public final class Ch1903 {

    /**
     * Private constructor, because the class is non-instantiable
     */
    private Ch1903() {
    }

    /**
     * Converts a position given in the WGS84 coordinate system to the east coordinate in the Ch1903 coordinate system
     *
     * @param lon the longitude in WGS84
     * @param lat the latitude in WGS84
     * @return e, the east coordinate in Ch1903
     */
    public static double e(double lon, double lat) {
        double lon1 = 1E-4 * Math.fma(3600, Math.toDegrees(lon), -26782.5);
        double lat1 = 1E-4 * Math.fma(3600, Math.toDegrees(lat), -169028.66);

        double e = 2600072.37 + 211455.93 * lon1 - 10938.51 * lat1 * lon1 - 0.36 * lat1 * lat1 * lon1
                - 44.54 * Math.pow(lon1, 3);
        return e;
    }

    /**
     * Converts a position given in the WGS84 coordinate system to the north coordinate in the Ch1903 coordinate system
     *
     * @param lon the longitude in WGS84
     * @param lat the latitude in WGS84
     * @return n, the north coordinate in Ch1903
     */
    public static double n(double lon, double lat) {
        double lon1 = 1E-4 * Math.fma(3600, Math.toDegrees(lon), -26782.5);
        double lat1 = 1E-4 * Math.fma(3600, Math.toDegrees(lat), -169028.66);

        double n = 1200147.07 + 308807.95 * lat1 + 3745.25 * lon1 * lon1 + 76.63 * lat1 * lat1
                - 194.56 * lon1 * lon1 * lat1 + 119.79 * Math.pow(lat1, 3);

        return n;
    }

    /**
     * Converts a position given in the Ch1903 coordinate system to the longitude in the WGS84 coordinate system
     *
     * @param e the east coordinate in Ch1903
     * @param n the north coordinate in Ch1903
     * @return lon, the longitude in WGS84
     */
    public static double lon(double e, double n) {
        float x = (float) (1E-6 * (e - 2600000));
        float y = (float) (1E-6 * (n - 1200000));

        double lon0 = 2.6779094 + 4.728982 * x + 0.791484 * x * y
                + 0.1306 * x * y * y - 0.0436 * Math.pow(x, 3);

        return Math.toRadians(lon0 * 100 / 36);
    }

    /**
     * Converts a position given in the Ch1903 coordinate system to the latitude in the WGS84 coordinate system
     *
     * @param e the east coordinate in Ch1903
     * @param n the north coordinate in Ch1903
     * @return lat, the longitude in WGS84
     */
    public static double lat(double e, double n) {
        float x = (float) (1E-6 * (e - 2600000));
        float y = (float) (1E-6 * (n - 1200000));

        double lat0 = 16.9023892 + 3.238272 * y - 0.270978 * x * x
                - 0.002528 * y * y - 0.0447 * x * x * y - 0.0140 * Math.pow(y, 3);

        return Math.toRadians(lat0 * 100 / 36);
    }
}
