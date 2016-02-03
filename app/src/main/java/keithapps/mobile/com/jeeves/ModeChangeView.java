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
    public static final int SELECTED_OFF = 0;
    public static final int SELECTED_LEAVE = 1;
    public static final int SELECTED_ON = 2;
    public static final int SELECTED_REBOOT = 3;
    private LinearLayout l;
    private RadioButton rb_OFF, rb_ON, rb_LEAVE, rb_REBOOT;
    private ItemChangedListener listener;
    private Runnable offPress = new Runnable() {
        @Override
        public void run() {
            rb_ON.setChecked(false);
            rb_LEAVE.setChecked(false);
            rb_OFF.setChecked(true);
            rb_REBOOT.setChecked(false);
            if (listener != null) listener.run();
        }
    }, onPress = new Runnable() {
        @Override
        public void run() {
            rb_LEAVE.setChecked(false);
            rb_ON.setChecked(true);
            rb_OFF.setChecked(false);
            rb_REBOOT.setChecked(false);
            if (listener != null) listener.run();
        }
    }, leavePress = new Runnable() {
        @Override
        public void run() {
            rb_ON.setChecked(false);
            rb_LEAVE.setChecked(true);
            rb_OFF.setChecked(false);
            rb_REBOOT.setChecked(false);
            if (listener != null) listener.run();
        }
    }, rebootPress = new Runnable() {
        @Override
        public void run() {
            rb_ON.setChecked(false);
            rb_LEAVE.setChecked(false);
            rb_OFF.setChecked(false);
            rb_REBOOT.setChecked(true);
            if (listener != null) listener.run();
        }
    };

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

    /**
     * Change whether or not the Reboot option is showing
     *
     * @param show true to show the reboot option, false to hide
     */
    public void showReboot(boolean show) {
        if (show) findViewById(R.id.modeChangeView_rebootParent).setVisibility(View.VISIBLE);
        else findViewById(R.id.modeChangeView_rebootParent).setVisibility(View.GONE);
    }

    private void init(Context c) {
        l = (LinearLayout) ((LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.modechangeview_layout, null);
        this.addView(l);
        rb_ON = (RadioButton) l.findViewById(R.id.modeChangeView_radioButton3);
        rb_LEAVE = (RadioButton) l.findViewById(R.id.modeChangeView_radioButton2);
        rb_OFF = (RadioButton) l.findViewById(R.id.modeChangeView_radioButton1);
        rb_REBOOT = (RadioButton) l.findViewById(R.id.modeChangeView_radioButton4);
        OnClickListener listen_OFF = new OnClickListener() {
            @Override
            public void onClick(View v) {
                offPress.run();
            }
        }, listen_ON = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPress.run();
            }
        }, listen_LEAVE = new OnClickListener() {
            @Override
            public void onClick(View v) {
                leavePress.run();
            }
        }, listen_REBOOT = new OnClickListener() {
            @Override
            public void onClick(View v) {
                rebootPress.run();
            }
        };
        rb_OFF.setOnClickListener(listen_OFF);
        rb_ON.setOnClickListener(listen_ON);
        rb_LEAVE.setOnClickListener(listen_LEAVE);
        rb_REBOOT.setOnClickListener(listen_REBOOT);
        l.findViewById(R.id.modeChangeView_textBoxLEAVE).setOnClickListener(listen_LEAVE);
        l.findViewById(R.id.modeChangeView_textBoxON).setOnClickListener(listen_ON);
        l.findViewById(R.id.modeChangeView_textBoxOFF).setOnClickListener(listen_OFF);
        l.findViewById(R.id.modeChangeView_textBoxREBOOT).setOnClickListener(listen_REBOOT);
        rb_ON.setChecked(true);
    }

    public void setItemChangedListener(ItemChangedListener l) {
        listener = l;
    }

    /**
     * Get the option that is currently selected
     *
     * @return one of the SELECTED_* numbers
     */
    public int getSelection() {
        if (rb_ON.isChecked()) return SELECTED_ON;
        else if (rb_OFF.isChecked()) return SELECTED_OFF;
        else if (rb_REBOOT.isChecked()) return SELECTED_REBOOT;
        else return SELECTED_LEAVE;
    }

    public void setSelection(int selection) {
        switch (selection) {
            case SELECTED_ON:
                onPress.run();
                return;
            case SELECTED_LEAVE:
                leavePress.run();
                return;
            case SELECTED_OFF:
                offPress.run();
                return;
            case SELECTED_REBOOT:
                rebootPress.run();
        }
    }

    public void setText(String s) {
        ((TextView) l.findViewById(R.id.modeChangeView_textBoxTEXT)).setText(s);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public interface ItemChangedListener {
        void run();
    }
}
