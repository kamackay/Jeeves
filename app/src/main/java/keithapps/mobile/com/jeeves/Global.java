package keithapps.mobile.com.jeeves;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by Keith on 1/17/2016.
 * Contains Global static methods and variables
 */
public class Global {
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
    public static boolean canToggleGPS(Context context) {
        PackageManager pacman = context.getPackageManager();
        PackageInfo pacInfo = null;
        try {
            pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
        } catch (PackageManager.NameNotFoundException e) {
            return false; //package not found
        }
        if(pacInfo != null)
            for(ActivityInfo actInfo : pacInfo.receivers)
                //test if recevier is exported. if so, we can toggle GPS.
                if(actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported)
                    return true;
        return false; //default
    }
}
