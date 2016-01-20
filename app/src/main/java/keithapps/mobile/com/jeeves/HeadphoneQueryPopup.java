package keithapps.mobile.com.jeeves;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

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
                clickPartial(null);
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
        final Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() { // Do this because it seems that the system will
            @Override   // lower the volume back down. Because it hates me
            public void run() {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                timer2.cancel();
                timer2.purge();
            }
        }, 5000);
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                t.cancel();
                t.purge();
            }
        }, 10000);
        finish();
    }

    /**
     * The user told the dialog to set the volume to partial
     *
     * @param v the button that was clicked
     */
    public void clickPartial(View v) {
        AudioManager audioManager =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) *
                        (getSharedPreferences(getString(R.string.sharedPrefrences_code),
                                MODE_PRIVATE).getInt(getString(R.string.settings_home_mediaVolume),
                                5)) * .1), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        finish();
    }
}
