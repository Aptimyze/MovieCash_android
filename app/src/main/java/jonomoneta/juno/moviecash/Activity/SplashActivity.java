package jonomoneta.juno.moviecash.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;


public class SplashActivity extends AppCompatActivity {

    private PreferenceSettings mPreferenceSettings;
    TextView participateTV, rewardTV, redeemTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();

        participateTV = findViewById(R.id.participateTV);
        rewardTV = findViewById(R.id.rewardTV);
        redeemTV = findViewById(R.id.redeemTV);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/berkshireswash-regular.ttf");
        participateTV.setTypeface(custom_font);
        rewardTV.setTypeface(custom_font);
        redeemTV.setTypeface(custom_font);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mPreferenceSettings.getIsLogin()) {
                    startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 1000);
    }
}
