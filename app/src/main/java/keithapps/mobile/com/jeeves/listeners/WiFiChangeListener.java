package keithapps.mobile.com.jeeves.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import keithapps.mobile.com.jeeves.tools.Log;
import keithapps.mobile.com.jeeves.tools.Settings;

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
        try {
            boolean connectedToWiFi = prefs.getBoolean(Settings.connectedToWiFi, false);
            if (!connectedToWiFi && netInfo != null && (netInfo.getType() ==
                    ConnectivityManager.TYPE_WIFI && netInfo.isConnected())) {
                Log.writeToLog("Connected to WiFi", c);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(Settings.connectedToWiFi, true);
                edit.apply();
            } else if (connectedToWiFi && (netInfo == null || (netInfo.getType() ==
                    ConnectivityManager.TYPE_WIFI && !netInfo.isConnected()))) {
                Log.writeToLog("No Longer Connected to WiFi", c);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(Settings.connectedToWiFi, false);
                edit.apply();
            }
        } catch (Exception e) {
            //do nothing
        }
        try {
            boolean connectedToMobile = prefs.getBoolean(Settings.connectedToMobile, false);
            if (!connectedToMobile && netInfo != null && (netInfo.getType() ==
                    ConnectivityManager.TYPE_MOBILE && netInfo.isConnected())) {
                Log.writeToLog("Connected to Mobile", c);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(Settings.connectedToMobile, true);
                edit.apply();
            } else if (connectedToMobile && (netInfo == null || (netInfo.getType() ==
                    ConnectivityManager.TYPE_MOBILE && !netInfo.isConnected()))) {
                Log.writeToLog("No Longer Connected to Mobile", c);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(Settings.connectedToMobile, false);
                edit.apply();
            }
        } catch (Exception e) {
            //Do nothing
        }
    }
}
