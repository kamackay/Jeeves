package keithapps.mobile.com.jeeves.views;

/**
 * Created by Keith on 2/16/2016.
 * Settings View
 */
public interface SettingsView {
    /**
     * Set the setting that this view controls
     *
     * @param setting the setting in the shared preferences that this view controls
     */
    void setMySetting(String setting);

    /**
     * An event to run after the setting is changed
     *
     * @param event the event to run on Setting Change
     */
    void setAfterChangeEvent(Runnable event);
}
