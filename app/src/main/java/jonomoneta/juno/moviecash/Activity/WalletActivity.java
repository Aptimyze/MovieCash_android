package jonomoneta.juno.moviecash.Activity;

import androidx.appcompat.app.AppCompatActivity;
import jonomoneta.juno.moviecash.CustomEditText;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.Model.Response.ApplyWinnerRewardResponse;
import jonomoneta.juno.moviecash.Model.Response.CommentResponse;
import jonomoneta.juno.moviecash.Model.Response.CommonResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import static jonomoneta.juno.moviecash.Activity.PredictionActivity.homePlayer;

public class WalletActivity extends AppCompatActivity {

    private ImageView backBtn;
    private CustomTextViewBold walletAmtTV, megaWalletAmtTV, megaWalletClaimBTN;
    private PreferenceSettings mPreferenceSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        init();
    }

    private void init() {

        backBtn = findViewById(R.id.backBtn);
        walletAmtTV = findViewById(R.id.walletAmtTV);
        megaWalletAmtTV = findViewById(R.id.megaWalletAmtTV);
        megaWalletClaimBTN = findViewById(R.id.megaWalletClaimBTN);

        walletAmtTV.setText("$ " + mPreferenceSettings.getUserDetails().getResponseData().getWalletAmount() + " JM");
        megaWalletAmtTV.setText("$ " + mPreferenceSettings.getUserDetails().getResponseData().getMegaWalletAmount() + " JM");
//
//        if (mPreferenceSettings.getUserDetails().getResponseData().getMegaWalletAmount() > 0) {
//            megaWalletClaimBTN.setVisibility(View.VISIBLE);
//        } else {
//            megaWalletClaimBTN.setVisibility(View.GONE);
//        }
        megaWalletClaimBTN.setVisibility(View.GONE);

        megaWalletClaimBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFormDialog(WalletActivity.this);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        homePlayer.start();
    }

    private void showFormDialog(final Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.redeem_form_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.flag_transparent));
        final String mobNo = MyApplication.getInstance().getPreferenceSettings().getMobileNumber();


        final CustomEditText fullNameET = dialog.findViewById(R.id.fullNameET),
                emailET = dialog.findViewById(R.id.emailET),
                addressET = dialog.findViewById(R.id.addressET);
        final CustomTextView mobileNoTV = dialog.findViewById(R.id.mobileNoTV);
        final CustomTextViewBold
                cancelTV = dialog.findViewById(R.id.cancelTV),
                redeemTV = dialog.findViewById(R.id.redeemTV);
        final ProgressBar loader = dialog.findViewById(R.id.loader);

        mobileNoTV.setText(mobNo);

        redeemTV.setText("CLAIM");
        redeemTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String email, fullName, address;

                fullName = fullNameET.getText().toString().trim();
                email = emailET.getText().toString().trim();
                address = addressET.getText().toString().trim();

                if (!fullName.isEmpty()) {
                    if (!email.isEmpty()) {
                        if (Utility.isValid(email)) {
                            if (!address.isEmpty()) {

                                if (Utility.isOnline(context)) {
                                    redeemTV.setVisibility(View.GONE);
                                    loader.setVisibility(View.VISIBLE);
                                    claimMegaWalletAPI(redeemTV, loader, dialog, mobNo, fullName, email, address);
                                } else {
                                    Utility.noInternetError(context);
                                }
                            } else {
                                Toast.makeText(context, "Please enter address", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Please enter your full name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void claimMegaWalletAPI(final CustomTextViewBold redeemTV, final ProgressBar loader, final Dialog dialog, String mobno, String fullname,
                                    String email, String address) {

        int uid = mPreferenceSettings.getUserDetails().getResponseData().getID();
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<CommonResponse> claimMegaWallet = retroInterface.claimMegaWalletAmount(uid, mobno, fullname, email, address, 100);
        claimMegaWallet.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                redeemTV.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    Toast.makeText(WalletActivity.this, "Claim process completed successfully.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    Utility.getProfileDetails();
                    megaWalletAmtTV.setText("$ " + mPreferenceSettings.getUserDetails().getResponseData().getMegaWalletAmount());
                } else {
                    Toast.makeText(WalletActivity.this, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                redeemTV.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                Toast.makeText(WalletActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
