package keithapps.mobile.com.jeeves.popups;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import keithapps.mobile.com.jeeves.R;

import static keithapps.mobile.com.jeeves.Global.emailException;
import static keithapps.mobile.com.jeeves.Global.getDeviceInfo;
import static keithapps.mobile.com.jeeves.Global.sendEmail;
import static keithapps.mobile.com.jeeves.Global.showCromulon;
import static keithapps.mobile.com.jeeves.Global.showHeadphonesPopup;
import static keithapps.mobile.com.jeeves.Global.showScreamingSun;
/**
 * Created by kamac on 2/13/2016.
 * Test Popup
 */
public class TestPopup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_popup);
        ActionBar a = getActionBar();
        if (a != null) a.hide();
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void screamingSun(View view) {
        showScreamingSun(getApplicationContext());
        finish();
    }

    public void cromulon(View view) {
        showCromulon(getApplicationContext());
        finish();
    }

    public void testEmail(View v) {
        sendEmail("Test Method run on Keith's Device", "Test Method was run\n\n" +
                getDeviceInfo(getApplicationContext()));
        finish();
    }

    public void testException(View v) {
        try {
            Exception e = new Exception("This is a text Exception");
            e.setStackTrace(new StackTraceElement[]{new StackTraceElement("TestPopup",
                    "testExeption", "TestPopup.java", 53)});
            throw e;
        } catch (Exception ex) {
            emailException("Test Exception thrown", getApplicationContext(), ex);
            finish();
        }
    }

    public void testKeithToast(View v) {
        KeithToast.show("Feedback Sent\nThank you!", getApplicationContext());
        finish();
    }

    public void headphonesPopup(View view) {
        showHeadphonesPopup(getApplicationContext());
        finish();
    }
}
