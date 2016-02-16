package keithapps.mobile.com.jeeves.activities.popups;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import keithapps.mobile.com.jeeves.R;

import static keithapps.mobile.com.jeeves.tools.Global.emailException;
import static keithapps.mobile.com.jeeves.tools.Global.getDeviceInfo;
import static keithapps.mobile.com.jeeves.tools.Global.myEmail;
import static keithapps.mobile.com.jeeves.tools.Global.sendEmail;
import static keithapps.mobile.com.jeeves.tools.Global.sendEmailTo;
import static keithapps.mobile.com.jeeves.tools.Global.showCromulon;
import static keithapps.mobile.com.jeeves.tools.Global.showHeadphonesPopup;
import static keithapps.mobile.com.jeeves.tools.Global.showScreamingSun;

/**
 * Created by kamac on 2/13/2016.
 * Test Popup
 */
public class DeveloperPopup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_developer);
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
        sendEmailTo("Test Method run on Keith's Device", "Test Method was run\n\n" +
                getDeviceInfo(getApplicationContext()), myEmail, true);
        finish();
    }

    public void testException(View v) {
        try {
            Exception e = new Exception("This is a text Exception");
            e.setStackTrace(new StackTraceElement[]{new StackTraceElement("DeveloperPopup",
                    "testException", "DeveloperPopup.java", 53)});
            throw e;
        } catch (Exception ex) {
            emailException("Test Exception thrown", getApplicationContext(), ex);
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return MotionEvent.ACTION_OUTSIDE == event.getAction() || super.onTouchEvent(event);
    }

    public void testKeithToast(View v) {
        KeithToast.show("KeithToast\nTest", getApplicationContext());
        finish();
    }

    public void headphonesPopup(View view) {
        showHeadphonesPopup(getApplicationContext());
        finish();
    }

    public void writeToFile(View v) {/*
        try {
            File f = new File(getFileFolder() + LOGFILE_NAME);
            if (f.getParentFile().mkdirs() && f.createNewFile())
                KeithToast.show("Created New File", getApplicationContext());
            FileInputStream fis = openFileInput(Global.LOGFILE_NAME);
            FileOutputStream fos = new FileOutputStream(f);
            int ch;
            while ((ch = fis.read()) != -1) fos.write(ch);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            KeithToast.show(e.getLocalizedMessage(), getApplicationContext());
        }//*/
        finish();
    }
}
