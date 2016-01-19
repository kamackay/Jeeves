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
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    if (previouslyPlugged) { //There used to be a headset
                        previouslyPlugged = false;
                        AudioManager audioManager =
                                (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                        SharedPreferences prefs = context.getSharedPreferences(context.getString(
                                        R.string.sharedPrefrences_code),
                                Context.MODE_PRIVATE);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                                (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) *
                                        (prefs.getInt(context.getString(R.string.settings_home_notificationVolume),
                                                5)) * .1), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                                (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) *
                                        prefs.getInt(context.getString(
                                                        R.string.settings_home_mediaVolume),
                                                5) * .1), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                    }
                    break;
                case 1:
                    //KeithToast.show("Headset is plugged", context);
                    if (!previouslyPlugged) {
                        previouslyPlugged = true;
                        Intent i = new Intent(context, HeadphoneQueryPopup.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(i);
                    }
                    break;
                default:
                    KeithToast.show("Headset is unknown", context);
            }
        }
    }
}
