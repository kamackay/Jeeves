package keithapps.mobile.com.jeeves;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

/**
 * Created by Keith on 2/2/2016.
 * Listens for volume changes
 */
public class SettingsContentObserver extends ContentObserver {
    private AudioManager audioManager;

    public SettingsContentObserver(Context context, Handler handler) {
        super(handler);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return false;
    }

    @Override
    public void onChange(boolean selfChange) {
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
}
