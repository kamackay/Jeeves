package keithapps.mobile.com.jeeves.tools;

import android.content.Context;

import java.util.Locale;

import static keithapps.mobile.com.jeeves.tools.Log.logLocation;
import static keithapps.mobile.com.jeeves.tools.Log.writeToLog;

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

    public static void onLocationChange(double latitude, double longitude, Context c) {
        logLocation(latitude, longitude);
        writeToLog(String.format(Locale.getDefault(),
                "Location: %s %c, %s %c",
                String.valueOf(Math.abs(latitude)),
                (latitude > 0) ? 'N' : 'S',
                String.valueOf(Math.abs(longitude)),
                (longitude > 0) ? 'E' : 'W'), c);
    }
}
