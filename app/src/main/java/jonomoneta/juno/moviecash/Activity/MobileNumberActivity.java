package jonomoneta.juno.moviecash.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import jonomoneta.juno.moviecash.CustomEditText;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.OTPResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.PermissionUtils;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MobileNumberActivity extends AppCompatActivity {

    private CustomTextView btnNext;
    private CustomEditText mobNumEditText;
    private ProgressBar progressBar;
    private CountryCodePicker countryCodePicker;
    private TextView descTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);

        PermissionUtils.requestPermission(MobileNumberActivity.this, 0,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED);
        initialize();
    }

    private void initialize() {

        btnNext = findViewById(R.id.btnNext);
        mobNumEditText = findViewById(R.id.mobNumEditText);
        progressBar = findViewById(R.id.progressBar);
        countryCodePicker = findViewById(R.id.ccp);
        descTextView = findViewById(R.id.descTextView);

        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        countryCodePicker.setCountryForNameCode(countryCodeValue);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isOnline(MobileNumberActivity.this)) {
//                    sendOTPAPI();
                    if (mobNumEditText.getText().toString().trim() != null && mobNumEditText.getText().toString().trim().length() > 0) {
                        final String mobNumber = "+" + countryCodePicker.getSelectedCountryCode() + mobNumEditText.getText().toString().trim();
                        MyApplication.getInstance().getPreferenceSettings().setMobileNumber(mobNumber);
                        startActivity(new Intent(MobileNumberActivity.this, VerifyOTPActivity.class)
                                .putExtra("mobileno", mobNumber));
                        finish();
                    } else {
                        Toast.makeText(MobileNumberActivity.this, "Please enter mobile number.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Utility.noInternetError(MobileNumberActivity.this);
                }
            }
        });

        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "By signing up, I confirm that I am over 18 years old, and I agree to MovieCash ");
        spanTxt.append("privacy policy");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = "https://junomoneta.io/movie-cash";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }, spanTxt.length() - "privacy policy".length(), spanTxt.length(), 0);
        spanTxt.append(" and");
        spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), 94, spanTxt.length(), 0);
        spanTxt.append(" tearms of services.");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = "https://junomoneta.io/movie-cash";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }, spanTxt.length() - " tearms of services.".length(), spanTxt.length(), 0);
        descTextView.setMovementMethod(LinkMovementMethod.getInstance());
        descTextView.setHighlightColor(Color.TRANSPARENT);
        descTextView.setText(spanTxt);
    }


    //todo redundant, @edit use fragment instead to reduce complexity
    private void sendOTPAPI() {

        //todo replace null with " "
        if (mobNumEditText.getText().toString().trim() != null && mobNumEditText.getText().toString().trim().length() > 0) {
            showProgress();
            final String mobNumber = "+" + countryCodePicker.getSelectedCountryCode() + mobNumEditText.getText().toString().trim();
            RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
            Call<OTPResponse> sendOtp = retroInterface.sendOTP(mobNumber);
            sendOtp.enqueue(new Callback<OTPResponse>() {
                @Override
                public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                    hideProgress();
                    if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                        Toast.makeText(MobileNumberActivity.this, "OTP sent successfully.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MobileNumberActivity.this, VerifyOTPActivity.class)
                                .putExtra("mobileno", mobNumber));
                        finish();
                    } else {
                        Toast.makeText(MobileNumberActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<OTPResponse> call, Throwable t) {
                    hideProgress();
                    Toast.makeText(MobileNumberActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please enter mobile number.", Toast.LENGTH_SHORT).show();
        }

    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        btnNext.setVisibility(View.VISIBLE);
    }

}
