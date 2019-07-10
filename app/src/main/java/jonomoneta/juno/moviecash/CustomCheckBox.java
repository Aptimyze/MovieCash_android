package jonomoneta.juno.moviecash;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.util.AttributeSet;

//test
public class CustomCheckBox extends AppCompatCheckBox {

    public CustomCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCheckBox(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Merriweather-Regular.otf");
        setTypeface(tf ,1);

    }
}
