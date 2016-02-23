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
    Handler handler;
    boolean movingLeft, movingRight;
    private WindowManager windowManager;
    private ImageView chatHead;

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
        chatHead.setImageResource(getImageResource());
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        movingRight = movingLeft = false;
        windowManager.addView(chatHead, params);
        try {
            chatHead.setOnTouchListener(new View.OnTouchListener() {
                private WindowManager.LayoutParams paramsF = params;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = paramsF.x;
                            initialY = paramsF.y;
                            movingLeft = movingRight = false;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            if (event.getRawX() == initialTouchX && event.getRawY() == initialTouchY)
                                onClick();
                            Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
                            Point size = new Point();
                            display.getSize(size);
                            final int width = size.x;
                            int height = size.y;
                            if (Math.abs((width / 2) - event.getRawX()) <= 200 &&
                                    Math.abs(height - event.getRawY()) <= 200)
                                onDestroy();
                            else if (paramsF.x < width / 2) {
                                movingLeft = true;
                                movingRight = false;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while (paramsF.x >= 0 && movingLeft && !movingRight) {
                                            try {
                                                Thread.sleep(25);
                                                final int finalX = paramsF.x - 50;
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        paramsF.x = finalX;
                                                        windowManager.updateViewLayout(chatHead, paramsF);
                                                    }
                                                });
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
                                        while (paramsF.x <= width - 50 && movingRight && !movingLeft) {
                                            try {
                                                Thread.sleep(25);
                                                final int finalX = paramsF.x + 50;
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        paramsF.x = finalX;
                                                        windowManager.updateViewLayout(chatHead, paramsF);
                                                    }
                                                });
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
                            paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(chatHead, paramsF);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            //It's ok
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (chatHead != null) windowManager.removeView(chatHead);
        } catch (Exception e) {
            //Do nothing
        }
        stopSelf();
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    public abstract void onClick();

    public int getImageResource() {
        return R.drawable.icon;
    }
}
