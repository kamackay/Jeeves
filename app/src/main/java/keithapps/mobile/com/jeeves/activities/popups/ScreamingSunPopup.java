package keithapps.mobile.com.jeeves.activities.popups;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.ViewGroup;
import android.view.WindowManager;

import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.views.ScreamingSunView;

import static keithapps.mobile.com.jeeves.tools.SystemTools.getPrefs;

/**
 * Created by Keith on 2/11/2016.
 * Cromulon Popup
 */
public class ScreamingSunPopup extends Activity {
    float volume = 1;
    private MediaPlayer player;

    public static void showScreamingSun(Context c) {
        if (!getPrefs(c).getBoolean(c.getString(R.string.permissions_drawOverOtherApps), true))
            return;
        Intent i = new Intent(c, ScreamingSunPopup.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screaming_sun_popup);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ActionBar a = getActionBar();
        if (a != null) a.hide();
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            //Just Wasn't meant to happen
        }
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            finish();
            return;
        }
        ScreamingSunView ssv = (ScreamingSunView) findViewById(R.id.screaming_sun);
        ssv.setOnAnimationEnd(new Runnable() {
            @Override
            public void run() {
                if (player.isPlaying()) fadeOut();
                else finish();
            }
        });
        try {
            player = new MediaPlayer();
            AssetFileDescriptor afd = getAssets().openFd("screaming.mp3");
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.start();
        } catch (Exception e) {

        }
    }

    public void fadeOut() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (volume >= 0) {
                    try {
                        Thread.sleep(100);
                        volume -= .05;
                        player.setVolume(volume, volume);
                    } catch (Exception e) {
                    }
                }
                player.stop();
                finish();
            }
        }).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (player.isPlaying()) player.stop();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player.isPlaying()) player.stop();
    }
}
