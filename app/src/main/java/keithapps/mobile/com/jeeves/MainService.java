package keithapps.mobile.com.jeeves;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.util.Calendar;

import keithapps.mobile.com.jeeves.ManageVolume.Mode;
import keithapps.mobile.com.jeeves.listeners.HeadphoneListener;
import keithapps.mobile.com.jeeves.listeners.NotificationListener;

public class MainService extends Service {

    private SettingsContentObserver mSettingsContentObserver;

    /**
     * The Current Mode
     */

    /**
     * Constructor
     */
    public MainService() {

    }

    /**
     * Get the current mode to show in the notification
     *
     * @return the current mode
     */
    public static int getMode(Context c) {
        return c.getSharedPreferences(Global.SHAREDPREF_CODE, MODE_PRIVATE)
                .getInt(c.getString(R.string.current_mode), Mode.Home);
    }

    /**
     * Show the Notification
     *
     * @param mode the current mode to put the notification in
     * @param c    the calling context
     */
    public static void showNotification(int mode, Context c) {
        SharedPreferences prefs = c.getSharedPreferences(Global.SHAREDPREF_CODE, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(c.getString(R.string.current_mode), mode);
        edit.apply();
        Intent intent = new Intent(c, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(c, 1,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(c);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(Notification.PRIORITY_LOW);
        if (mode == Mode.Home) {
            builder.setSmallIcon(android.R.color.transparent);
            builder.setPriority(Notification.PRIORITY_MIN);
        } else if (mode == Mode.Class) builder.setSmallIcon(R.drawable.icon_small_red);
        else if (mode == Mode.Car) builder.setSmallIcon(R.drawable.icon_car_white);
        else if (mode == Mode.Out) builder.setSmallIcon(R.drawable.icon_small);
        else builder.setSmallIcon(R.drawable.icon_small);
        if (prefs.getBoolean(c.getString(R.string.settings_showNotification), true))
            builder.setOngoing(true);
        builder.setAutoCancel(false);
        RemoteViews contentView = new RemoteViews(c.getPackageName(), R.layout.notification_layout);
        contentView.setOnClickPendingIntent(R.id.notification_button1,
                PendingIntent.getBroadcast(c, 0,
                        new Intent(c, HomeButtonListener.class), 0));
        contentView.setOnClickPendingIntent(R.id.notification_button2,
                PendingIntent.getBroadcast(c, 0,
                        new Intent(c, ClassButtonListener.class), 0));
        contentView.setOnClickPendingIntent(R.id.notification_button3,
                PendingIntent.getBroadcast(c, 0,
                        new Intent(c, OutButtonListener.class), 0));
        builder.setContent(contentView);
        Notification notification = builder.build();
        RemoteViews bigContent = new RemoteViews(c.getPackageName(), R.layout.notification_layout_big);
        bigContent.setOnClickPendingIntent(R.id.notification_big_button1,
                PendingIntent.getBroadcast(c, 0,
                        new Intent(c, HomeButtonListener.class), 0));
        bigContent.setOnClickPendingIntent(R.id.notification_big_button2,
                PendingIntent.getBroadcast(c, 0,
                        new Intent(c, ClassButtonListener.class), 0));
        bigContent.setOnClickPendingIntent(R.id.notification_big_button3,
                PendingIntent.getBroadcast(c, 0,
                        new Intent(c, OutButtonListener.class), 0));
        bigContent.setOnClickPendingIntent(R.id.notification_big_button4,
                PendingIntent.getBroadcast(c, 0,
                        new Intent(c, CarButtonListener.class), 0));
        if (prefs.getBoolean(c.getString(R.string.settings_showBigContentView), false))
            notification.bigContentView = bigContent;
        NotificationManager nMan =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        nMan.cancelAll();
        nMan.notify(992944, notification);

    }

    public static void startBackgroundProcess(Context c) {
        Intent myIntent = new Intent(c, BackgroundProcessListener.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 0); // first time
        long frequency = 60 * 1000; // in ms
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                frequency, pendingIntent);
    }

    /**
     * On Bind?
     *
     * @param intent the intent trying to bind with
     * @return Hell if I know
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        HeadphoneListener headphoneListener = new HeadphoneListener();
        IntentFilter headsetFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(headphoneListener, headsetFilter);

        IntentFilter notificationFilter = new IntentFilter(NOTIFICATION_SERVICE);
        NotificationListener notificationListener = new NotificationListener();
        registerReceiver(notificationListener, notificationFilter);

        mSettingsContentObserver = new SettingsContentObserver(getApplicationContext(), new Handler());

        getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver);

        BackgroundProcessListener backListener = new BackgroundProcessListener();
        IntentFilter f = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(backListener, f);
        showNotification(Mode.Home, getApplicationContext());
        startBackgroundProcess(getApplicationContext());
    }

    /**
     * When the Service is being closed
     */
    @Override
    public void onDestroy() {
        getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);
        super.onDestroy();
    }

    public static class HomeButtonListener extends BroadcastReceiver {
        /**
         * Listener for onClick for the Home button
         *
         * @param c      The Context in which the receiver is running.
         * @param intent The Intent being received.
         */
        @Override
        public void onReceive(Context c, Intent intent) {
            SetState.atHome(c);
        }
    }

    public static class ClassButtonListener extends BroadcastReceiver {
        /**
         * Listener for onClick for the Class button
         *
         * @param c      The Context in which the receiver is running.
         * @param intent The Intent being received.
         */
        @Override
        public void onReceive(Context c, Intent intent) {
            SetState.inClass(c);
        }
    }

    /**
     * Created by Keith on 1/19/2016.
     * Listens for the "Out" Button
     */
    public static class OutButtonListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent) {
            SetState.out(c);
        }
    }

    /**
     * Created by Keith on 1/19/2016.
     * Listens for the "Out" Button
     */
    public static class CarButtonListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent) {
            SetState.inCar(c);
        }
    }

    /**
     * Created by Keith on 1/19/2016.
     * Background Process Listener
     */
    public static class BackgroundProcessListener extends BroadcastReceiver {
        @Override
        public void onReceive(final Context c, Intent intent) {
            showNotification(getMode(c), c); //May or may not want to do this.
            // It looks as if the icon is being cleared from the Notification when the
            // main activity closes. IDK, man


            //Add functions that should be performed periodically in the background here

        }
    }

    public class SettingsContentObserver extends ContentObserver {
        private AudioManager audioManager;
        private SharedPreferences prefs;

        public SettingsContentObserver(Context context, Handler handler) {
            super(handler);
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            prefs = context.getSharedPreferences(Global.SHAREDPREF_CODE, Context.MODE_PRIVATE);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }

        @Override
        public void onChange(boolean selfChange) {
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        }
    }
}
