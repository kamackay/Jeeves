package keithapps.mobile.com.jeeves.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Locale;

import keithapps.mobile.com.jeeves.KeithToast;
import keithapps.mobile.com.jeeves.MainService;
import keithapps.mobile.com.jeeves.Settings;

import static keithapps.mobile.com.jeeves.Global.writeToLog;

/**
 * Created by Keith on 1/19/2016.
 * Background Process Listener
 */
public class BackgroundProcessListener extends BroadcastReceiver {
    @Override
    public void onReceive(final Context c, Intent intent) {
        String s = intent.getAction();
        if (s != null) {
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                writeToLog("Device Plugged in to charger", c);
                return;
            } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                writeToLog("Device Unplugged from charger", c);
                return;
            }
        }

        MainService.showNotification(MainService.getMode(c), c); //May or may not want to do this.
        // It looks as if the icon is being cleared from the Notification when the
        // main activity closes. IDK, man
        //writeToLog("Background Process Run", c);

        Calendar cal = Calendar.getInstance(Locale.US);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour == 0 && cal.get(Calendar.MINUTE) < 10 &&
                c.getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE)
                        .getInt(Settings.adderall_count, 0) > 0) {
            Intent i = new Intent(c, SetStateButtonListener.class);
            i.setAction(Settings.adderall_clear);
            c.sendBroadcast(i);
            KeithToast.show("Adderall Amount Reset", c);
        }
    }
}
