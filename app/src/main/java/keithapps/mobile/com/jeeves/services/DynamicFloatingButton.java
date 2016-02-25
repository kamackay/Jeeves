package keithapps.mobile.com.jeeves.services;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import keithapps.mobile.com.jeeves.activities.popups.CreateFloatingButton;

import static keithapps.mobile.com.jeeves.tools.SystemTools.openPackageLauncher;

/**
 * Created by Keith on 2/24/2016.
 * Dynamic Valued Floating Button
 */
public class DynamicFloatingButton extends PersistentFloatingButton {
    Drawable icon;
    String packageToOpen;

    /**
     * Use this to set the size of the bubble
     *
     * @return the size (pixels) of the bubble
     */
    @Override
    public int getBubbleSize() {
        return 125;
    }

    /**
     * Use this to set the image resource of the bubble
     *
     * @return the resource value of the bubble's image
     */
    @Override
    public Drawable getImage() {
        return (icon == null) ? super.getImage() : icon;
    }

    /**
     * What will happen when the button is clicked
     */
    @Override
    public void click() {
        try {
            openPackageLauncher(packageToOpen, getApplicationContext());
        } catch (Exception e) {
            //It's ok
        }
    }

    @Override
    void longClick() {
        super.longClick();
        CreateFloatingButton.showCreateFloatingButton(getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        packageToOpen = intent.getStringExtra("packageToOpen");
        PackageManager p = getPackageManager();
        try {
            icon = p.getApplicationIcon(packageToOpen);
        } catch (Exception e) {
            //I Don't know...
        }
        updateDrawable();
        return START_STICKY;
    }
}
