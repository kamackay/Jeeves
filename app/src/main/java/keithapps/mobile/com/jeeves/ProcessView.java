package keithapps.mobile.com.jeeves;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.TextView;

import static keithapps.mobile.com.jeeves.Global.breakIntoLines;

/**
 * Created by Keith on 1/19/2016.
 * Basically just a textview that stores a process
 */
public class ProcessView extends TextView {
    private ActivityManager.RunningServiceInfo p;

    public ProcessView(Context context, ActivityManager.RunningServiceInfo p) {
        super(context);
        this.p = p;
        String[] temp = p.service.toString().split("/");
        String c = temp[temp.length - 1].replace("}", "").split(":")[0];
        setText(String.format("Process -\n    Name: %s\n    PID: %d\n    Client Count: %d" +
                        "\n    Class: %s\n    Is Foreground: %s\n",
                breakIntoLines(p.process), p.pid, p.clientCount, breakIntoLines(c),
                (p.foreground ? "Yes" : "No")));
        setPadding(30, 40, 20, 20);
    }

    /**
     * Set the text, and based on it's value, set the color
     *
     * @param text the text to set this view to
     */
    public void setText(String text) {
        if (text.toLowerCase().contains("snapchat")) setTextColor(Color.RED);
        else if (text.toLowerCase().contains("google")) setTextColor(Color.CYAN);
        else if (text.toLowerCase().contains("facebook")) setTextColor(Color.rgb(0, 0, 100));
        else if (text.toLowerCase().contains("samsung")) setTextColor(Color.YELLOW);
        else if (text.toLowerCase().contains("com.android.") || p.process.equals("system"))
            setTextColor(Color.rgb(175, 80, 0));
        else if (text.toLowerCase().contains("keithapps")) setTextColor(Color.BLACK);
        else if (text.toLowerCase().contains("pushbullet")) setTextColor(Color.GREEN);
        super.setText(text);
        if (p.foreground) {
            setBackgroundResource(R.drawable.background_processview_inverted);
            super.setTextColor(Color.WHITE);
        } else setBackgroundResource(R.drawable.background_processview);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public ActivityManager.RunningServiceInfo getProcess() {
        return p;
    }

    public void setProcess(ActivityManager.RunningServiceInfo p) {
        this.p = p;
    }
}
