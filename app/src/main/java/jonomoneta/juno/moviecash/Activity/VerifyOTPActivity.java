package jonomoneta.juno.moviecash.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import jonomoneta.juno.moviecash.CustomEditText;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.OTPResponse;
import jonomoneta.juno.moviecash.Model.Response.UserDetailsResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.PermissionUtils;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VerifyOTPActivity extends AppCompatActivity {

    private String TAG = "Tag", mobileno, token, mVerificationId;
    private ImageView backBtn;
    private CustomTextView startBtn, resendTextView;
    private CustomEditText otpEditText;
    private ProgressBar progressBar;
    private PreferenceSettings mPreferenceSettings;
    FirebaseAuth mAuth;
    private Object mCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();

        token = mPreferenceSettings.getFirebaseToken();
        mobileno = getIntent().getStringExtra("mobileno");
        Log.e(TAG, "onCreate: mob no : " + mobileno);
        mAuth = FirebaseAuth.getInstance();


        PermissionUtils.requestPermission(VerifyOTPActivity.this, 0,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED);
        initialize();
        initCallbacks();
        startPhoneAuth();
    }

    private void initCallbacks() {
        showProgress();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                try {
                    mCode = phoneAuthCredential.getSmsCode();
                    if (mCode != null)
                        signInUser(phoneAuthCredential);
                } catch (Exception e) {
                    Log.e(TAG, "onVerificationCompleted: excp while auth" + e);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                // Show a message and update the UI
                // ...
                Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("authFail", e.getMessage());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                // Save verification ID and resending token so we can use them later
                mVerificationId = s;
                mResendToken = forceResendingToken;
                hideProgress();
            }
        };
    }

    public void signInUser(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        mPreferenceSettings.setIsLogin(true);
                        mPreferenceSettings.setMobileNumber(mobileno);
                        saveUserTokenAPI();
                        //TODO take action here
                        hideProgress();
//                        startActivity(new Intent(VerifyOTPActivity.this, EditProfileActivity.class));
//                        finish();
                        //end progress bar here
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //end progress bar here
                hideProgress();
                Toast.makeText(VerifyOTPActivity.this, "Failed to verify OTP, Please try again.", Toast.LENGTH_SHORT).show();
                Log.e("siginFail", e.getMessage());
            }
        });

    }

    private void startPhoneAuth() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobileno, 30, TimeUnit.SECONDS, this, mCallbacks);
    }

    private void initialize() {
        backBtn = findViewById(R.id.backBtn);
        startBtn = findViewById(R.id.startBtn);
        otpEditText = findViewById(R.id.otpEditText);
        progressBar = findViewById(R.id.progressBar);
        resendTextView = findViewById(R.id.resendTextView);

        showProgress();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //todo redundant
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isOnline(VerifyOTPActivity.this)) {
//                    verifyOTPAPI();
                    if (otpEditText.getText().toString().trim().length() > 0 && otpEditText.getText() != null) {
                        PhoneAuthCredential cred = PhoneAuthProvider.getCredential(mVerificationId, otpEditText.getText().toString().trim());
                        showProgress();
                        signInUser(cred);
                    } else {
                        Toast.makeText(VerifyOTPActivity.this, "Please enter OTP to verify.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Utility.noInternetError(VerifyOTPActivity.this);
                }
            }
        });

        resendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isOnline(VerifyOTPActivity.this)) {
//                    reSendOTPAPI();
                    //todo 30 sec timer here for halt time
                    showProgress();
                    resendOtp();
                } else {
                    Utility.noInternetError(VerifyOTPActivity.this);
                }
            }
        });
    }

    private void resendOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobileno, 30,
                TimeUnit.SECONDS, this, mCallbacks, mResendToken);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(VerifyOTPActivity.this, MobileNumberActivity.class));
        finish();
    }

    //todo redundant
    private void verifyOTPAPI() {

        if (otpEditText.getText().toString().trim().length() > 0) {
            showProgress();
            String otp = otpEditText.getText().toString().trim();

            RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
            Call<OTPResponse> verifyOTP = retroInterface.verifyOTP(mobileno, otp);
            verifyOTP.enqueue(new Callback<OTPResponse>() {
                @Override
                public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                    if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
//                        startActivity(new Intent(VerifyOTPActivity.this, MainActivity.class));
//                        finish();
                        mPreferenceSettings.setIsLogin(true);
                        mPreferenceSettings.setMobileNumber(mobileno);
                        saveUserTokenAPI();
                    } else {
                        hideProgress();
                        Toast.makeText(VerifyOTPActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<OTPResponse> call, Throwable t) {
                    hideProgress();
                    Toast.makeText(VerifyOTPActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void saveUserTokenAPI() {

        token = mPreferenceSettings.getFirebaseToken();

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<UserDetailsResponse> saveToken = retroInterface.saveUserToken(mobileno, token);
        saveToken.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                hideProgress();
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    if (response.body().getResponseMessage() != null &&
                            response.body().getResponseMessage().equalsIgnoreCase("existing")) {
                        mPreferenceSettings.setUserDetails(response.body());
                        mPreferenceSettings.setIsNewUser(false);
                        startActivity(new Intent(VerifyOTPActivity.this, MainActivity.class));
                        finish();

                    } else {
                        mPreferenceSettings.setIsNewUser(true);
                        mPreferenceSettings.setUserDetails(response.body());
                        startActivity(new Intent(VerifyOTPActivity.this, EditProfileActivity.class)
                                .putExtra("isNewUser", true));
                        finish();
                    }
                } else {
                    Toast.makeText(VerifyOTPActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                hideProgress();
                Toast.makeText(VerifyOTPActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    //todo redundant
    private void reSendOTPAPI() {
        progressBar.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<OTPResponse> sendOtp = retroInterface.reSendOTP(mobileno);
        sendOtp.enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    Toast.makeText(VerifyOTPActivity.this, "OTP sent successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerifyOTPActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(VerifyOTPActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        startBtn.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        startBtn.setVisibility(View.VISIBLE);
    }
}
