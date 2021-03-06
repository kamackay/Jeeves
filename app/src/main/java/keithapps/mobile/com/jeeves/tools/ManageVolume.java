package keithapps.mobile.com.jeeves.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

import static keithapps.mobile.com.jeeves.services.MainService.updateNotification;

/**
 * Created by Keith on 1/19/2016.
 * Holds a bunch of methods to help manage the volume
 */
public class ManageVolume {

    public static void unlockFullVolume(Context c) {
        SharedPreferences.Editor edit = c.getSharedPreferences(Settings.sharedPrefs_code,
                Context.MODE_PRIVATE).edit();
        edit.putBoolean(Settings.headset_full, false);
        edit.apply();
        SetState.setState(c);
        //updateNotification(c);//This is probably redundant since SetState calls it
    }

    public static void lockFullVolume(Context c) {
        final AudioManager audioManager =
                (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        SharedPreferences.Editor edit = c.getSharedPreferences(Settings.sharedPrefs_code,
                Context.MODE_PRIVATE).edit();
        edit.putBoolean(Settings.headset_full, true);
        edit.apply();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                            AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                } catch (Exception e) {
                    //Don't do anything with this
                }
            }
        }).start();
        Log.writeToLog("Headset Popup - Full", c);
        updateNotification(c);
    }

    public static void setSystemVolume(AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.B)
            setSystemVolume(a, prefs.getInt(Settings.B.systemVolume, 0));
        else if (mode == Mode.A)
            setSystemVolume(a, prefs.getInt(Settings.A.systemVolume, 5));
        else if (mode == Mode.C)
            setSystemVolume(a, prefs.getInt(Settings.C.systemVolume, 5));
        else if (mode == Mode.D)
            setSystemVolume(a, prefs.getInt(Settings.D.systemVolume, 5));
    }

    public static void setSystemVolume(AudioManager a, int setTo) {
        if (a == null) return;
        a.setStreamVolume(AudioManager.STREAM_SYSTEM,
                (int) (a.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) * (setTo * .1)),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public static void setNotificationVolume(AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.B)
            setNotificationVolume(a, prefs.getInt(Settings.B.notificationVolume, 0));
        else if (mode == Mode.A)
            setNotificationVolume(a, prefs.getInt(Settings.A.notificationVolume, 5));
        else if (mode == Mode.C)
            setNotificationVolume(a, prefs.getInt(Settings.C.notificationVolume, 5));
        else if (mode == Mode.D)
            setNotificationVolume(a, prefs.getInt(Settings.D.notificationVolume, 5));
    }

    public static void setNotificationVolume(AudioManager a, int setTo) {
        if (a == null) return;
        a.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                (int) (a.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) * (setTo * .1)),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public static void setAlarmVolume(AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.B)
            setAlarmVolume(a, prefs.getInt(Settings.B.alarmVolume, 0));
        else if (mode == Mode.A)
            setAlarmVolume(a, prefs.getInt(Settings.A.alarmVolume, 5));
        else if (mode == Mode.C)
            setAlarmVolume(a, prefs.getInt(Settings.C.alarmVolume, 5));
        else if (mode == Mode.D)
            setAlarmVolume(a, prefs.getInt(Settings.D.alarmVolume, 5));
    }

    public static void setAlarmVolume(AudioManager a, int setTo) {
        if (a == null) return;
        a.setStreamVolume(AudioManager.STREAM_ALARM,
                (int) (a.getStreamMaxVolume(AudioManager.STREAM_ALARM) * (setTo * .1)),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public static void setMediaVolume(AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.B)
            setMediaVolume(a, prefs.getInt(Settings.B.mediaVolume, 0));
        else if (mode == Mode.A)
            setMediaVolume(a, prefs.getInt(Settings.A.mediaVolume, 5));
        else if (mode == Mode.C)
            setMediaVolume(a, prefs.getInt(Settings.C.mediaVolume, 5));
        else if (mode == Mode.D)
            setMediaVolume(a, prefs.getInt(Settings.D.mediaVolume, 5));
    }

    public static void setMediaVolume(AudioManager a, int setTo) {
        if (a == null) return;
        a.setStreamVolume(AudioManager.STREAM_MUSIC,
                (int) (a.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * (setTo * .1)),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public static void setRingtoneVolume(AudioManager a, SharedPreferences prefs, int mode) {
        if (mode == Mode.B)
            setRingtoneVolume(a, prefs.getInt(Settings.B.ringtoneVolume, 0));
        else if (mode == Mode.A)
            setRingtoneVolume(a, prefs.getInt(Settings.A.ringtoneVolume, 5));
        else if (mode == Mode.C)
            setRingtoneVolume(a, prefs.getInt(Settings.C.ringtoneVolume, 10));
        else if (mode == Mode.D)
            setRingtoneVolume(a, prefs.getInt(Settings.D.ringtoneVolume, 5));
    }

    public static void setRingtoneVolume(AudioManager a, int setTo) {
        if (a == null) return;
        a.setStreamVolume(AudioManager.STREAM_RING,
                (int) (a.getStreamMaxVolume(AudioManager.STREAM_RING) * (setTo * .1)),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public class Mode {
        public static final int B = 0;
        public static final int D = 1;
        public static final int C = 2;
        public static final int A = 3;
    }
}
