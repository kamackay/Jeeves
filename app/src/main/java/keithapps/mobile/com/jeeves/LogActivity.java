package keithapps.mobile.com.jeeves;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;

import static keithapps.mobile.com.jeeves.Global.clearLog;

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
        ((EditText) findViewById(R.id.logScreen_searchTextbox))
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        showLog();
                    }
                });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mHandler = new Handler();
        mHandlerTask.run();
    }

    private void showLog() {
        final TextView tv = (TextView) findViewById(R.id.log_text);
        final EditText et = (EditText) findViewById(R.id.logScreen_searchTextbox);
        final String search = et.getText().toString();
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final StringBuilder sb = new StringBuilder(), chars = new StringBuilder();
                        int ch;
                        boolean s = !search.trim().isEmpty();
                        FileInputStream fis = openFileInput(Global.LOGFILE_NAME);
                        while ((ch = fis.read()) != -1) chars.append((char) ch);
                        fis.close();
                        String[] lines = chars.toString().split("\n");
                        for (int i = lines.length - 1; i >= 0; i--)
                            if (!s || lines[i].contains(search))
                                sb.append(lines[i]).append("\n\n");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(sb.toString());
                            }
                        });
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
}
