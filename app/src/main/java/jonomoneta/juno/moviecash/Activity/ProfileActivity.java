package jonomoneta.juno.moviecash.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import jonomoneta.juno.moviecash.CustomDialog;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.UserDetailsResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileActivity extends AppCompatActivity {

    private CustomTextView userNameTextView, quizEarnTextView, earnTextView, movWatchedTextView, trailerWatchedTextView, referralTextView;
    private PreferenceSettings mPreferenceSettings;
    private String selectedType = "";
    public static boolean editProf = false;
    private ImageView profileImageView, btnEditProf, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        initialize();
    }


    private void initialize() {

        btnEditProf = findViewById(R.id.btnEditProf);

        profileImageView = findViewById(R.id.profileImageView);
        userNameTextView = findViewById(R.id.userNameTextView);
        quizEarnTextView = findViewById(R.id.quizEarnTextView);
        earnTextView = findViewById(R.id.earnTextView);
        movWatchedTextView = findViewById(R.id.movWatchedTextView);
        trailerWatchedTextView = findViewById(R.id.trailerWatchedTextView);
        referralTextView = findViewById(R.id.referralTextView);
        backBtn = findViewById(R.id.backBtn);

        btnEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class)
                        .putExtra("isNewUser", false));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPreferenceSettings!=null && mPreferenceSettings.getUserDetails().getResponseData()!=null) {
                    if (mPreferenceSettings.getUserDetails().getResponseData().getProfilePicture() != null &&
                            mPreferenceSettings.getUserDetails().getResponseData().getProfilePicture().length() > 0) {
                        CustomDialog customDialog = new CustomDialog(ProfileActivity.this, mPreferenceSettings.getUserDetails().getResponseData().getProfilePicture());
                        customDialog.show();
                    }
                }
            }
        });

//        getProfileDetails();
        try {

            if (mPreferenceSettings.getUserDetails() != null) {
                if (mPreferenceSettings.getUserDetails().getResponseData() != null) {
                    if (mPreferenceSettings.getUserDetails().getResponseData().getName() != null &&
                            mPreferenceSettings.getUserDetails().getResponseData().getName().length() > 0) {
                        userNameTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getName());
                    } else {
                        userNameTextView.setText("Profile not set.");
                    }
                    if (mPreferenceSettings.getUserDetails().getResponseData().getMovieTypeIDs() != null &&
                            mPreferenceSettings.getUserDetails().getResponseData().getMovieTypeIDs().length() > 0) {
                        if (mPreferenceSettings.getUserDetails().getResponseData().getMovieTypeIDs() != null) {
                            selectedType = mPreferenceSettings.getUserDetails().getResponseData().getMovieTypeIDs();
                        }
                    }
                    earnTextView.setText("Total - $ " + mPreferenceSettings.getUserDetails().getResponseData().getTotalEarning() + " JM");
                    movWatchedTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getMoviesWatch());
                    trailerWatchedTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getTrailerWatch());
                    quizEarnTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getQuizWinner());
                    referralTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getReferFriend());
                    Picasso.get().load(mPreferenceSettings.getUserDetails().getResponseData().getProfilePicture())
                            .placeholder(R.drawable.user).error(R.drawable.user).into(profileImageView);
                } else {
                    getProfileDetails();
                }
            } else {
                getProfileDetails();
            }

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (editProf) {
            editProf = false;
            if (mPreferenceSettings.getUserDetails().getResponseData() != null) {
                if (mPreferenceSettings.getUserDetails().getResponseData().getName() != null &&
                        mPreferenceSettings.getUserDetails().getResponseData().getName().length() > 0) {
                    userNameTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getName());
                } else {
                    userNameTextView.setText("Profile not set.");
                }
                if (mPreferenceSettings.getUserDetails().getResponseData().getMovieTypeIDs() != null &&
                        mPreferenceSettings.getUserDetails().getResponseData().getMovieTypeIDs().length() > 0) {
                    selectedType = mPreferenceSettings.getUserDetails().getResponseData().getMovieTypeIDs();
                } else {
                    selectedType = "";
                }
                earnTextView.setText("Total - $ " + mPreferenceSettings.getUserDetails().getResponseData().getTotalEarning() + " JM");
                movWatchedTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getMoviesWatch());
                trailerWatchedTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getTrailerWatch());
                quizEarnTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getQuizWinner());
                referralTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getReferFriend());
                Picasso.get().load(mPreferenceSettings.getUserDetails().getResponseData().getProfilePicture())
                        .placeholder(R.drawable.user).error(R.drawable.user).into(profileImageView);
            }
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    private void getProfileDetails() {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);

        Call<UserDetailsResponse> getUserDetails = retroInterface.getUserProfile(mPreferenceSettings.getMobileNumber());
        getUserDetails.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    mPreferenceSettings.setUserDetails(response.body());
                    if (response.body().getResponseData().getName() != null &&
                            response.body().getResponseData().getName().length() > 0) {
                        userNameTextView.setText(response.body().getResponseData().getName());
                    } else {
                        userNameTextView.setText("Profile not set.");
                    }
                    if (response.body().getResponseData().getMovieTypeIDs() != null &&
                            response.body().getResponseData().getMovieTypeIDs().length() > 0) {
                        selectedType = mPreferenceSettings.getUserDetails().getResponseData().getMovieTypeIDs();
                    } else {
                        selectedType = "";
                    }
                    earnTextView.setText("Total - $ " + response.body().getResponseData().getTotalEarning() + " JM");
                    movWatchedTextView.setText(response.body().getResponseData().getMoviesWatch());
                    trailerWatchedTextView.setText(response.body().getResponseData().getTrailerWatch());
                    quizEarnTextView.setText(response.body().getResponseData().getQuizWinner());
                    referralTextView.setText(response.body().getResponseData().getReferFriend());
                    Picasso.get().load(response.body().getResponseData().getProfilePicture()).error(R.drawable.user).into(profileImageView);
                }
            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
