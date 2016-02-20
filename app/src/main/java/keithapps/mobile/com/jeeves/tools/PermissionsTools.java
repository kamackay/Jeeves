package keithapps.mobile.com.jeeves.tools;

import android.content.Context;

import keithapps.mobile.com.jeeves.R;

/**
 * Created by Keith on 2/19/2016.
 * Check to see if you have permissions
 */
public class PermissionsTools {
    public static boolean haveInternet(Context c) {
        return c.getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE)
                .getBoolean(c.getString(R.string.permissions_internet), true);
    }
}
