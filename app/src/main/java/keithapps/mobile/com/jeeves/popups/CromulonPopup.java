package keithapps.mobile.com.jeeves.popups;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.ViewGroup;
import android.view.WindowManager;

import keithapps.mobile.com.jeeves.CromulonView;
import keithapps.mobile.com.jeeves.R;

/**
 * Created by Keith on 2/12/2016.
 * Cromulon Activity
 */
public class CromulonPopup extends Activity {
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cromulon_popup);
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
        player = new MediaPlayer();
        CromulonView cromulonView = (CromulonView)findViewById(R.id.cromulon);
        cromulonView.setOnAnimationEnd(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
        cromulonView.setPlaySound(new Runnable() {
            @Override
            public void run() {
                player.start();
            }
        });
        try {
            AssetFileDescriptor afd = getAssets().openFd("cromulon.ogg");
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
        } catch (Exception e){
            //Fuck it
        }
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