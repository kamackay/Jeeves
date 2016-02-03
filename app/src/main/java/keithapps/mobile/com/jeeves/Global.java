package keithapps.mobile.com.jeeves;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.os.Process;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED;

/**
 * Created by Keith on 1/17/2016.
 * Contains Global static methods and variables
 */
public class Global {
    /**
     * Snapchat's package name
     */
    public final static String PACKAGE_SNAPCHAT = "com.snapchat.android";
    /**
     * Code to use to access the Shared Prefrences
     */
    public static final String SHAREDPREF_CODE = "jeeves.prefs";

    /**
     * Is the given service running.
     * <p/>
     * NOTE: The service must be in this APK
     *
     * @param serviceClass the class of the service that is being checked
     * @param c            the context of the calling class
     * @return true if the given service is running
     */
    public static boolean isServiceRunning(Class<?> serviceClass, Context c) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if (serviceClass.getName().equals(service.service.getClassName()))
                return true;
        return false;
    }

    /**
     * Change the WiFi state
     *
     * @param c  the calling context
     * @param on whether to turn the WiFi on or off
     */
    private static void changeWiFiState(Context c, boolean on) {
        try { //Try to turn on the WiFi
            ((WifiManager) c.getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(on);
        } catch (Exception e) {
            //Nothing is wrong
        }
    }

    /**
     * Turn on the WiFi
     *
     * @param c the calling context
     */
    public static void turnOnWiFi(Context c) {
        changeWiFiState(c, true);
    }

    /**
     * Turn off the WiFi
     *
     * @param c the calling context
     */
    public static void turnOffWiFi(Context c) {
        changeWiFiState(c, false);
    }

    /**
     * Turn on the GPS
     * <p/>
     * NOTE: May not work
     *
     * @param c The calling context
     */
    public static void turnGPSOn(Context c) {
        try {
            String provider = Settings.Secure.getString(c.getContentResolver(),
                    LOCATION_PROVIDERS_ALLOWED);
            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings",
                        "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                c.sendBroadcast(poke);
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
     * @param c The calling context
     */
    public static void turnGPSOff(Context c) {
        try {
            String provider = Settings.Secure.getString(c.getContentResolver(),
                    LOCATION_PROVIDERS_ALLOWED);
            if (provider.contains("gps")) { //if gps is enabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                c.sendBroadcast(poke);
            }
        } catch (Exception e) {
            KeithToast.show("didn't work", c);
        }
    }

    /**
     * Close the notification tray
     *
     * @param c the calling context
     */
    public static void closeNotificationTray(Context c) {
        try {
            c.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        } catch (Exception e) {
            //Everything's ok
        }
    }

    /**
     * Can the GPS be toggled off?
     *
     * @param c the calling context
     * @return false if the system is unable to turn off the GPS
     */
    public static boolean canToggleGPS(Context c) {
        PackageManager pacman = c.getPackageManager();
        PackageInfo pacInfo;
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
        return context.getSharedPreferences(Global.SHAREDPREF_CODE, Context.MODE_PRIVATE);
    }

    /**
     * Get the version name of this app
     *
     * @param context The calling context
     * @return the Name of this app, Unknown if it could not be found
     */
    public static String getVersionName(Context context) {
        try {
            String s = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
            if (s.isEmpty()) return "Unknown";
            else return s;
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public static boolean isKeith(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(Global.SHAREDPREF_CODE, Context.MODE_PRIVATE);
        return prefs.getBoolean(c.getString(R.string.settings_isKeith), false);
    }

    /**
     * I dislike Snapchat. If it has a background process, kill it.
     *
     * @param c the calling context
     */
    public static void tryToKillSnapchat(final Context c) {
        try {
            boolean b = false;
            final ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> listOfProcesses = manager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo process : listOfProcesses)
                if (process.processName.contains(PACKAGE_SNAPCHAT)) {
                    try {
                        android.os.Process.sendSignal(process.pid, Process.SIGNAL_KILL);
                        Process.killProcess(process.pid);
                        b = true;
                    } catch (Exception e) {
                        KeithToast.show("Error occurred killing Snapchat background process", c);
                    }
                }
            if (b) {
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        boolean isAlive = false;
                        List<ActivityManager.RunningAppProcessInfo> l = manager.getRunningAppProcesses();
                        for (ActivityManager.RunningAppProcessInfo process : l)
                            if (process.processName.toLowerCase().contains(PACKAGE_SNAPCHAT))
                                isAlive = true;
                        if (!isAlive) KeithToast.show("Snapchat background process killed", c);
                        else KeithToast.show("Failed to kill a Snapchat background process", c);
                    }
                }, 4000);
            }
        } catch (Exception e) {
            //Fuck you, snapchat
        }
    }

    /**
     * Turn on the Bluetooth
     *
     * @param c the calling context
     */
    public static void turnOffBluetooth(Context c) {
        ((BluetoothManager) c.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().disable();
    }

    /**
     * Turn off the Bluetooth
     *
     * @param c the calling context
     */
    public static void turnOnBluetooth(Context c) {
        ((BluetoothManager) c.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().enable();
    }

    public static void turnOffVibrate(AudioManager a) {
        a.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
        a.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
    }

    public static void turnOffVibrate(Context c) {
        turnOffVibrate((AudioManager) c.getSystemService(Context.AUDIO_SERVICE));
    }

    public static void turnOnVibrate(AudioManager a) {
        a.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
        a.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
    }

    public static boolean isVibrateOn(Context c) {
        return isVibrateOn((AudioManager) c.getSystemService(Context.AUDIO_SERVICE));
    }

    public static boolean isVibrateOn(AudioManager a) {
        if (a.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION) == AudioManager.VIBRATE_SETTING_OFF &&
                a.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_OFF)
            return false;
        return a.getRingerMode() != AudioManager.RINGER_MODE_SILENT;
    }

    public static void turnOnVibrate(Context c) {
        turnOnVibrate((AudioManager) c.getSystemService(Context.AUDIO_SERVICE));
    }

    public static String breakIntoLines(String s) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        boolean f = true;
        for (String x : s.split("[\\.:]")) {
            if (x.contains("\n")) i = -6;
            if (i + x.length() > 30) {
                sb.append("\n        .").append(x);
                i = x.length();
            } else {
                if (f) f = false;
                else sb.append(".");
                sb.append(x);
                i += x.length();
            }
        }
        return sb.toString().trim();
    }

    public static void turnOnNFC(final Context c) {
        final NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(c);
        new Thread("toggleNFC") {
            public void run() {
                try {
                    Class<?> NfcManagerClass;
                    Method setNfcEnabled;
                    NfcManagerClass = Class.forName(mNfcAdapter.getClass().getName());
                    setNfcEnabled = NfcManagerClass.getDeclaredMethod("enable");
                    setNfcEnabled.setAccessible(true);
                    boolean Nfc = (Boolean) setNfcEnabled.invoke(mNfcAdapter);
                    if (Nfc) KeithToast.show("Holy shit, it worked", c);
                } catch (Exception e) {
                    //Eh, worth a shot
                }
            }
        }.start();
    }

    /**
     * Show the screen size for this device
     *
     * @param a the application that is running
     */
    public static void showScreenSize(final Activity a) {
        Display display = a.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        KeithToast.show(String.format("Height: %d\nWidth: %d", height, width), a.getApplicationContext());
    }

    /**
     * Settings Strings without having to call from a Context
     */
    public class SETTINGS {
        public class CLASS {
            public static final String notificationVolume = "settings_class_notificationVolume",
                    ringtoneVolume = "settings_class_ringtoneVolume",
                    systemVolume = "settings_class_systemVolume",
                    alarmVolume = "settings_class_alarmVolume",
                    mediaVolume = "settings_class_mediaVolume";
        }

        public class OUT {
            public static final String notificationVolume = "settings_out_notificationVolume",
                    ringtoneVolume = "settings_out_ringtoneVolume",
                    systemVolume = "settings_out_systemVolume",
                    alarmVolume = "settings_out_alarmVolume",
                    mediaVolume = "settings_out_mediaVolume";
        }

        public class HOME {
            public static final String notificationVolume = "settings_home_notificationVolume",
                    ringtoneVolume = "settings_home_ringtoneVolume",
                    systemVolume = "settings_home_systemVolume",
                    alarmVolume = "settings_home_alarmVolume",
                    mediaVolume = "settings_home_mediaVolume";
        }
    }
}
