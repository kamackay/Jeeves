package keithapps.mobile.com.jeeves.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.io.FileInputStream;

import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.listeners.TextChangeListener;
import keithapps.mobile.com.jeeves.tools.Log;
import keithapps.mobile.com.jeeves.tools.Settings;

import static keithapps.mobile.com.jeeves.tools.Log.clearLog;

public class LogActivity extends AppCompatActivity {

    Handler mHandler;
    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            showLog();
            mHandler.postDelayed(mHandlerTask, 5000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        SharedPreferences prefs = getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE);
        Switch switch_log = (Switch) findViewById(R.id.logScreen_switchLog);
        switch_log.setChecked(prefs.getBoolean(Settings.record_log, true));
        switch_log.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) Log.writeToLog("Logging Turned Off", getApplicationContext());
                SharedPreferences.Editor edit = getSharedPreferences(Settings.sharedPrefs_code,
                        Context.MODE_PRIVATE).edit();
                edit.putBoolean(Settings.record_log, isChecked);
                edit.apply();
                if (isChecked) Log.writeToLog("Logging Turned Back on", getApplicationContext());
            }
        });
        ((EditText) findViewById(R.id.logScreen_searchTextbox))
                .addTextChangedListener(new TextChangeListener() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        showLog();
                    }
                });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mHandler = new Handler();
        mHandlerTask.run();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void showLog() {
        final TextView tv = (TextView) findViewById(R.id.log_text);
        final EditText et = (EditText) findViewById(R.id.logScreen_searchTextbox);
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final StringBuilder sb = new StringBuilder(), chars = new StringBuilder();
                        String search = et.getText().toString().toLowerCase().trim();
                        int ch;
                        boolean s = !search.trim().isEmpty(), not = search.startsWith("not ");
                        if (not) search = search.replace("not ", "").trim();
                        FileInputStream fis = openFileInput(Log.LOGFILE_NAME);
                        while ((ch = fis.read()) != -1) chars.append((char) ch);
                        fis.close();
                        final String[] lines = chars.toString().split("\n");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText("");
                            }
                        });
                        for (int i = lines.length - 1; i >= 0; i--) {
                            final int fi = i;
                            if (not && !(!s || lines[i].toLowerCase().contains(search)))
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv.append(lines[fi] + "\n\n");
                                    }
                                });
                            else if (!not && (!s || lines[i].toLowerCase().contains(search))) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv.append(lines[fi] + "\n\n");
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        //Do nothing again
                    }
                }
            }).start();
            tv.setTypeface(Typeface.createFromAsset(getAssets(), "calibri.ttf"));
        } catch (Exception e) {
            //Don't do nothin'
        }
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
        inflater.inflate(R.menu.logactivity_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mHandlerTask);
        super.onDestroy();
    }

    /**
     * @param item Menu Item that was selected
     * @return pretty much always true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logMenu_sync:
                showLog();
                return true;
            case R.id.logMenu_clearLog:
                clearLog(getApplicationContext());
                showLog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clearSearch(View view) {
        ((EditText) findViewById(R.id.logScreen_searchTextbox)).setText("");
        showLog();
    }

    /**
     * Save all appropriate fragment state.
     *
     * @param outState outstate
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
