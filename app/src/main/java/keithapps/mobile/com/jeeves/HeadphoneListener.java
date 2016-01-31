package keithapps.mobile.com.jeeves;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;

/**
 * Created by Keith on 1/18/2016.
 * Listens for Headphones to be plugged in
 */
public class HeadphoneListener extends BroadcastReceiver {
    private boolean previouslyPlugged = false;

    @Override
    public void onReceive(Context c, Intent intent) {
        if (!intent.getAction().equals(Intent.ACTION_HEADSET_PLUG))
            KeithToast.show(intent.getAction(), c);
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    if (previouslyPlugged) { //There used to be a headset
                        previouslyPlugged = false;
                        AudioManager audioManager =
                                (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
                        SharedPreferences prefs = c.getSharedPreferences(c.getString(
                                        R.string.sharedPrefrences_code),
                                Context.MODE_PRIVATE);
                        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                                (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) *
                                        (prefs.getInt(Global.SETTINGS.HOME.notificationVolume,
                                                5)) * .1), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                    }
                    break;
                case 1:
                    if (!previouslyPlugged && c.getSharedPreferences(
                            c.getString(R.string.sharedPrefrences_code), Context.MODE_PRIVATE)
                            .getBoolean(c.getString(R.string.settings_showHeadphonesPopup), true)) {
                        previouslyPlugged = true;
                        Intent i = new Intent(c, HeadphoneQueryPopup.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        c.startActivity(i);
                    }
                    break;
                default:
                    KeithToast.show("Headset is unknown", c);
            }
        }
    }
}
