package keithapps.mobile.com.jeeves.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import keithapps.mobile.com.jeeves.tools.Settings;

/**
 * Created by Keith on 2/16/2016.
 * An EditText that saves to a setting
 */
public class SettingsEditText extends EditText implements SettingsView {
    String mySetting;
    Runnable afterwards;
    OnFocusChangeListener listener;

    public SettingsEditText(Context context) {
        super(context);
        init();
    }

    public SettingsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SettingsEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SettingsEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mySetting != null && !mySetting.isEmpty()) {
                    SharedPreferences.Editor edit = getContext()
                            .getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE).edit();
                    edit.putString(mySetting, getText().toString());
                    edit.apply();
                    if (afterwards != null) afterwards.run();
                }
            }
        });
    }

    /**
     * Set the setting that this view controls
     *
     * @param setting the setting in the shared preferences that this view controls
     */
    @Override
    public void setMySetting(String setting) {
        mySetting = setting;
        setText(getContext().getSharedPreferences(Settings.sharedPrefs_code,
                Context.MODE_PRIVATE).getString(mySetting, ""));
        if (afterwards != null) afterwards.run();
    }

    /**
     * An event to run after the setting is changed
     *
     * @param event the event to run on Setting Change
     */
    @Override
    public void setAfterChangeEvent(Runnable event) {
        afterwards = event;
    }

    public void setListener(OnFocusChangeListener listener) {
        this.listener = listener;
    }
}
