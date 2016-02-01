package keithapps.mobile.com.jeeves;

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
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

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
                (Spinner) findViewById(R.id.settingsScreen_home_ringtoneVolume),
                (Spinner) findViewById(R.id.settingsScreen_home_mediaVolume),
                (Spinner) findViewById(R.id.settingsScreen_home_systemVolume),
                (Spinner) findViewById(R.id.settingsScreen_home_notificationVolume),
                (Spinner) findViewById(R.id.settingsScreen_home_alarmVolume),
                (Spinner) findViewById(R.id.settingsScreen_class_ringtoneVolume),
                (Spinner) findViewById(R.id.settingsScreen_class_mediaVolume),
                (Spinner) findViewById(R.id.settingsScreen_class_systemVolume),
                (Spinner) findViewById(R.id.settingsScreen_class_notificationVolume),
                (Spinner) findViewById(R.id.settingsScreen_class_alarmVolume),
                (Spinner) findViewById(R.id.settingsScreen_out_ringtoneVolume),
                (Spinner) findViewById(R.id.settingsScreen_out_mediaVolume),
                (Spinner) findViewById(R.id.settingsScreen_out_systemVolume),
                (Spinner) findViewById(R.id.settingsScreen_out_notificationVolume),
                (Spinner) findViewById(R.id.settingsScreen_out_alarmVolume)
        };
    }

    /**
     * Initialize
     */
    private void init() {
        mainShowing = true;
        final Spinner[] spinners = getSpinners();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.percentages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        AdapterView.OnItemSelectedListener saveListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor prefs = getPrefs().edit();
                prefs.putInt(Global.SETTINGS.HOME.ringtoneVolume, spinners[0].getSelectedItemPosition());
                prefs.putInt(Global.SETTINGS.HOME.mediaVolume, spinners[1].getSelectedItemPosition());
                prefs.putInt(Global.SETTINGS.HOME.systemVolume, spinners[2].getSelectedItemPosition());
                prefs.putInt(Global.SETTINGS.HOME.notificationVolume, spinners[3].getSelectedItemPosition());
                prefs.putInt(Global.SETTINGS.HOME.alarmVolume, spinners[4].getSelectedItemPosition());
                prefs.putInt(Global.SETTINGS.CLASS.ringtoneVolume, spinners[5].getSelectedItemPosition());
                prefs.putInt(Global.SETTINGS.CLASS.mediaVolume, spinners[6].getSelectedItemPosition());
                prefs.putInt(Global.SETTINGS.CLASS.systemVolume, spinners[7].getSelectedItemPosition());
                prefs.putInt(Global.SETTINGS.CLASS.notificationVolume, spinners[8].getSelectedItemPosition());
                prefs.putInt(Global.SETTINGS.CLASS.alarmVolume, spinners[9].getSelectedItemPosition());
                prefs.putInt(Global.SETTINGS.OUT.ringtoneVolume, spinners[10].getSelectedItemPosition());
                prefs.putInt(Global.SETTINGS.OUT.mediaVolume, spinners[11].getSelectedItemPosition());
                prefs.putInt(Global.SETTINGS.OUT.systemVolume, spinners[12].getSelectedItemPosition());
                prefs.putInt(Global.SETTINGS.OUT.notificationVolume, spinners[13].getSelectedItemPosition());
                prefs.putInt(Global.SETTINGS.OUT.alarmVolume, spinners[14].getSelectedItemPosition());
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
        SharedPreferences prefs = getPrefs();
        spinners[0].setSelection(prefs.getInt(Global.SETTINGS.HOME.ringtoneVolume, 5));
        spinners[1].setSelection(prefs.getInt(Global.SETTINGS.HOME.mediaVolume, 5));
        spinners[2].setSelection(prefs.getInt(Global.SETTINGS.HOME.systemVolume, 5));
        spinners[3].setSelection(prefs.getInt(Global.SETTINGS.HOME.notificationVolume, 1));
        spinners[4].setSelection(prefs.getInt(Global.SETTINGS.HOME.alarmVolume, 10));
        spinners[5].setSelection(prefs.getInt(Global.SETTINGS.CLASS.ringtoneVolume, 0));
        spinners[6].setSelection(prefs.getInt(Global.SETTINGS.CLASS.mediaVolume, 0));
        spinners[7].setSelection(prefs.getInt(Global.SETTINGS.CLASS.systemVolume, 0));
        spinners[8].setSelection(prefs.getInt(Global.SETTINGS.CLASS.notificationVolume, 0));
        spinners[9].setSelection(prefs.getInt(Global.SETTINGS.CLASS.alarmVolume, 0));
        spinners[10].setSelection(prefs.getInt(Global.SETTINGS.OUT.ringtoneVolume, 5));
        spinners[11].setSelection(prefs.getInt(Global.SETTINGS.OUT.mediaVolume, 5));
        spinners[12].setSelection(prefs.getInt(Global.SETTINGS.OUT.systemVolume, 5));
        spinners[13].setSelection(prefs.getInt(Global.SETTINGS.OUT.notificationVolume, 5));
        spinners[14].setSelection(prefs.getInt(Global.SETTINGS.OUT.alarmVolume, 10));
        TextView t = (TextView) findViewById(R.id.activity_main_versionText);
        t.setText(String.format("Version: %s", getVersionName(getApplicationContext())));
        t.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences prefs = getSharedPreferences(
                        getString(R.string.sharedPrefrences_code), MODE_PRIVATE);
                if (prefs.getBoolean(getString(R.string.settings_isKeith), false)) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(getString(R.string.settings_isKeith), false);
                    editor.apply();
                    KeithToast.show("You are no longer a developer.", getApplicationContext());
                } else {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(getString(R.string.settings_isKeith), true);
                    editor.apply();
                    KeithToast.show("Hello, Keith", getApplicationContext());
                }
                return false;
            }
        });
        Switch switchShowNotification = (Switch) findViewById(R.id.settingsScreen_showNotification);
        switchShowNotification.setChecked(prefs.getBoolean(getString(R.string.settings_showNotification), true));
        switchShowNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getSharedPreferences(getString(
                        R.string.sharedPrefrences_code), MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putBoolean(getString(R.string.settings_showNotification), isChecked);
                edit.apply();
                showNotification(preferences.getInt(getString(R.string.current_mode),
                        ManageVolume.Mode.Home), getApplicationContext());
            }
        });
        Switch switchShowBCV = (Switch) findViewById(R.id.settingsScreen_showBCV);
        switchShowBCV.setChecked(prefs.getBoolean(getString(R.string.settings_showBigContentView), false));
        switchShowBCV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getSharedPreferences(getString(
                        R.string.sharedPrefrences_code), MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putBoolean(getString(R.string.settings_showBigContentView), isChecked);
                edit.apply();
                showNotification(preferences.getInt(getString(R.string.current_mode),
                        ManageVolume.Mode.Home), getApplicationContext());
            }
        });
        Switch switchShowHeadphones = (Switch) findViewById(R.id.settingsScreen_showHeadphonePopup);
        switchShowHeadphones.setChecked(prefs.getBoolean(getString(R.string.settings_showHeadphonesPopup), true));
        switchShowHeadphones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getSharedPreferences(getString(
                        R.string.sharedPrefrences_code), MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putBoolean(getString(R.string.settings_showHeadphonesPopup), isChecked);
                edit.apply();
                showNotification(preferences.getInt(getString(R.string.current_mode),
                        ManageVolume.Mode.Home), getApplicationContext());
            }
        });
        final ModeChangeView mcv_home_wifi = (ModeChangeView) findViewById(R.id.settingsScreen_home_WiFiOption),
                mcv_class_wifi = (ModeChangeView) findViewById(R.id.settingsScreen_class_WiFiOption),
                mcv_out_wifi = (ModeChangeView) findViewById(R.id.settingsScreen_out_WiFiOption),
                mcv_home_bluetooth = (ModeChangeView) findViewById(R.id.settingsScreen_home_bluetoothOption),
                mcv_out_bluetooth = (ModeChangeView) findViewById(R.id.settingsScreen_out_bluetoothOption),
                mcv_class_bluetooth = (ModeChangeView) findViewById(R.id.settingsScreen_class_bluetoothOption);
        mcv_home_wifi.setText("WiFi");
        mcv_class_wifi.setText("WiFi");
        mcv_out_wifi.setText("WiFi");
        mcv_home_bluetooth.setText("Bluetooth");
        mcv_class_bluetooth.setText("Bluetooth");
        mcv_out_bluetooth.setText("Bluetooth");
        mcv_home_wifi.setItemChangedListener(new ModeChangeView.ItemChangedListener() {
            @Override
            public void run() {
                SharedPreferences.Editor edit = getSharedPreferences(
                        getString(R.string.sharedPrefrences_code), MODE_PRIVATE).edit();
                edit.putInt(getString(R.string.settings_home_wifiAction), mcv_home_wifi.getSelection());
                edit.apply();
            }
        });
        mcv_out_wifi.setItemChangedListener(new ModeChangeView.ItemChangedListener() {
            @Override
            public void run() {
                SharedPreferences.Editor edit = getSharedPreferences(
                        getString(R.string.sharedPrefrences_code), MODE_PRIVATE).edit();
                edit.putInt(getString(R.string.settings_out_wifiAction), mcv_out_wifi.getSelection());
                edit.apply();
            }
        });
        mcv_class_wifi.setItemChangedListener(new ModeChangeView.ItemChangedListener() {
            @Override
            public void run() {
                SharedPreferences.Editor edit = getSharedPreferences(
                        getString(R.string.sharedPrefrences_code), MODE_PRIVATE).edit();
                edit.putInt(getString(R.string.settings_class_wifiAction), mcv_class_wifi.getSelection());
                edit.apply();
            }
        });
        mcv_home_bluetooth.setItemChangedListener(new ModeChangeView.ItemChangedListener() {
            @Override
            public void run() {
                SharedPreferences.Editor edit = getSharedPreferences(
                        getString(R.string.sharedPrefrences_code), MODE_PRIVATE).edit();
                edit.putInt(getString(R.string.settings_home_bluetoothAction), mcv_home_bluetooth.getSelection());
                edit.apply();
            }
        });
        mcv_class_bluetooth.setItemChangedListener(new ModeChangeView.ItemChangedListener() {
            @Override
            public void run() {
                SharedPreferences.Editor edit = getSharedPreferences(
                        getString(R.string.sharedPrefrences_code), MODE_PRIVATE).edit();
                edit.putInt(getString(R.string.settings_class_bluetoothAction), mcv_class_bluetooth.getSelection());
                edit.apply();
            }
        });
        mcv_out_bluetooth.setItemChangedListener(new ModeChangeView.ItemChangedListener() {
            @Override
            public void run() {
                SharedPreferences.Editor edit = getSharedPreferences(
                        getString(R.string.sharedPrefrences_code), MODE_PRIVATE).edit();
                edit.putInt(getString(R.string.settings_out_bluetoothAction), mcv_out_bluetooth.getSelection());
                edit.apply();
            }
        });
        mcv_home_wifi.setSelection(getSharedPreferences(getString(R.string.sharedPrefrences_code),
                MODE_PRIVATE).getInt(getString(R.string.settings_home_wifiAction), SELECTED_ON));
        mcv_class_wifi.setSelection(getSharedPreferences(getString(R.string.sharedPrefrences_code),
                MODE_PRIVATE).getInt(getString(R.string.settings_class_wifiAction), SELECTED_ON));
        mcv_out_wifi.setSelection(getSharedPreferences(getString(R.string.sharedPrefrences_code),
                MODE_PRIVATE).getInt(getString(R.string.settings_out_wifiAction), SELECTED_OFF));
        mcv_home_bluetooth.setSelection(getSharedPreferences(getString(R.string.sharedPrefrences_code),
                MODE_PRIVATE).getInt(getString(R.string.settings_home_bluetoothAction), SELECTED_LEAVE));
        mcv_class_bluetooth.setSelection(getSharedPreferences(getString(R.string.sharedPrefrences_code),
                MODE_PRIVATE).getInt(getString(R.string.settings_class_bluetoothAction), SELECTED_LEAVE));
        mcv_out_bluetooth.setSelection(getSharedPreferences(getString(R.string.sharedPrefrences_code),
                MODE_PRIVATE).getInt(getString(R.string.settings_out_bluetoothAction), SELECTED_LEAVE));
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
            case R.id.button_showAllRunningActions:
                setContentView(R.layout.running_processes_screen);
                populateProcesses(null);
                return true;
            case R.id.button_showScreenSize:
                showScreenSize(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        return getSharedPreferences(getString(R.string.sharedPrefrences_code), MODE_PRIVATE);
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
}
