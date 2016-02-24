package keithapps.mobile.com.jeeves.services;

import keithapps.mobile.com.jeeves.R;

import static keithapps.mobile.com.jeeves.tools.SystemTools.openPackageLauncher;

/**
 * Created by Keith on 2/24/2016.
 * Facebook Button
 */
public class FacebookButton extends PersistentFloatingButton {
    /**
     * What will happen when the button is clicked
     */
    @Override
    public void click() {
        openPackageLauncher("uk.co.olabs.simply", getApplicationContext());
    }

    @Override
    public int getImageResource() {
        return R.drawable.facebook;
    }

    @Override
    public int getBubbleSize() {
        return 125;
    }
}
