package keithapps.mobile.com.jeeves.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import keithapps.mobile.com.jeeves.MainService;
import keithapps.mobile.com.jeeves.Settings;

import static keithapps.mobile.com.jeeves.Global.writeToLog;

/**
 * Created by Keith on 1/17/2016.
 * Listens for the boot to complete
 */
public class BootListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context c, Intent intent) {
        String s = intent.getAction();
        if (s != null) {
            switch (s) {
                case Intent.ACTION_REBOOT:
                    writeToLog("Device Restarting", c);
                    break;
                case Intent.ACTION_SHUTDOWN:
                    writeToLog("Device Shutting Down", c);
                    break;
                case Intent.ACTION_BOOT_COMPLETED:
                    writeToLog("Device Boot Finished", c);
                    c.startService(new Intent(c, MainService.class));
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    screenChange(false, c);
                    break;
                case Intent.ACTION_SCREEN_ON:
                    screenChange(true, c);
                    break;
            }
        }
    }

    public static void screenChange(boolean on, Context c) {
        SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code,
                Context.MODE_PRIVATE);
        if (on == prefs.getBoolean(Settings.screen_mode, false)) return;
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(Settings.screen_mode, on);
        edit.apply();
        writeToLog(String.format("Device Screen turned %s", on ? "On" : "Off"), c);
    }
}
