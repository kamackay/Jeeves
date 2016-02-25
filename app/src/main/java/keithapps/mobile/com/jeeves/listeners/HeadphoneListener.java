package keithapps.mobile.com.jeeves.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import keithapps.mobile.com.jeeves.tools.Log;
import keithapps.mobile.com.jeeves.tools.Settings;

import static keithapps.mobile.com.jeeves.activities.popups.HeadphonePopup.showHeadphonesPopup;
import static keithapps.mobile.com.jeeves.tools.SetState.setState;

/**
 * Created by Keith on 1/18/2016.
 * Listens for Headphones to be plugged in
 */
public class HeadphoneListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context c, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            Log.writeToLog(intent.getAction(), c);
        } else if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code,
                    Context.MODE_PRIVATE);
            switch (state) {
                case 0:
                    if (prefs.getBoolean(Settings.headset_pluggedIn, false)) { //There used to be a headset
                        Log.writeToLog("Headset Unplugged", c);
                        setState(c);
                        SharedPreferences.Editor edit = prefs.edit();
                        if (prefs.getBoolean(Settings.headset_full, false))
                            edit.putBoolean(Settings.headset_full, false);
                        edit.putBoolean(Settings.headset_pluggedIn, false);
                        edit.apply();
                        Log.writeToLog("Stop Pushing Media Vol to Full", c);
                    }
                    break;
                case 1:
                    if (!prefs.getBoolean(Settings.headset_pluggedIn, false)
                            && prefs.getBoolean(Settings.showHeadphonePopup, true))
                        showHeadphonesPopup(c);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean(Settings.headset_pluggedIn, true);
                    edit.apply();
                    Log.writeToLog("Headset Plugged in", c);
                    break;
                default:
                    Log.writeToLog("Unknown Headphone State", c);
            }
        }
    }
}
