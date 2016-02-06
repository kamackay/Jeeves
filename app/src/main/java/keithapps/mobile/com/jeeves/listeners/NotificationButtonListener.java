package keithapps.mobile.com.jeeves.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import keithapps.mobile.com.jeeves.Global;
import keithapps.mobile.com.jeeves.SetState;
import keithapps.mobile.com.jeeves.Settings;
import keithapps.mobile.com.jeeves.popups.AdderallPopup;

import static keithapps.mobile.com.jeeves.Global.getTimeStamp;
import static keithapps.mobile.com.jeeves.Global.writeToLog;

/**
 * Created by Keith on 2/4/2016.
 * Listens for the home button on the notification
 */
public class NotificationButtonListener extends BroadcastReceiver {
    /**
     * Listener for onClick for the A button
     *
     * @param c      The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    @Override
    public void onReceive(Context c, Intent intent) {
        SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code,
                Context.MODE_PRIVATE);
        switch (intent.getAction()) {
            case Settings.text_home:
                SetState.stateA(c);
                break;
            case Settings.text_out:
                SetState.stateC(c);
                break;
            case Settings.text_class:
                SetState.stateB(c);
                break;
            case Settings.text_add:
                int count = prefs.getInt(Settings.Adderall.adderall_count, 0);
                Global.closeNotificationTray(c);
                //KeithToast.show(String.format("That makes %d mg today", 10 * (++count)), c);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putInt(Settings.Adderall.adderall_count, count);
                edit.putString(Settings.Adderall.timeSince, getTimeStamp());
                edit.apply();
                //writeToLog(String.format("Took 10 mg of Adderall (%d mg total)", 10 * count), c);
                Intent i = new Intent(c, AdderallPopup.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                c.startActivity(i);
                break;
            case Settings.Adderall.adderall_clear:
                writeToLog(String.format("You took %d mg of Adderall today",
                        10 * prefs.getInt(Settings.Adderall.adderall_count, 0)), c);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(Settings.Adderall.adderall_count, 0);
                editor.apply();
                break;
        }
    }
}
