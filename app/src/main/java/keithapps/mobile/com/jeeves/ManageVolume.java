package keithapps.mobile.com.jeeves;

import android.content.SharedPreferences;
import android.media.AudioManager;

/**
 * Created by Keith on 1/19/2016.
 * Holds a bunch of methods to help manage the volume
 */
public class ManageVolume {

    public static void setSystemVolume(AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.Class)
            setSystemVolume(a, prefs.getInt(Global.SETTINGS.CLASS.systemVolume, 0));
        else if (mode == Mode.Home)
            setSystemVolume(a, prefs.getInt(Global.SETTINGS.HOME.systemVolume, 5));
        else if (mode == Mode.Out)
            setSystemVolume(a, prefs.getInt(Global.SETTINGS.OUT.systemVolume, 5));
    }

    public static void setSystemVolume(AudioManager a, int setTo) {
        if (a == null) return;
        a.setStreamVolume(AudioManager.STREAM_SYSTEM,
                (int) (a.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) * (setTo * .1)),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public static void setNotificationVolume(AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.Class)
            setNotificationVolume(a, prefs.getInt(Global.SETTINGS.CLASS.notificationVolume, 0));
        else if (mode == Mode.Home)
            setNotificationVolume(a, prefs.getInt(Global.SETTINGS.HOME.notificationVolume, 5));
        else if (mode == Mode.Out)
            setNotificationVolume(a, prefs.getInt(Global.SETTINGS.OUT.notificationVolume, 5));
    }

    public static void setNotificationVolume(AudioManager a, int setTo) {
        if (a == null) return;
        a.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                (int) (a.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) * (setTo * .1)),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public static void setAlarmVolume(AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.Class)
            setAlarmVolume(a, prefs.getInt(Global.SETTINGS.CLASS.alarmVolume, 0));
        else if (mode == Mode.Home)
            setAlarmVolume(a, prefs.getInt(Global.SETTINGS.HOME.alarmVolume, 5));
        else if (mode == Mode.Out)
            setAlarmVolume(a, prefs.getInt(Global.SETTINGS.OUT.alarmVolume, 5));
    }

    public static void setAlarmVolume(AudioManager a, int setTo) {
        if (a == null) return;
        a.setStreamVolume(AudioManager.STREAM_ALARM,
                (int) (a.getStreamMaxVolume(AudioManager.STREAM_ALARM) * (setTo * .1)),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public static void setMediaVolume(AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.Class)
            setMediaVolume(a, prefs.getInt(Global.SETTINGS.CLASS.mediaVolume, 0));
        else if (mode == Mode.Home)
            setMediaVolume(a, prefs.getInt(Global.SETTINGS.HOME.mediaVolume, 5));
        else if (mode == Mode.Out)
            setMediaVolume(a, prefs.getInt(Global.SETTINGS.OUT.mediaVolume, 5));
    }

    public static void setMediaVolume(AudioManager a, int setTo) {
        if (a == null) return;
        a.setStreamVolume(AudioManager.STREAM_MUSIC,
                (int) (a.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * (setTo * .1)),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public static void setRingtoneVolume(AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.Class)
            setRingtoneVolume(a, prefs.getInt(Global.SETTINGS.CLASS.ringtoneVolume, 0));
        else if (mode == Mode.Home)
            setRingtoneVolume(a, prefs.getInt(Global.SETTINGS.HOME.ringtoneVolume, 5));
        else if (mode == Mode.Out)
            setRingtoneVolume(a, prefs.getInt(Global.SETTINGS.OUT.ringtoneVolume, 10));
    }

    public static void setRingtoneVolume(AudioManager a, int setTo) {
        if (a == null) return;
        a.setStreamVolume(AudioManager.STREAM_RING,
                (int) (a.getStreamMaxVolume(AudioManager.STREAM_RING) * (setTo * .1)),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public class Mode {
        public static final int Class = 0;
        public static final int Car = 1;
        public static final int Out = 2;
        public static final int Home = 3;
    }
}
