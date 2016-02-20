package keithapps.mobile.com.jeeves.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Locale;

import keithapps.mobile.com.jeeves.R;

import static java.lang.String.format;
import static keithapps.mobile.com.jeeves.tools.Log.logLocation;
import static keithapps.mobile.com.jeeves.tools.Log.writeToLog;
import static keithapps.mobile.com.jeeves.tools.SystemTools.getDouble;
import static keithapps.mobile.com.jeeves.tools.SystemTools.getPrefs;

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
        writeToLog(format(Locale.getDefault(),
                "Location: %s %c, %s %c",
                String.valueOf(Math.abs(latitude)),
                (latitude > 0) ? 'N' : 'S',
                String.valueOf(Math.abs(longitude)),
                (longitude > 0) ? 'E' : 'W'), c);
    }

    public static String getLastKnownLocation(Context c) {
        SharedPreferences prefs = getPrefs(c);
        if (!prefs.getBoolean(c.getString(R.string.permissions_location), true))
            return "No Location Permissions";
        double lat = getDouble(prefs, Settings.Location.lastLat, 0),
                lon = getDouble(prefs, Settings.Location.lastLong, 0);
        return format(Locale.getDefault(), "%s %c, %s %c",
                (lat != 0) ? String.valueOf(Math.abs(lat)) : "Unknown", (lat >= 0) ? 'N' : 'S',
                (lon != 0) ? String.valueOf(Math.abs(lon)) : "Unknown", (lon >= 0) ? 'E' : 'W');
    }

    public static String getLocationLog() {
        try {
            StringBuilder sb = new StringBuilder();
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), "bs.txt");
            file = new File(file.getParentFile().getParentFile(), "/Jeeves/" + "location.txt");
            file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) sb.append(line + "\n\n");
            return sb.toString();
        } catch (Exception e) {
            return "Error Getting Location Log";
        }
    }
}
