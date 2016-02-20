package keithapps.mobile.com.jeeves.tools;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.Display;

import org.apache.commons.lang3.text.WordUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.activities.popups.KeithToast;

import static keithapps.mobile.com.jeeves.tools.LocationTools.getLastKnownLocation;

/**
 * Created by Keith on 2/18/2016.
 * Helpers for the Android System
 */
public class SystemTools {
    /**
     * Get the shared preferences
     *
     * @param c The calling Context
     * @return Shared Preferences
     */
    public static SharedPreferences getPrefs(Context c) {
        return c.getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE);
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
            Log.writeToLog("Turned on WiFi", c);
        } catch (Exception e) {
            Log.writeToLog(e.getLocalizedMessage(), c);
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
                Log.writeToLog("Turned off WiFi", c);
            }
        } catch (Exception e) {
            Log.writeToLog(e.getLocalizedMessage(), c);
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
     * Turn on the Bluetooth
     *
     * @param c the calling context
     */
    public static void turnOffBluetooth(Context c) {
        try {
            BluetoothAdapter bt = ((BluetoothManager) c.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
            int state = bt.getState();
            if (state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_CONNECTED
                    || state == BluetoothAdapter.STATE_TURNING_ON
                    || state == BluetoothAdapter.STATE_CONNECTING
                    || state == BluetoothAdapter.STATE_DISCONNECTED
                    || state == BluetoothAdapter.STATE_DISCONNECTING) {
                bt.disable();
                Log.writeToLog("Turned off Bluetooth", c);
            }
        } catch (Exception e) {
            Email.emailException("Error Turning off Bluetooth", c, e);
        }
    }

    /**
     * Turn off the Bluetooth
     *
     * @param c the calling context
     */
    public static void turnOnBluetooth(Context c) {
        try {
            BluetoothAdapter bt = ((BluetoothManager) c.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
            int state = bt.getState();
            if (state == BluetoothAdapter.STATE_OFF || state == BluetoothAdapter.STATE_TURNING_OFF) {
                bt.enable();
                Log.writeToLog("Turned on Bluetooth", c);
            }
        } catch (Exception e) {
            Email.emailException("Error Turning on Bluetooth", c, e);
        }
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

    /**
     * Show the screen size for this device
     *
     * @param a the application that is running
     */
    public static void showScreenSize(final Activity a) {
        try {
            Display display = a.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            KeithToast.show(String.format(Locale.getDefault(), "Height: %d\nWidth: %d", height, width), a.getApplicationContext());
        } catch (Exception e) {
            Email.emailException("Error Showing Screen Size", a.getApplicationContext(), e);
        }
    }

    /**
     * Get the name of this version of Android
     *
     * @param SDK the SDK version of this device
     * @return the name of this version of Android
     */
    public static String getSDKVersionName(int SDK) {
        switch (SDK) {
            case 1:
                return "";
            case 2:
                return "";
            case 3:
                return "Cupcake";
            case 4:
                return "Donut";
            case 5:
                return "Eclair";
            case 6:
                return "Eclair";
            case 7:
                return "Eclair";
            case 8:
                return "Froyo";
            case 9:
                return "Gingerbread";
            case 10:
                return "Gingerbread";
            case 11:
                return "Honeycomb";
            case 12:
                return "Honeycomb";
            case 13:
                return "Honeycomb";
            case 14:
                return "Ice Cream Sandwich";
            case 15:
                return "Ice Cream Sandwich";
            case 16:
                return "Jelly Bean";
            case 17:
                return "Jelly Bean";
            case 18:
                return "Jelly Bean";
            case 19:
                return "KitKat";
            case 20:
                return "KitKat";
            case 21:
                return "Lollipop";
            case 22:
                return "Lollipop MR1";
            case 23:
                return "Marshmallow";
        }
        return "";
    }

    /**
     * Get the main Google account on this device
     *
     * @param c the calling context
     * @return the main google account on this device. Empty String if it could not be found
     */
    public static String getGoogleUsername(Context c) {
        try {
            if (!getPrefs(c).getBoolean(c.getString(R.string.permissions_accounts), true))
                return "No Account Permissions";
            AccountManager manager = AccountManager.get(c);
            Account[] accounts = manager.getAccountsByType("com.google");
            List<String> possibleEmails = new LinkedList<>();
            for (Account account : accounts) possibleEmails.add(account.name);
            if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
                for (String email : possibleEmails)
                    if (email.contains("gmail")) return email;
                return possibleEmails.get(possibleEmails.size() - 1);
            } else return "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDeviceInfo(Context c) {
        final String lineStarter = "\n    ";
        StringBuilder builder = new StringBuilder();
        try {
            builder.append("    Build: ");
            builder.append(Build.VERSION.SDK_INT).append(" ").append(getSDKVersionName(Build.VERSION.SDK_INT));
            builder.append(lineStarter + "Device: ");
            builder.append(WordUtils.capitalizeFully(Build.MANUFACTURER)).append(" ");
            builder.append(Build.MODEL);
            builder.append(lineStarter + "Phone #").append(getPhoneNumber(c));
            builder.append(lineStarter + "IPv4: ").append(Utils.getIPAddress(true, c));
            builder.append(lineStarter + "Main Google Account: ").append(getGoogleUsername(c));
            builder.append(lineStarter + "Last Known Location: ").append(getLastKnownLocation(c));
            //builder.append(String.format("%sBattery Level: %f%%", lineStarter, getBatteryPercentage(c)));
            //builder.append(lineStarter + "IPv6: " + Utils.getIPAddress(false));
            //builder.append("MAC Address: " + Utils.getMACAddress("eth0")+"\n");
            return builder.toString();
        } catch (Exception e) {
            return builder.toString();
        }
    }

    public static String getPhoneNumber(Context c) {
        return (getPrefs(c).getBoolean(c.getString(R.string.permissions_phoneNumber), true)) ?
                ((TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number() :
                "No Phone Number Permissions";
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit,
                                                     final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    public static double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }
}
