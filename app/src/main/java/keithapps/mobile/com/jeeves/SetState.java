package keithapps.mobile.com.jeeves;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;

import keithapps.mobile.com.jeeves.ManageVolume.Mode;

import static keithapps.mobile.com.jeeves.Global.canToggleGPS;
import static keithapps.mobile.com.jeeves.Global.closeNotificationTray;
import static keithapps.mobile.com.jeeves.Global.getPrefs;
import static keithapps.mobile.com.jeeves.Global.tryToKillSnapchat;
import static keithapps.mobile.com.jeeves.Global.turnGPSOff;
import static keithapps.mobile.com.jeeves.Global.turnOffWiFi;
import static keithapps.mobile.com.jeeves.Global.turnOnWiFi;
import static keithapps.mobile.com.jeeves.MainService.showNotification;
import static keithapps.mobile.com.jeeves.ManageVolume.setAlarmVolume;
import static keithapps.mobile.com.jeeves.ManageVolume.setMediaVolume;
import static keithapps.mobile.com.jeeves.ManageVolume.setNotificationVolume;
import static keithapps.mobile.com.jeeves.ManageVolume.setRingtoneVolume;
import static keithapps.mobile.com.jeeves.ManageVolume.setSystemVolume;

/**
 * Created by Keith on 1/19/2016.
 * What to do when entering each state
 */
public class SetState {
    /**
     * Set the state to out
     *
     * @param c the calling context
     */
    public static void out(Context c) {
        try {
            Global.closeNotificationTray(c);
        } catch (Exception e) {
            //Everything's ok
        }
        //Turn off WiFi
        turnOffWiFi(c);
        setRingtoneVolume(c, ManageVolume.Mode.Home);
        setSystemVolume(c, ManageVolume.Mode.Home);
        setAlarmVolume(c, ManageVolume.Mode.Home);
        setMediaVolume(c, ManageVolume.Mode.Home);
        setNotificationVolume(c, ManageVolume.Mode.Home);

        tryToKillSnapchat(c);
        showNotification(Mode.Out, c);
    }

    public static void atHome(Context c) {
        KeithToast.show("Welcome Home, sir", c);
        closeNotificationTray(c);
        try {
            SharedPreferences prefs = getPrefs(c);
            AudioManager a = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
            setRingtoneVolume(c, a, prefs, ManageVolume.Mode.Home);
            setSystemVolume(c, a, prefs, ManageVolume.Mode.Home);
            setAlarmVolume(c, a, prefs, ManageVolume.Mode.Home);
            setMediaVolume(c, a, prefs, ManageVolume.Mode.Home);
            setNotificationVolume(c, a, prefs, ManageVolume.Mode.Home);
        } catch (Exception e) {
            //It's all good
        }
        turnOnWiFi(c);
        tryToKillSnapchat(c);
        showNotification(Mode.Home, c);
    }

    public static void inClass(Context c) {
        StringBuilder sb = new StringBuilder();
        sb.append("Have a good class, sir").append(Global.TEXT_NEWLINE)
                .append(Global.TEXT_NEWLINE);
        closeNotificationTray(c);
        try { //Try to turn off all of the volume
            SharedPreferences prefs = getPrefs(c);
            AudioManager a = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
            setRingtoneVolume(c, a, prefs, ManageVolume.Mode.Class);
            setSystemVolume(c, a, prefs, ManageVolume.Mode.Class);
            setAlarmVolume(c, a, prefs, ManageVolume.Mode.Class);
            setMediaVolume(c, a, prefs, ManageVolume.Mode.Class);
            setNotificationVolume(c, a, prefs, ManageVolume.Mode.Class);
        } catch (Exception e) {
            sb.append("Had a problem turning down the volume").append(Global.TEXT_NEWLINE);
        }
        turnOnWiFi(c);
        try { //Try to turn off the GPS
            if (canToggleGPS(c)) turnGPSOff(c);
            else {
                Intent i = new Intent("android.location.GPS_ENABLED_CHANGE");
                i.putExtra("enabled", false);
                c.sendBroadcast(i);
            }
        } catch (Exception e) {
            //sb.append("Had a problem turning off the GPS").append(Global.TEXT_NEWLINE);
        }
        tryToKillSnapchat(c);
        KeithToast.show(sb.toString().trim(), c);
        showNotification(Mode.Class, c);
    }

    public static void inCar(Context c) {
        closeNotificationTray(c);
        turnOffWiFi(c);

        SharedPreferences prefs = getPrefs(c);
        AudioManager a = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        setRingtoneVolume(c, a, prefs, ManageVolume.Mode.Home);
        setSystemVolume(c, a, prefs, ManageVolume.Mode.Home);
        setAlarmVolume(c, a, prefs, ManageVolume.Mode.Home);
        setMediaVolume(c, a, prefs, ManageVolume.Mode.Home);
        setNotificationVolume(c, a, prefs, ManageVolume.Mode.Home);

        tryToKillSnapchat(c);
        showNotification(Mode.Car, c);
    }
}
