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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import static keithapps.mobile.com.jeeves.Global.getVersionName;
import static keithapps.mobile.com.jeeves.Global.isServiceRunning;

public class MainActivity extends AppCompatActivity {
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
        final Spinner[] spinners = getSpinners();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.percentages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        AdapterView.OnItemSelectedListener saveListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor prefs = getPrefs().edit();
                prefs.putInt(getString(R.string.settings_home_ringtoneVolume), spinners[0].getSelectedItemPosition());
                prefs.putInt(getString(R.string.settings_home_mediaVolume), spinners[1].getSelectedItemPosition());
                prefs.putInt(getString(R.string.settings_home_systemVolume), spinners[2].getSelectedItemPosition());
                prefs.putInt(getString(R.string.settings_home_notificationVolume), spinners[3].getSelectedItemPosition());
                prefs.putInt(getString(R.string.settings_home_alarmVolume), spinners[4].getSelectedItemPosition());
                prefs.putInt(getString(R.string.settings_class_ringtoneVolume), spinners[5].getSelectedItemPosition());
                prefs.putInt(getString(R.string.settings_class_mediaVolume), spinners[6].getSelectedItemPosition());
                prefs.putInt(getString(R.string.settings_class_systemVolume), spinners[7].getSelectedItemPosition());
                prefs.putInt(getString(R.string.settings_class_notificationVolume), spinners[8].getSelectedItemPosition());
                prefs.putInt(getString(R.string.settings_class_alarmVolume), spinners[9].getSelectedItemPosition());
                prefs.putInt(getString(R.string.settings_out_ringtoneVolume), spinners[10].getSelectedItemPosition());
                prefs.putInt(getString(R.string.settings_out_mediaVolume), spinners[11].getSelectedItemPosition());
                prefs.putInt(getString(R.string.settings_out_systemVolume), spinners[12].getSelectedItemPosition());
                prefs.putInt(getString(R.string.settings_out_notificationVolume), spinners[13].getSelectedItemPosition());
                prefs.putInt(getString(R.string.settings_out_alarmVolume), spinners[14].getSelectedItemPosition());
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
        spinners[0].setSelection(prefs.getInt(getString(R.string.settings_home_ringtoneVolume), 5));
        spinners[1].setSelection(prefs.getInt(getString(R.string.settings_home_mediaVolume), 5));
        spinners[2].setSelection(prefs.getInt(getString(R.string.settings_home_systemVolume), 5));
        spinners[3].setSelection(prefs.getInt(getString(R.string.settings_home_notificationVolume), 1));
        spinners[4].setSelection(prefs.getInt(getString(R.string.settings_home_alarmVolume), 10));
        spinners[5].setSelection(prefs.getInt(getString(R.string.settings_class_ringtoneVolume), 0));
        spinners[6].setSelection(prefs.getInt(getString(R.string.settings_class_mediaVolume), 0));
        spinners[7].setSelection(prefs.getInt(getString(R.string.settings_class_systemVolume), 0));
        spinners[8].setSelection(prefs.getInt(getString(R.string.settings_class_notificationVolume), 0));
        spinners[9].setSelection(prefs.getInt(getString(R.string.settings_class_alarmVolume), 0));
        spinners[10].setSelection(prefs.getInt(getString(R.string.settings_out_ringtoneVolume), 5));
        spinners[11].setSelection(prefs.getInt(getString(R.string.settings_out_mediaVolume), 5));
        spinners[12].setSelection(prefs.getInt(getString(R.string.settings_out_systemVolume), 5));
        spinners[13].setSelection(prefs.getInt(getString(R.string.settings_out_notificationVolume), 5));
        spinners[14].setSelection(prefs.getInt(getString(R.string.settings_out_alarmVolume), 10));
        ((TextView) findViewById(R.id.activity_main_versionText))
                .setText(String.format("Version: %s", getVersionName(getApplicationContext())));
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
        Typeface tf = Typeface.createFromAsset(getAssets(), "calibri.ttf");
        LinearLayout l = (LinearLayout) findViewById(R.id.runningProcesses_root);
        if (l.getChildCount() > 0) l.removeAllViews();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (final ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            ProcessView tv = new ProcessView(getApplicationContext(), service);
            if (tf != null) tv.setTypeface(tf);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tv.setOnLongClickListener(getProcessViewListener());
            l.addView(tv);
        }
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
                            int pid = ((ProcessView) v).getProcess().pid;
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
                //Absorb the key event
                return true;
        }
        return super.onKeyDown(keycode, e);
    }
}
