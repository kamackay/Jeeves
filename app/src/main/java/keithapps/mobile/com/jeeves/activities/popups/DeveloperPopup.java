package keithapps.mobile.com.jeeves.activities.popups;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.activities.MainActivity;
import keithapps.mobile.com.jeeves.tools.Email;
import keithapps.mobile.com.jeeves.tools.Settings;

import static keithapps.mobile.com.jeeves.activities.popups.CromulonPopup.showCromulon;
import static keithapps.mobile.com.jeeves.activities.popups.HeadphonePopup.showHeadphonesPopup;
import static keithapps.mobile.com.jeeves.activities.popups.ScreamingSunPopup.showScreamingSun;
import static keithapps.mobile.com.jeeves.activities.popups.TextPopup.showTextPopup;
import static keithapps.mobile.com.jeeves.tools.Email.emailException;
import static keithapps.mobile.com.jeeves.tools.Email.myEmail;
import static keithapps.mobile.com.jeeves.tools.Email.sendEmail;
import static keithapps.mobile.com.jeeves.tools.GlobalTools.getAllChildren;
import static keithapps.mobile.com.jeeves.tools.Log.logException;
import static keithapps.mobile.com.jeeves.tools.SystemTools.getDeviceInfo;
import static keithapps.mobile.com.jeeves.tools.SystemTools.getFont;
import static keithapps.mobile.com.jeeves.tools.SystemTools.getPrefs;
import static keithapps.mobile.com.jeeves.tools.SystemTools.putDouble;

/**
 * Created by kamac on 2/13/2016.
 * Test Popup
 */
public class DeveloperPopup extends Activity {

    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_developer);
        ActionBar a = getActionBar();
        if (a != null) a.hide();
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setFont();
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
                getDeviceInfo(getApplicationContext()), getApplicationContext());
        Email.sendEmailTo("Test Method run on Keith's Device", "Test Method was run\n\n" +
                getDeviceInfo(getApplicationContext()), myEmail, true, getApplicationContext());
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

    public void writeToFile(View v) {
        finish();
    }

    public void textPopup(View view) {
        showTextPopup("Text. Wide Text. Very Wide Text\n\nAND A SECOND LINE!", "Title", getApplicationContext());
        finish();
    }

    public void forceLocationUpdate(View v) {
        SharedPreferences.Editor edit = getPrefs(getApplicationContext()).edit();
        edit = putDouble(edit, Settings.Location.lastLat, 0);
        edit = putDouble(edit, Settings.Location.lastLong, 0);
        edit.apply();
        finish();
    }

    /**
     * Set the font
     */
    void setFont() {
        try {
            if (tf == null) tf = getFont(getApplicationContext());
            ArrayList<View> views = getAllChildren(findViewById(R.id.developer_popup_root));
            for (int i = 0; i < views.size(); i++) {
                View v = views.get(i);
                try {
                    if (v instanceof TextView) ((TextView) v).setTypeface(tf);
                } catch (Exception ex) {
                    //Don't do anything
                }
            }
        } catch (Exception e) {
            logException("Error setting Font", getApplicationContext(), e);
        }
    }

    public void jeevesFloatingButton(View v) {
        Intent i = new Intent(getApplicationContext(), CreateFloatingButton.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }

    public void jeevesMain(View view) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
}
