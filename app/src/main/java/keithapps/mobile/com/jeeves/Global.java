package keithapps.mobile.com.jeeves;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Keith on 1/17/2016.
 * Contains Global static methods and variables
 */
public class Global {
    /**
     * The String "\n"
     */
    public static final String TEXT_NEWLINE = "\n";

    /**
     * Is the given service running.
     * <p/>
     * NOTE: The service must be in this APK
     *
     * @param serviceClass the class of the service that is being checked
     * @param context      the context of the calling class
     * @return true if the given service is running
     */
    public static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if (serviceClass.getName().equals(service.service.getClassName()))
                return true;
        return false;
    }

    /**
     * Turn on the GPS
     * <p/>
     * NOTE: May not work
     *
     * @param context The calling context
     */
    public static void turnGPSOn(Context context) {
        try {
            String provider = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings",
                        "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                context.sendBroadcast(poke);
            }
        } catch (Exception e) {
            //It's all good
        }
    }

    /**
     * Turn off the GPS
     * <p/>
     * NOTE: May not work
     *
     * @param context The calling context
     */
    public static void turnGPSOff(Context context) {
        try {
            String provider = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider.contains("gps")) { //if gps is enabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                context.sendBroadcast(poke);
            }
        } catch (Exception e) {
            KeithToast.show("didn't work", context);
        }
    }

    /**
     * Close the notification tray
     *
     * @param context the calling context
     */
    public static void closeNotificationTray(Context context) {
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    /**
     * Can the GPS be toggled off?
     *
     * @param context the calling context
     * @return false if the system is unable to turn off the GPS
     */
    public static boolean canToggleGPS(Context context) {
        PackageManager pacman = context.getPackageManager();
        PackageInfo pacInfo = null;
        try {
            pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        if (pacInfo != null)
            for (ActivityInfo actInfo : pacInfo.receivers)
                if (actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported)
                    return true;
        return false; //default
    }

    /**
     * Get All Children of the given view
     *
     * @param v the view to search for children of
     * @return All children of the given view
     */
    public static ArrayList<View> getAllChildren(View v) {
        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            return viewArrayList;
        } else {
            ArrayList<View> result = new ArrayList<>();
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                ArrayList<View> viewArrayList = new ArrayList<>();
                viewArrayList.add(v);
                viewArrayList.addAll(getAllChildren(child));
                result.addAll(viewArrayList);
            }
            return result;
        }
    }

    /**
     * Get the shared preferences
     *
     * @param context The calling Context
     * @return Shared Preferences
     */
    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(context.getString(R.string.sharedPrefrences_code),
                Context.MODE_PRIVATE);
    }

    /**
     * Get the version name of this app
     * @param context The calling context
     * @return the Name of this app, Unknown if it could not be found
     */
    public static String getVersionName(Context context){
        try{
            String s = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
            if (s.isEmpty()) return "Unknown";
            else return s;
        } catch (Exception e){
            return "Unknown";
        }
    }
}
