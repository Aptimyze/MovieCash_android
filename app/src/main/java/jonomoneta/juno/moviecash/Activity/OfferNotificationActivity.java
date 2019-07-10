package jonomoneta.juno.moviecash.Activity;

import androidx.appcompat.app.AppCompatActivity;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class OfferNotificationActivity extends AppCompatActivity {

    private ImageView closeBtn, offerIV;
    private CustomTextView offerDescTV;
    private String desc, imageUrl;
    private CustomTextViewBold playBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_notification);

        init();
    }

    private void init() {

        desc = getIntent().getStringExtra("desc");
        imageUrl = getIntent().getStringExtra("image");

        playBTN = findViewById(R.id.playBTN);
        closeBtn = findViewById(R.id.closeBtn);
        offerIV = findViewById(R.id.offerIV);
        offerDescTV = findViewById(R.id.offerDescTV);

        offerDescTV.setText(desc);
        Picasso.get().load(imageUrl).into(offerIV);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        playBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OfferNotificationActivity.this, MainActivity.class)
                        .putExtra("fromOffer", true));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OfferNotificationActivity.this, MainActivity.class));
        finish();
    }
}
