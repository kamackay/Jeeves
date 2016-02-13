package keithapps.mobile.com.jeeves.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import keithapps.mobile.com.jeeves.MainService;
import keithapps.mobile.com.jeeves.Settings;

import static keithapps.mobile.com.jeeves.Global.closeNotificationTray;
import static keithapps.mobile.com.jeeves.Global.showCromulon;
import static keithapps.mobile.com.jeeves.Global.showScreamingSun;
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
                writeToLog("Device Plugged in to charger", c, true);
                return;
            } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                writeToLog("Device Unplugged from charger", c, true);
                return;
            }
        }
        Calendar cal = Calendar.getInstance(Locale.US);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour == 0 && cal.get(Calendar.MINUTE) < 10) {
            SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code,
                    Context.MODE_PRIVATE);
            if (prefs.getInt(Settings.Adderall.adderall_count, 0) > 0 &&
                    prefs.getBoolean(Settings.Adderall.resetAtMidnight, false)) {
                Intent i = new Intent(c, NotificationButtonListener.class);
                i.setAction(Settings.Adderall.adderall_clear);
                c.sendBroadcast(i);
            }
        }
        MainService.showNotification(MainService.getMode(c), c);
        SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code,
                Context.MODE_PRIVATE);
        if (prefs.getBoolean(Settings.showScreamingSunRandomly, false) &&
                prefs.getBoolean(Settings.screen_mode, false)) {
            int n = new Random().nextInt(20);
            if (n == 0) {
                closeNotificationTray(c);
                showScreamingSun(c);
            } else if (n == 1) {
                closeNotificationTray(c);
                showCromulon(c);
            }
        }
    }
}
