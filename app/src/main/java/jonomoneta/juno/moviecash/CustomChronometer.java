package jonomoneta.juno.moviecash;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Chronometer;
import android.widget.EditText;

public class CustomChronometer extends Chronometer {

    public CustomChronometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomChronometer(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Merriweather-Regular.otf");
        setTypeface(tf ,1);

    }
}
