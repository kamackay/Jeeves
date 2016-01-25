package keithapps.mobile.com.jeeves;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;

import keithapps.mobile.com.jeeves.ManageVolume.Mode;

import static keithapps.mobile.com.jeeves.Global.canToggleGPS;
import static keithapps.mobile.com.jeeves.Global.closeNotificationTray;
import static keithapps.mobile.com.jeeves.Global.getPrefs;
import static keithapps.mobile.com.jeeves.Global.isVibrateOn;
import static keithapps.mobile.com.jeeves.Global.tryToKillSnapchat;
import static keithapps.mobile.com.jeeves.Global.turnGPSOff;
import static keithapps.mobile.com.jeeves.Global.turnOffVibrate;
import static keithapps.mobile.com.jeeves.Global.turnOffWiFi;
import static keithapps.mobile.com.jeeves.Global.turnOnNFC;
import static keithapps.mobile.com.jeeves.Global.turnOnVibrate;
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
        closeNotificationTray(c);
        turnOffWiFi(c); //Turn off WiFi
        SharedPreferences prefs = getPrefs(c);
        AudioManager a = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        setRingtoneVolume(c, a, prefs, ManageVolume.Mode.Home);
        setSystemVolume(c, ManageVolume.Mode.Home);
        setAlarmVolume(c, ManageVolume.Mode.Home);
        setMediaVolume(c, ManageVolume.Mode.Home);
        setNotificationVolume(c, ManageVolume.Mode.Home);
        if (!isVibrateOn(a)) turnOnVibrate(a);
        //turnOnBluetooth(c);
        tryToKillSnapchat(c);
        showNotification(Mode.Out, c);
    }

    /**
     * Call when at home
     *
     * @param c the calling context
     */
    public static void atHome(Context c) {
        closeNotificationTray(c);
        AudioManager a = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        SharedPreferences prefs = getPrefs(c);
        setRingtoneVolume(c, a, prefs, ManageVolume.Mode.Home);
        setSystemVolume(c, a, prefs, ManageVolume.Mode.Home);
        setAlarmVolume(c, a, prefs, ManageVolume.Mode.Home);
        setMediaVolume(c, a, prefs, ManageVolume.Mode.Home);
        setNotificationVolume(c, a, prefs, ManageVolume.Mode.Home);
        if (!isVibrateOn(a)) turnOnVibrate(a);
        turnOnWiFi(c);
        tryToKillSnapchat(c);
        showNotification(Mode.Home, c);
    }

    /**
     * Call when in class is pressed
     *
     * @param c the calling context
     */
    public static void inClass(Context c) {
        closeNotificationTray(c);
        SharedPreferences prefs = getPrefs(c);
        AudioManager a = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        setRingtoneVolume(c, a, prefs, ManageVolume.Mode.Class);
        setSystemVolume(c, a, prefs, ManageVolume.Mode.Class);
        setAlarmVolume(c, a, prefs, ManageVolume.Mode.Class);
        setMediaVolume(c, a, prefs, ManageVolume.Mode.Class);
        setNotificationVolume(c, a, prefs, ManageVolume.Mode.Class);
        if (isVibrateOn(a)) turnOffVibrate(a);
        turnOnWiFi(c);
        try { //Try to turn off the GPS
            if (canToggleGPS(c)) turnGPSOff(c);
            else {
                Intent i = new Intent("android.location.GPS_ENABLED_CHANGE");
                i.putExtra("enabled", false);
                c.sendBroadcast(i);
            }
        } catch (Exception e) {
            //Everything's Good
        }
        turnOnNFC(c);
        tryToKillSnapchat(c);
        showNotification(Mode.Class, c);
    }

    /**
     * When you get into the car
     *
     * @param c the calling context
     */
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
