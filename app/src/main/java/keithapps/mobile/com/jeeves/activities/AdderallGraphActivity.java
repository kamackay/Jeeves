package keithapps.mobile.com.jeeves.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import keithapps.mobile.com.jeeves.R;
import keithapps.mobile.com.jeeves.activities.popups.KeithToast;
import keithapps.mobile.com.jeeves.csv.CSVReader;
import keithapps.mobile.com.jeeves.tools.AdderallTools;

import static keithapps.mobile.com.jeeves.tools.SystemTools.getFont;

public class AdderallGraphActivity extends Activity {
    GraphView graph;
    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adderall_graph);
        graph = (GraphView) findViewById(R.id.graph);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), AdderallTools.adderallCSV_FILENAME);
            file = new File(file.getParentFile().getParentFile(), "/Jeeves/" +
                    AdderallTools.adderallCSV_FILENAME);
            file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
            CSVReader reader = new CSVReader(file);
            List<String[]> lines = reader.getAll();
            List<DataPoint> points = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat("MM/dd-HH:mm:ss", Locale.US);
            int i = lines.size() - 1;
            while (i >= 0) {
                Date d = format.parse(lines.get(i)[0]);
                points.add(0, new DataPoint(d.getHours() + ((double) d.getMinutes()) / 100,
                        Double.parseDouble(lines.get(i)[3])));
                if (lines.get(i)[3].trim().equals("0")) break;
                i--;
            }
            final LineGraphSeries<DataPoint> series =
                    new LineGraphSeries<>(points.toArray(new DataPoint[points.size()]));
            series.setDrawBackground(true);
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(),
                            "%d:%02d %s - %d mg",
                            (dataPoint.getX() == 12 || dataPoint.getX() == 24)
                                    ? 12 : (int) (dataPoint.getX() % 12),
                            (int) ((dataPoint.getX() % 1) * 100),
                            (dataPoint.getX() < 12 || ((int) dataPoint.getX()) == 24) ? "AM" : "PM",
                            (int) (dataPoint.getY())),
                            Toast.LENGTH_SHORT).show();
                }
            });
            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX) return String.format(Locale.getDefault(), "%d:%02d%s",
                            (value == 12 || value == 24) ? 12 : (int) (value % 12),
                            (int) ((value % 1) * 100),
                            (value < 12 || ((int) value) == 24) ? "AM" : "PM");
                    else return super.formatLabel(value, false) + " mg";
                }
            });
            graph.addSeries(series);
        } catch (Exception e) {
            KeithToast.show(e.getLocalizedMessage(), getApplicationContext());
        }
        tf = getFont(getApplicationContext());
        if (tf != null) ((TextView) findViewById(R.id.graphScreen_title)).setTypeface(tf);
    }

    /**
     * Go back to Main after back press
     *
     * @param keycode the key pressed
     * @param e       the key event
     * @return true to absorb event
     */
    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch (keycode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case KeyEvent.KEYCODE_HOME:
                return true;
        }
        return super.onKeyDown(keycode, e);
    }
}
