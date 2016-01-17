package keithapps.mobile.com.jeeves;

import static keithapps.mobile.com.jeeves.Global.canToggleGPS;
import static keithapps.mobile.com.jeeves.Global.turnGPSOff;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.RemoteViews;

public class MainService extends Service {
    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 01,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(android.R.color.transparent);
        builder.setOngoing(true);
        builder.setAutoCancel(false);
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
        contentView.setOnClickPendingIntent(R.id.notification_button1,
                PendingIntent.getBroadcast(this, 0,
                        new Intent(this, HomeButtonListener.class), 0));
        contentView.setOnClickPendingIntent(R.id.notification_button2,
                PendingIntent.getBroadcast(this, 0,
                        new Intent(this, ClassButtonListener.class), 0));
        builder.setContent(contentView);
        Notification notification = builder.build();
        NotificationManager notificationManger =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManger.notify(992944, notification);
    }

    public static class HomeButtonListener extends BroadcastReceiver {
        /**
         * Listener for onClick for the Home button
         *
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            KeithToast.show("Welcome Home, sir", context);
            AudioManager audioManager =
                    (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_RING,
                    (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_RING) * .25),
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
        }
    }

    public static class ClassButtonListener extends BroadcastReceiver {
        /**
         * Listener for onClick for the Class button
         *
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            StringBuilder sb = new StringBuilder();
            sb.append("Have a good class, sir").append("\n");
            AudioManager audioManager =
                    (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_RING, 0,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            audioManager.setStreamVolume(AudioManager.STREAM_DTMF, 0,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            try {
                audioManager.setStreamVolume(AudioManager.USE_DEFAULT_STREAM_TYPE, 0,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            } catch (Exception e) {
                sb.append("Could not reduce default volume").append("\n");
            }
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
            if (canToggleGPS(context)) turnGPSOff(context);
            else sb.append("Could not turn off GPS").append("\n");
            KeithToast.show(sb.toString(), context);
        }
    }
}
