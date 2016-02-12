package keithapps.mobile.com.jeeves;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Keith on 2/11/2016.
 */
public class ScreamingSunView extends View {
    public ScreamingSunView(Context context) {
        super(context);
        init();
    }

    public ScreamingSunView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScreamingSunView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ScreamingSunView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Bitmap bitmap;
    private Paint p;

    private void init() {
        animation = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    while (y > -350) {
                        try {
                            Thread.sleep(25);
                            y -= 3;
                            postInvalidate();
                        } catch (Exception e) {
                            //It's cool
                        }
                    }
                    if (onAnimationEnd != null) post(onAnimationEnd);
                } catch (Exception e) {
                    //It's cool
                }
            }
        });
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.screaming_sun), 294, 300, false);
        p = new Paint();
        y = -500;
        animationRunning = false;
    }

    @Override
    protected void onDraw(Canvas c) {
        if (y == -500) y = getMeasuredHeight();
        c.drawBitmap(bitmap, 100, y, p);
        if (!animationRunning) {
            animation.start();
            animationRunning = true;
        }
    }

    public void setOnAnimationEnd(Runnable runnable) {
        onAnimationEnd = runnable;
    }

    private boolean animationRunning;
    private int y;
    Thread animation;
    Runnable onAnimationEnd;
}
