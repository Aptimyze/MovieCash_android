package jonomoneta.juno.moviecash.Activity;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import jonomoneta.juno.moviecash.Adapter.MyVideoListAdapter;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.GetAllTalentBankVideosByUserResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class TalentUserProfileActivity extends AppCompatActivity {

    private ImageView backBtn, downloadBTN;
    private CircleImageView myProfileIV;
    private CustomTextView fullNameTV, mobNoTV, totalVideoTV, errorTextView;
    private GridView videoGridView;
    private AVLoadingIndicatorView loader;
    private static boolean isLoading = false, hasMoreItems = true;
    public static int currPage = 1;
    ArrayList<GetAllTalentBankVideosByUserResponse.ResponseData> videoList;
    private MyVideoListAdapter userVideoListAdapter;
    int userId;
    String name, image, mobNo, formUrl;
    PreferenceSettings mPreferenceSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent_user_profile);

        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        init();
    }

    private void init() {

        userId = getIntent().getIntExtra("userid", 0);
        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("profImage");
        mobNo = getIntent().getStringExtra("mobno");
        formUrl = getIntent().getStringExtra("formUrl");

        currPage = 1;
        backBtn = findViewById(R.id.backBtn);
        myProfileIV = findViewById(R.id.myProfileIV);
        fullNameTV = findViewById(R.id.fullNameTV);
        mobNoTV = findViewById(R.id.mobNoTV);
        totalVideoTV = findViewById(R.id.totalVideoTV);
        errorTextView = findViewById(R.id.errorTextView);
        videoGridView = findViewById(R.id.videoGridView);
        loader = findViewById(R.id.loader);
        downloadBTN = findViewById(R.id.downloadBTN);

        if (mPreferenceSettings.getUserDetails().getResponseData().isWorldsBestJudge()) {
            downloadBTN.setVisibility(View.VISIBLE);
        } else {
            downloadBTN.setVisibility(View.GONE);
        }

        downloadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isOnline(TalentUserProfileActivity.this)) {
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                    i.setData(Uri.parse(formUrl));
                    startActivity(i);
                } else {
                    Utility.noInternetError(TalentUserProfileActivity.this);
                }
            }
        });

        videoList = new ArrayList<>();
        userVideoListAdapter = new MyVideoListAdapter(TalentUserProfileActivity.this, videoList, false, false);
        videoGridView.setAdapter(userVideoListAdapter);

        Picasso.get().load(image).placeholder(R.drawable.user).error(R.drawable.user).into(myProfileIV);
        fullNameTV.setText(name);
        mobNoTV.setText(mobNo);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getUserVideosAPI(userId, currPage);

        videoGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0) {
                    int lastVisibleItem = firstVisibleItem + visibleItemCount;
                    if (!isLoading && hasMoreItems && (lastVisibleItem == totalItemCount)) {
                        //load more items--
                        if (Utility.isOnline(TalentUserProfileActivity.this)) {
                            isLoading = true;
                            currPage += 1;

                            getUserVideosAPI(userId, currPage);

                        }
                    }
                }
            }
        });
    }

    private void getUserVideosAPI(int uid, int pgNo) {
        loader.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GetAllTalentBankVideosByUserResponse> getMyVideo = retroInterface.getTalentBankVideoByUser(uid, pgNo, 10, false);
        getMyVideo.enqueue(new Callback<GetAllTalentBankVideosByUserResponse>() {
            @Override
            public void onResponse(Call<GetAllTalentBankVideosByUserResponse> call, Response<GetAllTalentBankVideosByUserResponse> response) {
                loader.setVisibility(View.GONE);
                try {
                    if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                        isLoading = false;
                        if (response.body().getResponseData() != null &&
                                response.body().getResponseData().size() > 0) {
                            totalVideoTV.setText(response.body().getResponseData().get(0).getTotalRecord() + "");
                            hasMoreItems = true;
                            videoList.addAll(response.body().getResponseData());
                            if (videoList != null && videoList.size() > 0) {
                                errorTextView.setVisibility(View.GONE);


                            } else {
                                errorTextView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (currPage == 1) {
                                errorTextView.setVisibility(View.VISIBLE);
                            }
                            hasMoreItems = false;
                        }
                        if (currPage == 1) {
                            videoGridView.smoothScrollToPosition(0);
                        }
                        userVideoListAdapter.notifyDataSetChanged();
                    } else {
                        errorTextView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.e("videoLoadExc", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<GetAllTalentBankVideosByUserResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                errorTextView.setVisibility(View.VISIBLE);
                Toast.makeText(TalentUserProfileActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
