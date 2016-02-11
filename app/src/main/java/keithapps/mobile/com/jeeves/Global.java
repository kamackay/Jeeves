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
import android.os.Vibrator;
import android.view.Display;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import keithapps.mobile.com.jeeves.popups.HeadphoneQueryPopup;
import keithapps.mobile.com.jeeves.popups.KeithToast;

import static javax.mail.Session.getInstance;

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

    public static boolean isJeevesActivityForeground(Context c) {
        try {
            List<ActivityManager.RunningAppProcessInfo> tasks = ((ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE))
                    .getRunningAppProcesses();
            if (tasks == null) return false;
            String mainActivity = tasks.get(0).processName;
            return mainActivity.toLowerCase().contains("keithapps");
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isKeith(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE);
        return prefs.getBoolean(c.getString(R.string.settings_isKeith), false) && BuildConfig.DEBUG;
    }

    public static void makeKeith(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code,
                Context.MODE_PRIVATE);
        if (prefs.getBoolean(c.getString(R.string.settings_isKeith), false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(c.getString(R.string.settings_isKeith), false);
            editor.apply();
            KeithToast.show("You are no longer a developer.", c);
        } else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(c.getString(R.string.settings_isKeith), true);
            editor.apply();
            KeithToast.show("Hello, Keith", c);
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
        a.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        a.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    public static void turnOnVibrate(Context c) {
        AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);

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
        writeToLog(text, c, false);
    }

    public static void showHeadphonesPopup(Context c) {
        try {
            Intent i = new Intent(c, HeadphoneQueryPopup.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            c.startActivity(i);
        } catch (Exception e) {
            writeToLog("Error Showing Headphone popup", c);
        }
    }

    public static void sendEmailTo(final String header, final String message, final String recipient) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String username = "android.jeeves@yahoo.com";
                final String password = "jeevspass";
                Properties props = new Properties();
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.host", "smtp.mail.yahoo.com");
                props.put("mail.debug", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.port", "465");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");
                Session session = getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
                try {
                    Message m = new MimeMessage(session);
                    m.setFrom(new InternetAddress(username));
                    m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                    m.setSubject(header);
                    m.setText(message);
                    Transport.send(m);
                } catch (Exception e) {
                    return;
                }
            }
        }).start();
    }

    public static void sendEmail(final String header, final String message) {
        sendEmailTo(header, message, "android.jeeves@yahoo.com");
    }

    public static void writeToLog(String text, Context c, boolean showToast) {
        try {
            if (!c.getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE)
                    .getBoolean(Settings.record_log, true)) return;
            String toPrint = String.format("%s- %s\n", getTimestamp(), text);
            byte[] bytes = toPrint.getBytes(Charset.forName("UTF-8"));
            FileOutputStream fos = c.openFileOutput(LOGFILE_NAME, Context.MODE_APPEND);
            fos.write(bytes);
            fos.close();
            if (showToast && isJeevesActivityForeground(c))
                Toast.makeText(c, toPrint.trim(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //Don't do anything
        }
    }

    public static String getForegroundAppName(Context c) {
        try {
            return ((ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE))
                    .getRunningAppProcesses().get(0).processName;
        } catch (Exception e) {
            return "Unknown";
        }
    }

    /**
     * Get the current Timestamp in the format
     * <p/>
     * MM/dd/yyyy:HH:mm:ss
     *
     * @return the current timestamp in string form
     */
    public static String getTimestamp() {
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
            String toPrint = String.format("%s: Cleared the Log File\n", getTimestamp());
            fos.write(toPrint.getBytes(Charset.forName("UTF-8")));
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
