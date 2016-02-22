package keithapps.mobile.com.jeeves;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import static keithapps.mobile.com.jeeves.tools.SystemTools.getFont;

public class FontSpinnerAdapter extends ArrayAdapter<String> {
    // Initialise custom font, for example:
    Typeface font;

    // (In reality I used a manager which caches the Typeface objects)
    // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

    public FontSpinnerAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(getFont(position, getContext()));
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(getFont(position, getContext()));
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        return view;
    }
}
