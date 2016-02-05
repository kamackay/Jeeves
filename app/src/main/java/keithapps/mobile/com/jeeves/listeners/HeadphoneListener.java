package keithapps.mobile.com.jeeves.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;

import keithapps.mobile.com.jeeves.HeadphoneQueryPopup;
import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.Settings;

import static keithapps.mobile.com.jeeves.Global.writeToLog;

/**
 * Created by Keith on 1/18/2016.
 * Listens for Headphones to be plugged in
 */
public class HeadphoneListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context c, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            writeToLog(intent.getAction(), c);
        } else if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code,
                    Context.MODE_PRIVATE);
            switch (state) {
                case 0:
                    if (prefs.getBoolean(Settings.headset_pluggedIn, false)) { //There used to be a headset
                        writeToLog("Headset Unplugged", c);
                        AudioManager audioManager =
                                (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
                        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                                (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) *
                                        (prefs.getInt(Settings.Home.notificationVolume,
                                                5)) * .1), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                        SharedPreferences.Editor edit = prefs.edit();
                        if (prefs.getBoolean(Settings.headset_full, false))
                            edit.putBoolean(Settings.headset_full, false);
                        edit.putBoolean(Settings.headset_pluggedIn, false);
                        edit.apply();
                        writeToLog("Stop Pushing Media Vol to Full", c);
                    }
                    break;
                case 1:
                    if (!prefs.getBoolean(Settings.headset_pluggedIn, false)
                            && prefs.getBoolean(c.getString(R.string.settings_showHeadphonesPopup), true)) {
                        Intent i = new Intent(c, HeadphoneQueryPopup.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        c.startActivity(i);
                    }
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean(Settings.headset_pluggedIn, true);
                    edit.apply();
                    writeToLog("Headset Plugged in", c);
                    break;
                default:
                    writeToLog("Unknown Headphone State", c);
            }
        }
    }
}
