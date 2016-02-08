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
import android.content.pm.PackageInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import keithapps.mobile.com.jeeves.ManageVolume.Mode;
import keithapps.mobile.com.jeeves.listeners.BackgroundProcessListener;
import keithapps.mobile.com.jeeves.listeners.HeadphoneListener;
import keithapps.mobile.com.jeeves.listeners.NotificationButtonListener;
import keithapps.mobile.com.jeeves.listeners.NotificationListener;
import keithapps.mobile.com.jeeves.listeners.VolumeChangeListener;

import static keithapps.mobile.com.jeeves.Global.getTimeStamp;
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
                .getInt(Settings.current_mode, Mode.A);
    }

    public static void updateNotification(Context c) {
        showNotification(c.getSharedPreferences(Settings.sharedPrefs_code, MODE_PRIVATE)
                .getInt(Settings.current_mode, Mode.A), c);
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
        Intent i = new Intent(c, NotificationButtonListener.class);
        i.setAction(Settings.modeA);
        contentView.setOnClickPendingIntent(R.id.notification_buttonA,
                PendingIntent.getBroadcast(c, 0, i, 0));
        i.setAction(Settings.modeB);
        contentView.setOnClickPendingIntent(R.id.notification_buttonB,
                PendingIntent.getBroadcast(c, 0, i, 0));
        i.setAction(Settings.modeC);
        contentView.setOnClickPendingIntent(R.id.notification_buttonC,
                PendingIntent.getBroadcast(c, 0, i, 0));
        i.setAction(Settings.modeD);
        contentView.setOnClickPendingIntent(R.id.notification_buttonD,
                PendingIntent.getBroadcast(c, 0, i, 0));
        i.setAction(Settings.text_add);
        contentView.setOnClickPendingIntent(R.id.notification_text_timeSinceAdderall,
                PendingIntent.getBroadcast(c, 0, i, 0));
        contentView.setOnClickPendingIntent(R.id.notification_text_adderallSoFar,
                PendingIntent.getBroadcast(c, 0, i, 0));
        contentView.setImageViewResource(R.id.notification_buttonA,
                R.drawable.notification_button_background);
        contentView.setImageViewResource(R.id.notification_buttonB,
                R.drawable.notification_button_background);
        contentView.setImageViewResource(R.id.notification_buttonC,
                R.drawable.notification_button_background);
        contentView.setImageViewResource(R.id.notification_buttonD,
                R.drawable.notification_button_background);
        contentView.setTextColor(R.id.notification_textC, Color.WHITE);
        contentView.setTextColor(R.id.notification_textB, Color.WHITE);
        contentView.setTextColor(R.id.notification_textA, Color.WHITE);
        contentView.setTextColor(R.id.notification_textD, Color.WHITE);
        contentView.setTextViewText(R.id.notification_textA,
                prefs.getString(Settings.action_a_name, c.getString(R.string.text_home)));
        contentView.setTextViewText(R.id.notification_textB,
                prefs.getString(Settings.action_b_name, c.getString(R.string.text_class)));
        contentView.setTextViewText(R.id.notification_textC,
                prefs.getString(Settings.action_c_name, c.getString(R.string.text_out)));
        contentView.setTextViewText(R.id.notification_textD,
                prefs.getString(Settings.action_d_name, c.getString(R.string.text_car)));
        contentView.setTextViewText(R.id.notification_text_adderallSoFar,
                String.format("%d mg", prefs.getInt(Settings.Adderall.adderall_count, 0)));
        String timestamp_last = prefs.getString(Settings.Adderall.timeSince, "");
        if (!timestamp_last.equals("")) {
            try {
                String timestamp_now = getTimeStamp();
                SimpleDateFormat format = new SimpleDateFormat("MM/dd-HH:mm:ss", Locale.US);
                long difference = format.parse(timestamp_now).getTime() -
                        format.parse(timestamp_last).getTime();
                String s = String.format("%02d:%02d", (difference / (1000 * 60 * 60)), (difference / (1000 * 60)) % 60);
                contentView.setTextViewText(R.id.notification_text_timeSinceAdderall, s);
            } catch (Exception e) {//Give Up Immediately}
            }
        }
        if (prefs.getBoolean(Settings.headset_full, false))
            builder.setSmallIcon(android.R.color.transparent);
        else if (mode == Mode.A) {
            contentView.setTextColor(R.id.notification_textA, Color.BLACK);
            contentView.setImageViewResource(R.id.notification_buttonA,
                    R.drawable.notification_button_background_selected);
            builder.setSmallIcon(android.R.color.transparent);
            //builder.setPriority(Notification.PRIORITY_MIN);
        } else if (mode == Mode.B) {
            contentView.setTextColor(R.id.notification_textB, Color.BLACK);
            contentView.setImageViewResource(R.id.notification_buttonB,
                    R.drawable.notification_button_background_selected);
            builder.setSmallIcon(R.drawable.icon_class);
        } else if (mode == Mode.C) {
            contentView.setImageViewResource(R.id.notification_buttonC,
                    R.drawable.notification_button_background_selected);
            contentView.setTextColor(R.id.notification_textC, Color.BLACK);
            builder.setSmallIcon(R.drawable.icon_small);
        } else if (mode == Mode.D) {
            contentView.setImageViewResource(R.id.notification_buttonD,
                    R.drawable.notification_button_background_selected);
            contentView.setTextColor(R.id.notification_textD, Color.BLACK);
            builder.setSmallIcon(R.drawable.icon_car_white);
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
        c.registerReceiver(new BackgroundProcessListener(), new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        c.registerReceiver(new BackgroundProcessListener(), new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 0); // first time
        long frequency = 60 * 5000; // in ms
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
        SharedPreferences prefs = getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE);
        BackgroundProcessListener backListener = new BackgroundProcessListener();
        IntentFilter f = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(backListener, f);
        showNotification(prefs.getInt(Settings.current_mode, Mode.A), getApplicationContext());
        startBackgroundProcess(getApplicationContext());
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            if (prefs.getInt(Settings.versionCode, 5) < info.versionCode) {
                writeToLog(String.format("Updated to version %s from %s",
                        info.versionName, prefs.getString(Settings.versionName, "1.1.6a")), c);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putInt(Settings.versionCode, info.versionCode);
                edit.putString(Settings.versionName, info.versionName);
                edit.apply();
            }
        } catch (Exception e) {
            writeToLog(e.getLocalizedMessage(), c);
        }
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
