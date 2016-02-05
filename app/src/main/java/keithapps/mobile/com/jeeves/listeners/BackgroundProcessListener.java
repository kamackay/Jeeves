package keithapps.mobile.com.jeeves.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Keith on 1/19/2016.
 * Background Process Listener
 */
public class BackgroundProcessListener extends BroadcastReceiver {
    @Override
    public void onReceive(final Context c, Intent intent) {
        //MainService.showNotification(MainService.getMode(c), c); //May or may not want to do this.
        // It looks as if the icon is being cleared from the Notification when the
        // main activity closes. IDK, man
        //writeToLog("Background Process Run", c);
    }
}
