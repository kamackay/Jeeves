package keithapps.mobile.com.jeeves.views;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Debug;
import android.widget.TextView;

import java.util.ArrayList;

import keithapps.mobile.com.jeeves.R;

import static keithapps.mobile.com.jeeves.tools.Global.breakIntoLines;

/**
 * Created by Keith on 1/19/2016.
 * Basically just a textview that stores a process
 */
public class ProcessView extends TextView {
    /**
     * Service
     */
    private ActivityManager.RunningServiceInfo service;
    /**
     * Process
     */
    private RunningAppProcessInfo process;

    public ProcessView(Context context, ActivityManager.RunningServiceInfo service) {
        super(context);
        this.service = service;
        String[] temp = service.service.toString().split("/");
        String c = temp[temp.length - 1].replace("}", "").split(":")[0];
        setText(String.format("Service -\n    Name: %s\n    PID: %d\n    Client Count: %d" +
                        "\n    B: %s\n    Is Foreground: %s\n",
                breakIntoLines(service.process), service.pid, service.clientCount, breakIntoLines(c),
                (service.foreground ? "Yes" : "No")));
        setPadding(40, 50, 20, 20);
    }

    public ProcessView(Context context, RunningAppProcessInfo process) {
        super(context);
        this.process = process;
        try {
            String[] temp = new String[]{"Unknown"};
            if (process.pkgList.length != 0) {
                boolean a = true;
                ArrayList<String> list = new ArrayList<>();
                for (String pkg : process.pkgList) {
                    if (a) a = false;
                    else list.add(" and\n     ");
                    list.add(pkg);
                }
                temp = new String[list.size()];
                for (int i = 0; i < list.size(); i++)
                    temp[i] = list.get(i);
            }
            Debug.MemoryInfo[] memInfo = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                    .getProcessMemoryInfo(new int[]{process.pid});
            String c = temp[temp.length - 1].replace("}", "");
            String importance;
            switch (process.importance) {
                case RunningAppProcessInfo.IMPORTANCE_FOREGROUND_SERVICE:
                    importance = "Foreground Service";
                    break;
                case RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE:
                    importance = "Perceptible";
                    break;
                case RunningAppProcessInfo.IMPORTANCE_VISIBLE:
                    importance = "Has Visible UI";
                    break;
                case RunningAppProcessInfo.IMPORTANCE_FOREGROUND:
                    importance = "Foreground UI Application";
                    break;
                case RunningAppProcessInfo.IMPORTANCE_SERVICE:
                    importance = "Regular Service";
                    break;
                case RunningAppProcessInfo.IMPORTANCE_BACKGROUND:
                    importance = "Expendable Service";
                    break;
                case RunningAppProcessInfo.IMPORTANCE_EMPTY:
                    importance = "Not running";
                    break;
                default:
                    importance = "Unimportant";
                    break;
            }
            setText(String.format("App Process -\n    Name: %s\n    PID: %d" +
                            "\n    B: %s\n    Memory: %fMB\n    Importance: %s\n",
                    breakIntoLines(process.processName), process.pid, breakIntoLines(c),
                    (memInfo[0].getTotalPss() * .001), importance));
        } catch (Exception e) {  //Everything's ok
        }
        setPadding(40, 50, 20, 20);
    }

    /**
     * Set the text, and based on it's value, set the color
     *
     * @param text the text to set this view to
     */
    public void setText(String text) {
        setTextColor(Color.WHITE);
        if (text.toLowerCase().contains("snapchat")) setTextColor(Color.RED);
        else if (text.toLowerCase().contains("google")) setTextColor(Color.CYAN);
        else if (text.toLowerCase().contains("facebook")) setTextColor(Color.rgb(0, 0, 100));
        else if (text.toLowerCase().contains("samsung")) setTextColor(Color.YELLOW);
        else if (text.toLowerCase().contains("com.android.") ||
                (service != null && service.process.equals("system")))
            setTextColor(Color.rgb(175, 80, 0));
        else if (text.toLowerCase().contains("keithapps")) setTextColor(Color.BLACK);
        else if (text.toLowerCase().contains("pushbullet")) setTextColor(Color.GREEN);
        super.setText(text);
        if ((service != null && service.foreground) || (process != null
                && (process.importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE
                || process.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)))
            setBackgroundResource(R.drawable.background_processview_inverted);
        else setBackgroundResource(R.drawable.background_processview);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public int getPID() {
        if (process != null) return process.pid;
        else if (service != null) return service.pid;
        return 0;
    }
}
