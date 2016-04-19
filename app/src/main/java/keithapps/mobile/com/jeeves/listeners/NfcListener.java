package keithapps.mobile.com.jeeves.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.widget.Toast;

/**
 * Created by Keith on 4/16/2016.
 *
 */
public class NfcListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Toast.makeText(context, "Tag", Toast.LENGTH_LONG).show();
        }
    }
}
