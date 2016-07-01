package keithapps.mobile.com.jeeves.services;

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
import android.util.TypedValue;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Locale;

import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.listeners.BackgroundProcessListener;
import keithapps.mobile.com.jeeves.listeners.BootListener;
import keithapps.mobile.com.jeeves.listeners.HeadphoneListener;
import keithapps.mobile.com.jeeves.listeners.NotificationButtonListener;
import keithapps.mobile.com.jeeves.listeners.NotificationListener;
import keithapps.mobile.com.jeeves.listeners.VolumeChangeListener;
import keithapps.mobile.com.jeeves.tools.ManageVolume.Mode;
import keithapps.mobile.com.jeeves.tools.Settings;

import static keithapps.mobile.com.jeeves.tools.Email.sendEmail;
import static keithapps.mobile.com.jeeves.tools.Log.writeToLog;
import static keithapps.mobile.com.jeeves.tools.SystemTools.getDeviceInfo;
import static keithapps.mobile.com.jeeves.tools.Utils.getStackTraceString;
import static keithapps.mobile.com.jeeves.tools.Utils.getTimestamp;

public class MainService extends Service {
    /**
     * Listens for the volume to change
     */
    private VolumeChangeListener mVolumeChangeListener;

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
        Intent intent = new Intent(c, BackgroundProcessListener.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(c, 1,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(c);
        builder.setContentIntent(pendingIntent);
        int priority = Notification.PRIORITY_LOW;
        RemoteViews contentView = new RemoteViews(c.getPackageName(), R.layout.notification_layout);
        Intent i = new Intent(c, NotificationButtonListener.class);
        i.setAction(Settings.showJeevesMain);
        contentView.setOnClickPendingIntent(R.id.notification_image,
                PendingIntent.getBroadcast(c, 0, i, 0));
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
        /*contentView.setImageViewResource(R.id.notification_buttonA,
                R.drawable.notification_button_background);
        contentView.setImageViewResource(R.id.notification_buttonB,
                R.drawable.notification_button_background);
        contentView.setImageViewResource(R.id.notification_buttonC,
                R.drawable.notification_button_background);
        contentView.setImageViewResource(R.id.notification_buttonD,
                R.drawable.notification_button_background);/**///This was from when the buttons were ImageViews with backgrounds... Ew
        contentView.setTextColor(R.id.notification_buttonC, Color.BLACK);
        contentView.setTextColor(R.id.notification_buttonB, Color.BLACK);
        contentView.setTextColor(R.id.notification_buttonA, Color.BLACK);
        contentView.setTextColor(R.id.notification_buttonD, Color.BLACK);
        contentView.setTextViewTextSize(R.id.notification_buttonA, TypedValue.COMPLEX_UNIT_SP, 12);
        contentView.setTextViewTextSize(R.id.notification_buttonB, TypedValue.COMPLEX_UNIT_SP, 12);
        contentView.setTextViewTextSize(R.id.notification_buttonC, TypedValue.COMPLEX_UNIT_SP, 12);
        contentView.setTextViewTextSize(R.id.notification_buttonD, TypedValue.COMPLEX_UNIT_SP, 12);
        contentView.setTextViewText(R.id.notification_buttonA,
                prefs.getString(Settings.action_a_name, c.getString(R.string.text_home)));
        contentView.setTextViewText(R.id.notification_buttonB,
                prefs.getString(Settings.action_b_name, c.getString(R.string.text_class)));
        contentView.setTextViewText(R.id.notification_buttonC,
                prefs.getString(Settings.action_c_name, c.getString(R.string.text_out)));
        contentView.setTextViewText(R.id.notification_buttonD,
                prefs.getString(Settings.action_d_name, c.getString(R.string.text_car)));
        contentView.setTextViewText(R.id.notification_text_adderallSoFar,
                String.format(Locale.getDefault(), "%d mg",
                        prefs.getInt(Settings.Adderall.adderall_count, 0)));
        contentView.setTextViewTextSize(R.id.notification_text_adderallSoFar, TypedValue.COMPLEX_UNIT_SP, 15);
        String timestamp_last = prefs.getString(Settings.Adderall.timeSince, "");
        if (!timestamp_last.equals("")) {
            try {
                String timestamp_now = getTimestamp();
                SimpleDateFormat format = new SimpleDateFormat("MM/dd-HH:mm:ss", Locale.US);
                long difference = format.parse(timestamp_now).getTime() -
                        format.parse(timestamp_last).getTime();
                String s = String.format(Locale.getDefault(), "%02d:%02d",
                        (difference / (1000 * 60 * 60)), (difference / (1000 * 60)) % 60);
                contentView.setTextViewText(R.id.notification_text_timeSinceAdderall, s);
                contentView.setTextViewTextSize(R.id.notification_text_timeSinceAdderall,
                        TypedValue.COMPLEX_UNIT_SP, 15);
            } catch (Exception e) {//Give Up Immediately}
            }
        }
        builder.setSmallIcon(android.R.color.transparent);
        if (prefs.getBoolean(Settings.headset_full, false))
            builder.setSmallIcon(android.R.color.transparent);
        else if (mode == Mode.A) {
            priority = prefs.getInt(Settings.A.notificationPriority, Notification.PRIORITY_LOW);
            contentView.setTextColor(R.id.notification_buttonA, Color.argb(255, 0, 175, 255));
            //contentView.setImageViewResource(R.id.notification_buttonA,                    R.drawable.notification_button_background_selected);
            builder.setSmallIcon(android.R.color.transparent);
            //builder.setPriority(Notification.PRIORITY_MIN);
        } else if (mode == Mode.B) {
            priority = prefs.getInt(Settings.B.notificationPriority, Notification.PRIORITY_LOW);
            contentView.setTextColor(R.id.notification_buttonB, Color.argb(255, 0, 175, 255));
            //contentView.setImageViewResource(R.id.notification_buttonB,                    R.drawable.notification_button_background_selected);
            if (prefs.getBoolean(c.getString(R.string.modeB_icon), true))
                builder.setSmallIcon(R.drawable.silent);
        } else if (mode == Mode.C) {
            priority = prefs.getInt(Settings.C.notificationPriority, Notification.PRIORITY_LOW);
            //contentView.setImageViewResource(R.id.notification_buttonC,                    R.drawable.notification_button_background_selected);
            contentView.setTextColor(R.id.notification_buttonC, Color.argb(255, 0, 175, 255));
            if (prefs.getBoolean(c.getString(R.string.modeC_icon), true))
                builder.setSmallIcon(R.drawable.icon_small);
        } else if (mode == Mode.D) {
            priority = prefs.getInt(Settings.D.notificationPriority, Notification.PRIORITY_LOW);
            //contentView.setImageViewResource(R.id.notification_buttonD,                    R.drawable.notification_button_background_selected);
            contentView.setTextColor(R.id.notification_buttonD, Color.argb(255, 0, 175, 255));
            if (prefs.getBoolean(c.getString(R.string.modeD_icon), true))
                builder.setSmallIcon(R.drawable.icon_car_white);
        } else builder.setSmallIcon(R.drawable.icon_small);
        switch (priority) {
            case Notification.PRIORITY_MIN:
                builder.setPriority(Notification.PRIORITY_MIN);
                break;
            case Notification.PRIORITY_LOW:
                builder.setPriority(Notification.PRIORITY_LOW);
                break;
            case Notification.PRIORITY_DEFAULT:
                builder.setPriority(Notification.PRIORITY_DEFAULT);
                break;
            case Notification.PRIORITY_HIGH:
                builder.setPriority(Notification.PRIORITY_HIGH);
                break;
            case Notification.PRIORITY_MAX:
                builder.setPriority(Notification.PRIORITY_MAX);
                break;
        }
        if (prefs.getBoolean(c.getString(R.string.settings_showNotification), true))
            builder.setOngoing(true);
        builder.setAutoCancel(false);
        builder.setContent(contentView);
        NotificationManager nMan =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        //nMan.cancelAll();
        nMan.notify(992944, builder.build());
    }

    public static void startBackgroundProcess(Context c) {
        c.registerReceiver(new BackgroundProcessListener(), new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        c.registerReceiver(new BackgroundProcessListener(), new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
        c.registerReceiver(new BootListener(), new IntentFilter(Intent.ACTION_SCREEN_OFF));
        c.registerReceiver(new BootListener(), new IntentFilter(Intent.ACTION_SCREEN_ON));
        c.registerReceiver(new BackgroundProcessListener(), new IntentFilter(Intent.ACTION_UNINSTALL_PACKAGE));
        c.registerReceiver(new BackgroundProcessListener(), new IntentFilter(Intent.ACTION_INSTALL_PACKAGE));
        c.registerReceiver(new BackgroundProcessListener(), new IntentFilter(Intent.ACTION_PACKAGE_CHANGED));
        /*
        Intent myIntent = new Intent(c, BackgroundProcessListener.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 0); // first time
        long frequency = 60 * 5000; // in ms
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                frequency, pendingIntent);/**///This code refreshed the Service every 5 minutes, which is bad for runtimes
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
        try {
            super.onCreate();
            HeadphoneListener headphoneListener = new HeadphoneListener();
            IntentFilter headsetFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
            registerReceiver(headphoneListener, headsetFilter);

            IntentFilter notificationFilter = new IntentFilter(NOTIFICATION_SERVICE);
            NotificationListener notificationListener = new NotificationListener();
            registerReceiver(notificationListener, notificationFilter);

          /*  if (!isServiceRunning(FloatingGoogleButton.class, getApplicationContext()))
                startService(new Intent(getApplicationContext(), FloatingGoogleButton.class));/**/

            mVolumeChangeListener = new VolumeChangeListener(this, new Handler());

            c.getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI,
                    true, mVolumeChangeListener);
            SharedPreferences prefs = getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE);
            BackgroundProcessListener backListener = new BackgroundProcessListener();
            IntentFilter f = new IntentFilter(Intent.ACTION_TIME_TICK);
            registerReceiver(backListener, f);
            showNotification(prefs.getInt(Settings.current_mode, Mode.A), getApplicationContext());
            startBackgroundProcess(getApplicationContext());
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            if (prefs.getInt(Settings.versionCode, 5) < info.versionCode) {
                writeToLog(String.format("Updated to version %s from %s",
                        info.versionName, prefs.getString(Settings.versionName, "1.1.6a")), c);
                sendEmail("Jeeves Update", String.format(Locale.getDefault(),
                        "An Android device:\n\n%s\n\nUpdated from version %s (code %d) to version " +
                                "%s (code %d)",
                        getDeviceInfo(getApplicationContext()),
                        prefs.getString(Settings.versionName, "1.1.6a"),
                        prefs.getInt(Settings.versionCode, 5),
                        info.versionName,
                        info.versionCode), c);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putInt(Settings.versionCode, info.versionCode);
                edit.putString(Settings.versionName, info.versionName);
                edit.apply();
            }

            // PendingIntent pendingIntent = PendingIntent.getActivity(
            //       this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            // IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            //try {
            //    ndef.addDataType("*/*");    /* Handles all MIME based dispatches.
            //                            You should specify only the ones that you need. */
            //}
            // catch (IntentFilter.MalformedMimeTypeException e) {
            //    throw new RuntimeException("fail", e);
            // }
            //intentFiltersArray = new IntentFilter[] {ndef, };
            writeToLog("MainService Startup", getApplicationContext(), true);
        } catch (Exception e) {
            writeToLog(String.format("Error occurred while starting MainService: %s",
                    e.getLocalizedMessage()), getApplicationContext());
            sendEmail("Error Occurred on MainService Startup",
                    String.format("An Android Device:\n\n%s\n\nExperienced an error while Starting " +
                                    "the MainService. \n\nMessage: %s\nLocalized Message: %s\n" +
                                    "Stack Trace: %s",
                            getDeviceInfo(getApplicationContext()),
                            e.getMessage(),
                            e.getLocalizedMessage(),
                            getStackTraceString(e.getStackTrace())), c);
        }
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
     * When the Service is being closed
     */
    @Override
    public void onDestroy() {
        getApplicationContext().getContentResolver().unregisterContentObserver(mVolumeChangeListener);
        writeToLog("Main Service Killed", getApplicationContext());
        super.onDestroy();
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

    @Override
    public void onLowMemory() {
        writeToLog("MainService onLowMemory", getApplicationContext());
        super.onLowMemory();
    }

}
