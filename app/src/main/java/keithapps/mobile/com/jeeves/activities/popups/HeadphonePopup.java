package keithapps.mobile.com.jeeves.activities.popups;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.tools.Email;
import keithapps.mobile.com.jeeves.tools.Log;
import keithapps.mobile.com.jeeves.tools.ManageVolume;
import keithapps.mobile.com.jeeves.tools.Settings;

import static keithapps.mobile.com.jeeves.services.MainService.updateNotification;
import static keithapps.mobile.com.jeeves.tools.GlobalTools.getAllChildren;
import static keithapps.mobile.com.jeeves.tools.Log.logException;
import static keithapps.mobile.com.jeeves.tools.SystemTools.closeNotificationTray;
import static keithapps.mobile.com.jeeves.tools.SystemTools.getFont;
import static keithapps.mobile.com.jeeves.tools.SystemTools.getPrefs;

public class HeadphonePopup extends Activity {

    Typeface tf;

    public static void showHeadphonesPopup(Context c) {
        try {
            if (!getPrefs(c).getBoolean(c.getString(R.string.permissions_drawOverOtherApps), true))
                return;
            closeNotificationTray(c);
            Intent i = new Intent(c, HeadphonePopup.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            c.startActivity(i);
        } catch (Exception e) {
            Email.emailException("Error Showing Headphone Popup", c, e);
        }
    }

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
        final AudioManager audioManager =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        getWindowManager().getDefaultDisplay().getSize(size);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().setLayout((int) (size.x * .75), ViewGroup.LayoutParams.WRAP_CONTENT);
        Log.writeToLog("Headphone Query Popup", getApplicationContext());
        /*
        Typeface tf = getFont(getApplicationContext());
        if (tf != null) {
            ((Button) findViewById(R.id.headphone_popup_fullButton)).setTypeface(tf);
            ((Button) findViewById(R.id.headphone_popup_partialButton)).setTypeface(tf);
            ((TextView) findViewById(R.id.headphone_popup_text)).setTypeface(tf);
        }/**/
        final TextView countdown = (TextView) findViewById(R.id.headphonePopup_countdown);
        final int x = 10;
        setFont();
        countdown.setText(String.valueOf(x));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = x - 1; i >= 0; i--) {
                        Thread.sleep(1000);
                        if (i == 0) runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                countdown.setText("0");
                                finish();
                            }
                        });
                        else {
                            final int finalI = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    countdown.setText(String.valueOf(finalI));
                                }
                            });
                            if (i == 3) runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    countdown.setTextColor(Color.RED);
                                }
                            });
                        }
                    }
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
        ManageVolume.lockFullVolume(getApplicationContext());
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
        Log.writeToLog("Headset Popup - Partial", getApplicationContext());
        updateNotification(getApplicationContext());
        finish();
    }

    void setFont() {
        try {
            if (tf == null)
                tf = getFont(getApplicationContext());
            if (tf == null) return;
            ArrayList<View> views = getAllChildren(findViewById(R.id.headphone_popup_root));
            for (int i = 0; i < views.size(); i++) {
                View v = views.get(i);
                if (v instanceof TextView) {
                    try {
                        ((TextView) v).setTypeface(tf);
                    } catch (Exception e) {
                        //It's cool, move on
                    }
                }
            }
        } catch (Exception e) {
            logException("Error loading font in Adderall Popup", getApplicationContext(), e);
        }
    }
}
