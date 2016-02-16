package keithapps.mobile.com.jeeves.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.Switch;

import keithapps.mobile.com.jeeves.tools.Settings;

/**
 * Created by Keith on 2/16/2016.
 * Switch that saves to a setting
 */
public class SettingsSwitch extends Switch implements SettingsView {
    String mySetting;
    Runnable afterwards;

    public SettingsSwitch(Context context) {
        super(context);
        init();
    }

    public SettingsSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SettingsSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SettingsSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {
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
