package keithapps.mobile.com.jeeves;

/**
 * Settings Strings without having to call from a Context
 */
public class Settings {
    public static final String current_mode = "curr.mode";
    public static final String headset_full = "vol.setToFull";
    public static final String headset_pluggedIn = "headset.plugged";
    public static final String versionCode = "version.code";
    public static final String versionName = "version.name";
    public static final String connectedToWiFi = "conn.WiFi";
    public static final String connectedToBT = "conn.BT";
    public static final String connectedToMobile = "conn.Mobile";
    /**
     * Code to use to access the Shared Preferences
     */
    public static final String sharedPrefs_code = "jeeves.prefs";
    public static final String text_home = "home", text_out = "stateC", text_class = "class", text_add = "add";
    public static final String record_log = "log.record";

    public static final String action_a_name = "action.a.n", action_b_name = "action.b.n",
            action_c_name = "action.c.n";


    public class Adderall{
        public static final String timeSince = "Adderall.timeSince";
        public static final String adderall_count = "adderall.count";
        public static final String adderall_clear = "adderall.clear";
    }

    public class B {
        public static final String notificationVolume = "settings_class_notificationVolume",
                ringtoneVolume = "settings_class_ringtoneVolume",
                systemVolume = "settings_class_systemVolume",
                alarmVolume = "settings_class_alarmVolume",
                mediaVolume = "settings_class_mediaVolume";
    }

    public class C {
        public static final String notificationVolume = "settings_out_notificationVolume",
                ringtoneVolume = "settings_out_ringtoneVolume",
                systemVolume = "settings_out_systemVolume",
                alarmVolume = "settings_out_alarmVolume",
                mediaVolume = "settings_out_mediaVolume";
    }

    public class A {
        public static final String notificationVolume = "settings_home_notificationVolume",
                ringtoneVolume = "settings_home_ringtoneVolume",
                systemVolume = "settings_home_systemVolume",
                alarmVolume = "settings_home_alarmVolume",
                mediaVolume = "settings_home_mediaVolume";
    }
}
