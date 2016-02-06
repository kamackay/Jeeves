package keithapps.mobile.com.jeeves.popups;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;

import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.Settings;

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
                                MODE_PRIVATE).getInt(Settings.A.mediaVolume,
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