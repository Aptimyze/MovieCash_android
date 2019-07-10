package jonomoneta.juno.moviecash.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.Model.Response.GenerateQR;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefferalActivity extends AppCompatActivity {

    private TextView referCodeTextView;
    private LinearLayout copyLL, btnInvite;
    private PreferenceSettings mPreferenceSettings;
    private String refCode;
    private ImageView backBtn, qrCodeIV;
    private AVLoadingIndicatorView loader;
    private CustomTextViewBold generateCodeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refferal);

        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();

        if (mPreferenceSettings.getUserDetails().getResponseData() != null) {
            refCode = mPreferenceSettings.getUserDetails().getResponseData().getReferCode();
        }

        initialize();
    }

    private void initialize() {

        backBtn = findViewById(R.id.backBtn);
        referCodeTextView = findViewById(R.id.referCodeTextView);
        copyLL = findViewById(R.id.copyLL);
        btnInvite = findViewById(R.id.btnInvite);
        qrCodeIV = findViewById(R.id.qrCodeIV);
        loader = findViewById(R.id.loader);
        generateCodeBtn = findViewById(R.id.generateCodeBtn);

        referCodeTextView.setText(refCode);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        loader.setVisibility(View.GONE);

        if (mPreferenceSettings.getUserDetails().getResponseData() != null) {
            if (mPreferenceSettings.getUserDetails().getResponseData().getQRCode() != null &&
                    mPreferenceSettings.getUserDetails().getResponseData().getQRCode().length() > 0) {
                generateCodeBtn.setVisibility(View.GONE);
                loader.setVisibility(View.VISIBLE);
                Picasso.get().load(mPreferenceSettings.getUserDetails().getResponseData().getQRCode())
                        .into(qrCodeIV);
                Picasso.get().load(mPreferenceSettings.getUserDetails().getResponseData().getQRCode())
                        .into(qrCodeIV, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                loader.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                generateCodeBtn.setVisibility(View.VISIBLE);
                                loader.setVisibility(View.GONE);
                            }
                        });
            } else {
                generateCodeBtn.setVisibility(View.VISIBLE);
                Log.e("QR", "null");
            }
        } else {
            Log.e("Profile", "null");
        }

        copyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("code", refCode);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(RefferalActivity.this, "Code copied to clipboard.", Toast.LENGTH_SHORT).show();
            }
        });

        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = "I am playing and enjoying quiz on MovieCash! You should enjoy too. Use my code - *" + refCode + "* to sign up and earn rewards." + "\n\nGet app now:\nhttps://play.google.com/store/apps/details?id=movieosis.mdadil2019.movieosis";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share with"));
            }
        });

        generateCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isOnline(RefferalActivity.this)) {
                    generateCodeBtn.setVisibility(View.GONE);
                    loader.setVisibility(View.VISIBLE);
                    generateQrCodeAPI();
                } else {
                    Utility.noInternetError(RefferalActivity.this);
                }
            }
        });

    }

    private void generateQrCodeAPI() {

        int userId = mPreferenceSettings.getUserDetails().getResponseData().getID();
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GenerateQR> generateQr = retroInterface.generateQrCode(userId, refCode);
        generateQr.enqueue(new Callback<GenerateQR>() {
            @Override
            public void onResponse(Call<GenerateQR> call, Response<GenerateQR> response) {
//                loader.setVisibility(View.GONE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    Utility.getProfileDetails();
                    Picasso.get().load(response.body().getResponseData())
                            .into(qrCodeIV, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    loader.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    loader.setVisibility(View.GONE);
                                    generateCodeBtn.setVisibility(View.VISIBLE);
                                }
                            });
//                    Picasso.get().load(response.body().getResponseData()).into(qrCodeIV);
                }
            }

            @Override
            public void onFailure(Call<GenerateQR> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
