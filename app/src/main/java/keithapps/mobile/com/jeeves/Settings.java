package keithapps.mobile.com.jeeves;

/**
 * Settings Strings without having to call from a Context
 */
public class Settings {
    public static final String current_mode = "curr.mode",
            headset_full = "vol.setToFull", headset_pluggedIn = "headset.plugged",
            adderall_count = "adderall.count", adderall_clear = "adderall.clear";
    /**
     * Code to use to access the Shared Preferences
     */
    public static final String sharedPrefs_code = "jeeves.prefs";
    public static final String text_home = "home", text_out = "out", text_class = "class", text_add = "add";

    public class Class {
        public static final String notificationVolume = "settings_class_notificationVolume",
                ringtoneVolume = "settings_class_ringtoneVolume",
                systemVolume = "settings_class_systemVolume",
                alarmVolume = "settings_class_alarmVolume",
                mediaVolume = "settings_class_mediaVolume";
    }

    public class Out {
        public static final String notificationVolume = "settings_out_notificationVolume",
                ringtoneVolume = "settings_out_ringtoneVolume",
                systemVolume = "settings_out_systemVolume",
                alarmVolume = "settings_out_alarmVolume",
                mediaVolume = "settings_out_mediaVolume";
    }

    public class Home {
        public static final String notificationVolume = "settings_home_notificationVolume",
                ringtoneVolume = "settings_home_ringtoneVolume",
                systemVolume = "settings_home_systemVolume",
                alarmVolume = "settings_home_alarmVolume",
                mediaVolume = "settings_home_mediaVolume";
    }
}
