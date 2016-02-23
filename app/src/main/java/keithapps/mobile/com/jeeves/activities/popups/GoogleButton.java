package keithapps.mobile.com.jeeves.activities.popups;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import keithapps.mobile.com.jeeves.views.GoogleButtonView;

import static keithapps.mobile.com.jeeves.tools.GlobalTools.openGoogle;

/**
 * Created by Keith on 2/23/2016.
 * Google Button
 */
public class GoogleButton extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleButtonView googleButtonView = new GoogleButtonView(getApplicationContext());
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                0,
                PixelFormat.TRANSLUCENT);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        wm.addView(googleButtonView, params);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
            return true;
        openGoogle(getApplicationContext());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch (keycode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
            case KeyEvent.KEYCODE_HOME:
                return true;
        }
        return super.onKeyDown(keycode, e);
    }
}
