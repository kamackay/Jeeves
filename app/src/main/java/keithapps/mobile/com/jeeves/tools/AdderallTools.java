package keithapps.mobile.com.jeeves.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;

import static keithapps.mobile.com.jeeves.tools.Global.getPrefs;
import static keithapps.mobile.com.jeeves.tools.Global.getTimestamp;
import static keithapps.mobile.com.jeeves.tools.Global.isExternalStorageWritable;

/**
 * Created by Keith on 2/16/2016.
 * Adderall Tools
 */
public class AdderallTools {
    public static void writeAdderall(Context c, int mg) {
        try {
            if (!isExternalStorageWritable()) return;
            SharedPreferences prefs = getPrefs(c);
            if (prefs.getString(Settings.Adderall.lastClearTime, null) == null) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString(Settings.Adderall.lastClearTime, getTimestamp());
                edit.apply();
            }
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), adderallCSV_FILENAME);
            file = new File(file.getParentFile().getParentFile(), "/Jeeves/" + adderallCSV_FILENAME);
            if (file.getParentFile().mkdirs()) Toast.makeText(c, "Created Parent Directory",
                    Toast.LENGTH_LONG).show();
            if (!file.exists() && file.createNewFile())
                Toast.makeText(c, "Created File", Toast.LENGTH_LONG).show();
            String s = String.format(Locale.getDefault(), "%s,%s,%d,%d\n", getTimestamp(),
                    prefs.getString(Settings.Adderall.lastClearTime, ""), mg,
                    prefs.getInt(Settings.Adderall.adderall_count, 0));
            try (BufferedWriter fos = new BufferedWriter(new FileWriter(file, true))) {
                fos.write(s, 0, s.length());
            } catch (Exception e) {
                Toast.makeText(c, "error writing to file", Toast.LENGTH_LONG).show();
            }
            //Toast.makeText(c, "wrote to file", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(c, "error creating file", Toast.LENGTH_LONG).show();
        }
    }

    public static final String adderallCSV_FILENAME = "AdderallUsage.csv";
}
