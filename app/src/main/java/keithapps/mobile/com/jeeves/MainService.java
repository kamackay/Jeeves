package keithapps.mobile.com.jeeves;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Process;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.List;

import keithapps.mobile.com.jeeves.ManageVolume.Mode;

import static keithapps.mobile.com.jeeves.Global.PACKAGE_SNAPCHAT;
import static keithapps.mobile.com.jeeves.Global.isKeith;

public class MainService extends Service {
    private ScreenListener screenListener;

    /**
     * Constructor
     */
    public MainService() {

    }

    /**
     * Show the Notification
     *
     * @param mode the current mode to put the notification in
     * @param c    the calling context
     */
    public static void showNotification(Mode mode, Context c) {
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
        notification.bigContentView = bigContent;
        NotificationManager notificationManger =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManger.notify(992944, notification);

    }

    public static void startBackgroundProcess(Context c) {
        Intent myIntent = new Intent(c, BackgroundProcessListener.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 0); // first time
        long frequency = 60 * 1000; // in ms
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pendingIntent);
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
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(headphoneListener, filter);
        showNotification();
        startBackgroundProcess(getApplicationContext());
        startScreenListener();
    }

    public void startScreenListener() {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        screenListener = new ScreenListener();
        registerReceiver(screenListener, filter);
    }

    /**
     * Can't remember why I made this.
     * Calls the show Notification and uses Home as a default
     */
    public void showNotification() {
        showNotification(Mode.Home, getApplicationContext());
    }

    /**
     * When the Service is being closed
     */
    @Override
    public void onDestroy() {
        if (screenListener != null) {
            unregisterReceiver(screenListener);
            screenListener = null;
        }
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
        public void onReceive(Context c, Intent intent) {
            if (!isKeith(c)) return;
            boolean triedToKill = false;
            final ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> listOfProcesses = manager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo process : listOfProcesses) {
                if (process.processName.contains(PACKAGE_SNAPCHAT)) {
                    try {
                        triedToKill = true;
                        Process.killProcess(process.pid);
                        android.os.Process.sendSignal(process.pid, Process.SIGNAL_KILL);
                    } catch (Exception e) {
                        //I doubt this'll ever happen
                    }
                }
            }
            if (!triedToKill) return;
            boolean alive = false;
            List<ActivityManager.RunningAppProcessInfo> l =
                    ((ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE))
                            .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo process : l)
                if (process.processName.contains(PACKAGE_SNAPCHAT)) alive = true;
            if (!alive) KeithToast.show("Snapchat was killed", c);
        }
    }

    public static class ScreenListener extends BroadcastReceiver {
        private boolean on = true;

        @Override
        public void onReceive(Context c, Intent i) {
            if (!isKeith(c)) return;/**
             if (i.getAction().equals(Intent.ACTION_SCREEN_ON)) {
             if (!on) {
             on = true;
             try {
             Intent in = new Intent(c, LockScreen.class);
             in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             //c.startActivity(in);
             } catch (Exception e) {
             KeithToast.show(e.getMessage(), c);
             }
             }
             } else*/if (i.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                if (on) on = false;
            }
        }
    }
}
