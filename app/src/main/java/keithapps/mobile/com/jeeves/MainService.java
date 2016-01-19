package keithapps.mobile.com.jeeves;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.RemoteViews;

import keithapps.mobile.com.jeeves.ManageVolume.Mode;

public class MainService extends Service {
    private static Notification n;

    public MainService() {
    }

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
    }

    public void showNotification() {
        showNotification(Mode.Home, getApplicationContext());
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

}
