package keithapps.mobile.com.jeeves.activities.popups;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import keithapps.mobile.com.jeeves.R;

/**
 * Created by Keith on 2/19/2016.
 * Show Text on a popup
 */
public class TextPopup extends Activity {
    public static void showTextPopup(String text, String title, Context c) {
        Intent i = new Intent(c, TextPopup.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra("text", text);
        i.putExtra("title", title);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_text);
        ActionBar a = getActionBar();
        if (a != null) a.hide();
        //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        String text = getIntent().getExtras().getString("text");
        String title = getIntent().getExtras().getString("title");
        ((TextView) findViewById(R.id.textPopup_text)).setText(text);
        ((TextView) findViewById(R.id.textPopup_title)).setText(title);
    }
}
