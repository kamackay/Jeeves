package keithapps.mobile.com.jeeves.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.io.FileInputStream;

import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.listeners.TextChangeListener;
import keithapps.mobile.com.jeeves.tools.Log;

import static keithapps.mobile.com.jeeves.tools.Log.clearLog;
import static keithapps.mobile.com.jeeves.tools.SystemTools.getFont;

public class LogActivity extends AppCompatActivity {

    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            showLog();
        }
    };
Typeface tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        tf = getFont(getApplicationContext());
        if (tf != null){
            ((Switch) findViewById(R.id.logScreen_switchLog)).setTypeface(tf);
            ((TextView)findViewById(R.id.logScreen_searchTextbox)).setTypeface(tf);
        }
        ((EditText) findViewById(R.id.logScreen_searchTextbox))
                .addTextChangedListener(new TextChangeListener() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        showLog();
                    }
                });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
                        for (int i = lines.length - 1; i >= 0; i--) {
                            if (not && !(!s || lines[i].toLowerCase().contains(search)))
                                sb.append(lines[i]).append("\n\n");
                            else if (!not && (!s || lines[i].toLowerCase().contains(search))) {
                                sb.append(lines[i]).append("\n\n");
                            }
                        }
                        final String finS = sb.toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(finS);
                            }
                        });
                    } catch (Exception e) {
                        //Do nothing again
                    }
                }
            }).start();
            tv.setTypeface(getFont(getApplicationContext()));
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
