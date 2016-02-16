package keithapps.mobile.com.jeeves.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.WifiManager;

import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.tools.ManageVolume.Mode;

import static keithapps.mobile.com.jeeves.MainService.showNotification;
import static keithapps.mobile.com.jeeves.tools.Global.closeNotificationTray;
import static keithapps.mobile.com.jeeves.tools.Global.getPrefs;
import static keithapps.mobile.com.jeeves.tools.Global.turnOffBluetooth;
import static keithapps.mobile.com.jeeves.tools.Global.turnOffWiFi;
import static keithapps.mobile.com.jeeves.tools.Global.turnOnBluetooth;
import static keithapps.mobile.com.jeeves.tools.Global.turnOnWiFi;
import static keithapps.mobile.com.jeeves.tools.Global.writeToLog;
import static keithapps.mobile.com.jeeves.tools.ManageVolume.setAlarmVolume;
import static keithapps.mobile.com.jeeves.tools.ManageVolume.setMediaVolume;
import static keithapps.mobile.com.jeeves.tools.ManageVolume.setNotificationVolume;
import static keithapps.mobile.com.jeeves.tools.ManageVolume.setRingtoneVolume;
import static keithapps.mobile.com.jeeves.tools.ManageVolume.setSystemVolume;
import static keithapps.mobile.com.jeeves.views.ModeChangeView.SELECTED_LEAVE;
import static keithapps.mobile.com.jeeves.views.ModeChangeView.SELECTED_OFF;
import static keithapps.mobile.com.jeeves.views.ModeChangeView.SELECTED_ON;
import static keithapps.mobile.com.jeeves.views.ModeChangeView.SELECTED_REBOOT;

/**
 * Created by Keith on 1/19/2016.
 * What to do when entering each state
 */
public class SetState {
    /**
     * Call for changing to state A
     *
     * @param c the calling context
     */
    public static void stateA(Context c) {
        try {
            SharedPreferences prefs = getPrefs(c);
            writeToLog("Mode Set to \"" + prefs.getString(Settings.action_a_name,
                    c.getString(R.string.text_home)) + "\"", c);
            AudioManager a = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
            closeNotificationTray(c);
            setRingtoneVolume(a, prefs, ManageVolume.Mode.A);
            setSystemVolume(a, prefs, ManageVolume.Mode.A);
            setAlarmVolume(a, prefs, ManageVolume.Mode.A);
            setMediaVolume(a, prefs, ManageVolume.Mode.A);
            setNotificationVolume(a, prefs, ManageVolume.Mode.A);
            int wifiAction = prefs.getInt(c.getString(R.string.settings_a_wifiAction),
                    SELECTED_LEAVE), bluetoothAction = prefs.getInt(c.getString(
                    R.string.settings_a_bluetoothAction), SELECTED_LEAVE);
            if (wifiAction == SELECTED_ON) turnOnWiFi(c);
            if (wifiAction == SELECTED_REBOOT) {
                WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
                if (wifiManager.isWifiEnabled()) wifiManager.setWifiEnabled(false);
                wifiManager.setWifiEnabled(true);
            } else if (wifiAction == SELECTED_OFF) turnOffWiFi(c);
            if (bluetoothAction == SELECTED_ON) turnOnBluetooth(c);
            else if (bluetoothAction == SELECTED_OFF) turnOffBluetooth(c);
            SharedPreferences.Editor prefsEdit = prefs.edit();
            prefsEdit.putInt(Settings.current_mode, Mode.A);
            prefsEdit.apply();
            showNotification(Mode.A, c);
        } catch (Exception e) {
            writeToLog(e.getLocalizedMessage(), c);
        }
    }

    /**
     * Call for changing to state B
     *
     * @param c the calling context
     */
    public static void stateB(Context c) {
        try {
            SharedPreferences prefs = getPrefs(c);
            writeToLog("Mode Set to \"" + prefs.getString(Settings.action_b_name,
                    c.getString(R.string.text_class)) + "\"", c);
            closeNotificationTray(c);
            AudioManager a = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
            setRingtoneVolume(a, prefs, ManageVolume.Mode.B);
            setSystemVolume(a, prefs, ManageVolume.Mode.B);
            setAlarmVolume(a, prefs, ManageVolume.Mode.B);
            setMediaVolume(a, prefs, ManageVolume.Mode.B);
            setNotificationVolume(a, prefs, ManageVolume.Mode.B);
            int wifiAction = prefs.getInt(c.getString(R.string.settings_b_wifiAction),
                    SELECTED_LEAVE),
                    bluetoothAction = prefs.getInt(c.getString(
                            R.string.settings_b_bluetoothAction), SELECTED_LEAVE);
            if (wifiAction == SELECTED_REBOOT) {
                WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
                if (wifiManager.isWifiEnabled()) wifiManager.setWifiEnabled(false);
                wifiManager.setWifiEnabled(true);
            } else if (wifiAction == SELECTED_ON) turnOnWiFi(c);
            else if (wifiAction == SELECTED_OFF) turnOffWiFi(c);
            if (bluetoothAction == SELECTED_ON) turnOnBluetooth(c);
            else if (bluetoothAction == SELECTED_OFF) turnOffBluetooth(c);
            SharedPreferences.Editor prefsEdit = prefs.edit();
            prefsEdit.putInt(Settings.current_mode, Mode.B);
            prefsEdit.apply();
            showNotification(Mode.B, c);
        } catch (Exception e) {
            writeToLog(e.getLocalizedMessage(), c);
        }
    }

