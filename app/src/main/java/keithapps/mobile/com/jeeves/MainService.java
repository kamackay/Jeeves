package keithapps.mobile.com.jeeves;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.RemoteViews;

import static keithapps.mobile.com.jeeves.Global.canToggleGPS;
import static keithapps.mobile.com.jeeves.Global.getPrefs;
import static keithapps.mobile.com.jeeves.Global.turnGPSOff;

public class MainService extends Service {
    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private HeadphoneListener headphoneListener;

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        headphoneListener = new HeadphoneListener();
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(headphoneListener, filter);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(android.R.color.transparent);
        builder.setOngoing(true);
        builder.setAutoCancel(false);
        builder.setPriority(Notification.PRIORITY_MIN);
        RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
        contentView.setOnClickPendingIntent(R.id.notification_button1,
                PendingIntent.getBroadcast(this, 0,
                        new Intent(this, HomeButtonListener.class), 0));
        contentView.setOnClickPendingIntent(R.id.notification_button2,
                PendingIntent.getBroadcast(this, 0,
                        new Intent(this, ClassButtonListener.class), 0));
        builder.setContent(contentView);
        Notification notification = builder.build();
        RemoteViews bigContent = new RemoteViews(this.getPackageName(), R.layout.notification_layout_big);
        bigContent.setOnClickPendingIntent(R.id.notification_big_button1,
                PendingIntent.getBroadcast(this, 0,
                        new Intent(this, HomeButtonListener.class), 0));
        bigContent.setOnClickPendingIntent(R.id.notification_big_button2,
                PendingIntent.getBroadcast(this, 0,
                        new Intent(this, ClassButtonListener.class), 0));
        notification.bigContentView = bigContent;
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
            try {
                Global.closeNotificationTray(context);
            } catch (Exception e) {
                //Everything should be OK
            }
            try {
                SharedPreferences prefs = getPrefs(context);
                double ringtoneVol = .1 * prefs.getInt(context
                        .getString(R.string.settings_home_ringtoneVolume), 5),
                        mediaVol = .1 * prefs.getInt(context
                                .getString(R.string.settings_home_mediaVolume), 5),
                        systemVol = .1 * prefs.getInt(context
                                .getString(R.string.settings_home_systemVolume), 5),
                        notificationVol = .1 * prefs.getInt(context
                                .getString(R.string.settings_home_notificationVolume), 1),
                        alarmVol = .1 * prefs.getInt(context
                                .getString(R.string.settings_home_alarmVolume), 10);
                AudioManager audioManager =
                        (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_RING,
                        (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
                                * ringtoneVol), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,
                        (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) *
                                systemVol), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM,
                        (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM) *
                                alarmVol), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) *
                                mediaVol), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                        (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) *
                                notificationVol), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            } catch (Exception e) {
                //It's all good
            }
            try {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
            } catch (Exception e) {
                //It's all good
            }
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
            sb.append("Have a good class, sir").append(Global.TEXT_NEWLINE)
                    .append(Global.TEXT_NEWLINE);
            try {
                Global.closeNotificationTray(context);
            } catch (Exception e) {
                //Everything should be OK
            }
            try { //Try to turn off all of the volume
                SharedPreferences prefs = getPrefs(context);
                double ringtoneVol = .1 * prefs.getInt(context
                        .getString(R.string.settings_class_ringtoneVolume), 5),
                        mediaVol = .1 * prefs.getInt(context
                                .getString(R.string.settings_class_mediaVolume), 5),
                        systemVol = .1 * prefs.getInt(context
                                .getString(R.string.settings_class_systemVolume), 5),
                        notificationVol = .1 * prefs.getInt(context
                                .getString(R.string.settings_class_notificationVolume), 1),
                        alarmVol = .1 * prefs.getInt(context
                                .getString(R.string.settings_class_alarmVolume), 10);
                AudioManager audioManager =
                        (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_RING,
                        (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
                                * ringtoneVol), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,
                        (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) *
                                systemVol), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM,
                        (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM) *
                                alarmVol), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) *
                                mediaVol), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                        (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) *
                                notificationVol), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            } catch (Exception e) {
                sb.append("Had a problem turning down the volume").append(Global.TEXT_NEWLINE);
            }
            try { //Try to turn on the WiFi
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
            } catch (Exception e) {
                sb.append("Had a problem turning on the Wi-Fi").append(Global.TEXT_NEWLINE);
            }
            try { //Try to turn off the GPS
                if (canToggleGPS(context)) turnGPSOff(context);
                else {
                    Intent i = new Intent("android.location.GPS_ENABLED_CHANGE");
                    i.putExtra("enabled", false);
                    context.sendBroadcast(i);
                }
            } catch (Exception e) {
                //sb.append("Had a problem turning off the GPS").append(Global.TEXT_NEWLINE);
            }
            KeithToast.show(sb.toString().trim(), context);
        }
    }
}
