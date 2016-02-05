package keithapps.mobile.com.jeeves;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.util.Calendar;

import keithapps.mobile.com.jeeves.ManageVolume.Mode;
import keithapps.mobile.com.jeeves.listeners.BackgroundProcessListener;
import keithapps.mobile.com.jeeves.listeners.HeadphoneListener;
import keithapps.mobile.com.jeeves.listeners.NotificationListener;
import keithapps.mobile.com.jeeves.listeners.SetStateButtonListener;
import keithapps.mobile.com.jeeves.listeners.VolumeChangeListener;

import static keithapps.mobile.com.jeeves.Global.writeToLog;

public class MainService extends Service {

    private VolumeChangeListener mVolumeChangeListener;

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
        return c.getSharedPreferences(Settings.sharedPrefs_code, MODE_PRIVATE)
                .getInt(Settings.current_mode, Mode.Home);
    }

    /**
     * Show the Notification
     *
     * @param mode the current mode to put the notification in
     * @param c    the calling context
     */
    public static void showNotification(int mode, Context c) {
        SharedPreferences prefs = c.getSharedPreferences(Settings.sharedPrefs_code, MODE_PRIVATE);
        Intent intent = new Intent(c, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(c, 1,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(c);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(Notification.PRIORITY_LOW);
        RemoteViews contentView = new RemoteViews(c.getPackageName(), R.layout.notification_layout);
        Intent i = new Intent(c, SetStateButtonListener.class);
        i.setAction(Settings.text_home);
        contentView.setOnClickPendingIntent(R.id.notification_buttonHome,
                PendingIntent.getBroadcast(c, 0, i, 0));
        i.setAction(Settings.text_class);
        contentView.setOnClickPendingIntent(R.id.notification_buttonClass,
                PendingIntent.getBroadcast(c, 0, i, 0));
        i.setAction(Settings.text_out);
        contentView.setOnClickPendingIntent(R.id.notification_buttonOut,
                PendingIntent.getBroadcast(c, 0, i, 0));
        i.setAction(Settings.text_add);
        contentView.setOnClickPendingIntent(R.id.notification_add,
                PendingIntent.getBroadcast(c, 0, i, 0));
        contentView.setImageViewResource(R.id.notification_buttonHome,
                R.drawable.notification_button_background);
        contentView.setImageViewResource(R.id.notification_buttonClass,
                R.drawable.notification_button_background);
        contentView.setImageViewResource(R.id.notification_buttonOut,
                R.drawable.notification_button_background);
        contentView.setTextColor(R.id.notification_textOut, Color.WHITE);
        contentView.setTextColor(R.id.notification_textClass, Color.WHITE);
        contentView.setTextColor(R.id.notification_textHome, Color.WHITE);
        if (mode == Mode.Home) {
            contentView.setTextColor(R.id.notification_textHome, Color.BLACK);
            contentView.setImageViewResource(R.id.notification_buttonHome,
                    R.drawable.notification_button_background_selected);
            builder.setSmallIcon(android.R.color.transparent);
            builder.setPriority(Notification.PRIORITY_MIN);
        } else if (mode == Mode.Class) {
            contentView.setTextColor(R.id.notification_textClass, Color.BLACK);
            contentView.setImageViewResource(R.id.notification_buttonClass,
                    R.drawable.notification_button_background_selected);
            builder.setSmallIcon(R.drawable.icon_class);
        } else if (mode == Mode.Out) {
            contentView.setImageViewResource(R.id.notification_buttonOut,
                    R.drawable.notification_button_background_selected);
            contentView.setTextColor(R.id.notification_textOut, Color.BLACK);
            builder.setSmallIcon(R.drawable.icon_small);
        } else builder.setSmallIcon(R.drawable.icon_small);
        if (prefs.getBoolean(c.getString(R.string.settings_showNotification), true))
            builder.setOngoing(true);
        builder.setAutoCancel(false);
        builder.setContent(contentView);
        NotificationManager nMan =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        nMan.cancelAll();
        nMan.notify(992944, builder.build());

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
        writeToLog("Background Process Started", c);
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
        Context c = getApplicationContext();
        super.onCreate();
        HeadphoneListener headphoneListener = new HeadphoneListener();
        IntentFilter headsetFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(headphoneListener, headsetFilter);

        IntentFilter notificationFilter = new IntentFilter(NOTIFICATION_SERVICE);
        NotificationListener notificationListener = new NotificationListener();
        registerReceiver(notificationListener, notificationFilter);

        mVolumeChangeListener = new VolumeChangeListener(this, new Handler());

        c.getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI,
                true, mVolumeChangeListener);

        BackgroundProcessListener backListener = new BackgroundProcessListener();
        IntentFilter f = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(backListener, f);
        showNotification(getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE)
                .getInt(Settings.current_mode, Mode.Home), getApplicationContext());
        startBackgroundProcess(getApplicationContext());

        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(c, SetStateButtonListener.class);
        i.setAction(Settings.adderall_clear);
        PendingIntent pi = PendingIntent.getBroadcast(c, 0, i, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 20);
        calendar.set(Calendar.HOUR,9);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        mgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), pi);

        writeToLog("MainService Startup", getApplicationContext());
    }

    @Override
    public void onTrimMemory(int level) {
        //writeToLog("MainService onMemoryTrim", getApplicationContext());
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        writeToLog("MainService onConfigurationChanged", getApplicationContext());
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Called when all clients have disconnected from a particular interface
     * published by the service.  The default implementation does nothing and
     * returns false.
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return true if you would like to have the service's
     * {@link #onRebind} method later called when new clients bind to it.
     */
    @Override
    public boolean onUnbind(Intent intent) {
        writeToLog("MainService onUnbind", getApplicationContext());
        return super.onUnbind(intent);
    }

    /**
     * This is called if the service is currently running and the user has
     * removed a task that comes from the service's application.  If you have
     * set {@link ServiceInfo#FLAG_STOP_WITH_TASK ServiceInfo.FLAG_STOP_WITH_TASK}
     * then you will not receive this callback; instead, the service will simply
     * be stopped.
     *
     * @param rootIntent The original root Intent that was used to launch
     *                   the task that is being removed.
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        writeToLog("MainService onTaskRemoved", getApplicationContext());
        super.onTaskRemoved(rootIntent);
    }

    /**
     * When the Service is being closed
     */
    @Override
    public void onDestroy() {
        getApplicationContext().getContentResolver().unregisterContentObserver(mVolumeChangeListener);
        writeToLog("Main Service Killed", getApplicationContext());
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        writeToLog("MainService onLowMemory", getApplicationContext());
        super.onLowMemory();
    }

}
