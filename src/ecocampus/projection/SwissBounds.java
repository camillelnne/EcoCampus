package ecocampus.projection;

/**
 * Class containing constants and methods linked to the bounds of Switzerland
 *
 */

public final class SwissBounds {
    public final static double MIN_E = 2485000;
    public final static double MAX_E = 2834000;
    public final static double MIN_N = 1075000;
    public final static double MAX_N = 1296000;

    private SwissBounds() {}

    /**
     * Check if the coordinates are within the bounds of Switzerland
     *
     * @param e the east coordinate in the Ch1903 coordinate system
     * @param n the north coordinate in the Ch1903 coordinate system
     * @return true if the coordinates are within the bounds of Switzerland, else return false
     */
    public static boolean containsEN(double e, double n) {
        return e >= MIN_E && e <= MAX_E && n >= MIN_N && n <= MAX_N;
    }
}
