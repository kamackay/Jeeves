package keithapps.mobile.com.jeeves.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.Switch;

import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.tools.Settings;

/**
 * Created by Keith on 2/16/2016.
 * Switch that saves to a setting
 */
public class SettingsSwitch extends Switch implements SettingsView {
    String mySetting;
    Runnable afterwards;

    public SettingsSwitch(Context c) {
        super(c);
        init(c, null);
    }

    public SettingsSwitch(Context c, AttributeSet attrs) {
        super(c, attrs);
        init(c, attrs);
    }

    public SettingsSwitch(Context c, AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
        init(c, attrs);
    }

    public SettingsSwitch(Context c, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(c, attrs, defStyleAttr, defStyleRes);
        init(c, attrs);
    }

    void init(Context c, AttributeSet attrs) {
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mySetting != null && !mySetting.isEmpty()) {
                    SharedPreferences.Editor edit = getContext()
                            .getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE).edit();
                    edit.putBoolean(mySetting, isChecked);
                    edit.apply();
                    if (afterwards != null) afterwards.run();
                }
            }
        });
        if (attrs == null) return;
        TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.SettingsSwitch, 0, 0);
        setMySetting(ta.getString(R.styleable.SettingsSwitch_setting));
        ta.recycle();
    }

    /**
     * Set the setting that this view controls
     *
     * @param setting the setting in the shared preferences that this view controls
     */
    @Override
    public void setMySetting(String setting) {
        mySetting = setting;
        setChecked(getContext().getSharedPreferences(Settings.sharedPrefs_code,
                Context.MODE_PRIVATE).getBoolean(mySetting, true));
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
}
