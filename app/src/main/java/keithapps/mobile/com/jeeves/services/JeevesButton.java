package keithapps.mobile.com.jeeves.services;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import keithapps.mobile.com.jeeves.R;

import static keithapps.mobile.com.jeeves.tools.SystemTools.openPackageLauncher;

/**
 * Created by Keith on 2/24/2016.
 * Jeeves Button
 */
public class JeevesButton extends PersistentFloatingButton {
    /**
     * What will happen when the button is clicked
     */
    @Override
    public void click() {
        openPackageLauncher("keithapps.mobile.com.jeeves", getApplicationContext());
    }

    /**
     * Use this to set the image resource of the bubble
     *
     * @return the resource value of the bubble's image
     */
    @Override
    public Drawable getImage() {
        return ContextCompat.getDrawable(getApplicationContext(), R.drawable.jeeves_icon);
    }

    /**
     * Use this to set the size of the bubble
     *
     * @return the size (pixels) of the bubble
     */
    @Override
    public int getBubbleSize() {
        return 150;
    }
}
