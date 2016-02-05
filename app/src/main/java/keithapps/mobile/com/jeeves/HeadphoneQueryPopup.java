package keithapps.mobile.com.jeeves;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import static keithapps.mobile.com.jeeves.Global.writeToLog;

public class HeadphoneQueryPopup extends Activity {

    /**
     * Initialize
     *
     * @param savedInstanceState idk
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headphone_query_popup);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                timer.purge();
                finish();
            }
        }, 5000);
    }

    /**
     * The user told the dialog to set the volume to full
     *
     * @param v the button that was clicked
     */
    public void clickFull(View v) {
        final AudioManager audioManager =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                MODE_PRIVATE).edit();
        edit.putBoolean(Settings.headset_full, true);
        edit.apply();
        writeToLog("Headset Popup - Full", getApplicationContext());
        finish();
    }

    /**
     * The user told the dialog to set the volume to partial
     *
     * @param v the button that was clicked
     */
    public void clickPartial(View v) {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) *
                        (getSharedPreferences(Settings.sharedPrefs_code,
                                MODE_PRIVATE).getInt(Settings.Home.mediaVolume,
                                5)) * .1), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                MODE_PRIVATE).edit();
        edit.putBoolean(Settings.headset_full, false);
        edit.apply();
        writeToLog("Headset Popup - Partial", getApplicationContext());
        finish();
    }
}
