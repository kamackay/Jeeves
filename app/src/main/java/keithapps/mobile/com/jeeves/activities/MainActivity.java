package keithapps.mobile.com.jeeves.activities;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import keithapps.mobile.com.jeeves.MainService;
import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.activities.popups.KeithToast;
import keithapps.mobile.com.jeeves.listeners.SwipeListener;
import keithapps.mobile.com.jeeves.listeners.TextChangeListener;
import keithapps.mobile.com.jeeves.tools.Email;
import keithapps.mobile.com.jeeves.tools.Log;
import keithapps.mobile.com.jeeves.tools.ManageVolume;
import keithapps.mobile.com.jeeves.tools.Settings;
import keithapps.mobile.com.jeeves.views.ModeChangeView;
import keithapps.mobile.com.jeeves.views.ProcessView;
import keithapps.mobile.com.jeeves.views.SettingsSwitch;

import static keithapps.mobile.com.jeeves.MainService.showNotification;
import static keithapps.mobile.com.jeeves.tools.Email.emailException;
import static keithapps.mobile.com.jeeves.tools.Email.myEmail;
import static keithapps.mobile.com.jeeves.tools.Email.sendEmail;
import static keithapps.mobile.com.jeeves.tools.GlobalTools.getAllChildren;
import static keithapps.mobile.com.jeeves.tools.GlobalTools.isServiceRunning;
import static keithapps.mobile.com.jeeves.tools.GlobalTools.testMethod;
import static keithapps.mobile.com.jeeves.tools.Log.logException;
import static keithapps.mobile.com.jeeves.tools.SystemTools.getDeviceInfo;
import static keithapps.mobile.com.jeeves.tools.SystemTools.getVersionName;
import static keithapps.mobile.com.jeeves.tools.SystemTools.showScreenSize;
import static keithapps.mobile.com.jeeves.views.ModeChangeView.SELECTED_LEAVE;
import static keithapps.mobile.com.jeeves.views.ModeChangeView.SELECTED_OFF;
import static keithapps.mobile.com.jeeves.views.ModeChangeView.SELECTED_ON;

/**
 * The main activity, which contains all of the changes that can be made to the Service's settings
 */