    /**
     * Call for changing to state C
     *
     * @param c the calling context
     */
    public static void stateC(Context c) {
        try {
            SharedPreferences prefs = getPrefs(c);
            writeToLog("Mode Set to \"" + prefs.getString(Settings.action_c_name,
                    c.getString(R.string.text_out)) + "\"", c);
            closeNotificationTray(c);
            AudioManager a = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
            setRingtoneVolume(a, prefs, ManageVolume.Mode.A);
            setSystemVolume(a, prefs, ManageVolume.Mode.A);
            setAlarmVolume(a, prefs, ManageVolume.Mode.A);
            setMediaVolume(a, prefs, ManageVolume.Mode.A);
            setNotificationVolume(a, prefs, ManageVolume.Mode.A);
            int wifiAction = prefs.getInt(c.getString(R.string.settings_c_wifiAction),
                    SELECTED_LEAVE), bluetoothAction = prefs.getInt(c.getString(
                    R.string.settings_c_bluetoothAction), SELECTED_LEAVE);
            if (wifiAction == SELECTED_REBOOT) {
                WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
                if (wifiManager.isWifiEnabled()) wifiManager.setWifiEnabled(false);
                wifiManager.setWifiEnabled(true);
            } else if (wifiAction == SELECTED_ON) turnOnWiFi(c);
            else if (wifiAction == SELECTED_OFF) turnOffWiFi(c);
            if (bluetoothAction == SELECTED_ON) turnOnBluetooth(c);
            else if (bluetoothAction == SELECTED_OFF) turnOffBluetooth(c);
            //tryToKillSnapchat(c);
            SharedPreferences.Editor prefsEdit = prefs.edit();
            prefsEdit.putInt(Settings.current_mode, Mode.C);
            prefsEdit.apply();
            showNotification(Mode.C, c);
        } catch (Exception e) {
            writeToLog(e.getLocalizedMessage(), c);
        }
    }

    /**
     * Call for changing to state B
     *
     * @param c the calling context
     */
    public static void stateD(Context c) {
        try {
            SharedPreferences prefs = getPrefs(c);
            writeToLog("Mode Set to \"" + prefs.getString(Settings.action_d_name,
                    c.getString(R.string.text_class)) + "\"", c);
            closeNotificationTray(c);
            AudioManager a = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
            setRingtoneVolume(a, prefs, ManageVolume.Mode.D);
            setSystemVolume(a, prefs, ManageVolume.Mode.D);
            setAlarmVolume(a, prefs, ManageVolume.Mode.D);
            setMediaVolume(a, prefs, ManageVolume.Mode.D);
            setNotificationVolume(a, prefs, ManageVolume.Mode.D);
            int wifiAction = prefs.getInt(c.getString(R.string.settings_d_wifiAction),
                    SELECTED_LEAVE),
                    bluetoothAction = prefs.getInt(c.getString(
                            R.string.settings_d_bluetoothAction), SELECTED_LEAVE);
            if (wifiAction == SELECTED_REBOOT) {
                WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
                if (wifiManager.isWifiEnabled()) wifiManager.setWifiEnabled(false);
                wifiManager.setWifiEnabled(true);
            } else if (wifiAction == SELECTED_ON) turnOnWiFi(c);
            else if (wifiAction == SELECTED_OFF) turnOffWiFi(c);
            if (bluetoothAction == SELECTED_ON) turnOnBluetooth(c);
            else if (bluetoothAction == SELECTED_OFF) turnOffBluetooth(c);
            SharedPreferences.Editor prefsEdit = prefs.edit();
            prefsEdit.putInt(Settings.current_mode, Mode.D);
            prefsEdit.apply();
            showNotification(Mode.D, c);
        } catch (Exception e) {
            writeToLog(e.getLocalizedMessage(), c);
        }
    }
}
