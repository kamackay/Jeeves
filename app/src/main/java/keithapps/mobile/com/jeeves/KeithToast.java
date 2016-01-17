package keithapps.mobile.com.jeeves;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Custom Toast Implementation
 */
public class KeithToast {
    /**
     * Show KeithToast
     *
     * @param text    the text to show
     * @param context the context of the Toast caller
     * @param color   the general color of the toast
     * @param length  the length of time to show the toast
     */
    private static void show(String text, Context context, int color, int length) {
        try {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.keithtoast, null);
            TextView textView = (TextView) layout.findViewById(R.id.keithtoast_textview);
            textView.setText(text);
            textView.setTextColor(color);
            ImageView iv = (ImageView) layout.findViewById(R.id.keithtoast_imageview);
            iv.setImageResource(getColoredImage(color));
            TextView t2 = (TextView) layout.findViewById(R.id.keithToast_iconText);
            t2.setTextColor(color);
            Toast toast = new Toast(context);
            toast.setGravity(Gravity.TOP, 0, 50);
            if (length == Toast.LENGTH_LONG || length == Toast.LENGTH_SHORT)
                toast.setDuration(length);
            else toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            View.OnClickListener dismissListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ArrayList<View> views = getAllChildren(getSuperParent(((View) v.getParent())));
                        for (View view : views) view.setVisibility(View.GONE);
                    } catch (Exception e) {
                        //Everything's Cool
                    }
                }
            };
            ArrayList<View> vs = getAllChildren(layout.findViewById(R.id.toast_layout_root));
            for (View v : vs) v.setOnClickListener(dismissListener);
            toast.show();
        } catch (Exception e) {
            //Everything's cool
        }
    }

    public static View getSuperParent(View v) {
        try {
            if (v.getParent() != null) return getSuperParent((View) v.getParent());
            else return v;
        } catch (Exception e) {
            return v;
        }
    }

    public static ArrayList<View> getAllChildren(View v) {
        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            return viewArrayList;
        } else {
            ArrayList<View> result = new ArrayList<>();
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                ArrayList<View> viewArrayList = new ArrayList<>();
                viewArrayList.add(v);
                viewArrayList.addAll(getAllChildren(child));
                result.addAll(viewArrayList);
            }
            return result;
        }
    }

    /**
     * Get the version of the keithapps logo that should be shown with the given color
     *
     * @param color the color to find an image that is associated with it
     * @return code to one of the
     */
    private static int getColoredImage(int color) {
        switch (color) {
            case Color.RED:
                return R.drawable.keithapps_red;
            case Color.BLUE:
                return R.drawable.keithapps_blue;
            case Color.GREEN:
                return R.drawable.keithapps_limegreen;
            case Color.GRAY:
                return R.drawable.keithapps_gray;
            case Color.YELLOW:
                return R.drawable.keithapps_yellow;
            case Color.BLACK:
                return R.drawable.keithapps_black;
            case Color.MAGENTA:
                return R.drawable.keithapps_purple;
            case Color.CYAN:
                return R.drawable.keithapps_cyan;
        }
        return R.drawable.keithapps_white;
    }

    /**
     * Show KeithToast
     *
     * @param text    the text to show
     * @param context the context of the Toast caller
     */
    public static void show(String text, Context context) {
        show(text, context, getRandomColor(), Toast.LENGTH_SHORT);
    }

    /**
     * Get a random color
     *
     * @return one of the possible random colors used for the Toast
     */
    private static int getRandomColor() {
        int r = new Random().nextInt(8);
        switch (r) {
            case 0:
                return Color.GRAY;
            case 1:
                return Color.WHITE;
            case 2:
                return Color.RED;
            case 3:
                return Color.CYAN;
            case 4:
                return Color.MAGENTA;
            case 5:
                return Color.GREEN;
            case 6:
                return Color.YELLOW;
            case 7:
                return Color.WHITE;
        }
        return Color.WHITE;
    }

    /**
     * Show the toast using the general color given
     *
     * @param text    the text to show
     * @param context the context of the Toast caller
     * @param color   the general color of the Toast to show
     */
    public static void showWithColor(String text, Context context, int color) {
        show(text, context, color, Toast.LENGTH_LONG);
    }

    /**
     * Show the given text for a shorter period of time
     *
     * @param text    the text to show
     * @param context context to show the toast in
     */
    public static void showLong(String text, Context context) {
        show(text, context, getRandomColor(), Toast.LENGTH_LONG);
    }
}
