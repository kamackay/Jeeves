package keithapps.mobile.com.jeeves.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import keithapps.mobile.com.jeeves.Settings;

import static keithapps.mobile.com.jeeves.Global.writeToLog;

/**
 * Created by Keith on 2/5/2016.
 * Listens for changes in the Connectivity Settings
 */
public class WiFiChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context c, Intent i) {
        ConnectivityManager conMan = (ConnectivityManager)
                c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code,
                Context.MODE_PRIVATE);
        boolean connectedToWiFi = prefs.getBoolean(Settings.connectedToWiFi, false);
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI
                && !connectedToWiFi) {
            writeToLog("Connected to WiFi", c);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(Settings.connectedToWiFi, true);
            edit.apply();
        } else if (netInfo == null && connectedToWiFi) {
            writeToLog("No Longer Connected to WiFi", c);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(Settings.connectedToWiFi, false);
            edit.apply();
        }


    }
}
