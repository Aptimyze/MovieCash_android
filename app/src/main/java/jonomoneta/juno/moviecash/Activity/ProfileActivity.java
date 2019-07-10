package jonomoneta.juno.moviecash.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jonomoneta.juno.moviecash.Adapter.CategoryAdapter;
import jonomoneta.juno.moviecash.CustomDialog;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.MovieTypesResponse;
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
    private ArrayList<MovieTypesResponse.ResponseData> movieTypesList;
    private RecyclerView categoryGridView;
    private PreferenceSettings mPreferenceSettings;
    private String selectedType = "";
    private NestedScrollView nestedSCrollView;
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
        categoryGridView = findViewById(R.id.categoryGridView);
        profileImageView = findViewById(R.id.profileImageView);
        userNameTextView = findViewById(R.id.userNameTextView);
        quizEarnTextView = findViewById(R.id.quizEarnTextView);
        earnTextView = findViewById(R.id.earnTextView);
        movWatchedTextView = findViewById(R.id.movWatchedTextView);
        trailerWatchedTextView = findViewById(R.id.trailerWatchedTextView);
        nestedSCrollView = findViewById(R.id.nestedSCrollView);
        referralTextView = findViewById(R.id.referralTextView);
        backBtn = findViewById(R.id.backBtn);

        categoryGridView.setLayoutManager(new GridLayoutManager(this, 2));
        categoryGridView.setItemAnimator(new DefaultItemAnimator());
        categoryGridView.setNestedScrollingEnabled(false);

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

        movieTypesList = new ArrayList<>();


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
//                    String dateTimeArray[] = mPreferenceSettings.getUserDetails().getResponseData().getBirthDate().split("T");
//                    String date = dateTimeArray[0];
//                    long year = Long.parseLong(date.split("-")[0]);
//                    Calendar calendar = Calendar.getInstance();
//                    long curYear = calendar.get(Calendar.YEAR);
//                    if (year == 1) {
//                        ageTextView.setText("Age");
//                    } else {
//                        ageTextView.setText(String.valueOf(curYear - year));
//                    }
                    earnTextView.setText("Total - $ " + mPreferenceSettings.getUserDetails().getResponseData().getTotalEarning() + " JM");
                    movWatchedTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getMoviesWatch());
                    trailerWatchedTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getTrailerWatch());
                    quizEarnTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getQuizWinner());
                    referralTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getReferFriend());
                    Picasso.get().load(mPreferenceSettings.getUserDetails().getResponseData().getProfilePicture())
                            .placeholder(R.drawable.user).error(R.drawable.user).into(profileImageView);

                    if (mPreferenceSettings.getMovieTypes() != null) {
                        if (mPreferenceSettings.getMovieTypes().getResponseData() != null &&
                                mPreferenceSettings.getMovieTypes().getResponseData().size() > 0) {
                            movieTypesList = mPreferenceSettings.getMovieTypes().getResponseData();
                            CategoryAdapter categoryAdapter = new CategoryAdapter(ProfileActivity.this, movieTypesList, selectedType, false);
                            categoryGridView.setAdapter(categoryAdapter);
//                categoryAdapter.notifyDataSetChanged();
                        }
                    } else {
                        getMovieTypeAPI();
                    }
                } else {
                    getProfileDetails();
                }
            } else {
                getProfileDetails();
            }

