package keithapps.mobile.com.jeeves.listeners;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

import java.util.Locale;

import keithapps.mobile.com.jeeves.services.MainService;
import keithapps.mobile.com.jeeves.tools.Settings;

import static keithapps.mobile.com.jeeves.tools.Email.emailException;
import static keithapps.mobile.com.jeeves.tools.Log.writeToLog;
import static keithapps.mobile.com.jeeves.tools.SystemTools.getPrefs;

/**
 * Created by Keith on 2/5/2016.
 * Listens for the volume to change
 */
public class VolumeChangeListener extends ContentObserver {
    private MainService mainService;
    private int mediaVol;

    public VolumeChangeListener(MainService mainService, Handler handler) {
        super(handler);
        this.mainService = mainService;
        mediaVol = ((AudioManager) mainService.getSystemService(Context.AUDIO_SERVICE))
                .getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return false;
    }

    @Override
    public void onChange(boolean selfChange) {
        final Context c = mainService.getApplicationContext();
        final AudioManager aMan = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final int newVol = aMan.getStreamVolume(AudioManager.STREAM_MUSIC),
                music_max = aMan.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        try {
            if (newVol != mediaVol) {
                boolean full = c.getSharedPreferences(Settings.sharedPrefs_code,
                        Context.MODE_PRIVATE).getBoolean(Settings.headset_full, false);
                if (full && Math.abs(newVol - mediaVol) == 1) {
                    writeToLog("Volume Button Press", c, true);
                    SharedPreferences.Editor edit = c.getSharedPreferences(Settings.sharedPrefs_code,
                            Context.MODE_PRIVATE).edit();
                    edit.putBoolean(Settings.headset_full, false);
                    edit.apply();
                    writeToLog("Stop Pushing Media Vol to Full", c, true);
                    full = false;
                }
                if (full && mediaVol != music_max) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(200);
                                aMan.setStreamVolume(AudioManager.STREAM_MUSIC,
                                        music_max, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                                writeToLog("Media Vol Pushed Back To Full", c);
                            } catch (Exception e) {
                                writeToLog(e.getLocalizedMessage(), c);
                            }
                        }
                    }).start();
                } else
                    writeToLog(String.format(Locale.getDefault(), "Media Vol set to %d out of %d (was %d)", newVol,
                            aMan.getStreamMaxVolume(AudioManager.STREAM_MUSIC), mediaVol), c, true);
                mediaVol = newVol;
            }
            if (getPrefs(c).getBoolean(Settings.headset_pluggedIn, false) &&
                    aMan.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0)
                aMan.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        } catch (Exception e) {
            emailException("Error in Volume Change Listener", c, e);
        }
    }
}
