package jonomoneta.juno.moviecash.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.SpannableStringBuilder;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.stripe.android.view.CardMultilineWidget;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.activity.VideoPickActivity;
import com.vincent.filepicker.filter.entity.NormalFile;
import com.vincent.filepicker.filter.entity.VideoFile;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import jonomoneta.juno.moviecash.Adapter.MyVideoListAdapter;
import jonomoneta.juno.moviecash.Adapter.TalentCategoryAdapter;
import jonomoneta.juno.moviecash.Adapter.TrendingAdapter;
import jonomoneta.juno.moviecash.Adapter.UpcomingMovieSynopsisAdapter;
import jonomoneta.juno.moviecash.CustomEditText;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.Model.Response.CommonResponse;
import jonomoneta.juno.moviecash.Model.Response.GetAllBestTalentBankVideosResponse;
import jonomoneta.juno.moviecash.Model.Response.GetAllTalentBankVideosByUserResponse;
import jonomoneta.juno.moviecash.Model.Response.GetQuizGamePaymentPlanResponse;
import jonomoneta.juno.moviecash.Model.Response.GetTalentCategoryListResponse;
import jonomoneta.juno.moviecash.Model.Response.SaveVideoReviewResponse;
import jonomoneta.juno.moviecash.Model.Response.WorldsBestSubscriptionResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorldsBestActivity extends AppCompatActivity {

    private RelativeLayout homeRL, profileRL, mainRL;
    private LinearLayout addVideoLL, trendLL, subscribeLL, likeDislikeLL;
    private VideoView videoView, homeVideoView;
    private CustomTextViewBold selectOtherVideoBtn, goBTN, uploadVideoBtn, subsTv, myVideoBTN, reviewedBTN;
    private AVLoadingIndicatorView loader;
    private CustomTextView categoryTV, addVideoCategoryTV, errorTextView, retryBTN, userNameTV, fullNameTV,
            mobNoTV, upcomingMoviesBTN, moreDetailsBTN, rulesTV, termsTV, demoBTN;
    public static CustomTextView totalVideoTV;
    private String stripeCusID = "", email, worldsBestSubsEndDate, worldsBestSubsStartDate;
    private PreferenceSettings mPreferenceSettings;
    private ImageView subsIv;
    private CircleImageView menuBTN, closeBTN, userImgView, myProfileIV;
    private int uId;
    private String home = "home", add = "add", trend = "trend", profile = "profile",
            videoPath, lastVideoPath = null, videoFileName = null;
    private Uri fileUri = null;
    private String videoBase64 = null;
    private ImageView backBtn;
    private double fileSize = 0;
    private ArrayList<GetAllTalentBankVideosByUserResponse.ResponseData> videoList, reviewedVideoList;
    private ArrayList<GetAllBestTalentBankVideosResponse.ResponseData> trendingList;
    public static ArrayList<GetAllTalentBankVideosByUserResponse.ResponseData> myVideosList;
    private LinearLayout homeBTN, addBTN, trendBTN, profileBTN;
    private ImageView homeIV, addIV, trendIV, profileIV;
    private TextView homeTV, addTV, trendTV, profileTV;
    private static boolean isLoading = false, hasMoreItems = true;
    private static boolean isLoadingReview = false, hasMoreItemsReview = true;
    private static boolean isLoadingTrend = false, hasMoreItemsTrend = true;
    public static int currPage = 1, currPageReviewed = 1, currPageTrend = 1;
    private MyVideoListAdapter myVideoListAdapter, reviewedVideoListAdapter;
    //    private MyVideoRecyclerAdapter myVideoRecyclerAdapter;
    private GridView videoGridView, reviewedGridView;
    private ImageView disLikeBTN, likeBTN, competitionBTN;
    private ProgressBar reviewCountProgressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private ArrayList<GetTalentCategoryListResponse.ResponseData> categoryList;
    public static int categoryId = 0;
    public static String catName = "";
    private GridView trendingRV;
    private TrendingAdapter trendingAdapter;
    private double worldbest_subs_amount = 0, judge_subscription_amount = 0;
    private boolean isSubscribed = false, isRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worlds_best);


        init();
    }

    private void init() {

        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();

        menuBTN = findViewById(R.id.menuBTN);
        trendingRV = findViewById(R.id.trendingRV);
        trendBTN = findViewById(R.id.trendBTN);
        trendIV = findViewById(R.id.trendIV);
        trendTV = findViewById(R.id.trendTV);
        trendLL = findViewById(R.id.trendLL);
        termsTV = findViewById(R.id.termsTV);
        rulesTV = findViewById(R.id.rulesTV);
        addVideoCategoryTV = findViewById(R.id.addVideoCategoryTV);
        goBTN = findViewById(R.id.goBTN);
        categoryTV = findViewById(R.id.categoryTV);
        reviewedGridView = findViewById(R.id.reviewedGridView);
        myVideoBTN = findViewById(R.id.myVideoBTN);
        reviewedBTN = findViewById(R.id.reviewedBTN);
        subscribeLL = findViewById(R.id.subscribeLL);
        homeRL = findViewById(R.id.homeRL);
        profileRL = findViewById(R.id.profileRL);
        addVideoLL = findViewById(R.id.addVideoLL);
        videoView = findViewById(R.id.videoView);
        homeVideoView = findViewById(R.id.homeVideoView);
        selectOtherVideoBtn = findViewById(R.id.selectOtherVideoBtn);
        uploadVideoBtn = findViewById(R.id.uploadVideoBtn);
        loader = findViewById(R.id.loader);
        errorTextView = findViewById(R.id.errorTextView);
        subsTv = findViewById(R.id.subsTv);
        subsIv = findViewById(R.id.subsIv);
        closeBTN = findViewById(R.id.closeBTN);
        mainRL = findViewById(R.id.mainRL);
        backBtn = findViewById(R.id.backBtn);
        userImgView = findViewById(R.id.userImgView);
        userNameTV = findViewById(R.id.userNameTV);
        homeBTN = findViewById(R.id.homeBTN);
        addBTN = findViewById(R.id.addBTN);
        profileBTN = findViewById(R.id.profileBTN);
        homeIV = findViewById(R.id.homeIV);
        addIV = findViewById(R.id.addIV);
        profileIV = findViewById(R.id.profileIV);
        homeTV = findViewById(R.id.homeTV);
        addTV = findViewById(R.id.addTV);
        profileTV = findViewById(R.id.profileTV);
        videoGridView = findViewById(R.id.videoGridView);
        myProfileIV = findViewById(R.id.myProfileIV);
        fullNameTV = findViewById(R.id.fullNameTV);
        mobNoTV = findViewById(R.id.mobNoTV);
        totalVideoTV = findViewById(R.id.totalVideoTV);
        likeDislikeLL = findViewById(R.id.likeDislikeLL);
        disLikeBTN = findViewById(R.id.disLikeBTN);
        likeBTN = findViewById(R.id.likeBTN);
        reviewCountProgressBar = findViewById(R.id.reviewCountProgressBar);
        retryBTN = findViewById(R.id.retryBTN);
        competitionBTN = findViewById(R.id.competitionBTN);
        upcomingMoviesBTN = findViewById(R.id.upcomingMoviesBTN);
        moreDetailsBTN = findViewById(R.id.moreDetailsBTN);
        demoBTN = findViewById(R.id.demoBTN);

        videoList = new ArrayList<>();
        reviewedVideoList = new ArrayList<>();
        myVideosList = new ArrayList<>();
        categoryList = new ArrayList<>();
        trendingList = new ArrayList<>();

        trendingAdapter = new TrendingAdapter(WorldsBestActivity.this, trendingList);
        trendingRV.setAdapter(trendingAdapter);

        demoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isOnline(WorldsBestActivity.this)) {
                    startActivity(new Intent(WorldsBestActivity.this, DemoVideoActivity.class));
                } else {
                    Utility.noInternetError(WorldsBestActivity.this);
                }
            }
        });

        menuBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
//Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(WorldsBestActivity.this, menuBTN);
                //Inflating the Popup using xml file
                popup.inflate(R.menu.judge_menu);
//                        .inflate(R.menu.main, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_judge:
                                showJudgeSubscribeDialog();
                                break;
                        }
                        return true;
                    }
                });

                MenuPopupHelper menuHelper = new MenuPopupHelper(WorldsBestActivity.this, (MenuBuilder) popup.getMenu(), menuBTN);
                menuHelper.setForceShowIcon(true);
                menuHelper.show(); //showing popup menu
            }
        });


//        videoGridView.setLayoutManager(new GridLayoutManager(WorldsBestActivity.this, 3,RecyclerView.VERTICAL,false));

//        myVideoRecyclerAdapter = new MyVideoRecyclerAdapter(WorldsBestActivity.this, myVideosList);
        myVideoListAdapter = new MyVideoListAdapter(WorldsBestActivity.this, myVideosList, true, false);
        videoGridView.setAdapter(myVideoListAdapter);

        reviewedVideoListAdapter = new MyVideoListAdapter(WorldsBestActivity.this, reviewedVideoList, false, true);
        reviewedGridView.setAdapter(reviewedVideoListAdapter);

        getPlanPriceAPI();

        if (mPreferenceSettings.getUserDetails().getResponseData() != null) {
            uId = mPreferenceSettings.getUserDetails().getResponseData().getID();
            if (mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID() != null &&
                    mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID().length() > 0) {
                stripeCusID = mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID();
            }
            if (mPreferenceSettings.getUserDetails().getResponseData().getEmail() != null &&
                    mPreferenceSettings.getUserDetails().getResponseData().getEmail().length() > 0) {
                email = mPreferenceSettings.getUserDetails().getResponseData().getEmail();
            }
            if (mPreferenceSettings.getUserDetails().getResponseData().isWorldsBestRegister()) {
                isRegistered = true;
            } else {
                isRegistered = false;
            }
            if (mPreferenceSettings.getUserDetails().getResponseData().getWorldsBestSubscriptionEndDate() != null) {
                worldsBestSubsEndDate = mPreferenceSettings.getUserDetails().getResponseData().getWorldsBestSubscriptionEndDate().split("T")[0];
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat ndf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                ndf.setTimeZone(TimeZone.getDefault());


                try {
                    Date endDate = sdf.parse(worldsBestSubsEndDate),
                            todayDate = ndf.parse(ndf.format(Calendar.getInstance().getTime()));

                    String endDateLocal = ndf.format(endDate);

                    try {
                        endDate = ndf.parse(endDateLocal);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (todayDate.after(endDate)) {
                        subsTv.setText("SUBSCRIBE CHANNEL");
                        subsIv.setImageResource(R.drawable.ic_click);
                        isSubscribed = false;
                    } else {
                        subsTv.setText("SUBSCRIBED");
                        subsIv.setImageResource(R.drawable.ic_correct);
                        isSubscribed = true;


                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        goBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHomeScreen();
            }
        });

        if (Utility.isOnline(WorldsBestActivity.this)) {
            getTalentCategoryAPI();
        }

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
                        if (Utility.isOnline(WorldsBestActivity.this)) {
                            isLoading = true;
                            currPage += 1;

                            getMyVideosAPI(currPage);

                        }
                    }
                }
            }
        });

        reviewedGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0) {
                    int lastVisibleItem = firstVisibleItem + visibleItemCount;
                    if (!isLoadingReview && hasMoreItemsReview && (lastVisibleItem == totalItemCount)) {
                        //load more items--
                        if (Utility.isOnline(WorldsBestActivity.this)) {
                            isLoadingReview = true;
                            currPageReviewed += 1;

                            getReviewedVideoListAPI(currPageReviewed);

                        }
                    }
                }
            }
        });

        trendingRV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0) {
                    int lastVisibleItem = firstVisibleItem + visibleItemCount;
                    if (!isLoadingTrend && hasMoreItemsTrend && (lastVisibleItem == totalItemCount)) {
                        //load more items--
                        if (Utility.isOnline(WorldsBestActivity.this)) {
                            isLoadingTrend = true;
                            currPageTrend += 1;

                            getTrendingVideosAPI();

                        }
                    }
                }
            }
        });

        upcomingMoviesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpcomingMoviesDialog();
            }
        });

        moreDetailsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(WorldsBestActivity.this);
                dialog.setContentView(R.layout.custom_more_details_dialog);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

                CircleImageView closeBtn = dialog.findViewById(R.id.closeBtn);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        rulesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(WorldsBestActivity.this);
                dialog.setContentView(R.layout.custom_rules_req_dialog);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

                CircleImageView closeBtn = dialog.findViewById(R.id.closeBtn);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        termsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorldsBestActivity.this, WebViewActivity.class)
                        .putExtra("url", "https://junomoneta.io/terms")
                        .putExtra("title", "Terms of use"));
            }
        });


        homeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!homeIV.isSelected()) {
                    lastVideoPath = null;
                    videoBase64 = null;
                    updateUI(home);
                    currPage = 1;
                }
            }
        });

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addIV.isSelected()) {

                    if (isSubscribed) {
                        if (isRegistered) {
                            homeVideoView.pause();
                            categoryId = 0;
                            videoPath = null;
                            videoFileName = null;
                            videoBase64 = null;
                            showCategoryDialog();
                        } else {
                            Toast.makeText(WorldsBestActivity.this, "Please fill up registration form available at WORLDS BEST main screen.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        showSubscribeDialog();
                    }
                }
            }
        });

        trendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!trendIV.isSelected()) {
                    likeDislikeLL.setVisibility(View.GONE);
                    homeVideoView.pause();
                    updateUI(trend);
                    lastVideoPath = null;
                    videoBase64 = null;
                    currPageTrend = 1;
                    getTrendingVideosAPI();
                }
            }
        });


        profileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!profileIV.isSelected()) {
                    myVideoBTN.setSelected(true);
                    reviewedBTN.setSelected(false);
                    likeDislikeLL.setVisibility(View.GONE);
                    homeVideoView.pause();
                    updateUI(profile);
                    lastVideoPath = null;
                    videoBase64 = null;

                    String profImg = mPreferenceSettings.getUserDetails().getResponseData().getProfilePicture(),
                            name = mPreferenceSettings.getUserDetails().getResponseData().getName(),
                            mobNo = mPreferenceSettings.getUserDetails().getResponseData().getMobileNo();
                    Picasso.get().load(profImg).error(R.drawable.user).into(myProfileIV);
                    fullNameTV.setText(name);
                    mobNoTV.setText(mobNo);
                    currPage = 1;
                    currPageReviewed = 1;
                    myVideosList.clear();
                    reviewedVideoList.clear();
                    getMyVideosAPI(currPage);
                }
            }
        });

        myVideoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!myVideoBTN.isSelected()) {
                    myVideoBTN.setSelected(true);
                    reviewedBTN.setSelected(false);

                    videoGridView.setVisibility(View.VISIBLE);
                    reviewedGridView.setVisibility(View.GONE);
                }
            }
        });

        reviewedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!reviewedBTN.isSelected()) {
                    myVideoBTN.setSelected(false);
                    reviewedBTN.setSelected(true);

                    videoGridView.setVisibility(View.GONE);
                    reviewedGridView.setVisibility(View.VISIBLE);

                    if (reviewedVideoList.size() == 0) {
                        currPageReviewed = 1;
                        getReviewedVideoListAPI(currPageReviewed);
                    }
                }
            }
        });

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isOnline(WorldsBestActivity.this)) {
                    errorTextView.setVisibility(View.GONE);
                    retryBTN.setVisibility(View.GONE);
                    if (homeIV.isSelected()) {
                        getTalentBankVideoAPI();
                    } else if (profileIV.isSelected()) {
                        currPage = 1;
                        getMyVideosAPI(currPage);
                    } else if (trendIV.isSelected()) {
                        currPageTrend = 1;
                        getTrendingVideosAPI();
                    }
                } else {
                    Utility.noInternetError(WorldsBestActivity.this);
                }
            }
        });

        selectOtherVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 0);
                Intent intent2 = new Intent(WorldsBestActivity.this, VideoPickActivity.class);
                intent2.putExtra(Constant.MAX_NUMBER, 1);
                startActivityForResult(intent2, 0);

            }
        });


        competitionBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(WorldsBestActivity.this, competitionBTN);
                //Inflating the Popup using xml file
                popup.inflate(R.menu.upload_download_menu);
