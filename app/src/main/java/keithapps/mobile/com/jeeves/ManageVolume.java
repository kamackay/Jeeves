package keithapps.mobile.com.jeeves;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

/**
 * Created by Keith on 1/19/2016.
 * Holds a bunch of methods to help manage the volume
 */
public class ManageVolume {

    public static void setSystemVolume(Context c, AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.Class)
            setSystemVolume(a, prefs.getInt(c.getString(R.string.settings_class_systemVolume), 0));
        else if (mode == Mode.Home)
            setSystemVolume(a, prefs.getInt(c.getString(R.string.settings_home_systemVolume), 5));
        else if (mode == Mode.Out)
            setSystemVolume(a, prefs.getInt(c.getString(R.string.settings_out_systemVolume), 5));
    }

    public static void setSystemVolume(Context c, int mode) {
        if (mode == Mode.Class)
            setSystemVolume((AudioManager) c.getSystemService(Context.AUDIO_SERVICE),
                    c.getSharedPreferences(c.getString(R.string.sharedPrefrences_code),
                            Context.MODE_PRIVATE).getInt(c.getString(R.string.settings_class_systemVolume), 0));
        else if (mode == Mode.Home)
            setSystemVolume((AudioManager) c.getSystemService(Context.AUDIO_SERVICE),
                    c.getSharedPreferences(c.getString(R.string.sharedPrefrences_code),
                            Context.MODE_PRIVATE).getInt(c.getString(R.string.settings_home_systemVolume), 1));
    }

    public static void setSystemVolume(AudioManager a, int setTo) {
        if (a == null) return;
        a.setStreamVolume(AudioManager.STREAM_SYSTEM,
                (int) (a.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) * (setTo * .1)),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public static void setNotificationVolume(Context c, AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.Class)
            setNotificationVolume(a, prefs.getInt(c.getString(R.string.settings_class_notificationVolume), 0));
        else if (mode == Mode.Home)
            setNotificationVolume(a, prefs.getInt(c.getString(R.string.settings_home_notificationVolume), 5));
        else if (mode == Mode.Out)
            setNotificationVolume(a, prefs.getInt(c.getString(R.string.settings_out_notificationVolume), 5));
    }

    public static void setNotificationVolume(Context c, int mode) {
        if (mode == Mode.Class)
            setNotificationVolume((AudioManager) c.getSystemService(Context.AUDIO_SERVICE),
                    c.getSharedPreferences(c.getString(R.string.sharedPrefrences_code),
                            Context.MODE_PRIVATE).getInt(c.getString(R.string.settings_class_notificationVolume), 0));
        else if (mode == Mode.Home)
            setNotificationVolume((AudioManager) c.getSystemService(Context.AUDIO_SERVICE),
                    c.getSharedPreferences(c.getString(R.string.sharedPrefrences_code),
                            Context.MODE_PRIVATE).getInt(c.getString(R.string.settings_home_notificationVolume), 1));
    }

    public static void setNotificationVolume(AudioManager a, int setTo) {
        if (a == null) return;
        a.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                (int) (a.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) * (setTo * .1)),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public static void setAlarmVolume(Context c, AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.Class)
            setAlarmVolume(a, prefs.getInt(c.getString(R.string.settings_class_alarmVolume), 0));
        else if (mode == Mode.Home)
            setAlarmVolume(a, prefs.getInt(c.getString(R.string.settings_home_alarmVolume), 5));
        else if (mode == Mode.Out)
            setAlarmVolume(a, prefs.getInt(c.getString(R.string.settings_out_alarmVolume), 5));
    }

    public static void setAlarmVolume(Context c, int mode) {
        if (mode == Mode.Class)
            setAlarmVolume((AudioManager) c.getSystemService(Context.AUDIO_SERVICE),
                    c.getSharedPreferences(c.getString(R.string.sharedPrefrences_code),
                            Context.MODE_PRIVATE).getInt(c.getString(R.string.settings_class_alarmVolume), 0));
        else if (mode == Mode.Home)
            setAlarmVolume((AudioManager) c.getSystemService(Context.AUDIO_SERVICE),
                    c.getSharedPreferences(c.getString(R.string.sharedPrefrences_code),
                            Context.MODE_PRIVATE).getInt(c.getString(R.string.settings_home_alarmVolume), 10));
    }

    public static void setAlarmVolume(AudioManager a, int setTo) {
        if (a == null) return;
        a.setStreamVolume(AudioManager.STREAM_ALARM,
                (int) (a.getStreamMaxVolume(AudioManager.STREAM_ALARM) * (setTo * .1)),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public static void setMediaVolume(Context c, AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.Class)
            setMediaVolume(a, prefs.getInt(c.getString(R.string.settings_class_mediaVolume), 0));
        else if (mode == Mode.Home)
            setMediaVolume(a, prefs.getInt(c.getString(R.string.settings_home_mediaVolume), 5));
        else if (mode == Mode.Out)
            setMediaVolume(a, prefs.getInt(c.getString(R.string.settings_out_mediaVolume), 5));
    }

    public static void setMediaVolume(Context c, int mode) {
        if (mode == Mode.Class)
            setMediaVolume((AudioManager) c.getSystemService(Context.AUDIO_SERVICE),
                    c.getSharedPreferences(c.getString(R.string.sharedPrefrences_code),
                            Context.MODE_PRIVATE).getInt(c.getString(R.string.settings_class_mediaVolume), 0));
        else if (mode == Mode.Home)
            setMediaVolume((AudioManager) c.getSystemService(Context.AUDIO_SERVICE),
                    c.getSharedPreferences(c.getString(R.string.sharedPrefrences_code),
                            Context.MODE_PRIVATE).getInt(c.getString(R.string.settings_home_mediaVolume), 5));
    }

    public static void setMediaVolume(AudioManager a, int setTo) {
        if (a == null) return;
        a.setStreamVolume(AudioManager.STREAM_MUSIC,
                (int) (a.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * (setTo * .1)),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public static void setRingtoneVolume(Context c, AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.Class)
            setRingtoneVolume(a, prefs.getInt(c.getString(R.string.settings_class_ringtoneVolume), 0));
        else if (mode == Mode.Home)
            setRingtoneVolume(a, prefs.getInt(c.getString(R.string.settings_home_ringtoneVolume), 5));
        else if (mode == Mode.Out)
            setRingtoneVolume(a, prefs.getInt(c.getString(R.string.settings_out_ringtoneVolume), 10));
    }

    public static void setRingtoneVolume(Context c, int mode) {
        if (mode == Mode.Class)
            setRingtoneVolume((AudioManager) c.getSystemService(Context.AUDIO_SERVICE),
                    c.getSharedPreferences(c.getString(R.string.sharedPrefrences_code),
                            Context.MODE_PRIVATE).getInt(c.getString(R.string.settings_class_ringtoneVolume), 0));
        else if (mode == Mode.Home)
            setRingtoneVolume((AudioManager) c.getSystemService(Context.AUDIO_SERVICE),
                    c.getSharedPreferences(c.getString(R.string.sharedPrefrences_code),
                            Context.MODE_PRIVATE).getInt(c.getString(R.string.settings_home_ringtoneVolume), 1));
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
