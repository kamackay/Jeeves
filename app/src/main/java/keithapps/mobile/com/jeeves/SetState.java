package keithapps.mobile.com.jeeves;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.WifiManager;

import keithapps.mobile.com.jeeves.ManageVolume.Mode;

import static keithapps.mobile.com.jeeves.Global.closeNotificationTray;
import static keithapps.mobile.com.jeeves.Global.getPrefs;
import static keithapps.mobile.com.jeeves.Global.isVibrateOn;
import static keithapps.mobile.com.jeeves.Global.tryToKillSnapchat;
import static keithapps.mobile.com.jeeves.Global.turnOffBluetooth;
import static keithapps.mobile.com.jeeves.Global.turnOffVibrate;
import static keithapps.mobile.com.jeeves.Global.turnOffWiFi;
import static keithapps.mobile.com.jeeves.Global.turnOnBluetooth;
import static keithapps.mobile.com.jeeves.Global.turnOnVibrate;
import static keithapps.mobile.com.jeeves.Global.turnOnWiFi;
import static keithapps.mobile.com.jeeves.MainService.showNotification;
import static keithapps.mobile.com.jeeves.ManageVolume.setAlarmVolume;
import static keithapps.mobile.com.jeeves.ManageVolume.setMediaVolume;
import static keithapps.mobile.com.jeeves.ManageVolume.setNotificationVolume;
import static keithapps.mobile.com.jeeves.ManageVolume.setRingtoneVolume;
import static keithapps.mobile.com.jeeves.ManageVolume.setSystemVolume;
import static keithapps.mobile.com.jeeves.ModeChangeView.SELECTED_LEAVE;
import static keithapps.mobile.com.jeeves.ModeChangeView.SELECTED_OFF;
import static keithapps.mobile.com.jeeves.ModeChangeView.SELECTED_ON;

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
        SharedPreferences prefs = getPrefs(c);
        AudioManager a = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        setRingtoneVolume(a, prefs, ManageVolume.Mode.Home);
        setSystemVolume(a, prefs, ManageVolume.Mode.Home);
        setAlarmVolume(a, prefs, ManageVolume.Mode.Home);
        setMediaVolume(a, prefs, ManageVolume.Mode.Home);
        setNotificationVolume(a, prefs, ManageVolume.Mode.Home);
        if (!isVibrateOn(a)) turnOnVibrate(a);
        int wifiAction = prefs.getInt(c.getString(R.string.settings_out_wifiAction),
                SELECTED_LEAVE), bluetoothAction = prefs.getInt(c.getString(
                R.string.settings_out_bluetoothAction), SELECTED_LEAVE);
        if (wifiAction == SELECTED_ON) turnOnWiFi(c);
        else if (wifiAction == SELECTED_OFF) turnOffWiFi(c);
        if (bluetoothAction == SELECTED_ON) turnOnBluetooth(c);
        else if (bluetoothAction == SELECTED_OFF) turnOffBluetooth(c);
        tryToKillSnapchat(c);
        showNotification(Mode.Out, c);
    }

    /**
     * Call when at home
     *
     * @param c the calling context
     */
    public static void atHome(Context c) {
        AudioManager a = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        closeNotificationTray(c);
        SharedPreferences prefs = getPrefs(c);
        setRingtoneVolume(a, prefs, ManageVolume.Mode.Home);
        setSystemVolume(a, prefs, ManageVolume.Mode.Home);
        setAlarmVolume(a, prefs, ManageVolume.Mode.Home);
        setMediaVolume(a, prefs, ManageVolume.Mode.Home);
        setNotificationVolume(a, prefs, ManageVolume.Mode.Home);
        if (!isVibrateOn(a)) turnOnVibrate(a);
        int wifiAction = prefs.getInt(c.getString(R.string.settings_home_wifiAction),
                SELECTED_LEAVE), bluetoothAction = prefs.getInt(c.getString(
                R.string.settings_home_bluetoothAction), SELECTED_LEAVE);
        if (wifiAction == SELECTED_ON) {
            WifiManager wifiManager = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(false);
            wifiManager.setWifiEnabled(true);
        } else if (wifiAction == SELECTED_OFF) turnOffWiFi(c);
        if (bluetoothAction == SELECTED_ON) turnOnBluetooth(c);
        else if (bluetoothAction == SELECTED_OFF) turnOffBluetooth(c);
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
        setRingtoneVolume(a, prefs, ManageVolume.Mode.Class);
        setSystemVolume(a, prefs, ManageVolume.Mode.Class);
        setAlarmVolume(a, prefs, ManageVolume.Mode.Class);
        setMediaVolume(a, prefs, ManageVolume.Mode.Class);
        setNotificationVolume(a, prefs, ManageVolume.Mode.Class);
        if (isVibrateOn(a)) turnOffVibrate(a);
        int wifiAction = prefs.getInt(c.getString(R.string.settings_class_wifiAction),
                SELECTED_LEAVE), bluetoothAction = prefs.getInt(c.getString(
                R.string.settings_class_bluetoothAction), SELECTED_LEAVE);
        if (wifiAction == SELECTED_ON) turnOnWiFi(c);
        else if (wifiAction == SELECTED_OFF) turnOffWiFi(c);
        if (bluetoothAction == SELECTED_ON) turnOnBluetooth(c);
        else if (bluetoothAction == SELECTED_OFF) turnOffBluetooth(c);
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
        setRingtoneVolume(a, prefs, ManageVolume.Mode.Home);
        setSystemVolume(a, prefs, ManageVolume.Mode.Home);
        setAlarmVolume(a, prefs, ManageVolume.Mode.Home);
        setMediaVolume(a, prefs, ManageVolume.Mode.Home);
        setNotificationVolume(a, prefs, ManageVolume.Mode.Home);
        tryToKillSnapchat(c);
        showNotification(Mode.Car, c);
    }
}