public class MainActivity extends AppCompatActivity {
    /**
     * The font
     */
    Typeface tf;
    int mode;
    /**
     * Is the main screen showing?
     */
    private boolean mainShowing;
    /**
     * The long click listener for the process views
     */
    View.OnLongClickListener processViewListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(final View v) {
            AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
            b.setTitle("Are you sure you'd like to kill this process?");
            b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        int pid = ((ProcessView) v).getPID();
                        try {
                            android.os.Process.sendSignal(pid, Process.SIGNAL_KILL);
                            Process.killProcess(pid);
                        } catch (Exception e) {
                            KeithToast.show("Error killing process", getApplicationContext());
                            Log.writeToLog(String.format("Error killing process %s\n%s",
                                    e.getLocalizedMessage(), e.getMessage()),
                                    getApplicationContext());
                            populateProcesses(null);
                            return;
                        }
                        boolean killed = true;
                        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        for (ActivityManager.RunningServiceInfo ser : manager.getRunningServices(Integer.MAX_VALUE))
                            if (ser.pid == pid) killed = false;
                        if (!killed)
                            KeithToast.show("Couldn't kill process", getApplicationContext());
                        else
                            KeithToast.show("Successfully Killed", getApplicationContext());
                        populateProcesses(null);
                    } catch (Exception exc) {
                        KeithToast.show("Error killing process", getApplicationContext());
                        Log.writeToLog(String.format("Error killing process %s\n%s",
                                exc.getLocalizedMessage(), exc.getMessage()),
                                getApplicationContext());
                    }
                }
            });
            b.setMessage("Verify that you want to close this application");
            b.create().show();
            return true;
        }
    };
    /**
     * The main frame to load all of the screens into
     */
    private FrameLayout frame;
    SwipeListener swipeListener = new SwipeListener() {
        @Override
        public void onSwipe(Details details) {
            if (details.getDirection() == Direction.Right) {
                if (mode == 2) showModeSettings();
                else if (mode == 3) showFeatures();
                else if (mode == 4) showFeedback();
            } else if (details.getDirection() == Direction.Left) {
                if (mode == 1) showFeatures();
                else if (mode == 2) showFeedback();
                else if (mode == 3) showPermissions();
            }
        }
    };
    /**
     * The runnable to send feedback info
     */
    Runnable sendFeedback = new Runnable() {
        @Override
        public void run() {
            try {
                final String header = "Feedback: " +
                        ((EditText) findViewById(R.id.feedback_titleText)).getText().toString().trim(),
                        message = String.format("%s\n\nSent from Device:\n%s",
                                ((EditText) findViewById(R.id.feedback_bodyText))
                                        .getText().toString().trim(),
                                getDeviceInfo(getApplicationContext()));
                if (header.isEmpty() || message.isEmpty()) return;
                sendEmail(header, message, getApplicationContext());
                Email.sendEmailTo(header, message, myEmail, getApplicationContext());
                hideKeyboard();
                KeithToast.show("Feedback Sent\nThank you!", getApplicationContext());
                showModeSettings(null);
            } catch (Exception e) {
                emailException("Error Sending feedback email", getApplicationContext(), e);
                KeithToast.show("Error sending feedback", getApplicationContext());
            }
        }
    };

    /**
     * Creation event
     *
     * @param savedInstanceState idk
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            tf = Typeface.createFromAsset(getAssets(), "arial.ttf");
        } catch (Exception e) {
            logException("Error Loading Typeface", getApplicationContext(), e);
        }
        frame = (FrameLayout) findViewById(R.id.mainScreen_frame);
        showModeSettings(null);
    }

    /**
     * Show the Features on the main screen.
     *
     * @param useless Not used, feel free to pass null. Only here to be used as an onClick event
     */
    public void showFeatures(View useless) {
        mode = 2;
        frame = (FrameLayout) findViewById(R.id.mainScreen_frame);
        frame.removeAllViews();
        LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.layout_features_settings, frame, true);
        SharedPreferences prefs = getPrefs();
        EditText frequency = (EditText) findViewById(R.id.intrusivePopup_frequency);
        frequency.setText(String.valueOf(prefs.getInt(Settings.intrusivePopupFrequency, 50)));
        frequency.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                int n = Integer.parseInt(s.toString());
                if (n >= 0 && n <= 999) {
                    SharedPreferences.Editor edit = getPrefs().edit();
                    edit.putInt(Settings.intrusivePopupFrequency, n);
                    edit.apply();
                }
            }
        });
        SettingsSwitch switchShowNotification =
                (SettingsSwitch) findViewById(R.id.settingsScreen_showNotification);
        switchShowNotification.setAfterChangeEvent(new Runnable() {
            @Override
            public void run() {
                showNotification(getSharedPreferences(Settings.sharedPrefs_code, MODE_PRIVATE)
                                .getInt(Settings.current_mode, ManageVolume.Mode.A),
                        getApplicationContext());
            }
        });
        switchShowNotification.setMySetting(Settings.showNotification);
        final SettingsSwitch switchIntrusivePopup =
                (SettingsSwitch) findViewById(R.id.features_showIntrusivePopup);
        switchIntrusivePopup.setAfterChangeEvent(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.features_intrusivePopup_frequency_root)
                        .setVisibility(switchIntrusivePopup.isChecked() ? View.VISIBLE : View.GONE);
            }
        });
        switchIntrusivePopup.setMySetting(Settings.showScreamingSunRandomly);
        ((SettingsSwitch) findViewById(R.id.settingsScreen_showHeadphonePopup))
                .setMySetting(Settings.showHeadphonePopup);
        getApplicationContext().setTheme(R.style.AppTheme);
        setFont();
        findViewById(R.id.main_buttonBar_2).setBackgroundResource(R.color.lighter_background);
        findViewById(R.id.main_buttonBar_3).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.main_buttonBar_1).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.main_buttonBar_4).setBackgroundResource(android.R.color.transparent);
        hideKeyboard();
        setAllSwipes();
    }

    /**
     * Show the mode settings on the Main Screen
     *
     * @param useless Not used, feel free to pass null. Only here so that it can be used as an
     *                onClick event
     */
    public void showModeSettings(View useless) {
        mode = 1;
        frame = (FrameLayout) findViewById(R.id.mainScreen_frame);
        frame.removeAllViews();
        LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.layout_mode_settings, frame, true);
        SharedPreferences prefs = getPrefs();
        EditText et_A = (EditText) findViewById(R.id.main_text_A),
                et_B = (EditText) findViewById(R.id.main_text_B),
                et_C = (EditText) findViewById(R.id.main_text_C),
                et_D = (EditText) findViewById(R.id.main_text_D);
        et_A.setText(prefs.getString(Settings.action_a_name, getString(R.string.text_home)));
        et_B.setText(prefs.getString(Settings.action_b_name, getString(R.string.text_class)));
        et_C.setText(prefs.getString(Settings.action_c_name, getString(R.string.text_out)));
        et_D.setText(prefs.getString(Settings.action_d_name, getString(R.string.text_car)));
        et_A.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        Context.MODE_PRIVATE).edit();
                edit.putString(Settings.action_a_name, s.toString().trim());
                edit.apply();
            }
        });
        et_B.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        Context.MODE_PRIVATE).edit();
                edit.putString(Settings.action_b_name, s.toString().trim());
                edit.apply();
            }
        });
        et_C.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        Context.MODE_PRIVATE).edit();
                edit.putString(Settings.action_c_name, s.toString().trim());
                edit.apply();
            }
        });
        et_D.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        Context.MODE_PRIVATE).edit();
                edit.putString(Settings.action_d_name, s.toString().trim());
                edit.apply();
            }
        });
        mainShowing = true;
        final Spinner[] spinners = getSpinners();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.percentages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        AdapterView.OnItemSelectedListener saveListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor prefs = getPrefs().edit();
                prefs.putInt(Settings.A.ringtoneVolume, spinners[0].getSelectedItemPosition());
                prefs.putInt(Settings.A.mediaVolume, spinners[1].getSelectedItemPosition());
                prefs.putInt(Settings.A.systemVolume, spinners[2].getSelectedItemPosition());
                prefs.putInt(Settings.A.notificationVolume, spinners[3].getSelectedItemPosition());
                prefs.putInt(Settings.A.alarmVolume, spinners[4].getSelectedItemPosition());
                prefs.putInt(Settings.B.ringtoneVolume, spinners[5].getSelectedItemPosition());
                prefs.putInt(Settings.B.mediaVolume, spinners[6].getSelectedItemPosition());
                prefs.putInt(Settings.B.systemVolume, spinners[7].getSelectedItemPosition());
                prefs.putInt(Settings.B.notificationVolume, spinners[8].getSelectedItemPosition());
                prefs.putInt(Settings.B.alarmVolume, spinners[9].getSelectedItemPosition());
                prefs.putInt(Settings.C.ringtoneVolume, spinners[10].getSelectedItemPosition());
                prefs.putInt(Settings.C.mediaVolume, spinners[11].getSelectedItemPosition());
                prefs.putInt(Settings.C.systemVolume, spinners[12].getSelectedItemPosition());
                prefs.putInt(Settings.C.notificationVolume, spinners[13].getSelectedItemPosition());
                prefs.putInt(Settings.C.alarmVolume, spinners[14].getSelectedItemPosition());
                prefs.putInt(Settings.D.ringtoneVolume, spinners[15].getSelectedItemPosition());
                prefs.putInt(Settings.D.mediaVolume, spinners[16].getSelectedItemPosition());
                prefs.putInt(Settings.D.systemVolume, spinners[17].getSelectedItemPosition());
                prefs.putInt(Settings.D.notificationVolume, spinners[18].getSelectedItemPosition());
                prefs.putInt(Settings.D.alarmVolume, spinners[19].getSelectedItemPosition());
                prefs.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Well, don't do anything
            }
        };
        for (final Spinner spinner : spinners) {
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(saveListener);
        }
        spinners[0].setSelection(prefs.getInt(Settings.A.ringtoneVolume, 5));
        spinners[1].setSelection(prefs.getInt(Settings.A.mediaVolume, 5));
        spinners[2].setSelection(prefs.getInt(Settings.A.systemVolume, 5));
        spinners[3].setSelection(prefs.getInt(Settings.A.notificationVolume, 1));
        spinners[4].setSelection(prefs.getInt(Settings.A.alarmVolume, 10));
        spinners[5].setSelection(prefs.getInt(Settings.B.ringtoneVolume, 0));
        spinners[6].setSelection(prefs.getInt(Settings.B.mediaVolume, 0));
        spinners[7].setSelection(prefs.getInt(Settings.B.systemVolume, 0));
        spinners[8].setSelection(prefs.getInt(Settings.B.notificationVolume, 0));
        spinners[9].setSelection(prefs.getInt(Settings.B.alarmVolume, 0));
        spinners[10].setSelection(prefs.getInt(Settings.C.ringtoneVolume, 5));
        spinners[11].setSelection(prefs.getInt(Settings.C.mediaVolume, 5));
        spinners[12].setSelection(prefs.getInt(Settings.C.systemVolume, 5));
        spinners[13].setSelection(prefs.getInt(Settings.C.notificationVolume, 5));
        spinners[14].setSelection(prefs.getInt(Settings.C.alarmVolume, 10));
        spinners[15].setSelection(prefs.getInt(Settings.D.ringtoneVolume, 5));
        spinners[16].setSelection(prefs.getInt(Settings.D.mediaVolume, 5));
        spinners[17].setSelection(prefs.getInt(Settings.D.systemVolume, 5));
        spinners[18].setSelection(prefs.getInt(Settings.D.notificationVolume, 5));
        spinners[19].setSelection(prefs.getInt(Settings.D.alarmVolume, 10));
        TextView t = (TextView) findViewById(R.id.activity_main_versionText);
        t.setText(String.format("Version: %s", getVersionName(getApplicationContext())));
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testMethod(getApplicationContext());
            }
        });
        t.setOnTouchListener(swipeListener);
        final ModeChangeView mcv_A_wifi = (ModeChangeView) findViewById(R.id.settingsScreen_A_WiFiOption),
                mcv_B_wifi = (ModeChangeView) findViewById(R.id.settingsScreen_B_WiFiOption),
                mcv_C_wifi = (ModeChangeView) findViewById(R.id.settingsScreen_C_WiFiOption),
                mcv_D_wifi = (ModeChangeView) findViewById(R.id.settingsScreen_D_WiFiOption),
                mcv_A_bluetooth = (ModeChangeView) findViewById(R.id.settingsScreen_A_bluetoothOption),
                mcv_C_bluetooth = (ModeChangeView) findViewById(R.id.settingsScreen_C_bluetoothOption),
                mcv_B_bluetooth = (ModeChangeView) findViewById(R.id.settingsScreen_B_bluetoothOption),
                mcv_D_bluetooth = (ModeChangeView) findViewById(R.id.settingsScreen_D_bluetoothOption);
        mcv_A_wifi.setText("WiFi");
        mcv_B_wifi.setText("WiFi");
        mcv_C_wifi.setText("WiFi");
        mcv_D_wifi.setText("WiFi");
        mcv_A_bluetooth.setText("Bluetooth");
        mcv_B_bluetooth.setText("Bluetooth");
        mcv_C_bluetooth.setText("Bluetooth");
        mcv_D_bluetooth.setText("Bluetooth");
        mcv_A_wifi.setItemChangedListener(new ModeChangeView.ItemChangedListener() {
            @Override
            public void run() {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        MODE_PRIVATE).edit();
                edit.putInt(getString(R.string.settings_a_wifiAction), mcv_A_wifi.getSelection());
                edit.apply();
            }
        });
        mcv_C_wifi.setItemChangedListener(new ModeChangeView.ItemChangedListener() {
            @Override
            public void run() {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        MODE_PRIVATE).edit();
                edit.putInt(getString(R.string.settings_c_wifiAction), mcv_C_wifi.getSelection());
                edit.apply();
            }
        });
        mcv_B_wifi.setItemChangedListener(new ModeChangeView.ItemChangedListener() {
            @Override
            public void run() {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        MODE_PRIVATE).edit();
                edit.putInt(getString(R.string.settings_b_wifiAction), mcv_B_wifi.getSelection());
                edit.apply();
            }
        });
        mcv_D_wifi.setItemChangedListener(new ModeChangeView.ItemChangedListener() {
            @Override
            public void run() {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        MODE_PRIVATE).edit();
                edit.putInt(getString(R.string.settings_d_wifiAction), mcv_D_wifi.getSelection());
                edit.apply();
            }
        });
        mcv_A_bluetooth.setItemChangedListener(new ModeChangeView.ItemChangedListener() {
            @Override
            public void run() {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        MODE_PRIVATE).edit();
                edit.putInt(getString(R.string.settings_a_bluetoothAction), mcv_A_bluetooth.getSelection());
                edit.apply();
            }
        });
        mcv_B_bluetooth.setItemChangedListener(new ModeChangeView.ItemChangedListener() {
            @Override
            public void run() {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        MODE_PRIVATE).edit();
                edit.putInt(getString(R.string.settings_b_bluetoothAction), mcv_B_bluetooth.getSelection());
                edit.apply();
            }
        });
        mcv_C_bluetooth.setItemChangedListener(new ModeChangeView.ItemChangedListener() {
            @Override
            public void run() {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        MODE_PRIVATE).edit();
                edit.putInt(getString(R.string.settings_c_bluetoothAction), mcv_C_bluetooth.getSelection());
                edit.apply();
            }
        });
        mcv_D_bluetooth.setItemChangedListener(new ModeChangeView.ItemChangedListener() {
            @Override
            public void run() {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        MODE_PRIVATE).edit();
                edit.putInt(getString(R.string.settings_d_bluetoothAction), mcv_D_bluetooth.getSelection());
                edit.apply();
            }
        });
        mcv_A_wifi.showReboot(true);
        mcv_B_wifi.showReboot(true);
        mcv_C_wifi.showReboot(true);
        mcv_D_wifi.showReboot(true);
        mcv_A_wifi.setSelection(getSharedPreferences(Settings.sharedPrefs_code,
                MODE_PRIVATE).getInt(getString(R.string.settings_a_wifiAction), SELECTED_ON));
        mcv_B_wifi.setSelection(getSharedPreferences(Settings.sharedPrefs_code,
                MODE_PRIVATE).getInt(getString(R.string.settings_b_wifiAction), SELECTED_ON));
        mcv_C_wifi.setSelection(getSharedPreferences(Settings.sharedPrefs_code,
                MODE_PRIVATE).getInt(getString(R.string.settings_c_wifiAction), SELECTED_OFF));
        mcv_D_wifi.setSelection(getSharedPreferences(Settings.sharedPrefs_code,
                MODE_PRIVATE).getInt(getString(R.string.settings_d_wifiAction), SELECTED_OFF));
        mcv_A_bluetooth.setSelection(getSharedPreferences(Settings.sharedPrefs_code,
                MODE_PRIVATE).getInt(getString(R.string.settings_a_bluetoothAction), SELECTED_LEAVE));
        mcv_B_bluetooth.setSelection(getSharedPreferences(Settings.sharedPrefs_code,
                MODE_PRIVATE).getInt(getString(R.string.settings_b_bluetoothAction), SELECTED_LEAVE));
        mcv_C_bluetooth.setSelection(getSharedPreferences(Settings.sharedPrefs_code,
                MODE_PRIVATE).getInt(getString(R.string.settings_c_bluetoothAction), SELECTED_LEAVE));
        mcv_D_bluetooth.setSelection(getSharedPreferences(Settings.sharedPrefs_code,
                MODE_PRIVATE).getInt(getString(R.string.settings_d_bluetoothAction), SELECTED_LEAVE));
        final RadioGroup rdogrp_priorityA = (RadioGroup) findViewById(R.id.main_priorityRdoGrp_A),
                rdogrp_priorityB = (RadioGroup) findViewById(R.id.main_priorityRdoGrp_B),
                rdogrp_priorityC = (RadioGroup) findViewById(R.id.main_priorityRdoGrp_C),
                rdogrp_priorityD = (RadioGroup) findViewById(R.id.main_priorityRdoGrp_D);
        rdogrp_priorityA.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        MODE_PRIVATE).edit();
                if (checkedId == R.id.main_prioRB_A_min)
                    edit.putInt(Settings.A.notificationPriority, -2);
                else if (checkedId == R.id.main_prioRB_A_low)
                    edit.putInt(Settings.A.notificationPriority, -1);
                else if (checkedId == R.id.main_prioRB_A_def)
                    edit.putInt(Settings.A.notificationPriority, 0);
                else if (checkedId == R.id.main_prioRB_A_high)
                    edit.putInt(Settings.A.notificationPriority, 1);
                else if (checkedId == R.id.main_prioRB_A_max)
                    edit.putInt(Settings.A.notificationPriority, 2);
                edit.apply();
            }
        });
        rdogrp_priorityB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        MODE_PRIVATE).edit();
                if (checkedId == R.id.main_prioRB_B_min)
                    edit.putInt(Settings.B.notificationPriority, -2);
                else if (checkedId == R.id.main_prioRB_B_low)
                    edit.putInt(Settings.B.notificationPriority, -1);
                else if (checkedId == R.id.main_prioRB_B_def)
                    edit.putInt(Settings.B.notificationPriority, 0);
                else if (checkedId == R.id.main_prioRB_B_high)
                    edit.putInt(Settings.B.notificationPriority, 1);
                else if (checkedId == R.id.main_prioRB_B_max)
                    edit.putInt(Settings.B.notificationPriority, 2);
                edit.apply();
            }
        });
        rdogrp_priorityC.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        MODE_PRIVATE).edit();
                if (checkedId == R.id.main_prioRB_C_min)
                    edit.putInt(Settings.C.notificationPriority, -2);
                else if (checkedId == R.id.main_prioRB_C_low)
                    edit.putInt(Settings.C.notificationPriority, -1);
                else if (checkedId == R.id.main_prioRB_C_def)
                    edit.putInt(Settings.C.notificationPriority, 0);
                else if (checkedId == R.id.main_prioRB_C_high)
                    edit.putInt(Settings.C.notificationPriority, 1);
                else if (checkedId == R.id.main_prioRB_C_max)
                    edit.putInt(Settings.C.notificationPriority, 2);
                edit.apply();
            }
        });
        rdogrp_priorityD.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        MODE_PRIVATE).edit();
                if (checkedId == R.id.main_prioRB_D_min)
                    edit.putInt(Settings.D.notificationPriority, -2);
                else if (checkedId == R.id.main_prioRB_D_low)
                    edit.putInt(Settings.D.notificationPriority, -1);
                else if (checkedId == R.id.main_prioRB_D_def)
                    edit.putInt(Settings.D.notificationPriority, 0);
                else if (checkedId == R.id.main_prioRB_D_high)
                    edit.putInt(Settings.D.notificationPriority, 1);
                else if (checkedId == R.id.main_prioRB_D_max)
                    edit.putInt(Settings.D.notificationPriority, 2);
                edit.apply();
            }
        });
        int iA = prefs.getInt(Settings.A.notificationPriority, -2),
                iB = prefs.getInt(Settings.B.notificationPriority, -2),
                iC = prefs.getInt(Settings.C.notificationPriority, -2),
                iD = prefs.getInt(Settings.D.notificationPriority, -2);
        if (iA == -2) ((RadioButton) findViewById(R.id.main_prioRB_A_min)).setChecked(true);
        else if (iA == -1) ((RadioButton) findViewById(R.id.main_prioRB_A_low)).setChecked(true);
        else if (iA == 0) ((RadioButton) findViewById(R.id.main_prioRB_A_def)).setChecked(true);
        else if (iA == 1) ((RadioButton) findViewById(R.id.main_prioRB_A_high)).setChecked(true);
        else if (iA == 2) ((RadioButton) findViewById(R.id.main_prioRB_A_max)).setChecked(true);
        if (iB == -2) ((RadioButton) findViewById(R.id.main_prioRB_B_min)).setChecked(true);
        else if (iB == -1) ((RadioButton) findViewById(R.id.main_prioRB_B_low)).setChecked(true);
        else if (iB == 0) ((RadioButton) findViewById(R.id.main_prioRB_B_def)).setChecked(true);
        else if (iB == 1) ((RadioButton) findViewById(R.id.main_prioRB_B_high)).setChecked(true);
        else if (iB == 2) ((RadioButton) findViewById(R.id.main_prioRB_B_max)).setChecked(true);
        if (iC == -2) ((RadioButton) findViewById(R.id.main_prioRB_C_min)).setChecked(true);
        else if (iC == -1) ((RadioButton) findViewById(R.id.main_prioRB_C_low)).setChecked(true);
        else if (iC == 0) ((RadioButton) findViewById(R.id.main_prioRB_C_def)).setChecked(true);
        else if (iC == 1) ((RadioButton) findViewById(R.id.main_prioRB_C_high)).setChecked(true);
        else if (iC == 2) ((RadioButton) findViewById(R.id.main_prioRB_C_max)).setChecked(true);
        if (iD == -2) ((RadioButton) findViewById(R.id.main_prioRB_D_min)).setChecked(true);
        else if (iD == -1) ((RadioButton) findViewById(R.id.main_prioRB_D_low)).setChecked(true);
        else if (iD == 0) ((RadioButton) findViewById(R.id.main_prioRB_D_def)).setChecked(true);
        else if (iD == 1) ((RadioButton) findViewById(R.id.main_prioRB_D_high)).setChecked(true);
        else if (iD == 2) ((RadioButton) findViewById(R.id.main_prioRB_D_max)).setChecked(true);
        hideKeyboard();
        getApplicationContext().setTheme(R.style.AppTheme);
        setFont();
        findViewById(R.id.main_buttonBar_1).setBackgroundResource(R.color.lighter_background);
        findViewById(R.id.main_buttonBar_2).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.main_buttonBar_3).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.main_buttonBar_4).setBackgroundResource(android.R.color.transparent);
        setAllSwipes();
    }

    void setAllSwipes() {
        try {
            if (frame != null)
                for (View view : getAllChildren(frame))
                    view.setOnTouchListener(swipeListener);
            findViewById(R.id.main_root).setOnTouchListener(swipeListener);
        } catch (Exception e) {
            //It's cool
        }
    }

    /**
     * Hide the keyboard from the screen
     */
    void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(),
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
            if (frame != null) {
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(frame.getWindowToken(), 0);
            }
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } catch (Exception e) {
            //Should be ok
        }
    }

    /**
     * Set the font
     */
    void setFont() {
        try {
            if (tf == null) return;
            ArrayList<View> views = getAllChildren(findViewById(R.id.main_root));
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

    /**
     * Get all of the spinners to interact with
     *
     * @return array of all of the spinners on the screen
     */
    private Spinner[] getSpinners() {
        return new Spinner[]{
                (Spinner) findViewById(R.id.settingsScreen_A_ringtoneVolume),
                (Spinner) findViewById(R.id.settingsScreen_A_mediaVolume),
                (Spinner) findViewById(R.id.settingsScreen_A_systemVolume),
                (Spinner) findViewById(R.id.settingsScreen_A_notificationVolume),
                (Spinner) findViewById(R.id.settingsScreen_A_alarmVolume),
                (Spinner) findViewById(R.id.settingsScreen_B_ringtoneVolume),
                (Spinner) findViewById(R.id.settingsScreen_B_mediaVolume),
                (Spinner) findViewById(R.id.settingsScreen_B_systemVolume),
                (Spinner) findViewById(R.id.settingsScreen_B_notificationVolume),
                (Spinner) findViewById(R.id.settingsScreen_B_alarmVolume),
                (Spinner) findViewById(R.id.settingsScreen_C_ringtoneVolume),
                (Spinner) findViewById(R.id.settingsScreen_C_mediaVolume),
                (Spinner) findViewById(R.id.settingsScreen_C_systemVolume),
                (Spinner) findViewById(R.id.settingsScreen_C_notificationVolume),
                (Spinner) findViewById(R.id.settingsScreen_C_alarmVolume),
                (Spinner) findViewById(R.id.settingsScreen_D_ringtoneVolume),
                (Spinner) findViewById(R.id.settingsScreen_D_mediaVolume),
                (Spinner) findViewById(R.id.settingsScreen_D_systemVolume),
                (Spinner) findViewById(R.id.settingsScreen_D_notificationVolume),
                (Spinner) findViewById(R.id.settingsScreen_D_alarmVolume)
        };
    }

    /**
     * Make the options menu using the xml file
     *
     * @param menu Passed by the system
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    /**
     * @param item Menu Item that was selected
     * @return pretty much always true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainMenu_showSysInfo:
                showSystemInfo();
                return true;
            case R.id.mainMenu_showAllRunningActions:
                setContentView(R.layout.running_processes_screen);
                populateProcesses(null);
                return true;
            case R.id.mainMenu_showScreenSize:
                showScreenSize(this);
                return true;
            case R.id.mainMenu_showLog:
                startActivity(new Intent(getApplicationContext(), LogActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Show the system info in a custom toast
     */
    private void showSystemInfo() {
        String s = getDeviceInfo(getApplicationContext()).replace("    ", "\n");
        KeithToast.show(s, getApplicationContext());
    }

    /**
     * Populate all of the processes to the scrollview
     *
     * @param v the sync button
     */
    public void populateProcesses(View v) {
        mainShowing = false;
        final Typeface tf = Typeface.createFromAsset(getAssets(), "calibri.ttf");
        final LinearLayout l = (LinearLayout) findViewById(R.id.runningProcesses_root);
        if (l.getChildCount() > 0) l.removeAllViews();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ActivityManager localActivityManager =
                        (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                for (final ActivityManager.RunningAppProcessInfo process :
                        localActivityManager.getRunningAppProcesses()) {
                    try {
                        final ProcessView tv = new ProcessView(getApplicationContext(), process);
                        if (tf != null) tv.setTypeface(tf);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                        tv.setOnLongClickListener(processViewListener);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                l.addView(tv);
                            }
                        });
                    } catch (Exception e) {  //It's all good}
                    }
                }
            }
        }).start();
    }

    /**
     * Get the shared preferences
     *
     * @return Shared Preferences
     */
    private SharedPreferences getPrefs() {
        return getSharedPreferences(Settings.sharedPrefs_code, MODE_PRIVATE);
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

    /**
     * Click the done button
     *
     * @param view the done button
     */
    public void clickDone(View view) {
        setContentView(R.layout.activity_main);
        showModeSettings(null);
    }

    /**
     * Absorb the hardware keypresses, because they annoy me
     *
     * @param keycode the key pressed
     * @param e       the key event
     * @return true to absorb event
     */
    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch (keycode) {
            case KeyEvent.KEYCODE_BACK:
                if (!mainShowing) clickDone(null);
                return true;
            case KeyEvent.KEYCODE_HOME:
                return true;
        }
        return super.onKeyDown(keycode, e);
    }

    /**
     * Save all appropriate fragment state.
     *
     * @param outState Out State
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * The onPause event
     */
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    /**
     * Show the feedback screen on the main screen
     *
     * @param useless Not used, feel free to pass null. Only here so this method can be an onClick
     *                Listener.
     */
    public void showFeedback(View useless) {
        mode = 3;
        findViewById(R.id.main_buttonBar_3).setBackgroundResource(R.color.lighter_background);
        findViewById(R.id.main_buttonBar_2).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.main_buttonBar_1).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.main_buttonBar_4).setBackgroundResource(android.R.color.transparent);
        frame = (FrameLayout) findViewById(R.id.mainScreen_frame);
        frame.removeAllViews();
        LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.layout_send_feedback, frame, true);
        setFont();
        getApplicationContext().setTheme(R.style.AppTheme);
        findViewById(R.id.sendFeedback_sendButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendFeedback.run();
                    }
                });
        View v = findViewById(R.id.feedback_titleText);
        v.requestFocus();
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .showSoftInput(v, InputMethodManager.SHOW_FORCED);
        setAllSwipes();
    }

    public void showFeatures() {
        showFeatures(null);
    }

    public void showModeSettings() {
        showModeSettings(null);
    }

    public void showFeedback() {
        showFeedback(null);
    }

    public void showPermissions() {
        showPermissions(null);
    }

    public void showPermissions(View useless) {
        mode = 4;
        findViewById(R.id.main_buttonBar_4).setBackgroundResource(R.color.lighter_background);
        findViewById(R.id.main_buttonBar_2).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.main_buttonBar_1).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.main_buttonBar_3).setBackgroundResource(android.R.color.transparent);
        if (frame == null) frame = (FrameLayout) findViewById(R.id.mainScreen_frame);
        frame.removeAllViews();
        LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.layout_permissions, frame, true);
        setFont();
        setAllSwipes();
        getApplicationContext().setTheme(R.style.AppTheme);
    }
}
