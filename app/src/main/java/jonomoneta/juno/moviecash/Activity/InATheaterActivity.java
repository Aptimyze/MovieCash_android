package jonomoneta.juno.moviecash.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.R;

public class InATheaterActivity extends AppCompatActivity {

    private ImageView closeBtn;
    private CustomTextViewBold linkTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_atheater);

        initialize();
    }

    private void initialize() {

        closeBtn = findViewById(R.id.closeBtn);
        linkTV = findViewById(R.id.linkTV);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "www.junomoneta.io");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = "https://www.junomoneta.io/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }, spanTxt.length() - "www.junomoneta.io".length(), spanTxt.length(), 0);
        spanTxt.append(" link to rewards - for more details.");
        linkTV.setMovementMethod(LinkMovementMethod.getInstance());
        linkTV.setHighlightColor(Color.TRANSPARENT);
        linkTV.setText(spanTxt);

    }

}
