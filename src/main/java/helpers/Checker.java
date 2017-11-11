package helpers;

public class Checker {
    public static boolean inRangeInclusive(int lower, int upper, int testValue)
    {
        return ((testValue >= lower) && (testValue <= upper));
    }

    public static boolean inRangeExclusive(int lower, int upper, int testValue)
    {
        return ((testValue > lower) && (testValue < upper));
    }
}
