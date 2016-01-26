package keithapps.mobile.com.jeeves;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class ModeChangeView extends LinearLayout {

    private LinearLayout l;

    public ModeChangeView(Context context) {
        super(context);
        init(context);
    }

    public ModeChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ModeChangeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context c) {
        l = (LinearLayout) ((LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.modechangeview_layout, null);
        this.addView(l);
        final RadioButton rbOFF = (RadioButton) l.findViewById(R.id.modeChangeView_radioButton1),
                rbON = (RadioButton) l.findViewById(R.id.modeChangeView_radioButton3),
                rbLEAVE = (RadioButton) l.findViewById(R.id.modeChangeView_radioButton2);
        OnClickListener listen_OFF = new OnClickListener() {
            @Override
            public void onClick(View v) {
                rbON.setChecked(false);
                rbLEAVE.setChecked(false);
                rbOFF.setChecked(true);
            }
        }, listen_ON = new OnClickListener() {
            @Override
            public void onClick(View v) {
                rbLEAVE.setChecked(false);
                rbON.setChecked(true);
                rbOFF.setChecked(false);
            }
        }, listen_LEAVE = new OnClickListener() {
            @Override
            public void onClick(View v) {
                rbON.setChecked(false);
                rbLEAVE.setChecked(true);
                rbOFF.setChecked(false);
            }
        };
        rbOFF.setOnClickListener(listen_OFF);
        rbON.setOnClickListener(listen_ON);
        rbLEAVE.setOnClickListener(listen_LEAVE);
        l.findViewById(R.id.modeChangeView_textBoxLEAVE).setOnClickListener(listen_LEAVE);
        l.findViewById(R.id.modeChangeView_textBoxON).setOnClickListener(listen_ON);
        l.findViewById(R.id.modeChangeView_textBoxOFF).setOnClickListener(listen_OFF);
        rbON.setChecked(true);
    }

    public void setText(String s) {
        ((TextView) l.findViewById(R.id.modeChangeView_textBoxTEXT)).setText(s);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
