package keithapps.mobile.com.jeeves.popups;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.Settings;

import static keithapps.mobile.com.jeeves.Global.writeToLog;
import static keithapps.mobile.com.jeeves.MainService.updateNotification;

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
        ActionBar a = getActionBar();
        if (a != null) a.hide();
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        getWindow().setLayout((int) (size.x * .75), ViewGroup.LayoutParams.WRAP_CONTENT);
        writeToLog("Headphone Query Popup", getApplicationContext());
        Typeface tf = Typeface.createFromAsset(getAssets(), "calibri.ttf");
        if (tf != null) {
            ((Button) findViewById(R.id.headphone_popup_fullButton)).setTypeface(tf);
            ((Button) findViewById(R.id.headphone_popup_partialButton)).setTypeface(tf);
            ((TextView) findViewById(R.id.headphone_popup_text)).setTypeface(tf);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(8000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } catch (Exception e) {
                    //Do nothing
                }
            }
        }).start();
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
        updateNotification(getApplicationContext());
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
        updateNotification(getApplicationContext());
        finish();
    }
}
