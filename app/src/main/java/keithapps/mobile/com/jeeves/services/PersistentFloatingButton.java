package keithapps.mobile.com.jeeves.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import keithapps.mobile.com.jeeves.R;

/**
 * Created by Keith on 2/23/2016.
 * Persistent Floating Button
 */
public abstract class PersistentFloatingButton extends Service {
    final int deltaFactor = 20;
    final int closeTraySize = 600;
    final int centerVal = 50;
    Handler handler;
    boolean movingLeft, movingRight, movingToClose, running;
    WindowManager.LayoutParams closeParams;
    private WindowManager windowManager;
    private ImageView chatHead, closeView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        chatHead = new ImageView(this);
        closeView = new ImageView(this);
        chatHead.setImageResource(getImageResource());
        chatHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
        closeView.setImageResource(R.drawable.close);
        running = true;
        /* To show above Lockscreen
        WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                PixelFormat.TRANSLUCENT);
        */
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        closeParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        final int bubbleSize = getBubbleSize();
        params.height = bubbleSize;
        params.width = bubbleSize;
        closeParams.height = (int) (bubbleSize * 1.5);
        closeParams.width = (int) (bubbleSize * 1.5);
        params.gravity = Gravity.TOP | Gravity.START;
        closeParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        /*
        Point rr = new Point();
        (((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay()).getSize(rr);
        closeParams.x = rr.x / 2;
        closeParams.y = rr.y - closeTrayHeight;//*/
        movingToClose = movingRight = movingLeft = false;
        closeView.setVisibility(View.GONE);
        windowManager.addView(closeView, closeParams);
        windowManager.addView(chatHead, params);
        try {
            chatHead.setOnTouchListener(new View.OnTouchListener() {
                private WindowManager.LayoutParams paramsF = params;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, final MotionEvent event) {
                    if (running) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                initialX = paramsF.x;
                                initialY = paramsF.y;
                                movingToClose = movingLeft = movingRight = false;
                                initialTouchX = event.getRawX();
                                initialTouchY = event.getRawY();
                                break;
                            case MotionEvent.ACTION_UP:
                                hideClose();
                                if (event.getRawX() == initialTouchX && event.getRawY() == initialTouchY)
                                    click();
                                Display display = windowManager.getDefaultDisplay();
                                Point size = new Point();
                                display.getSize(size);
                                final int width = size.x;
                                final int height = size.y;
                                if (Math.abs((width / 2) - event.getRawX()) <= 200 &&
                                        Math.abs((height - getBubbleSize() * 1.7) - event.getRawY()) <= 200)
                                    onDestroy();
                                else if (paramsF.x < width / 2) {
                                    movingLeft = true;
                                    movingRight = false;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            while (paramsF.x >= 0 && movingLeft
                                                    && !movingRight && !movingToClose) {
                                                try {
                                                    Thread.sleep(10);
                                                    paramsF.x = paramsF.x - 25;
                                                    updateBubble(paramsF);
                                                } catch (Exception e) {
                                                    //Do Nothing
                                                }
                                            }
                                            movingLeft = false;
                                        }
                                    }).start();
                                } else {
                                    movingRight = true;
                                    movingLeft = false;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            while (paramsF.x <= width - 50 && movingRight
                                                    && !movingLeft && !movingToClose) {
                                                try {
                                                    Thread.sleep(10);
                                                    paramsF.x = paramsF.x + 25;
                                                    updateBubble(paramsF);
                                                } catch (Exception e) {
                                                    //Do Nothing
                                                }
                                            }
                                            movingRight = false;
                                        }
                                    }).start();
                                }
                                break;
                            case MotionEvent.ACTION_MOVE:
                                Display dis = windowManager.getDefaultDisplay();
                                Point size2 = new Point();
                                dis.getSize(size2);
                                final int w = size2.x;
                                final int h = size2.y;
                                if (Math.abs((w / 2) - event.getRawX()) <= closeTraySize &&
                                        Math.abs(h - event.getRawY()) <= closeTraySize) {
                                    movingToClose = true;
                                    showClose();
                                    final int center = w / 2 - centerVal;
                                    final int bottom = h - (int) (getBubbleSize() * 1.7);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            while ((paramsF.x != center || paramsF.y != bottom)
                                                    && movingToClose && !movingLeft && !movingRight) {
                                                try {
                                                    Thread.sleep(50);
                                                    paramsF.x = Math.abs(paramsF.x - center) > 10 ?
                                                            paramsF.x - ((paramsF.x - center) / deltaFactor)
                                                            : center;
                                                    paramsF.y = Math.abs(paramsF.y - bottom) > 10 ?
                                                            paramsF.y - ((paramsF.y - bottom) / deltaFactor) : bottom;
                                                    updateBubble(paramsF);
                                                } catch (Exception e) {
                                                    //Do nothing
                                                }
                                            }
                                            movingToClose = false;
                                        }
                                    }).start();
                                } else {
                                    hideClose();
                                    movingToClose = false;
                                    paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                                    paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                                    updateBubble(paramsF);
                                }
                                break;
                        }
                    } else stopSelf();
                    return false;
                }
            });
        } catch (Exception e) {
            //It's ok
        }
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        chatHead.setVisibility(View.GONE);
        closeView.setVisibility(View.GONE);
        running = false;
        stopSelf();
    }

    /**
     * What will happen when the button is clicked
     */
    public abstract void click();

    /**
     * Use this to set the image resource of the bubble
     *
     * @return the resource value of the bubble's image
     */
    public int getImageResource() {
        return R.drawable.white_circle;
    }

    void showClose() {
        closeView.setVisibility(View.VISIBLE);
        windowManager.updateViewLayout(closeView, closeParams);
    }

    /**
     * Hide the close icon
     */
    void hideClose() {
        closeView.setVisibility(View.GONE);
    }

    /**
     * Update the location of the bubble icon on the UI thread
     *
     * @param params the new params of the bubble
     */
    void updateBubble(final WindowManager.LayoutParams params) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (running) windowManager.updateViewLayout(chatHead, params);
            }
        });
    }

    /**
     * Use this to set the size of the bubble
     *
     * @return the size (pixels) of the bubble
     */
    public int getBubbleSize() {
        return 100;
    }
}