//                        .inflate(R.menu.main, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_download:
                                if (Utility.isOnline(WorldsBestActivity.this)) {
                                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                                    i.setData(Uri.parse("http://moviecashapi.junomoneta.io/WorldsBestParticipateForm.docx"));
                                    startActivity(i);
                                } else {
                                    Utility.noInternetError(WorldsBestActivity.this);
                                }
                                break;
                            case R.id.action_upload:
                                Intent intent4 = new Intent(WorldsBestActivity.this, NormalFilePickActivity.class);
                                intent4.putExtra(Constant.MAX_NUMBER, 1);
                                intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"docx"});
                                startActivityForResult(intent4, 1);
                                break;
                        }
                        return true;
                    }
                });

                if (subsTv.getText().toString().trim().equalsIgnoreCase("SUBSCRIBED")) {
                    MenuPopupHelper menuHelper = new MenuPopupHelper(WorldsBestActivity.this,
                            (MenuBuilder) popup.getMenu(), competitionBTN);
                    menuHelper.setForceShowIcon(true);
                    menuHelper.show(); //showing popup menu
                } else {
                    Toast.makeText(WorldsBestActivity.this, "Please subscribe channel.", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        downloadLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Utility.isOnline(WorldsBestActivity.this)) {
//                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
//                    i.setData(Uri.parse("http://moviecashapi.junomoneta.io/WorldsBestParticipateForm.docx"));
//                    startActivity(i);
//
////                   new DownloadFile().execute("http://moviecashapi.junomoneta.io/WorldsBestParticipateForm.docx");
//
////                    downloadFormAPI("http://moviecashapi.junomoneta.io/WorldsBestParticipateForm.docx");
//
//                } else {
//                    Utility.noInternetError(WorldsBestActivity.this);
//                }
//            }
//        });
//
//
//        uploadLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent4 = new Intent(WorldsBestActivity.this, NormalFilePickActivity.class);
//                intent4.putExtra(Constant.MAX_NUMBER, 1);
//                intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"docx"});
//                startActivityForResult(intent4, 1);
//
//            }
//        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        subscribeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subsTv.getText().toString().equalsIgnoreCase("SUBSCRIBE CHANNEL")) {
                    showSubscribeDialog();
                } else {
                    showSubsDetailsDialog();
                }
            }
        });

        uploadVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isOnline(WorldsBestActivity.this)) {

                    if (fileSize <= 10) {
                        if (videoBase64 != null) {
//                            showCategoryDialog();
                            final ProgressDialog progressDialog = new ProgressDialog(WorldsBestActivity.this);
                            progressDialog.setTitle("Uploading...");
                            progressDialog.setMessage("Please wait.");
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    uploadVideoAPI(progressDialog, categoryId);
                                }
                            });
                        } else {
                            Toast.makeText(WorldsBestActivity.this, "Error, Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(WorldsBestActivity.this, "File must be less than 10 MB", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Utility.noInternetError(WorldsBestActivity.this);
                }
            }
        });

        userImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorldsBestActivity.this, TalentUserProfileActivity.class)
                        .putExtra("userid", videoList.get(0).getUserID())
                        .putExtra("name", videoList.get(0).getUserName())
                        .putExtra("profImage", videoList.get(0).getProfilePicture())
                        .putExtra("mobno", videoList.get(0).getMobileNo())
                        .putExtra("formUrl", videoList.get(0).getFormUrl()));
            }
        });

        userNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorldsBestActivity.this, TalentUserProfileActivity.class)
                        .putExtra("userid", videoList.get(0).getUserID())
                        .putExtra("name", videoList.get(0).getUserName())
                        .putExtra("profImage", videoList.get(0).getProfilePicture())
                        .putExtra("mobno", videoList.get(0).getMobileNo())
                        .putExtra("formUrl", videoList.get(0).getFormUrl()));
            }
        });

        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getTrendingVideosAPI() {
        loader.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GetAllBestTalentBankVideosResponse> getTrending = retroInterface.getTrendingVideos(uId, currPageTrend, 10);
        getTrending.enqueue(new Callback<GetAllBestTalentBankVideosResponse>() {
            @Override
            public void onResponse(Call<GetAllBestTalentBankVideosResponse> call, Response<GetAllBestTalentBankVideosResponse> response) {
                loader.setVisibility(View.GONE);
                try {

                    if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                        isLoadingTrend = false;
                        if (response.body().getResponseData() != null &&
                                response.body().getResponseData().size() > 0) {
                            hasMoreItemsTrend = true;
                            trendingList.addAll(response.body().getResponseData());
                            if (trendingList != null && trendingList.size() > 0) {
                                errorTextView.setVisibility(View.GONE);
                                retryBTN.setVisibility(View.GONE);
                            } else {
                                errorTextView.setVisibility(View.VISIBLE);
                                retryBTN.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (currPageTrend == 1) {
                                errorTextView.setVisibility(View.VISIBLE);
                                retryBTN.setVisibility(View.VISIBLE);
                            }
                            hasMoreItemsTrend = false;
                        }
                        if (currPageTrend == 1) {
                            trendingRV.smoothScrollToPosition(0);
                        }
//                        myVideoRecyclerAdapter.notifyDataSetChanged();
                        trendingAdapter.notifyDataSetChanged();
                    } else {
                        errorTextView.setVisibility(View.VISIBLE);
                        retryBTN.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.e("videoLoadExc", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<GetAllBestTalentBankVideosResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                errorTextView.setVisibility(View.VISIBLE);
                retryBTN.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showCategoryDialog() {

        final Dialog dialog = new Dialog(WorldsBestActivity.this);
        dialog.setContentView(R.layout.custom_talent_category_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        RecyclerView categoryRV = dialog.findViewById(R.id.categoryRV);
        categoryRV.setLayoutManager(new GridLayoutManager(WorldsBestActivity.this, 3, RecyclerView.VERTICAL, false));
        categoryRV.setItemAnimator(new DefaultItemAnimator());

        TalentCategoryAdapter talentCategoryAdapter = new TalentCategoryAdapter(WorldsBestActivity.this, categoryList,
                dialog);
        categoryRV.setAdapter(talentCategoryAdapter);
        talentCategoryAdapter.notifyDataSetChanged();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                homeVideoView.resume();
            }
        });

        dialog.show();

    }

    private void showUpcomingMoviesDialog() {

        final Dialog dialog = new Dialog(WorldsBestActivity.this);
        dialog.setContentView(R.layout.custom_movie_synopsis_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        ViewPager viewPager;
        UpcomingMovieSynopsisAdapter upcomingMovieSynopsisAdapter;
        final LinearLayout dotsLayout;
        final TextView[][] dots = {new TextView[0]};
        final LinearLayout.LayoutParams params;
        final ArrayList<Integer> posters = new ArrayList<>();
        posters.add(R.drawable.the_heist);
        posters.add(R.drawable.the_scare);
        posters.add(R.drawable.no_clue);

        ArrayList<String> movNameList = new ArrayList<>();
        movNameList.add("THE HEIST");
        movNameList.add("THE SCARE");
        movNameList.add("NO CLUE - The Perfect Crime");

        CircleImageView closeBTN = dialog.findViewById(R.id.closeBTN);
        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ArrayList<String> synopsisList = new ArrayList<>();
        synopsisList.add("Movie SYNOPSIS:\nThe airport tarmac in Brussels was turned into a crime scene Monday night when thieves stole more than $300 million worth of diamonds from a Zurich, Switzerland-bound airplane.\n" +
                "The heist unfolded in fewer than five minutes without a single shot fired.\n" +
                "The gang cut through an airport fence and drove up to the Helvetic Airways jet in cars with flashing lights.\n" +
                "Dressed in police uniforms and carrying submachine guns, they unloaded 120 packages of rough and cut stones while holding the pilot and co-pilot at gunpoint.\n" +
                "A lightning-fast strike that was over before passengers aboard the plane had any inkling of what had happened.\n" +
                "The Helvetic Airways jet was ready for takeoff to Zurich, Switzerland and then enroute to India ., on the evening of Feb. 18. As flight attendants went through final safety checks on board, Brinks security guards outside finished transferring a shipment of cut and uncut diamonds from their armored car to the plane.\n" +
                "Suddenly, what looked like two police vehicles, one of them a Mercedes van, raced up to the aircraft, blue lights flashing. Eight armed men jumped out, wearing police uniforms but also balaclavas that hid their faces.\n" +
                "After prying open the door to the plane's hold, the group snatched about 120 packages and sped away without firing a shot. The vehicles escaped through a hole cut in the airport fence; the van was later found burned out and abandoned.\n" +
                "The robbery lasted about five minutes. Passengers saw nothing.\n" +
                "This was a  Jet and   regularly carrying diamonds  to India .\n" +
                "Now  who was  involved this ?  Indian and Jews  own  the  diamond business  In Belgium . Some body  who  knew  that  on particular  day  diamonds  worth 300 $ million were to be  transported . \n" +
                "\n" +
                "It was  found that  shipment belonged to Indian company  who  do big business .The owner  of the company a young sophisticated  , very smart , casanova  and courteous  business man was  a shaken person . He lost  $ 300 million .in just 5 minutes !     He planned  the  robbery  or Police or somebody from Insurance company  ?  Diamonds  and Insurance money  Remains  with him?     or Police or some body from Insurance  company or Security company involved  in this ?\n" +
                "The  global  hunt was launched Mororocco , Zurich , Telaviv , India , London and USA ,Russia   were  on scanner ");
        synopsisList.add("Movie SYNOPSIS:\nThe  girl  and her child  jump from  10  th fl   and  commit  suicide .  the detective  had   doubt  about it . So  he was  keeping  eyes  and ears  open .\n" +
                "The  new couple  comes  to  live in this  building  and same floor  where the suicide took place .  And the girl who was model starts  feeling presence  of somebody .  and  seeing some shadows  of boy  and  Girl  .\n" +
                "So who  are   these shadows and  is  that  girl  and child  same  who committed suicide ? if  yes , why they are back ?  suspense  and horror grows  now ,  and model  girl  is possessed !  ");
        synopsisList.add("Movie SYNOPSIS:\nThe  young girl  was  kidnapped  from her  daily routine of jogging , she was barely  16  and daughter  of wealthy business man .  No trace of her . No CC  tv footage , \n" +
                "After ransom of 2 millions  $  she  was freed ,  but  she was  drugged , moved  from one place to another , she did not remember anything . She  was regularly seeing 3 guys  but all  were bearded  so  it was  impossible  to draw  sketches  and identify .    She was  scared  to talk to  police or anybody  she was  bruised , beaten was  scared  and molested  but not raped ,The Kidnappers along played psychological game\n" +
                "The detective  Lori was  very sensible  young  officer  who  was  Dr  in Psychology also . \n" +
                "With lot of patience  she was  trying to talk to  her  and find some clue .  But  Nothing ,  and after  2 months  when  she was  better  , she  heard  from conversation    the name of  one of the kidnappers . One of the  days  our of 3 guys  one guy was  absent  and  The boss  asked  the other  guy , why he is not there ?   His  friend  responded Jasons wife  gave a birth to boy  at NYC hospital  \n" +
                "Now this  was  the only clue  but  Detective  -------------   and  traced  the whole gang .");

        viewPager = dialog.findViewById(R.id.view_pager);
        dotsLayout = dialog.findViewById(R.id.layoutDots);

        params = new LinearLayout.LayoutParams(20, 20);
        params.setMargins(2, 0, 2, 0);


        dots[0] = new TextView[posters.size()];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots[0].length; i++) {
            dots[0][i] = new TextView(this);
            dots[0][i].setBackgroundResource(R.drawable.ic_ring);
            dots[0][i].setTextSize(35);
            dots[0][i].setTextColor(colorsInactive[0]);
            dots[0][i].setLayoutParams(params);
            dotsLayout.addView(dots[0][i]);
        }


        dots[0][0].setBackgroundResource(R.drawable.ic_filled_circle);
        dots[0][0].setLayoutParams(params);

        if (dots[0].length > 0)
            dots[0][0].setTextColor(colorsActive[0]);


        upcomingMovieSynopsisAdapter = new UpcomingMovieSynopsisAdapter(WorldsBestActivity.this, posters, synopsisList, movNameList);
        viewPager.setAdapter(upcomingMovieSynopsisAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                dots[0] = new TextView[posters.size()];

                int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
                int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

                dotsLayout.removeAllViews();
                for (int i = 0; i < dots[0].length; i++) {
                    dots[0][i] = new TextView(WorldsBestActivity.this);
                    dots[0][i].setBackgroundResource(R.drawable.ic_ring);
                    dots[0][i].setTextSize(35);
                    dots[0][i].setTextColor(colorsInactive[position]);
                    dots[0][i].setLayoutParams(params);
                    dotsLayout.addView(dots[0][i]);
                }

                if (position == 0) {
                    dots[0][position].setBackgroundResource(R.drawable.ic_filled_circle);
                    dots[0][position].setLayoutParams(params);
                }
                if (dots[0].length > 0)
                    dots[0][position].setTextColor(colorsActive[position]);

                dots[0][position].setBackgroundResource(R.drawable.ic_filled_circle);
                dots[0][position].setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        dialog.show();


    }

    private void getTalentCategoryAPI() {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GetTalentCategoryListResponse> getCategory = retroInterface.getTalentCategoryList();
        getCategory.enqueue(new Callback<GetTalentCategoryListResponse>() {
            @Override
            public void onResponse(Call<GetTalentCategoryListResponse> call, Response<GetTalentCategoryListResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    categoryList.clear();
                    categoryList = response.body().getResponseData();
                }
            }

            @Override
            public void onFailure(Call<GetTalentCategoryListResponse> call, Throwable t) {

            }
        });

    }

    private void loadHomeScreen() {

        if (Utility.isOnline(WorldsBestActivity.this)) {
            Animation sol = AnimationUtils.loadAnimation(WorldsBestActivity.this, R.anim.slide_out_left);
            mainRL.startAnimation(sol);
            mainRL.setVisibility(View.GONE);
            if (mPreferenceSettings.getUserDetails().getResponseData().isWorldsBestJudge()) {
                trendBTN.setVisibility(View.VISIBLE);
            } else {
                trendBTN.setVisibility(View.GONE);
            }
            updateUI(home);
        } else {
            Utility.noInternetError(WorldsBestActivity.this);
        }

    }

    public void uploadVideoAPI(final ProgressDialog progressDialog, int catId) {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);

        final File file = new File(videoPath);
        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), videoFileName);
        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uId));
        RequestBody categoryId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(catId));
        RequestBody base64 = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(videoBase64));
        final MultipartBody.Part[] filePart = new MultipartBody.Part[1];
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                filePart[0] = MultipartBody.Part.createFormData("file", file.getName(), fbody);
            }
        });

        Log.e("catId", categoryId + "");
        Call<CommonResponse> uploadVideo = retroInterface.uploadWorldsBestTalentVideo(id, name, base64, categoryId, filePart[0]);
        uploadVideo.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                progressDialog.dismiss();
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    Toast.makeText(WorldsBestActivity.this, "Video uploaded successfully.", Toast.LENGTH_SHORT).show();
                    if (videoView != null) {
                        videoView.pause();
                    }
                    loadHomeScreen();
                } else {
                    Toast.makeText(WorldsBestActivity.this, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(WorldsBestActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getTalentBankVideoAPI() {
        categoryTV.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GetAllTalentBankVideosByUserResponse> getVideo = retroInterface.getAllTalentBankVideo(uId, 1, 1);
        getVideo.enqueue(new Callback<GetAllTalentBankVideosByUserResponse>() {
            @Override
            public void onResponse(Call<GetAllTalentBankVideosByUserResponse> call, Response<GetAllTalentBankVideosByUserResponse> response) {

                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    reviewCountProgressBar.setVisibility(View.GONE);
                    errorTextView.setVisibility(View.GONE);
                    retryBTN.setVisibility(View.GONE);
                    videoList = response.body().getResponseData();
                    if (videoList != null && videoList.size() > 0) {
                        likeDislikeLL.setVisibility(View.VISIBLE);
                        categoryTV.setVisibility(View.VISIBLE);
                        categoryTV.setText("#" + response.body().getResponseData().get(0).getCategoryName());
                        if (homeVideoView != null) {
                            Picasso.get().load(videoList.get(0).getProfilePicture()).error(R.drawable.user).placeholder(R.drawable.user).into(userImgView);
                            userNameTV.setText(videoList.get(0).getUserName());
                            homeVideoView.setBackgroundColor(0);
                            homeVideoView.setVideoPath(videoList.get(0).getVideoUrl());


                            homeVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    homeVideoView.start();
                                }
                            });

                            homeVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                @Override
                                public boolean onError(MediaPlayer mp, int what, int extra) {
                                    loader.setVisibility(View.GONE);
                                    return false;
                                }
                            });

                            homeVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    loader.setVisibility(View.GONE);
                                    homeVideoView.start();
                                    Log.e("height", String.valueOf(mp.getVideoHeight()));
                                    Log.e("width", String.valueOf(mp.getVideoWidth()));
                                }
                            });

                            likeBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    saveVideoReviewAPI(videoList.get(0).getID(),
                                            true);
                                }
                            });

                            disLikeBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    saveVideoReviewAPI(videoList.get(0).getID(),
                                            false);
                                }
                            });
                        }
                    } else {
                        userNameTV.setText("");
                        userImgView.setImageResource(0);
                        likeDislikeLL.setVisibility(View.GONE);
                        homeVideoView.setBackgroundColor(getResources().getColor(R.color.background));
                        errorTextView.setVisibility(View.VISIBLE);
                        retryBTN.setVisibility(View.VISIBLE);
                        loader.setVisibility(View.GONE);
                    }
                } else {
                    likeDislikeLL.setVisibility(View.GONE);
                    loader.setVisibility(View.GONE);
                    errorTextView.setVisibility(View.VISIBLE);
                    retryBTN.setVisibility(View.VISIBLE);
                    Toast.makeText(WorldsBestActivity.this, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetAllTalentBankVideosByUserResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                errorTextView.setVisibility(View.VISIBLE);
                retryBTN.setVisibility(View.VISIBLE);
                Toast.makeText(WorldsBestActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getPlanPriceAPI() {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GetQuizGamePaymentPlanResponse> getPlan = retroInterface.getQuizGamePaymentPlan();
        getPlan.enqueue(new Callback<GetQuizGamePaymentPlanResponse>() {
            @Override
            public void onResponse(Call<GetQuizGamePaymentPlanResponse> call, Response<GetQuizGamePaymentPlanResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {

                    for (int i = 0; i < response.body().getResponseData().size(); i++) {
                        if (response.body().getResponseData().get(i).getPlanName().equalsIgnoreCase("WORLDBESTSUBSCRIPTION")) {
                            worldbest_subs_amount = response.body().getResponseData().get(i).getPrice();
                        } else if (response.body().getResponseData().get(i).getPlanName().equalsIgnoreCase("JUDGESUBSCRITPION")) {
                            judge_subscription_amount = response.body().getResponseData().get(i).getPrice();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetQuizGamePaymentPlanResponse> call, Throwable t) {

            }
        });

    }

    private void saveVideoReviewAPI(int videoId, boolean review) {
        loader.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<SaveVideoReviewResponse> saveReview = retroInterface.saveVideoReview(videoId, uId, review);
        saveReview.enqueue(new Callback<SaveVideoReviewResponse>() {
            @Override
            public void onResponse(Call<SaveVideoReviewResponse> call, Response<SaveVideoReviewResponse> response) {
                loader.setVisibility(View.GONE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    Utility.getProfileDetails();
                    reviewCountProgressBar.setVisibility(View.VISIBLE);
                    progressStatus = 0;
                    final double positiveReview = (response.body().getResponseData().getTotalRightReview() / response.body().getResponseData().getTotalReview()) * 100;
                    Log.e("positiveReview", positiveReview + "");
                    // Start the lengthy operation in a background thread
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            while (progressStatus < (int) positiveReview) {
                                // Update the progress status
                                progressStatus += 1;

                                // Try to sleep the thread for 20 milliseconds
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                // Update the progress bar
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        reviewCountProgressBar.setProgress(progressStatus);
                                    }
                                });
                            }
                        }
                    }).start();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (homeVideoView.isPlaying()) {
                                homeVideoView.pause();
                            }
                            getTalentBankVideoAPI();
                        }
                    }, 3000);
                } else {
                    Toast.makeText(WorldsBestActivity.this, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SaveVideoReviewResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(WorldsBestActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getMyVideosAPI(int pgNo) {
        loader.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GetAllTalentBankVideosByUserResponse> getMyVideo = retroInterface.getTalentBankVideoByUser(uId, pgNo, 10, false);
        getMyVideo.enqueue(new Callback<GetAllTalentBankVideosByUserResponse>() {
            @Override
            public void onResponse(Call<GetAllTalentBankVideosByUserResponse> call, Response<GetAllTalentBankVideosByUserResponse> response) {
                loader.setVisibility(View.GONE);
                try {

                    if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                        videoGridView.setVisibility(View.VISIBLE);
                        reviewedGridView.setVisibility(View.GONE);
                        isLoading = false;
                        if (response.body().getResponseData() != null &&
                                response.body().getResponseData().size() > 0) {
                            totalVideoTV.setText("" + response.body().getResponseData().get(0).getTotalRecord());
                            hasMoreItems = true;
                            myVideosList.addAll(response.body().getResponseData());
                            if (myVideosList != null && myVideosList.size() > 0) {
                                errorTextView.setVisibility(View.GONE);
                                retryBTN.setVisibility(View.GONE);
                            } else {
                                errorTextView.setVisibility(View.VISIBLE);
                                retryBTN.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (currPage == 1) {
                                errorTextView.setVisibility(View.VISIBLE);
                                retryBTN.setVisibility(View.VISIBLE);
                            }
                            hasMoreItems = false;
                        }
                        if (currPage == 1) {
                            videoGridView.smoothScrollToPosition(0);
                        }
//                        myVideoRecyclerAdapter.notifyDataSetChanged();
                        myVideoListAdapter.notifyDataSetChanged();
                    } else {
                        errorTextView.setVisibility(View.VISIBLE);
                        retryBTN.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.e("videoLoadExc", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<GetAllTalentBankVideosByUserResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                errorTextView.setVisibility(View.VISIBLE);
                retryBTN.setVisibility(View.VISIBLE);
                Toast.makeText(WorldsBestActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getReviewedVideoListAPI(int pgNo) {
        loader.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GetAllTalentBankVideosByUserResponse> getReviewedVideo = retroInterface.getTalentBankVideoByUser(uId, pgNo, 10, true);
        getReviewedVideo.enqueue(new Callback<GetAllTalentBankVideosByUserResponse>() {
            @Override
            public void onResponse(Call<GetAllTalentBankVideosByUserResponse> call, Response<GetAllTalentBankVideosByUserResponse> response) {
                loader.setVisibility(View.GONE);
                try {

                    if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                        isLoadingReview = false;
                        if (response.body().getResponseData() != null &&
                                response.body().getResponseData().size() > 0) {

                            hasMoreItemsReview = true;
                            reviewedVideoList.addAll(response.body().getResponseData());
                            if (reviewedVideoList != null && reviewedVideoList.size() > 0) {
                                errorTextView.setVisibility(View.GONE);
                                retryBTN.setVisibility(View.GONE);
                            } else {
                                errorTextView.setVisibility(View.VISIBLE);
                                retryBTN.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (currPageReviewed == 1) {
                                errorTextView.setVisibility(View.VISIBLE);
                                retryBTN.setVisibility(View.VISIBLE);
                            }
                            hasMoreItemsReview = false;
                        }
                        if (currPageReviewed == 1) {
                            reviewedGridView.smoothScrollToPosition(0);
                        }
//                        myVideoRecyclerAdapter.notifyDataSetChanged();
                        reviewedVideoListAdapter.notifyDataSetChanged();
                    } else {
                        errorTextView.setVisibility(View.VISIBLE);
                        retryBTN.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.e("videoLoadExc", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<GetAllTalentBankVideosByUserResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                errorTextView.setVisibility(View.VISIBLE);
                retryBTN.setVisibility(View.VISIBLE);
                Toast.makeText(WorldsBestActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (homeRL.getVisibility() == View.VISIBLE) {
            if (homeVideoView != null && videoList != null && videoList.size() > 0) {
                homeVideoView.setBackgroundResource(0);
                homeVideoView.setVideoPath(videoList.get(0).getVideoUrl());
                homeVideoView.start();
            }
        }
    }

    private void showSubsDetailsDialog() {

        final Dialog dialog = new Dialog(WorldsBestActivity.this);
        dialog.setContentView(R.layout.custom_subscription_details_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        CustomTextViewBold okBtn = dialog.findViewById(R.id.okBtn),
                startDateTV = dialog.findViewById(R.id.startDateTV),
                endDateTV = dialog.findViewById(R.id.endDateTV);

        worldsBestSubsStartDate = mPreferenceSettings.getUserDetails().getResponseData().getWorldsBestSubscriptionStartDate().split("T")[0];
        worldsBestSubsEndDate = mPreferenceSettings.getUserDetails().getResponseData().getWorldsBestSubscriptionEndDate().split("T")[0];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat ndf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        try {
            Date endDate = sdf.parse(worldsBestSubsEndDate),
                    startDate = sdf.parse(worldsBestSubsStartDate);
            startDateTV.setText(ndf.format(startDate));
            endDateTV.setText(ndf.format(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void showSubscribeDialog() {
        final Dialog dialog = new Dialog(WorldsBestActivity.this);
        dialog.setContentView(R.layout.custom_subscribe_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        CustomTextViewBold subscribeBtn = dialog.findViewById(R.id.subscribeBtn);
        CircleImageView closeBtn = dialog.findViewById(R.id.closeBtn);
        CustomTextView subs_descTV = dialog.findViewById(R.id.subs_descTV);
        final CustomEditText refferalCodeEditText = dialog.findViewById(R.id.refferalCodeEditText);

        refferalCodeEditText.setVisibility(View.GONE);

        subs_descTV.setText("- Subscribe Worlds Best Channel.\n- 3 months subscription at just $ " + worldbest_subs_amount + ".");

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isOnline(WorldsBestActivity.this)) {
                    dialog.dismiss();

                    if (stripeCusID != null && stripeCusID.length() > 0) {
                        showFilledCardDetailsDialog(false);
                    } else {
                        showCardDetailDialog(false);
                    }
                } else {
                    Utility.noInternetError(WorldsBestActivity.this);
                }

            }
        });

        dialog.show();
    }

    private void showJudgeSubscribeDialog() {
        final Dialog dialog = new Dialog(WorldsBestActivity.this);
        dialog.setContentView(R.layout.custom_subscribe_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        CustomTextViewBold subscribeBtn = dialog.findViewById(R.id.subscribeBtn),
                titleTV = dialog.findViewById(R.id.titleTV);
        CircleImageView closeBtn = dialog.findViewById(R.id.closeBtn);
        CustomTextView subs_descTV = dialog.findViewById(R.id.subs_descTV);
        final CustomEditText refferalCodeEditText = dialog.findViewById(R.id.refferalCodeEditText);

        refferalCodeEditText.setVisibility(View.GONE);
        titleTV.setText("PURCHASE LICENSE");

        subs_descTV.setText("- Purchase 1 month license at just $ " + judge_subscription_amount + ".");

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isOnline(WorldsBestActivity.this)) {
                    dialog.dismiss();

                    if (stripeCusID != null && stripeCusID.length() > 0) {
                        showFilledCardDetailsDialog(true);
                    } else {
                        showCardDetailDialog(true);
                    }
                } else {
                    Utility.noInternetError(WorldsBestActivity.this);
                }

            }
        });

        dialog.show();
    }

    private void showFilledCardDetailsDialog(final boolean isForJudge) {

        final Dialog dialog = new Dialog(WorldsBestActivity.this);
        dialog.setContentView(R.layout.custom_filled_card_details_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        final CustomTextViewBold changeCardBtn = dialog.findViewById(R.id.changeCardBtn),
                payNowBtn = dialog.findViewById(R.id.payNowBtn);
        CustomTextView cardNameTV = dialog.findViewById(R.id.cardNameTV),
                cardNumberTV = dialog.findViewById(R.id.cardNumberTV),
                cardExpTV = dialog.findViewById(R.id.cardExpTV);
        final ProgressBar progress = dialog.findViewById(R.id.progress);
        CircleImageView closeDialogBtn = dialog.findViewById(R.id.closeDialogBtn);

        cardNameTV.setText(mPreferenceSettings.getUserDetails().getResponseData().getCardHolderName());
        cardNumberTV.setText("\u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022 " +
                mPreferenceSettings.getUserDetails().getResponseData().getCardNumberLast4());
        cardExpTV.setText(mPreferenceSettings.getUserDetails().getResponseData().getExpiryMonth() + "/" +
                String.valueOf(mPreferenceSettings.getUserDetails().getResponseData().getExpiryYear()).substring(2));

        changeCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showCardDetailDialog(isForJudge);
            }
        });

        closeDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amount = 0;
                if (isForJudge) {
                    amount = judge_subscription_amount;
                } else {
                    amount = worldbest_subs_amount;
                }

                payNowBtn.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                stripeCusID = mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID();

                if (!isForJudge) {
                    subscribeAPI(stripeCusID, amount, email, "", "", 0, 0, "",
                            progress, payNowBtn, dialog);
                } else {
                    subscribeForJudgeAPI(stripeCusID, amount, email, "", "", 0, 0, "",
                            progress, payNowBtn, dialog);
                }

            }
        });

        dialog.show();
    }

    private void showCardDetailDialog(final boolean isForJudge) {

        final Dialog dialog = new Dialog(WorldsBestActivity.this);
        dialog.setContentView(R.layout.custom_card_details_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        final AppCompatEditText cardNameET, emailET;
        final CardMultilineWidget cardDetailWidget;
        final CustomTextViewBold payNowBTN;
        final ProgressBar progress;
        CircleImageView closeBtn;

        cardNameET = dialog.findViewById(R.id.cardNameET);
        emailET = dialog.findViewById(R.id.emailET);
        cardDetailWidget = dialog.findViewById(R.id.cardDetailWidget);
        payNowBTN = dialog.findViewById(R.id.payNowBTN);
        progress = dialog.findViewById(R.id.progress);
        closeBtn = dialog.findViewById(R.id.closeBtn);

        if (email != null && email.length() > 0) {
            emailET.setText(email);
        }

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        payNowBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                double amount = 0;
                if (isForJudge) {
                    amount = judge_subscription_amount;
                } else {
                    amount = worldbest_subs_amount;
                }
                String email = "", cardName = "", cardNumber = "", cvc = "";
                int expYear = 0, expMonth = 0;

                email = emailET.getText().toString().trim();
                cardName = cardNameET.getText().toString().trim();

                if (!cardName.isEmpty()) {
                    if (!email.isEmpty()) {
                        if (cardDetailWidget.getCard() != null) {
                            cardNumber = cardDetailWidget.getCard().getNumber();
                            expYear = cardDetailWidget.getCard().getExpYear();
                            expMonth = cardDetailWidget.getCard().getExpMonth();
                            cvc = cardDetailWidget.getCard().getCVC();
                            if (cardDetailWidget.getCard().validateCard()) {
                                if (Utility.isOnline(WorldsBestActivity.this)) {
                                    progress.setVisibility(View.VISIBLE);
                                    payNowBTN.setVisibility(View.GONE);

                                    if (!isForJudge) {
                                        subscribeAPI("", amount, email, cardName, cardNumber, expYear, expMonth,
                                                cvc, progress, payNowBTN, dialog);
                                    } else {
                                        subscribeForJudgeAPI("", amount, email, cardName, cardNumber, expYear, expMonth,
                                                cvc, progress, payNowBTN, dialog);
                                    }

                                } else {
                                    Utility.noInternetError(WorldsBestActivity.this);
                                }
                            } else {
                                Toast.makeText(WorldsBestActivity.this, "Invalid card details.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(WorldsBestActivity.this, "Please enter card details.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(WorldsBestActivity.this, "Please enter email address.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(WorldsBestActivity.this, "Please enter card holder name.", Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialog.show();
    }

    private void subscribeAPI(String stripeId, double amount, String email, String
            cardName, String cardNumber, int cardExpYear,
                              int cardExpMonth, String cvc, final ProgressBar progress, final CustomTextViewBold payNowBtn,
                              final Dialog dialog) {
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<WorldsBestSubscriptionResponse> subscribe = retroInterface.subscribeUserForWorldsBest(uId, stripeId, amount, "",
                email, cardName, cardNumber, cardExpYear, cardExpMonth, cvc);
        subscribe.enqueue(new Callback<WorldsBestSubscriptionResponse>() {
            @Override
            public void onResponse(Call<WorldsBestSubscriptionResponse> call, Response<WorldsBestSubscriptionResponse> response) {
                progress.setVisibility(View.GONE);
                payNowBtn.setVisibility(View.VISIBLE);
                dialog.dismiss();
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    if (response.body().getResponseData().getPaymentStatus().equalsIgnoreCase("succeeded")) {
                        successDialog("Subscribed successfully.", response.body().getResponseData().getReceiptUrl());
                        subsTv.setText("SUBSCRIBED");
                        subsIv.setImageResource(R.drawable.ic_correct);
                        isSubscribed = true;
                        Utility.getProfileDetails();
                    } else {
                        Toast.makeText(WorldsBestActivity.this, response.body().getResponseData().getFailureMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(WorldsBestActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WorldsBestSubscriptionResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                payNowBtn.setVisibility(View.VISIBLE);
                dialog.dismiss();
                Toast.makeText(WorldsBestActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void subscribeForJudgeAPI(String stripeId, double amount, String email, String
            cardName, String cardNumber, int cardExpYear,
                                      int cardExpMonth, String cvc, final ProgressBar progress, final CustomTextViewBold payNowBtn,
                                      final Dialog dialog) {
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<WorldsBestSubscriptionResponse> subscribe = retroInterface.subscribeForWorldsBestJudge(uId, stripeId, amount, "",
                email, cardName, cardNumber, cardExpYear, cardExpMonth, cvc);
        subscribe.enqueue(new Callback<WorldsBestSubscriptionResponse>() {
            @Override
            public void onResponse(Call<WorldsBestSubscriptionResponse> call, Response<WorldsBestSubscriptionResponse> response) {
                progress.setVisibility(View.GONE);
                payNowBtn.setVisibility(View.VISIBLE);
                dialog.dismiss();
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    if (response.body().getResponseData().getPaymentStatus().equalsIgnoreCase("succeeded")) {
                        successDialog("Subscribed successfully.", response.body().getResponseData().getReceiptUrl());
                        Utility.getProfileDetails();
                    } else {
                        Toast.makeText(WorldsBestActivity.this, response.body().getResponseData().getFailureMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(WorldsBestActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WorldsBestSubscriptionResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                payNowBtn.setVisibility(View.VISIBLE);
                dialog.dismiss();
                Toast.makeText(WorldsBestActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void successDialog(String msg, final String receiptURL) {
        final Dialog dialog = new Dialog(WorldsBestActivity.this);
        dialog.setContentView(R.layout.custom_success_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        TextView okBTN = dialog.findViewById(R.id.okBTN),
                receiptBTN = dialog.findViewById(R.id.receiptBTN),
                descTV = dialog.findViewById(R.id.descTV);

        descTV.setText(msg);
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "View receipt");
        receiptBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiptDialog(receiptURL);
            }
        });

        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void receiptDialog(final String receiptURL) {
        final Dialog dialog = new Dialog(WorldsBestActivity.this);
        dialog.setContentView(R.layout.custom_receipt_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        WebView webView = dialog.findViewById(R.id.webView);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(receiptURL);

        webView.setWebViewClient(new MyBrowser());

        dialog.show();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            MediaController mediaControls = null;
            if (mediaControls == null) {
                // create an object of media controller class
                mediaControls = new MediaController(WorldsBestActivity.this);
            }
            if (resultCode == RESULT_OK) {
                addVideoCategoryTV.setText("#" + catName);
                fileUri = data.getData();
//                if (fileUri.toString().contains("image")) {
//                    //handle image
//                    Toast.makeText(this, "Please select video.", Toast.LENGTH_SHORT).show();
////                    if (lastSelectedUri != null) {
////                        if (videoView != null) {
////                            videoView.setMediaController(mediaControls);
////                            videoView.setVideoURI(lastSelectedUri);
////                            videoView.requestFocus();
////                            videoView.start();
////
////                            updateUI(add);
////
////                            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
////                                @Override
////                                public void onCompletion(MediaPlayer mp) {
////                                    videoView.start();
////                                }
////                            });
////                        }
////                    }
//                } else if (fileUri.toString().contains("video")) {
//                    //handle video
////                    lastSelectedUri = selectedMediaUri;
//                    videoPath = ImageFilePath.getPath(WorldsBestActivity.this, fileUri);
//                    //                        videoBase64 = convertFileToBase64(videoPath);
//                    Log.e("videoFilePath", videoPath);
////                        Log.e("videoBase64", videoBase64);
//
//                    if (videoView != null) {
//                        videoView.setMediaController(mediaControls);
//                        videoView.setVideoURI(fileUri);
//                        videoView.requestFocus();
//                        videoView.start();
//
//                        updateUI(add);
//                    }
//                }

                ArrayList<VideoFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO);
                VideoFile videoFile = list.get(0);

                Log.e("video", "path-" + videoFile.getPath() + "\nname-" + videoFile.getName() +
                        "\nduration-" + videoFile.getDuration() + "\nsize" + videoFile.getSize());
                if (videoFile.getSize() > 0) {
                    fileSize = videoFile.getSize() / (1000 * 1000);
                }
                videoFileName = videoFile.getName() + ".mp4";
                videoPath = videoFile.getPath();
                Log.e("videoFilePath", videoPath);
                Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);
                Log.e("bMap", String.valueOf(bMap));
                videoBase64 = Utility.getBase64(bMap);
                Log.e("base64", videoBase64);
                lastVideoPath = videoPath;
                if (videoView != null) {
                    if (errorTextView.getVisibility() == View.VISIBLE) {
                        errorTextView.setVisibility(View.GONE);
                    }
                    if (retryBTN.getVisibility() == View.VISIBLE) {
                        retryBTN.setVisibility(View.GONE);
                    }
                    if (loader.getVisibility() == View.VISIBLE) {
                        loader.setVisibility(View.GONE);
                    }
                    videoView.setMediaController(mediaControls);
                    videoView.setVideoPath(videoFile.getPath());
                    videoView.requestFocus();
                    videoView.start();

                    updateUI(add);
                }

            } else {

                if (lastVideoPath != null) {
                    if (videoView != null) {
                        if (errorTextView.getVisibility() == View.VISIBLE) {
                            errorTextView.setVisibility(View.GONE);
                        }
                        if (retryBTN.getVisibility() == View.VISIBLE) {
                            retryBTN.setVisibility(View.GONE);
                        }
                        if (loader.getVisibility() == View.VISIBLE) {
                            loader.setVisibility(View.GONE);
                        }
                        videoView.setMediaController(mediaControls);
                        videoView.setVideoPath(lastVideoPath);
                        videoView.requestFocus();
                        videoView.start();

                        updateUI(add);
                    }
                } else {
                    updateUI(home);
                }
            }
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                String fileName = list.get(0).getName();
                Log.e("fileName", fileName);
                if (fileName.equalsIgnoreCase("WorldsBestParticipateForm")) {
                    fileName += ".docx";
                    String path = list.get(0).getPath();
                    Log.e("filePath", path);
                    try {
                        String base64 = convertFileToBase64(path);
                        Log.e("base64", base64);
                        if (base64.length() > 0) {
                            if (Utility.isOnline(WorldsBestActivity.this)) {
                                uploadWorldsBestFormAPI(fileName, base64);
                            } else {
                                Utility.noInternetError(WorldsBestActivity.this);
                            }
                        } else {
                            Toast.makeText(this, "Fail to upload file. PLease try again.", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("error", fileName);
                    Toast.makeText(this, "Oops, Wrong file selected.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void uploadWorldsBestFormAPI(String fileName, String base64) {
        final ProgressDialog progressDialog = new ProgressDialog(WorldsBestActivity.this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("PLease wait.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<CommonResponse> worldsBest = retroInterface.uploadWorldsBestForm(uId, fileName, base64);
        worldsBest.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                progressDialog.dismiss();
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    Toast.makeText(WorldsBestActivity.this, "Your form uploaded successfully.", Toast.LENGTH_SHORT).show();
                    Utility.getProfileDetails();
                    isRegistered = true;
                } else {
                    Toast.makeText(WorldsBestActivity.this, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(WorldsBestActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String convertFileToBase64(String dest) throws IOException {

        byte pdfByteArray[];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            File resolveMe = new File(String.valueOf(dest));
            pdfByteArray = new byte[(int) resolveMe.length()];
            try {
                pdfByteArray = org.apache.commons.io.FileUtils.readFileToByteArray(resolveMe);
                Log.e("Byte array1", ">" + pdfByteArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File file = new File(String.valueOf(dest));
            FileInputStream fis = new FileInputStream(file);
            //System.out.println(file.exists() + "!!");
            //InputStream in = resource.openStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            pdfByteArray = new byte[1024];
            try {
                for (int readNum; (readNum = fis.read(pdfByteArray)) != -1; ) {
                    bos.write(pdfByteArray, 0, readNum); //no doubt here is 0
                    //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                    Log.e("read ", +readNum + " bytes,");
                }
            } catch (IOException ex) {

            }
            pdfByteArray = bos.toByteArray();
            Log.e("Byte array2", ">" + pdfByteArray);
        }

        return Base64.encodeToString(pdfByteArray, Base64.NO_WRAP);

    }

    public static String getFileName(final Context context, final Uri uri) {
        String uriString = uri.toString();
        File myFile = new File(uriString);
        String displayName = null;

        if (uriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.getName();
        }
        return displayName;
    }

    private void updateUI(String layoutName) {

        switch (layoutName) {
            case "home":
                getTalentBankVideoAPI();
                homeRL.setVisibility(View.VISIBLE);
                addVideoLL.setVisibility(View.GONE);
                profileRL.setVisibility(View.GONE);
                trendLL.setVisibility(View.GONE);

                homeVideoView.setVisibility(View.VISIBLE);
                videoGridView.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
                homeVideoView.requestFocus();


                homeIV.setSelected(true);
                homeTV.setSelected(true);
                addIV.setSelected(false);
                addTV.setSelected(false);
                profileIV.setSelected(false);
                profileTV.setSelected(false);
                trendIV.setSelected(false);
                trendTV.setSelected(false);

                break;
            case "add":
                homeRL.setVisibility(View.GONE);
                addVideoLL.setVisibility(View.VISIBLE);
                profileRL.setVisibility(View.GONE);
                trendLL.setVisibility(View.GONE);

                homeVideoView.setVisibility(View.GONE);
                videoGridView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);

                homeIV.setSelected(false);
                homeTV.setSelected(false);
                addIV.setSelected(true);
                addTV.setSelected(true);
                profileIV.setSelected(false);
                profileTV.setSelected(false);
                trendIV.setSelected(false);
                trendTV.setSelected(false);

                break;
            case "profile":
                homeRL.setVisibility(View.GONE);
                addVideoLL.setVisibility(View.GONE);
                profileRL.setVisibility(View.VISIBLE);
                trendLL.setVisibility(View.GONE);

                homeVideoView.setVisibility(View.GONE);
                videoGridView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);

                homeIV.setSelected(false);
                homeTV.setSelected(false);
                addIV.setSelected(false);
                addTV.setSelected(false);
                profileIV.setSelected(true);
                profileTV.setSelected(true);
                trendIV.setSelected(false);
                trendTV.setSelected(false);
                break;

            case "trend":
                homeRL.setVisibility(View.GONE);
                addVideoLL.setVisibility(View.GONE);
                profileRL.setVisibility(View.GONE);
                trendLL.setVisibility(View.VISIBLE);

                homeVideoView.setVisibility(View.GONE);
                videoGridView.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);

                homeIV.setSelected(false);
                homeTV.setSelected(false);
                addIV.setSelected(false);
                addTV.setSelected(false);
                profileIV.setSelected(false);
                profileTV.setSelected(false);
                trendIV.setSelected(true);
                trendTV.setSelected(true);
                break;
        }

    }
}
