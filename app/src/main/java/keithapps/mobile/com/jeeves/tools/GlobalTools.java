package keithapps.mobile.com.jeeves.tools;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import keithapps.mobile.com.jeeves.BuildConfig;
import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.activities.popups.DeveloperPopup;
import keithapps.mobile.com.jeeves.activities.popups.KeithToast;

/**
 * Created by Keith on 1/17/2016.
 * Contains Global static methods and variables
 */
public class GlobalTools {

    public static boolean isJeevesRunning(Context c) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo s : manager.getRunningServices(Integer.MAX_VALUE))
            if (s.service.getClassName().contains("keithapps")) return true;
        return false;
    }

    /**
     * Is Jeeves the currently running foreground app
     *
     * @param c the callng context
     * @return true if the foreground app is one of Jeeves' children
     */
    public static boolean isJeevesActivityForeground(Context c) {
        try {
            List<ActivityManager.RunningAppProcessInfo> tasks = ((ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE))
                    .getRunningAppProcesses();
            if (tasks == null) return false;
            String mainActivity = tasks.get(0).processName;
            return mainActivity.toLowerCase().contains("keithapps");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Are you a motherfucking God?
     *
     * @param c the Calling Context
     * @return true if you are essentially perfect.
     */
    public static boolean isKeith(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE);
        return prefs.getBoolean(c.getString(R.string.settings_isKeith), false) &&
                BuildConfig.DEBUG &&
                Build.BRAND.toLowerCase().equals("samsung") &&
                Build.MODEL.toLowerCase().equals("sm-n900");
    }

    public static void makeKeith(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code,
                Context.MODE_PRIVATE);
        if (prefs.getBoolean(c.getString(R.string.settings_isKeith), false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(c.getString(R.string.settings_isKeith), false);
            editor.apply();
            KeithToast.show("You are no longer a developer.", c);
        } else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(c.getString(R.string.settings_isKeith), true);
            editor.apply();
            KeithToast.show("Hello, Keith", c);
        }
    }

    public static void turnOffVibrate(AudioManager a) {/**
     a.setRingerMode(AudioManager.RINGER_MODE_SILENT);
     a.setRingerMode(AudioManager.RINGER_MODE_SILENT);*/
    }

    public static void turnOnVibrate(Context c) {/**
     AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
     audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
     Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);//*/
    }

    public static ArrayList<View> getAllChildren(View v) {
        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            return viewArrayList;
        }
        ArrayList<View> result = new ArrayList<>();
        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));
            result.addAll(viewArrayList);
        }
        return result;
    }

    public static String getForegroundAppName(Context c) {
        try {
            return ((ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE))
                    .getRunningAppProcesses().get(0).processName;
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public static void putInClipboard(Context c, String s, String label) {
        ClipboardManager clipboard = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, s);
        clipboard.setPrimaryClip(clip);
    }

    public static void showTestPopup(Context c) {
        Intent i = new Intent(c, DeveloperPopup.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        c.startActivity(i);
    }

    public static void testMethod(Context c) {
        if (isKeith(c)) showTestPopup(c);
    }

    public static void openGoogle(Context c) {
        try {
            Intent mainLauncher = new Intent();
            mainLauncher.setAction("MAIN");
            mainLauncher.addCategory("LAUNCHER");
            PackageManager p = c.getPackageManager();
            Intent i = p.getLaunchIntentForPackage("com.google.android.googlequicksearchbox");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(i);
        } catch (Exception e) {
            KeithToast.show("Error opening Google: " + e.getLocalizedMessage(), c);
        }
    }

}
