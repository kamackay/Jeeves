package keithapps.mobile.com.jeeves.activities.popups;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import keithapps.mobile.com.jeeves.R;

import static keithapps.mobile.com.jeeves.services.PersistentFloatingButton.createButtonFor;
import static keithapps.mobile.com.jeeves.tools.PermissionsTools.canDrawOverOtherApps;

/**
 * Created by Keith on 2/24/2016.
 * Popup to Create a floating button from all installed apps
 */
public class CreateFloatingButton extends Activity {

    public static void showCreateFloatingButton(Context c) {
        Intent i = new Intent(c, CreateFloatingButton.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!canDrawOverOtherApps(getApplicationContext())) {
            finish();
            return;
        }
        setContentView(R.layout.layout_create_floating_button);
        ActionBar a = getActionBar();
        if (a != null) a.hide();
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        /*
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//*/
        final PackageManager p = getPackageManager();
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        final LinearLayout root = (LinearLayout) findViewById(R.id.createButton_root);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.createButton_loading);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PackageInfo> packages = new ArrayList<>();
                List<PackageInfo> installed = p.getInstalledPackages(0);
                for (int i = 0; i < installed.size(); i++) {
                    final PackageInfo info = installed.get(i);
                    String name = info.applicationInfo.loadLabel(p).toString();
                    final Button b = new Button(getApplicationContext());
                    b.setAllCaps(false);
                    b.setPadding(10, 10, 10, 10);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createButtonFor(info.packageName, getApplicationContext());
                            finish();
                        }
                    });
                    b.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                            R.color.background));
                    b.setText(name);
                    b.setTextColor(Color.WHITE);
                    final Space s = new Space(getApplicationContext());
                    s.setMinimumHeight(10);
                    int x = 0, size = packages.size();
                    while (x < size && name.compareTo(packages
                            .get(x).applicationInfo.loadLabel(p).toString()) >= 0)
                        x++;
                    packages.add(x, info);
                    final int fX = x;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            root.addView(b, fX * 2, params);
                            root.addView(s, fX * 2 + 1);
                            root.invalidate();
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return event.getAction() == MotionEvent.ACTION_OUTSIDE || super.onTouchEvent(event);
    }

    public void close(View view) {
        finish();
    }
}
