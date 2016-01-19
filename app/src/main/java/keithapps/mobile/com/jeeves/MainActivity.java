package keithapps.mobile.com.jeeves;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import static keithapps.mobile.com.jeeves.Global.getVersionName;
import static keithapps.mobile.com.jeeves.Global.isServiceRunning;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Spinner[] spinners = new Spinner[]{
                (Spinner) findViewById(R.id.settingsScreen_home_ringtoneVolume),
                (Spinner) findViewById(R.id.settingsScreen_home_mediaVolume),
                (Spinner) findViewById(R.id.settingsScreen_home_systemVolume),
                (Spinner) findViewById(R.id.settingsScreen_home_notificationVolume),
                (Spinner) findViewById(R.id.settingsScreen_home_alarmVolume),
                (Spinner) findViewById(R.id.settingsScreen_class_ringtoneVolume),
                (Spinner) findViewById(R.id.settingsScreen_class_mediaVolume),
                (Spinner) findViewById(R.id.settingsScreen_class_systemVolume),
                (Spinner) findViewById(R.id.settingsScreen_class_notificationVolume),
                (Spinner) findViewById(R.id.settingsScreen_class_alarmVolume)
        };
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
        ((TextView)findViewById(R.id.activity_main_versionText))
                .setText(String.format("Version: %s", getVersionName(getApplicationContext())));
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
}
