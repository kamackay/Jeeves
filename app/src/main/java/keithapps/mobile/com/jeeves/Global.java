package keithapps.mobile.com.jeeves;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.os.Process;
import android.view.Display;

import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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
     * The name of the Logfile
     */
    public static final String LOGFILE_NAME = "log.txt";

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

    public static boolean isJeevesRunning(Context c) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo s : manager.getRunningServices(Integer.MAX_VALUE))
            if (s.service.getClassName().contains("keithapps")) return true;
        return false;
    }

    /**
     * Turn on the WiFi
     *
     * @param c the calling context
     */
    public static void turnOnWiFi(Context c) {
        try {
            WifiManager wm = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
            wm.setWifiEnabled(true);
            writeToLog("Turned on WiFi", c);
        } catch (Exception e) {
            writeToLog(e.getLocalizedMessage(), c);
        }
    }

    /**
     * Turn off the WiFi
     *
     * @param c the calling context
     */
    public static void turnOffWiFi(Context c) {
        try {
            WifiManager wm = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
            NetworkInfo mWifi = ((ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (mWifi == null || mWifi.isConnected()) {
                wm.setWifiEnabled(false);
                writeToLog("Turned off WiFi", c);
            }
        } catch (Exception e) {
            writeToLog(e.getLocalizedMessage(), c);
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
     * Get the shared preferences
     *
     * @param context The calling Context
     * @return Shared Preferences
     */
    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE);
    }

    /**
     * Get the versionCode name of this app
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
        SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE);
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
        BluetoothAdapter bt = ((BluetoothManager) c.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        int state = bt.getState();
        if (state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_CONNECTED
                || state == BluetoothAdapter.STATE_TURNING_ON
                || state == BluetoothAdapter.STATE_CONNECTING
                || state == BluetoothAdapter.STATE_DISCONNECTED
                || state == BluetoothAdapter.STATE_DISCONNECTING) {
            bt.disable();
            writeToLog("Turned off Bluetooth", c);
        }
    }

    /**
     * Turn off the Bluetooth
     *
     * @param c the calling context
     */
    public static void turnOnBluetooth(Context c) {
        BluetoothAdapter bt = ((BluetoothManager) c.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        int state = bt.getState();
        if (state == BluetoothAdapter.STATE_OFF || state == BluetoothAdapter.STATE_TURNING_OFF) {
            bt.enable();
            writeToLog("Turned on Bluetooth", c);
        }
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
     * Write to the Log file
     *
     * @param text the text to write to the log file
     * @param c    the context of the calling method
     */
    public static void writeToLog(String text, Context c) {
        try {
            if (!c.getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE)
                    .getBoolean(Settings.record_log, true)) return;
            String toPrint = String.format("%s- %s\n", getTimeStamp(), text);
            byte[] bytes = toPrint.getBytes(Charset.forName("UTF-8"));
            FileOutputStream fos = c.openFileOutput(LOGFILE_NAME, Context.MODE_APPEND);
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            //Don't do anything
        }
    }

    /**
     * Get the current Timestamp in the format
     * <p/>
     * MM/dd/yyyy:HH:mm:ss
     *
     * @return the current timestamp in string form
     */
    public static String getTimeStamp() {
        return new SimpleDateFormat("MM/dd-HH:mm:ss", Locale.US).format(new Date());
    }

    /**
     * Clear Log FIle
     *
     * @param c the Calling context
     * @return false if clearing the file did not work
     */
    public static boolean clearLog(Context c) {
        try {
            FileOutputStream fos = c.openFileOutput(LOGFILE_NAME, Context.MODE_PRIVATE);
            String toPrint = String.format("%s: Cleared the Log File\n", getTimeStamp());
            fos.write(toPrint.getBytes(Charset.forName("UTF-8")));
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
