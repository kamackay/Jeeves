package keithapps.mobile.com.jeeves.popups;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.Settings;

import static keithapps.mobile.com.jeeves.Global.emailException;
import static keithapps.mobile.com.jeeves.Global.getAllChildren;
import static keithapps.mobile.com.jeeves.Global.getTimestamp;
import static keithapps.mobile.com.jeeves.Global.logException;
import static keithapps.mobile.com.jeeves.Global.writeToLog;
import static keithapps.mobile.com.jeeves.MainService.updateNotification;

public class AdderallPopup extends Activity {
    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_adderall_popup);
            ActionBar a = getActionBar();
            if (a != null) a.hide();
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            setValues();
            Button b20 = (Button) findViewById(R.id.adderallPopup_20Button),
                    b10 = (Button) findViewById(R.id.adderallPopup_10Button),
                    b5 = (Button) findViewById(R.id.adderallPopup_5Button);
            b20.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs = getSharedPreferences(Settings.sharedPrefs_code,
                            MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putInt(Settings.Adderall.adderall_count,
                            prefs.getInt(Settings.Adderall.adderall_count, 0) + 20);
                    edit.putString(Settings.Adderall.timeSince, getTimestamp());
                    edit.apply();
                    writeToLog(String.format(Locale.getDefault(), "Took 20 mg of Adderall (%d mg total)",
                            prefs.getInt(Settings.Adderall.adderall_count, 0)),
                            getApplicationContext());
                    finish();
                    updateNotification(getApplicationContext());
                }
            });
            b10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs = getSharedPreferences(Settings.sharedPrefs_code,
                            MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putInt(Settings.Adderall.adderall_count,
                            prefs.getInt(Settings.Adderall.adderall_count, 0) + 10);
                    edit.putString(Settings.Adderall.timeSince, getTimestamp());
                    edit.apply();
                    writeToLog(String.format(Locale.getDefault(), "Took 10 mg of Adderall (%d mg total)",
                            prefs.getInt(Settings.Adderall.adderall_count, 0)),
                            getApplicationContext());
                    finish();
                    updateNotification(getApplicationContext());
                }
            });
            b5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs = getSharedPreferences(Settings.sharedPrefs_code,
                            MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putInt(Settings.Adderall.adderall_count,
                            prefs.getInt(Settings.Adderall.adderall_count, 0) + 5);
                    edit.putString(Settings.Adderall.timeSince, getTimestamp());
                    edit.apply();
                    writeToLog(String.format(Locale.getDefault(), "Took 5 mg of Adderall (%d mg total)",
                            prefs.getInt(Settings.Adderall.adderall_count, 0)),
                            getApplicationContext());
                    finish();
                    updateNotification(getApplicationContext());
                }
            });
            setFont();
            writeToLog("Adderall Popup shown", getApplicationContext());
        } catch (Exception e) {
            emailException("Error loading Adderall popup", getApplicationContext(), e);
        }
    }

    public void clearAdderall(View view) {
        try {
            SharedPreferences prefs = getSharedPreferences(Settings.sharedPrefs_code,
                    MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt(Settings.Adderall.adderall_count, 0);
            edit.apply();
            writeToLog("Cleared Adderall Information", getApplicationContext());
            updateNotification(getApplicationContext());
            setValues();
        } catch (Exception e) {
            emailException("Error clearing Adderall Information", getApplicationContext(), e);
        }
    }

    public void setValues() {
        try {
            SharedPreferences prefs = getSharedPreferences(Settings.sharedPrefs_code, MODE_PRIVATE);
            TextView currentAmount = (TextView) findViewById(R.id.adderallPopup_current);
            int current = prefs.getInt(Settings.Adderall.adderall_count, 0);
            currentAmount.setText(String.format(Locale.getDefault(), "%d mg", current));
            if (current <= 30) currentAmount.setTextColor(Color.GREEN);
            else if (current > 60) currentAmount.setTextColor(Color.RED);
            else currentAmount.setTextColor(Color.YELLOW);
            TextView lastTime_min = (TextView) findViewById(R.id.adderallPopup_lastTime_minutes),
                    lastTime_hours = (TextView) findViewById(R.id.adderallPopup_lastTime_hours);
            try {
                String timestamp_last = prefs.getString(Settings.Adderall.timeSince, "");
                String timestamp_now = getTimestamp();
                SimpleDateFormat format = new SimpleDateFormat("MM/dd-HH:mm:ss", Locale.US);
                long difference = format.parse(timestamp_now).getTime() -
                        format.parse(timestamp_last).getTime();
                long hours = difference / (1000 * 60 * 60), minutes = (difference / 60000) % 60;
                lastTime_hours.setText(String.format(Locale.getDefault(), "%d", hours));
                lastTime_min.setText(String.format(Locale.getDefault(), "%d", minutes));
                if (hours == 0) {
                    lastTime_hours.setTextColor(Color.RED);
                    lastTime_min.setTextColor(Color.RED);
                } else if (hours < 2) {
                    lastTime_hours.setTextColor(Color.YELLOW);
                    lastTime_min.setTextColor(Color.YELLOW);
                } else {
                    lastTime_hours.setTextColor(Color.GREEN);
                    lastTime_min.setTextColor(Color.GREEN);
                }
            } catch (Exception e) {
                lastTime_hours.setText("?");
                lastTime_min.setText("?");
            }
        } catch (Exception e) {
            emailException("Error loading values in Adderall Popup", getApplicationContext(), e);
        }
    }

    public void setLastTime(View v) {///**
        try {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);
            TimePickerDialog timePicker;
            timePicker = new TimePickerDialog(AdderallPopup.this, R.style.TimePickerDialogStyle,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                                    MODE_PRIVATE).edit();
                            edit.putString(Settings.Adderall.timeSince, String.format(Locale.getDefault(),
                                    "%s%02d:%02d:00", getTimestamp().substring(0, 6), selectedHour,
                                    selectedMinute));
                            edit.apply();
                            updateNotification(getApplicationContext());
                            setValues();
                        }
                    }, hour, minute, false);
            timePicker.setTitle("What time did you last take Adderall?");
            timePicker.show();//*/
        } catch (Exception e) {
            emailException("Error setting last time in Adderall Popup", getApplicationContext(), e);
        }
    }

    void setFont() {
        try {
            if (tf == null)
                tf = Typeface.createFromAsset(getAssets(), "arial.ttf");
            if (tf == null) return;
            ArrayList<View> views = getAllChildren(findViewById(R.id.adderallPopup_root));
            for (int i = 0; i < views.size(); i++) {
                View v = views.get(i);
                if (v instanceof TextView) {
                    try {
                        ((TextView) v).setTypeface(tf);
                    } catch (Exception e) {
                        //It's cool, move on
                    }
                }
            }
        } catch (Exception e) {
            logException("Error loading font in Adderall Popup", getApplicationContext(), e);
        }
    }

    /**
     * When leaving the activity
     *
     * @param outState out State
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        finish();
    }

    /**
     * Pause the popup
     */
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