//            if (mPreferenceSettings.getMovieTypes() != null) {
//                if (mPreferenceSettings.getMovieTypes().getResponseData() != null &&
//                        mPreferenceSettings.getMovieTypes().getResponseData().size() > 0) {
//                    movieTypesList = mPreferenceSettings.getMovieTypes().getResponseData();
//                    CategoryAdapter categoryAdapter = new CategoryAdapter(ProfileActivity.this, movieTypesList, selectedType, false);
//                    categoryGridView.setAdapter(categoryAdapter);
////                categoryAdapter.notifyDataSetChanged();
//                }
//            } else {
//                getMovieTypeAPI();
//            }

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (editProf) {
            editProf = false;
            nestedSCrollView.fullScroll(NestedScrollView.FOCUS_UP);
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
//                String dateTimeArray[] = mPreferenceSettings.getUserDetails().getResponseData().getBirthDate().split("T");
//                String date = dateTimeArray[0];
//                long year = Long.parseLong(date.split("-")[0]);
//                Calendar calendar = Calendar.getInstance();
//                long curYear = calendar.get(Calendar.YEAR);
//                if (year == 1) {
//                    ageTextView.setText("Age");
//                } else {
//                    ageTextView.setText(String.valueOf(curYear - year));
//                }
                earnTextView.setText("Total - $ " + mPreferenceSettings.getUserDetails().getResponseData().getTotalEarning() + " JM");
                movWatchedTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getMoviesWatch());
                trailerWatchedTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getTrailerWatch());
                quizEarnTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getQuizWinner());
                referralTextView.setText(mPreferenceSettings.getUserDetails().getResponseData().getReferFriend());
                Picasso.get().load(mPreferenceSettings.getUserDetails().getResponseData().getProfilePicture())
                        .placeholder(R.drawable.user).error(R.drawable.user).into(profileImageView);
            }

            if (mPreferenceSettings.getMovieTypes() != null) {
                if (mPreferenceSettings.getMovieTypes().getResponseData() != null &&
                        mPreferenceSettings.getMovieTypes().getResponseData().size() > 0) {
                    movieTypesList = mPreferenceSettings.getMovieTypes().getResponseData();
                    CategoryAdapter categoryAdapter = new CategoryAdapter(ProfileActivity.this, movieTypesList, selectedType, false);
                    categoryGridView.setAdapter(categoryAdapter);
//                categoryAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void getMovieTypeAPI() {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        final PreferenceSettings mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        Call<MovieTypesResponse> getMovieTypes = retroInterface.getMovieTypes();
        getMovieTypes.enqueue(new Callback<MovieTypesResponse>() {
            @Override
            public void onResponse(Call<MovieTypesResponse> call, Response<MovieTypesResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    mPreferenceSettings.setMovieTypes(response.body());
                    if (response.body().getResponseData().size() > 0) {
                        movieTypesList = response.body().getResponseData();
                        CategoryAdapter categoryAdapter = new CategoryAdapter(ProfileActivity.this, movieTypesList, selectedType, false);
                        categoryGridView.setAdapter(categoryAdapter);
//                        categoryAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieTypesResponse> call, Throwable t) {
                Log.e("movieTypesFailure", t.getMessage());
            }
        });
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
//                    String dateTimeArray[] = response.body().getResponseData().getBirthDate().split("T");
//                    String date = dateTimeArray[0];
//                    long year = Long.parseLong(date.split("-")[0]);
//                    Calendar calendar = Calendar.getInstance();
//                    long curYear = calendar.get(Calendar.YEAR);
//                    if (year == 1) {
//                        ageTextView.setText("Age");
//                    } else {
//                        ageTextView.setText(String.valueOf(curYear - year));
//                    }
                    earnTextView.setText("Total - $ " + response.body().getResponseData().getTotalEarning() + " JM");
                    movWatchedTextView.setText(response.body().getResponseData().getMoviesWatch());
                    trailerWatchedTextView.setText(response.body().getResponseData().getTrailerWatch());
                    quizEarnTextView.setText(response.body().getResponseData().getQuizWinner());
                    referralTextView.setText(response.body().getResponseData().getReferFriend());
                    Picasso.get().load(response.body().getResponseData().getProfilePicture()).error(R.drawable.user).into(profileImageView);

                    if (mPreferenceSettings.getMovieTypes() != null) {
                        if (mPreferenceSettings.getMovieTypes().getResponseData() != null &&
                                mPreferenceSettings.getMovieTypes().getResponseData().size() > 0) {
                            movieTypesList = mPreferenceSettings.getMovieTypes().getResponseData();
                            CategoryAdapter categoryAdapter = new CategoryAdapter(ProfileActivity.this, movieTypesList, selectedType, false);
                            categoryGridView.setAdapter(categoryAdapter);
//                categoryAdapter.notifyDataSetChanged();
                        }
                    } else {
                        getMovieTypeAPI();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
