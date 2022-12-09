package ecocampus;

/**
 * Class that contains a method that throws an exception.
 */
public final class Preconditions {

    private Preconditions() {
    }

    /**
     * Checks if a condition is met, if not, throws an IllegalArgumentException>
     *
     * @param shouldBeTrue the condition
     * @throws IllegalArgumentException if the given boolean is not true
     */
    public static void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue) {
            throw new IllegalArgumentException("invalid argument");
        }
    }
}

