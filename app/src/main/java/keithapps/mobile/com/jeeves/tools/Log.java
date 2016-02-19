package keithapps.mobile.com.jeeves.tools;

import android.content.Context;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.nio.charset.Charset;

/**
 * Created by Keith on 2/18/2016.
 * Logging Tools
 */
public class Log {

    /**
     * The name of the Logfile
     */
    public static final String LOGFILE_NAME = "log.txt";

    /**
     * Write to the Log file
     *
     * @param text the text to write to the log file
     * @param c    the context of the calling method
     */
    public static void writeToLog(String text, Context c) {
        writeToLog(text, c, false);
    }

    public static void writeToLog(String text, Context c, boolean showToast) {
        try {
            if (!c.getSharedPreferences(Settings.sharedPrefs_code, Context.MODE_PRIVATE)
                    .getBoolean(Settings.record_log, true)) return;
            String toPrint = String.format("%s- %s\n", Utils.getTimestamp(), text);
            String lines[] = toPrint.split("\n");
            StringBuilder temp = new StringBuilder();
            for (int i = lines.length - 1; i >= 0; i--)
                temp.append(lines[i]).append("\n");
            toPrint = temp.toString();
            byte[] bytes = toPrint.getBytes(Charset.forName("UTF-8"));
            try (FileOutputStream fos = c.openFileOutput(LOGFILE_NAME, Context.MODE_APPEND)) {
                fos.write(bytes);
            } catch (Exception e) {
                //Shit happens
            }
            if (showToast && GlobalTools.isKeith(c) && GlobalTools.isJeevesActivityForeground(c))
                Toast.makeText(c, toPrint.trim(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //Don't do anything
        }
    }

    /**
     * Clear Log FIle
     *
     * @param c the Calling context
     * @return false if clearing the file did not work
     */
    public static boolean clearLog(Context c) {
        try {
            FileOutputStream fos = c.openFileOutput(LOGFILE_NAME, Context.MODE_PRIVATE);
            String toPrint = String.format("%s: Cleared the Log File\n", Utils.getTimestamp());
            fos.write(toPrint.getBytes(Charset.forName("UTF-8")));
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void logException(String info, Context c, Exception e) {
        writeToLog(String.format("%s\n    %s\n%s", info, e.getLocalizedMessage(),
                Utils.getStackTraceString(e.getStackTrace())).trim(), c);
    }
}
