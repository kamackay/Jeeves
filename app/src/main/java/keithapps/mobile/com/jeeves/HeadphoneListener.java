package keithapps.mobile.com.jeeves;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Keith on 1/18/2016.
 * Listens for Headphones to be plugged in
 */
public class HeadphoneListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    KeithToast.show("Headset is unplugged", context);
                    break;
                case 1:
                    KeithToast.show("Headset is plugged", context);
                    break;
                default:
                    KeithToast.show("Headset is unknown", context);
            }
        }
    }
}
