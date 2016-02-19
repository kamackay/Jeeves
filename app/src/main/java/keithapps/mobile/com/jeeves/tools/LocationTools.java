package keithapps.mobile.com.jeeves.tools;

/**
 * Created by Keith on 2/18/2016.
 * Location Tools
 */
public class LocationTools {
    /**
     * Get a coordinate's (latitude or longitude) minute value
     *
     * @param coordinate the coordinate to convert
     * @return the minutes of the given coordinate
     */
    public static int getMinutes(double coordinate) {
        return (int) ((coordinate % 1) * 60);
    }

    /**
     * Get a coordinate's (latitude or longitude) second value
     *
     * @param coordinate the coordinate to convert
     * @return the seconds of the given coordinate
     */
    public static int getSeconds(double coordinate) {
        return (int) ((((coordinate % 1) * 60) % 1) * 60);
    }
}
