package keithapps.mobile.com.jeeves;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Keith on 2/12/2016.
 * Cromulon View
 */
public class CromulonView extends View {
    Bitmap bitmap;

    public CromulonView(Context context) {
        super(context);
        init();
    }

    public CromulonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CromulonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CromulonView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    Paint p;
    int left;

    public void init() {
        animation = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = -1 * bitmap.getWidth() - 10; i < 50; i += 5) {
                        Thread.sleep(25);
                        left = i;
                        postInvalidate();
                    }
                    if (playSound != null) playSound.run();
                    Thread.sleep(3000);
                    while (left > -1 * bitmap.getWidth() - 10) {
                        Thread.sleep(25);
                        left -= 5;
                        postInvalidate();
                    }
                    if (onAnimationEnd != null) post(onAnimationEnd);
                } catch (Exception e) {
                    //It's fine
                }
            }
        });
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cromulon);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
        p = new Paint();
        left = -1 * bitmap.getWidth() - 10;
        animationRunning = false;
    }

    Thread animation;
    boolean animationRunning;

    @Override
    protected void onDraw(Canvas c) {
        if (!animationRunning) {
            animation.start();
            animationRunning = true;
        }
        c.drawBitmap(bitmap, left, 200, p);
    }

    public void setOnAnimationEnd(Runnable runnable) {
        onAnimationEnd = runnable;
    }

    public void setPlaySound(Runnable runnable){playSound = runnable;
    }

    Runnable onAnimationEnd, playSound;

}
