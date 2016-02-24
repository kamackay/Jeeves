package keithapps.mobile.com.jeeves.services;

import keithapps.mobile.com.jeeves.R;

import static keithapps.mobile.com.jeeves.tools.GlobalTools.openGoogle;
import static keithapps.mobile.com.jeeves.tools.SystemTools.getPrefs;

/**
 * Created by Keith on 2/23/2016.
 * Floating Google Button
 */
public class FloatingGoogleButton extends PersistentFloatingButton {
    @Override
    public void click() {
        openGoogle(getApplicationContext());
    }

    @Override
    public int getImageResource() {
        return R.drawable.google;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!getPrefs(getApplicationContext())
                .getBoolean(getString(R.string.settings_showGoogleButton), true)) onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    @Override
    public int getBubbleSize() {
        return 125;
    }
}
