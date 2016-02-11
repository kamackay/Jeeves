package keithapps.mobile.com.jeeves;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.text.WordUtils;

import keithapps.mobile.com.jeeves.listeners.TextChangeListener;

import static keithapps.mobile.com.jeeves.Global.getVersionName;
import static keithapps.mobile.com.jeeves.Global.isServiceRunning;
import static keithapps.mobile.com.jeeves.Global.showScreenSize;
import static keithapps.mobile.com.jeeves.MainService.showNotification;
import static keithapps.mobile.com.jeeves.ModeChangeView.SELECTED_LEAVE;
import static keithapps.mobile.com.jeeves.ModeChangeView.SELECTED_OFF;
import static keithapps.mobile.com.jeeves.ModeChangeView.SELECTED_ON;

/**
 * The main activity, which contains all of the changes that can be made to the Service's settings
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Is the main screen showing?
     */
    private boolean mainShowing;

    /**
     * Creation event
     *
     * @param savedInstanceState idk
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
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
     * Initialize
     */
    private void init() {
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
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"keith.mackay3@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT, "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(),
                            "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Switch switchShowNotification = (Switch) findViewById(R.id.settingsScreen_showNotification);
        switchShowNotification.setChecked(prefs.getBoolean(getString(R.string.settings_showNotification), true));
        switchShowNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getSharedPreferences(Settings.sharedPrefs_code, MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putBoolean(getString(R.string.settings_showNotification), isChecked);
                edit.apply();
                showNotification(preferences.getInt(Settings.current_mode,
                        ManageVolume.Mode.A), getApplicationContext());
            }
        });
        Switch switchShowBCV = (Switch) findViewById(R.id.settingsScreen_showBCV);
        switchShowBCV.setChecked(prefs.getBoolean(getString(R.string.settings_showBigContentView), false));
        switchShowBCV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getSharedPreferences(Settings.sharedPrefs_code, MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putBoolean(getString(R.string.settings_showBigContentView), isChecked);
                edit.apply();
                showNotification(preferences.getInt(Settings.current_mode,
                        ManageVolume.Mode.A), getApplicationContext());
            }
        });
        Switch switchShowHeadphones = (Switch) findViewById(R.id.settingsScreen_showHeadphonePopup);
        switchShowHeadphones.setChecked(prefs.getBoolean(getString(R.string.settings_showHeadphonesPopup), true));
        switchShowHeadphones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getSharedPreferences(Settings.sharedPrefs_code, MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putBoolean(getString(R.string.settings_showHeadphonesPopup), isChecked);
                edit.apply();
                showNotification(preferences.getInt(Settings.current_mode,
                        ManageVolume.Mode.A), getApplicationContext());
            }
        });
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
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //ActionBar bar = getActionBar();
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

    private void showSystemInfo() {
        String name = "";
        switch (Build.VERSION.SDK_INT) {
            case Build.VERSION_CODES.KITKAT:
                name = "KitKat";
                break;
            case Build.VERSION_CODES.LOLLIPOP:
                name = "Lollipop";
                break;
            case Build.VERSION_CODES.LOLLIPOP_MR1:
                name = "Lollipop";
                break;
            case Build.VERSION_CODES.M:
                name = "Marshmallow";
                break;
        }
        String s = String.format("Android %d: %s\n\n%s %s\n\n", Build.VERSION.SDK_INT, name,
                WordUtils.capitalizeFully(Build.MANUFACTURER), Build.MODEL);
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
                        tv.setOnLongClickListener(getProcessViewListener());
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
     * Get a listener for the process that was long pressed.
     *
     * @return new listener for the processtextview. need one per.
     */

    private View.OnLongClickListener getProcessViewListener() {
        return new View.OnLongClickListener() {
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
                            //Something may be wrong
                        }
                    }
                });
                b.setMessage("Verify that you want to close this application");
                b.create().show();
                return true;
            }
        };
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
        init();
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
            case KeyEvent.KEYCODE_MENU:
                //Absorb the key event
                return true;
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
        finish();
    }
}
