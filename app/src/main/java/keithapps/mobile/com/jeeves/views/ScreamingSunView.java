package keithapps.mobile.com.jeeves.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

import keithapps.mobile.com.jeeves.R;

/**
 * Created by Keith on 2/11/2016.
 * Screaming Sun View
 */
public class ScreamingSunView extends View {
    Thread animation;
    Runnable onAnimationEnd;
    private Bitmap[] bitmaps;
    private Bitmap bitmap;
    private Paint p;
    private boolean animationRunning;
    private int y;

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

    private void init() {
        animation = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                    while (y > -1 * bitmap.getHeight()) {
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
        Resources r = getResources();
        int bitmapCount = 3;
        bitmaps = new Bitmap[bitmapCount];
        bitmaps[0] = BitmapFactory.decodeResource(r, R.drawable.screaming_sun);
        bitmaps[1] = BitmapFactory.decodeResource(r, R.drawable.screaming_sun_2);
        bitmaps[2] = BitmapFactory.decodeResource(r, R.drawable.screaming_sun_3);
        for (int i = 0; i < bitmapCount; i++)
            while (bitmaps[i].getWidth() > 500) bitmaps[i] = Bitmap.createScaledBitmap(bitmaps[i],
                    (int) (bitmaps[i].getWidth() * .75), (int) (bitmaps[i].getHeight() * .75), false);
        p = new Paint();
        y = -500;
        animationRunning = false;
        bitmap = bitmaps[new Random().nextInt(bitmapCount)];
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
}
