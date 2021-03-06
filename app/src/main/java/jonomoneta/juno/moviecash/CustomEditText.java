package jonomoneta.juno.moviecash;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

public class CustomEditText extends AppCompatEditText {

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Merriweather-Regular.otf");
        setTypeface(tf ,1);

    }
}
