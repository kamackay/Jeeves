package keithapps.mobile.com.jeeves.listeners;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import keithapps.mobile.com.jeeves.MainService;
import keithapps.mobile.com.jeeves.tools.Settings;

import static keithapps.mobile.com.jeeves.activities.popups.CromulonPopup.showCromulon;
import static keithapps.mobile.com.jeeves.activities.popups.ScreamingSunPopup.showScreamingSun;
import static keithapps.mobile.com.jeeves.tools.AndroidTools.getDouble;
import static keithapps.mobile.com.jeeves.tools.AndroidTools.getPrefs;
import static keithapps.mobile.com.jeeves.tools.AndroidTools.putDouble;
import static keithapps.mobile.com.jeeves.tools.Log.writeToLog;

/**
 * Created by Keith on 1/19/2016.
 * Background Process Listener
 */
public class BackgroundProcessListener extends BroadcastReceiver implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    @Override
    public void onReceive(final Context c, Intent intent) {
        String s = intent.getAction();
        if (s != null) {
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                writeToLog("Device Plugged in to charger", c, true);
                return;
            } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                writeToLog("Device Unplugged from charger", c, true);
                return;
            }
        }
        Calendar cal = Calendar.getInstance(Locale.US);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour == 0 && cal.get(Calendar.MINUTE) < 10) {
            SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code,
                    Context.MODE_PRIVATE);
            if (prefs.getInt(Settings.Adderall.adderall_count, 0) > 0 &&
                    prefs.getBoolean(Settings.Adderall.resetAtMidnight, false)) {
                Intent i = new Intent(c, NotificationButtonListener.class);
                i.setAction(Settings.Adderall.intentAction_adderallClear);
                c.sendBroadcast(i);
            }
        }
        MainService.showNotification(MainService.getMode(c), c);
        SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code,
                Context.MODE_PRIVATE);
        if (prefs.getBoolean(Settings.showScreamingSunRandomly, false) &&
                prefs.getBoolean(Settings.screen_mode, false)) {
            int n = new Random().nextInt(prefs.getInt(Settings.intrusivePopupFrequency, 50) + 1);
            if (n == 0) {
                showScreamingSun(c);
            } else if (n == 1) {
                showCromulon(c);
            }
        }
        if (mGoogleApiClient == null) mGoogleApiClient = new GoogleApiClient.Builder(c)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if (!mGoogleApiClient.isConnected()) mGoogleApiClient.connect();
        else mGoogleApiClient.reconnect();
        //mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(mGoogleApiClient.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mGoogleApiClient.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        SharedPreferences prefs = getPrefs(mGoogleApiClient.getContext());
        if (mLastLocation != null &&
                (getDouble(prefs, Settings.Location.lastLat, 0) != mLastLocation.getLatitude() ||
                        getDouble(prefs, Settings.Location.lastLong, 0)
                                != mLastLocation.getLongitude())) {
            SharedPreferences.Editor edit = prefs.edit();
            edit = putDouble(edit, Settings.Location.lastLat, mLastLocation.getLatitude());
            edit = putDouble(edit, Settings.Location.lastLong, mLastLocation.getLongitude());
            edit.apply();
            double lat = mLastLocation.getLatitude(), lon = mLastLocation.getLongitude();
            writeToLog(String.format(Locale.getDefault(), "Location: %s %c, %s %c",
                    String.valueOf(Math.abs(lat)), (lat > 0) ? 'N' : 'S',
                    String.valueOf(Math.abs(lon)), (lon > 0) ? 'E' : 'W'),
                    mGoogleApiClient.getContext());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
