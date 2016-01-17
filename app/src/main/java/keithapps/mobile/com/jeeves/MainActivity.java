package keithapps.mobile.com.jeeves;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;

import static keithapps.mobile.com.jeeves.Global.isServiceRunning;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showWelcome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 01,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setContentTitle("Hello, I am Jeeves, by Keith MacKay");
        builder.setContentText("I am now on the Android platform");
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setSmallIcon(R.drawable.icon_small);
        builder.setTicker("Hello, I am Jeeves");
        builder.setOngoing(false);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        Notification notification = builder.build();
        NotificationManager notificationManger =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManger.notify(992945, notification);
    }

    /**
     * Called after the Activity is created
     *
     * @param savedInstanceState idk
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!isServiceRunning(MainService.class, getApplicationContext()))
            startService(new Intent(getApplicationContext(), MainService.class));
    }
}
