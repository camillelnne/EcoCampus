package ecocampus;

/**
 * Class contains static methods for performing mathematical calculations
 *
 */
public final class Math2 
{
    /**
     * Private constructor that makes the class non instantiable
     */
    private Math2(){}

    /**
     * Function that keeps the v value in the interval given by min and max
     *
     * @param min minimum of the interval
     * @param v   value that has to be bounded
     * @param max maximum of the interval
     * @return min if v is inferior to min, max if v is superior to max and v otherwise
     * @throws IllegalArgumentException if min is strictly superior to max
     */
    public static int clamp(int min, int v, int max) {
        Preconditions.checkArgument(min <= max);
        if (v < min) return min;
        return Math.min(v, max);
    }


    /**
     * Calculates the hyperbolic sine of a given variable
     *
     * @param x the double variable
     * @return the hyperbolic sine
     */
    public static double asinh(double x) { return Math.log(x + Math.hypot(1,x));}


}
