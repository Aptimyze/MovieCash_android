package jonomoneta.juno.moviecash.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.VolumeProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;
import com.bambuser.broadcaster.PlayerState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardMultilineWidget;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import jonomoneta.juno.moviecash.Adapter.PredictionAdapter;
import jonomoneta.juno.moviecash.Adapter.WinnersAdapter;
import jonomoneta.juno.moviecash.AlarmReceiver;
import jonomoneta.juno.moviecash.CustomEditText;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.Model.BroadcastResult;
import jonomoneta.juno.moviecash.Model.Response.AnswerCountResponse;
import jonomoneta.juno.moviecash.Model.Response.ApplyEndorseResponse;
import jonomoneta.juno.moviecash.Model.Response.ApplyGoldanBuzzerResponse;
import jonomoneta.juno.moviecash.Model.Response.ApplyLifeLineResponse;
import jonomoneta.juno.moviecash.Model.Response.ApplyWinnerRewardResponse;
import jonomoneta.juno.moviecash.Model.Response.ChargePurchaseDiamondsPaymentResponse;
import jonomoneta.juno.moviecash.Model.Response.GetNextQuizGameDateTimeResponse;
import jonomoneta.juno.moviecash.Model.Response.GetQuizGamePaymentPlanResponse;
import jonomoneta.juno.moviecash.Model.Response.Prediction;
import jonomoneta.juno.moviecash.Model.Response.QuizGameFreeSubscribeUserResponse;
import jonomoneta.juno.moviecash.Model.Response.QuizResponse;
import jonomoneta.juno.moviecash.Model.Response.SaveQuizAnswerResponse;
import jonomoneta.juno.moviecash.Model.Response.WinnerResponse;
import jonomoneta.juno.moviecash.Model.WinnerListItem;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PredictionCustomViewPager;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.PermissionUtils;
import jonomoneta.juno.moviecash.Utils.Utility;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static jonomoneta.juno.moviecash.Adapter.PredictionAdapter.setCheckBox;

public class PredictionActivity extends AppCompatActivity {

    private PredictionCustomViewPager predictionViewPager;
    private ArrayList<Prediction.ResponseData> predictionArrayList;
    private ArrayList<QuizResponse.ResponseData.QuestionList> quizList;
    private int NUM_PAGES = 0, currentPage = 0;
    private CustomTextViewBold playNowBtn, nextBtn, questionTV, countTV, timerTextView;
    private ImageView backBtn;
    private AVLoadingIndicatorView loader, loaderTwo;
    PreferenceSettings mPreferenceSettings;
    int uId;
    public static int ansid = 0, ansPos = 10;
    public static String anstxt = "";
    private Timer mTimer1, countDownTImer;
    private TimerTask mTt1, countDownTImerTask;
    private Handler mTimerHandler = new Handler(),
            countDownHandler = new Handler();
    private int timerSec = 10, streamTimer;
    private CustomTextView descTV;
    private VideoView videoView;
    private ProgressBar videoLoader;
    private LinearLayout videoViewRL, mainLL;
    private RelativeLayout quizbackgroundRl;
    private CircleImageView closeBTN, addKeyBtn, helpBTN;
    private ProgressBar timeLoader, playLoader;
    private int quizGameId, quizQuestionId, gameLevel, quizgameuserattendid;
    public static boolean isRight = false;
    PredictionAdapter predictionAdapter;
    private Animation zoomIn, zoomOut;
    int onlineUser = 0;
    boolean added = false;
    int countSec = 11;
    private CustomTextView termsTV, privacyTV, rulesTV;

    private RelativeLayout streamRL, streamTimerRL;
    private SurfaceView videoSurfaceView, videoSurfaceView2, streamSurfaceView, storySurfaceView;
    private RelativeLayout videoSurfaceRL, videoSurfaceRL2, streamSurfaceRL, storySurfaceRL;
    private TextView availableDiamondTV, onlineUserTV, streamQueTV, correctCountTV, wrongCountTV;
    private ProgressBar streamTimeLoader, correctCountPB, wrongCountPB;
    private LinearLayout questionLL, ansCountLL, quizTimeLL, countDownLL, daysLL;
    private CustomTextViewBold streamTimerTextView, quizTimeTV, noWinnerTV, subsTv,
            daysTV, hoursTV, minsTV, secTV, prizeTV, showStoryBTN;
    private CustomTextView ansOneRB, ansTwoRB, ansThreeRB, ansFourRB, ansFiveRB, adTV, loadingAdTV;
    BroadcastPlayer mBroadcastPlayer, mBroadcastPlayerForStory, mBroadcastPlayerForMultiAnchor;
    private static final String APPLICATION_ID = "k8JN9zTnxb9quc3406khyA"; //stream
    private static final String BAMBUSER_APPLICATION_ID = "IE208QHdUAaXXYlHMcwzlg"; // player
    private static final String BAMBUSER_API_KEY = "16lxikuz8g7dftv18ehin7qsw";
    final OkHttpClient mOkHttpClient = new OkHttpClient();
    final OkHttpClient mOkHttpClientForStory = new OkHttpClient();
    final OkHttpClient mOkHttpClientForMultiAnchor = new OkHttpClient();
    String broadCastState;
    private RecyclerView winnersRecyclerView;
    private ArrayList<WinnerListItem> winnerList;
    private WinnersAdapter winnersAdapter;
    private LinearLayout winnersLL, subscribeLL;
    private CustomTextView viewAllTV;
    ValueEventListener valueEventListener;
    private boolean firebaseGet = false;
    DatabaseReference databaseReference;
    String stripeCusID = "", email;
    boolean quizStarted;
    int lifeUsedCount = 0, correctAnsId = 0;
    private ImageView subsIv;
    private boolean adAvailable = false;

    Animation sid, sod;
    public static MediaPlayer homePlayer;
    MediaPlayer quePlayer, tickPLayer, quizPLayer;
    Vibrator vibrator;
    String subscriptionEndDate, subscriptionStartDate;
    private double diamond_price = 0, subscription_price = 0;
    Broadcaster mBroadcaster;
    int storyUserId = 0, currentquizid = 0;
    LinearLayout btnInvite;
    TextView availableStreamDiamondTV;
    boolean addLifeLineClicked = false;
    RelativeLayout.LayoutParams mainVideoParams, smallViewParams, multiViewParams1, multiViewParams2;
    LinearLayout walletLL, endorseBTN;
    ImageView reminderBTN;
    boolean multiAnchor = false;
    Date quizDateTime;
    String refCode = "", streamId = null, multiAnchorId = null, storyId = null;
    CircleImageView buzzerBtn, addKeyBtnStream;
    boolean isMegaQuiz = false, isPause = false, isStoryAvailable = false;
    int dividePrize = 0;
    private RelativeLayout adRL, parentRL;
    private VideoView adVideoView;
    private boolean isUpdate = false;
    int adStopPosition = 0;

//    CustomTextView talentTV;
//    SurfaceView mPreviewSurface;
//    Broadcaster mBroadcaster;
//    MediaScannerConnection mediaScannerConnection;
//    Button mBroadcastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();
    }

    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {
            Log.e("streamState", playerState.toString());
            if (playerState != null && playerState.toString().equalsIgnoreCase("COMPLETED")) {
                if (questionEventListener != null) {
                    databaseReference.child("MovieCashMillionaire").child("QuizQuestionNumber").removeEventListener(questionEventListener);
                }
                if (valueEventListener != null) {
                    databaseReference.child("MovieCashMillionaire").child("QuestionCountDown").removeEventListener(valueEventListener);
                }
                if (homePlayer != null) {
                    homePlayer.stop();
                }
                if (quizPLayer != null) {
                    quizPLayer.stop();
                }
                if (storyAvailableListener != null) {
                    databaseReference.child("MovieCashMillionaire").child("StoryAvailable").removeEventListener(storyAvailableListener);
                }
                if (storyUserIdListener != null) {
                    databaseReference.child("MovieCashMillionaire").child("StoryUserId").removeEventListener(storyUserIdListener);
                }
//                mPreferenceSettings.setStreamId(null);
                streamId = null;
                finish();
            }
            if (playerState != null && playerState.toString().equalsIgnoreCase("ERROR")) {
                Log.e("streamERROR", playerState.toString());
                getLatestResourceUri();
            }
        }

        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {

        }
    };

    BroadcastPlayer.Observer mBroadcastPlayerObserverForStory = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {
            Log.e("storyState", playerState.toString());
            if (playerState != null && playerState.toString().equalsIgnoreCase("COMPLETED")) {
//                mPreferenceSettings.setStoryId(null);
                storyId = null;
            }
            if (playerState != null && playerState.toString().equalsIgnoreCase("ERROR")) {
                getLatestResourceUriForStory();
            }
        }

        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {

        }
    };

    BroadcastPlayer.Observer mBroadcastPlayerObserverForMultiAnchor = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {
            Log.e("multiAnchorState", playerState.toString());
            if (playerState != null && playerState.toString().equalsIgnoreCase("COMPLETED")) {
//                mPreferenceSettings.setMultiAnchorId(null);
                multiAnchorId = null;
                databaseReference.child("MovieCashMillionaire").child("MultiAnchor").setValue(false);
            }
        }

        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {

        }
    };

    BroadcastPlayer.ViewerCountObserver viewerCountObserver = new BroadcastPlayer.ViewerCountObserver() {
        @Override
        public void onCurrentViewersUpdated(long l) {
            if (l >= 1000000) {
                double viewer = l / 1000000;
                onlineUserTV.setText(String.format("Value of a: %.2f", viewer) + "M");
            } else if (l >= 1000) {
                double viewer = l / 1000;
                onlineUserTV.setText(String.format("Value of a: %.2f", viewer) + "K");
            } else {
                onlineUserTV.setText(String.valueOf(l));
            }
        }

        @Override
        public void onTotalViewersUpdated(long l) {

        }
    };

    public class GooglePlayStoreAppVersionNameLoader extends AsyncTask<String, Void, String> {

        String newVersion;

        protected String doInBackground(String... urls) {
            try {
                return Jsoup.connect("https://play.google.com/store/apps/details?id=" + "movieosis.mdadil2019.movieosis" + "&hl=en")
                        .timeout(15000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();

            } catch (Exception e) {
                return e.getMessage();
            }
        }

        protected void onPostExecute(String string) {
            newVersion = string;
            Log.e("new Version", newVersion);
            try {
                String currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "";

                if (newVersion != null && !newVersion.isEmpty()) {

                    Log.e("curVer:", currentVersion + "");
                    try {
                        String cur[] = currentVersion.split("\\."),
                                newV[] = newVersion.split("\\.");
                        int size = 0;
                        if (cur.length < newV.length) {
                            size = newV.length;
                        } else {
                            size = cur.length;
                        }

                        for (int i = 0; i < size; i++) {
                            if (Integer.parseInt(cur[i]) < Integer.parseInt(newV[i])) {
                                isUpdate = true;
                                Utility.updateAlert(PredictionActivity.this, newVersion);
                                break;
                            }
                        }

                    } catch (Exception e) {
                        Log.e("exc", e.getMessage());
                        e.printStackTrace();
                    }
                }
                Log.e("update", "Current version " + currentVersion + "playstore version " + newVersion);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("pause", "pause");
        isPause = true;
        mOkHttpClient.dispatcher().cancelAll();
        mOkHttpClientForStory.dispatcher().cancelAll();
        mOkHttpClientForMultiAnchor.dispatcher().cancelAll();

        videoSurfaceView = null;
        storySurfaceView = null;
        videoSurfaceView2 = null;

        if (adVideoView != null && adVideoView.getVisibility() == View.VISIBLE) {
            adStopPosition = adVideoView.getCurrentPosition(); //stopPosition is an int
            adVideoView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBroadcaster != null)
            mBroadcaster.onActivityDestroy();
    }

    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    //    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
//        @Override
//        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
//
//            if (broadcastStatus == BroadcastStatus.STARTING)
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            if (broadcastStatus == BroadcastStatus.IDLE)
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            mBroadcastButton.setText(broadcastStatus == BroadcastStatus.IDLE ? "BROADCAST" : "DISCONNECT");
//        }
//
//        @Override
//        public void onStreamHealthUpdate(int i) {
//        }
//
//        @Override
//        public void onConnectionError(ConnectionError connectionError, String s) {
//        }
//
//        @Override
//        public void onCameraError(CameraError cameraError) {
//        }
//
//        @Override
//        public void onChatMessage(String s) {
//        }
//
//        @Override
//        public void onResolutionsScanned() {
//        }
//
//        @Override
//        public void onCameraPreviewStateChanged() {
//
//            Log.e("preview", "changed");
//        }
//
//        @Override
//        public void onBroadcastInfoAvailable(String s, String s1) {
//
//            Log.e("info", s + "-------" + s1);
//        }
//
//        @Override
//        public void onBroadcastIdAvailable(String s) {
//
//            Log.e("idAvail", s);
//        }
//    };

    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {

            if (broadcastStatus == BroadcastStatus.STARTING)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (broadcastStatus == BroadcastStatus.IDLE)
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            Log.e("status", broadcastStatus.toString());
            showStoryBTN.setText(broadcastStatus == BroadcastStatus.IDLE ? "START" : "STOP");


//            mBroadcastButton.setBackground(getResources().getDrawable(broadcastStatus == BroadcastStatus.IDLE ? R.drawable.circle_bg : R.drawable.square_bg));
        }

        @Override
        public void onStreamHealthUpdate(int i) {
        }

        @Override
        public void onConnectionError(ConnectionError connectionError, String s) {
            Log.e("connection ERROR", connectionError.toString() + "---String: " + s);
        }

        @Override
        public void onCameraError(CameraError cameraError) {
            Log.e("camera ERROR", cameraError.toString());
        }

        @Override
        public void onChatMessage(String s) {
        }

        @Override
        public void onResolutionsScanned() {
        }

        @Override
        public void onCameraPreviewStateChanged() {

            Log.e("preview", "changed");
        }

        @Override
        public void onBroadcastInfoAvailable(String s, String s1) {

            Log.e("info", s + "-------" + s1);
            if (s != null && s.length() > 0) {
                databaseReference.child("MovieCashMillionaire").child("StoryUserId").setValue(uId);
                databaseReference.child("MovieCashMillionaire").child("StoryAvailable").setValue(true);
            }
        }

        @Override
        public void onBroadcastIdAvailable(String s) {

            Log.e("idAvail", s);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (PermissionUtils.requestPermission(PredictionActivity.this, 0,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED)) {

            } else {
                Utility.permissionDialog(PredictionActivity.this, PredictionActivity.this);
            }
        }
    }

    private void init() {

        if (PermissionUtils.requestPermission(PredictionActivity.this, 0,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED)) {

        } else {
            Utility.permissionDialog(PredictionActivity.this, PredictionActivity.this);
        }

        if (Utility.isOnline(PredictionActivity.this)) {
            new GooglePlayStoreAppVersionNameLoader().execute();
        }

        Utility.getProfileDetails();
        mBroadcaster = new Broadcaster(PredictionActivity.this, APPLICATION_ID, mBroadcasterObserver);
        mBroadcaster.setTitle("Story");
        mBroadcaster.switchCamera();

        quePlayer = MediaPlayer.create(PredictionActivity.this, R.raw.question);
        tickPLayer = MediaPlayer.create(PredictionActivity.this, R.raw.tick);
        homePlayer = MediaPlayer.create(PredictionActivity.this, R.raw.home);
        quizPLayer = MediaPlayer.create(PredictionActivity.this, R.raw.quiz);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (!homePlayer.isPlaying()) {
            homePlayer.setLooping(true);
            homePlayer.start();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();


        uId = mPreferenceSettings.getUserDetails().getResponseData().getID();

        zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in_gift);
        zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out_gift);
        sid = AnimationUtils.loadAnimation(PredictionActivity.this, R.anim.slide_in_down_ques);
        sod = AnimationUtils.loadAnimation(PredictionActivity.this, R.anim.slide_out_down_ques);

        parentRL = findViewById(R.id.parentRL);
        adVideoView = findViewById(R.id.adVideoView);
        adRL = findViewById(R.id.adRL);
        mainLL = findViewById(R.id.mainLL);
        descTV = findViewById(R.id.descTV);
        timerTextView = findViewById(R.id.timerTextView);
        backBtn = findViewById(R.id.backBtn);
        predictionViewPager = findViewById(R.id.predictionViewPager);
        nextBtn = findViewById(R.id.nextBtn);
        questionTV = findViewById(R.id.questionTV);
        loader = findViewById(R.id.loader);
        loaderTwo = findViewById(R.id.loaderTwo);
        countTV = findViewById(R.id.countTV);
        videoView = findViewById(R.id.videoView);
        videoViewRL = findViewById(R.id.videoViewRL);
        quizbackgroundRl = findViewById(R.id.quizbackgroundRl);
        closeBTN = findViewById(R.id.closeBTN);
        playNowBtn = findViewById(R.id.playNowBtn);
        videoLoader = findViewById(R.id.videoLoader);
        timeLoader = findViewById(R.id.timeLoader);
        playLoader = findViewById(R.id.playLoader);
        streamRL = findViewById(R.id.streamRL);
        videoSurfaceView = findViewById(R.id.videoSurfaceView);
        onlineUserTV = findViewById(R.id.onlineUserTV);
        streamQueTV = findViewById(R.id.streamQueTV);
        questionLL = findViewById(R.id.questionLL);
        ansOneRB = findViewById(R.id.ansOneRB);
        ansTwoRB = findViewById(R.id.ansTwoRB);
        ansThreeRB = findViewById(R.id.ansThreeRB);
        ansFourRB = findViewById(R.id.ansFourRB);
        ansFiveRB = findViewById(R.id.ansFiveRB);
        streamTimeLoader = findViewById(R.id.streamTimeLoader);
        streamTimerTextView = findViewById(R.id.streamTimerTextView);
        streamTimerRL = findViewById(R.id.timerRL);
        ansCountLL = findViewById(R.id.ansCountLL);
        correctCountTV = findViewById(R.id.correctCountTV);
        wrongCountTV = findViewById(R.id.wrongCountTV);
        correctCountPB = findViewById(R.id.correctCountPB);
        wrongCountPB = findViewById(R.id.wrongCountPB);
        termsTV = findViewById(R.id.termsTV);
        privacyTV = findViewById(R.id.privacyTV);
        rulesTV = findViewById(R.id.rulesTV);
        winnersRecyclerView = findViewById(R.id.winnersRecyclerView);
        winnersLL = findViewById(R.id.winnersLL);
        viewAllTV = findViewById(R.id.viewAllTV);
        quizTimeTV = findViewById(R.id.quizTimeTV);
        quizTimeLL = findViewById(R.id.quizTimeLL);
        noWinnerTV = findViewById(R.id.noWinnerTV);
        addKeyBtn = findViewById(R.id.addKeyBtn);
        subscribeLL = findViewById(R.id.subscribeLL);
        availableDiamondTV = findViewById(R.id.availableDiamondTV);
        subsIv = findViewById(R.id.subsIv);
        subsTv = findViewById(R.id.subsTv);
        countDownLL = findViewById(R.id.countDownLL);
        daysLL = findViewById(R.id.daysLL);
        daysTV = findViewById(R.id.daysTV);
        hoursTV = findViewById(R.id.hoursTV);
        minsTV = findViewById(R.id.minsTV);
        secTV = findViewById(R.id.secTV);
        prizeTV = findViewById(R.id.prizeTV);
        helpBTN = findViewById(R.id.helpBTN);
        showStoryBTN = findViewById(R.id.showStoryBTN);
        videoSurfaceView2 = findViewById(R.id.videoSurfaceView2);
        streamSurfaceView = findViewById(R.id.streamSurfaceView);
        storySurfaceView = findViewById(R.id.storySurfaceView);
        btnInvite = findViewById(R.id.btnInvite);
        videoSurfaceRL = findViewById(R.id.videoSurfaceRL);
        streamSurfaceRL = findViewById(R.id.streamSurfaceRL);
        storySurfaceRL = findViewById(R.id.storySurfaceRL);
        videoSurfaceRL2 = findViewById(R.id.videoSurfaceRL2);
        walletLL = findViewById(R.id.walletLL);
        reminderBTN = findViewById(R.id.reminderBTN);
        endorseBTN = findViewById(R.id.endorseBTN);
        buzzerBtn = findViewById(R.id.buzzerBtn);
        availableStreamDiamondTV = findViewById(R.id.availableStreamDiamondTV);
        addKeyBtnStream = findViewById(R.id.addKeyBtnStream);
        adTV = findViewById(R.id.adTV);
        loadingAdTV = findViewById(R.id.loadingAdTV);

        databaseReference.child("MovieCashMillionaire").child("CurrentQuizID").addValueEventListener(currentQuizIdListener);

        buzzerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBuzzerDialog();
            }
        });

        reminderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReminderDialog();
            }
        });

        endorseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndorsementDialog();
            }
        });

        walletLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PredictionActivity.this, WalletActivity.class));
                homePlayer.pause();
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int smallViewHeight = (size.y) / 5;
        int multiViewWidth = (size.x) / 2;
        int multiViewHeight = ((size.y) / 2);

        mainVideoParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainVideoParams.setMargins(0, 0, 0, smallViewHeight);

        multiViewParams1 = new RelativeLayout.LayoutParams(multiViewWidth, multiViewHeight);
        multiViewParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        multiViewParams2 = new RelativeLayout.LayoutParams(multiViewWidth, multiViewHeight);
        multiViewParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        smallViewParams = new RelativeLayout.LayoutParams(smallViewHeight, smallViewHeight);
        smallViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        smallViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);


        showStoryBTN.setVisibility(View.GONE);

        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String refCode = mPreferenceSettings.getUserDetails().getResponseData().getReferCode();
                String shareBody = "I am playing and enjoying quiz on MovieCash! You should enjoy too. Use my code - *" + refCode + "* to sign up and earn rewards." + "\n\nGet app now:\nhttps://play.google.com/store/apps/details?id=movieosis.mdadil2019.movieosis";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share with"));
            }
        });

        showStoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mBroadcaster.canStartBroadcasting()) {
                if (showStoryBTN.getText().toString().trim().equalsIgnoreCase("START")) {
                    if (mBroadcaster.canStartBroadcasting()) {
                        mBroadcaster.startBroadcast();
                    } else {
                        Toast.makeText(PredictionActivity.this, "Camera is not ready.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    databaseReference.child("MovieCashMillionaire").child("StoryUserId").setValue(0);
                    databaseReference.child("MovieCashMillionaire").child("StoryAvailable").setValue(false);
                    streamSurfaceRL.setVisibility(View.GONE);
                    if (streamSurfaceView != null) {
                        streamSurfaceView.setVisibility(View.GONE);
                    }
                    mBroadcaster.stopBroadcast();
                    videoSurfaceRL.setClickable(false);
                    streamSurfaceRL.setClickable(false);
                    videoSurfaceRL.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    if (videoSurfaceView != null) {
                        videoSurfaceView.setBackground(null);
                    }
                }
            }
        });

        helpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PredictionActivity.this, HelpActivity.class));
            }
        });

        getPlanPriceAPI();

        if (mPreferenceSettings.getUserDetails() != null) {
            if (mPreferenceSettings.getUserDetails().getResponseData() != null) {
                if (mPreferenceSettings.getUserDetails().getResponseData().getBuzzerRefrralCount() >= Utility.golden_buzzer_count) {
                    buzzerBtn.setVisibility(View.VISIBLE);
                } else {
                    buzzerBtn.setVisibility(View.GONE);
                }
                availableDiamondTV.setText(mPreferenceSettings.getUserDetails().getResponseData().getAvailableDiamonds() + "");
                if (mPreferenceSettings.getUserDetails().getResponseData().getEmail() != null &&
                        mPreferenceSettings.getUserDetails().getResponseData().getEmail().length() > 0) {
                    email = mPreferenceSettings.getUserDetails().getResponseData().getEmail();
                }
                if (mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID() != null &&
                        mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID().length() > 0) {
                    stripeCusID = mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID();
                }
                if (mPreferenceSettings.getUserDetails().getResponseData().getSubscriptionEndDate() != null) {
                    subscriptionEndDate = mPreferenceSettings.getUserDetails().getResponseData().getSubscriptionEndDate().split("T")[0];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    SimpleDateFormat ndf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    ndf.setTimeZone(TimeZone.getDefault());


                    try {
                        Date endDate = sdf.parse(subscriptionEndDate),
                                todayDate = ndf.parse(ndf.format(Calendar.getInstance().getTime()));

                        String endDateLocal = ndf.format(endDate);

                        try {
                            endDate = ndf.parse(endDateLocal);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (todayDate.after(endDate)) {
                            if (mPreferenceSettings.getUserDetails().getResponseData().isPaidSubscription()) {
                                subsTv.setText("SUBSCRIBE");
                            } else {
                                subsTv.setText("FREE SUBSCRIBE");
                            }
                            subsIv.setImageResource(R.drawable.ic_click);
                        } else {
                            subsTv.setText("SUBSCRIBED");
                            subsIv.setImageResource(R.drawable.ic_correct);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        addKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLifeLineClicked = true;
                showPurchaseKeyDialog(false);

            }
        });

        addKeyBtnStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLifeLineClicked = true;
                showPurchaseKeyDialog(false);

            }
        });

        viewAllTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isOnline(PredictionActivity.this)) {
                    startActivity(new Intent(PredictionActivity.this, WinnersActivity.class)
                            .putExtra("quizgameid", quizGameId));
//                    finish();
                } else {
                    Utility.noInternetError(PredictionActivity.this);
                }
            }
        });

        subscribeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subsTv.getText().toString().equalsIgnoreCase("SUBSCRIBE") ||
                        subsTv.getText().toString().equalsIgnoreCase("FREE SUBSCRIBE")) {
                    showSubscribeDialog();
                } else {
                    showSubsDetailsDialog();
                }
            }
        });

        winnersRecyclerView.setLayoutManager(new LinearLayoutManager(PredictionActivity.this, LinearLayoutManager.HORIZONTAL, false));
        winnersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        winnerList = new ArrayList<>();


//                  auto scroll recyclerview like news feed horizontally.
//        final int speedScroll = 50;
//        final Handler handler = new Handler();
//        final Runnable runnable = new Runnable() {
//            int count = 0;
//            boolean flag = true;
//
//            @Override
//            public void run() {
//                if (count < winnersAdapter.getItemCount()) {
//                    if (count == winnersAdapter.getItemCount() - 1) {
//                        flag = false;
//                    } else if (count == 0) {
//                        flag = true;
//                    }
//                    if (flag) count++;
//
//
//                    winnersRecyclerView.smoothScrollBy(25, 0);
//                    handler.postDelayed(this, speedScroll);
//                }
//            }
//        };
//
//        handler.postDelayed(runnable, speedScroll);

        termsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Utility.terms_link;
                startActivity(new Intent(PredictionActivity.this, WebViewActivity.class)
                        .putExtra("url", url)
                        .putExtra("title", "Terms"));
                homePlayer.pause();
            }
        });

        rulesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Utility.rules_link;
                startActivity(new Intent(PredictionActivity.this, WebViewActivity.class)
                        .putExtra("url", url)
                        .putExtra("title", "Rules"));
                homePlayer.pause();
            }
        });

        privacyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Utility.privacy_link;
                startActivity(new Intent(PredictionActivity.this, WebViewActivity.class)
                        .putExtra("url", url)
                        .putExtra("title", "Privacy"));
                homePlayer.pause();
            }
        });

        getNextQuizDateTimeAPI();

        nextBtn.setVisibility(View.GONE);
        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimer1 != null) {
                    mTimer1.cancel();
                    mTimer1.purge();
                }
                stopCounrDownTimer();
                finish();
                if (homePlayer != null) {
                    homePlayer.stop();
                }
            }
        });


        playNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (playNowBtn.getText().toString().trim().equalsIgnoreCase("Play Now")) {
                    if (Utility.isOnline(PredictionActivity.this)) {

                        playLoader.setVisibility(View.VISIBLE);
                        playNowBtn.setVisibility(View.GONE);
                        getQuizAPI();
                    } else {
                        Utility.noInternetError(PredictionActivity.this);
                    }
                }

            }
        });

        predictionArrayList = new ArrayList<>();

        loader.setVisibility(View.GONE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        videoSurfaceRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoSurfaceRL.setClickable(false);
                if (videoSurfaceView != null) {
                    videoSurfaceView.setBackground(null);
                }
                if (streamSurfaceRL.getVisibility() == View.VISIBLE) {
                    streamSurfaceRL.setClickable(true);
                    streamSurfaceRL.setLayoutParams(smallViewParams);
                    if (streamSurfaceView != null) {
                        streamSurfaceView.setBackground(getResources().getDrawable(R.drawable.circle));
                    }

                } else if (storySurfaceRL.getVisibility() == View.VISIBLE) {
                    storySurfaceRL.setClickable(true);
                    storySurfaceRL.setLayoutParams(smallViewParams);
                    if (storySurfaceView != null) {
                        storySurfaceView.setBackground(getResources().getDrawable(R.drawable.circle));
                    }
                }

                if (multiAnchor) {
                    videoSurfaceRL.setLayoutParams(multiViewParams1);
                    videoSurfaceRL2.setLayoutParams(multiViewParams2);
                } else {
                    videoSurfaceRL.setLayoutParams(mainVideoParams);
                }
            }
        });

        streamSurfaceRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streamSurfaceRL.setClickable(false);
                videoSurfaceRL.setClickable(true);

                if (videoSurfaceView != null) {
                    videoSurfaceView.setBackground(getResources().getDrawable(R.drawable.circle));
                }
                if (streamSurfaceView != null) {
                    streamSurfaceView.setBackground(null);
                }


                if (multiAnchor) {
                    videoSurfaceRL.setLayoutParams(smallViewParams);
                    streamSurfaceRL.setLayoutParams(multiViewParams1);
                    videoSurfaceRL2.setLayoutParams(multiViewParams2);
                } else {
                    videoSurfaceRL.setLayoutParams(smallViewParams);
                    streamSurfaceRL.setLayoutParams(mainVideoParams);

                }
            }
        });

        storySurfaceRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storySurfaceRL.setClickable(false);
                videoSurfaceRL.setClickable(true);

                if (videoSurfaceView != null) {
                    videoSurfaceView.setBackground(getResources().getDrawable(R.drawable.circle));
                }
                if (storySurfaceView != null) {
                    storySurfaceView.setBackground(null);
                }


                if (multiAnchor) {
                    videoSurfaceRL.setLayoutParams(smallViewParams);
                    storySurfaceRL.setLayoutParams(multiViewParams1);
                    videoSurfaceRL2.setLayoutParams(multiViewParams2);
                } else {
                    videoSurfaceRL.setLayoutParams(smallViewParams);
                    storySurfaceRL.setLayoutParams(mainVideoParams);
                }
            }
        });

        adVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                adAvailable = false;
                adRL.setVisibility(View.GONE);
                adVideoView.setVisibility(View.GONE);
                parentRL.setVisibility(View.VISIBLE);
                if (mBroadcastPlayer != null) {
                    mBroadcastPlayer.setAudioVolume(1.0f);
                }
                if (mBroadcastPlayerForMultiAnchor != null) {
                    mBroadcastPlayerForMultiAnchor.setAudioVolume(1.0f);
                }
                if (mBroadcastPlayerForStory != null) {
                    mBroadcastPlayerForStory.setAudioVolume(1.0f);
                }
            }
        });

        adVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                loadingAdTV.setVisibility(View.GONE);
                if (mBroadcastPlayer != null) {
                    mBroadcastPlayer.setAudioVolume(0.0f);
                }
                if (mBroadcastPlayerForMultiAnchor != null) {
                    mBroadcastPlayerForMultiAnchor.setAudioVolume(0.0f);
                }
                if (mBroadcastPlayerForStory != null) {
                    mBroadcastPlayerForStory.setAudioVolume(0.0f);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!hasPermission(Manifest.permission.CAMERA)
                && !hasPermission(Manifest.permission.RECORD_AUDIO)
                && !hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                && !hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && !hasPermission(Manifest.permission.BLUETOOTH))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.BLUETOOTH}, 1);
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        else if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        else if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        else if (!hasPermission(Manifest.permission.BLUETOOTH))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 1);

        Log.e("pause", String.valueOf(isPause));
        if (isPause) {
            isPause = false;
            videoSurfaceView = findViewById(R.id.videoSurfaceView);
            getLatestResourceUri();

            if (multiAnchor) {
                videoSurfaceView2 = findViewById(R.id.videoSurfaceView2);
                getLatestResourceUriForMultiAnchor();
            }

            if (isStoryAvailable) {
                storySurfaceView = findViewById(R.id.storySurfaceView);
                getLatestResourceUriForStory();
            }

            if (storyUserId == uId) {
                streamSurfaceView = findViewById(R.id.streamSurfaceView);
                streamSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                streamSurfaceView.setVisibility(View.VISIBLE);
                streamSurfaceView.setBackground(getResources().getDrawable(R.drawable.circle));

                mBroadcaster.setCameraSurface(streamSurfaceView);
                mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
                mBroadcaster.onActivityResume();

                videoSurfaceRL.setClickable(false);
                streamSurfaceRL.setClickable(true);

                if (multiAnchor) {
                    videoSurfaceView.setBackground(null);
                    videoSurfaceView2.setBackground(null);

                    videoSurfaceRL.setLayoutParams(multiViewParams1);
                    videoSurfaceRL2.setLayoutParams(multiViewParams2);
                    streamSurfaceRL.setLayoutParams(smallViewParams);
                } else {
                    videoSurfaceView.setBackground(null);

                    videoSurfaceRL.setLayoutParams(mainVideoParams);
                    streamSurfaceRL.setLayoutParams(smallViewParams);
                }
            }

            if (mBroadcaster != null) {
                mBroadcaster.onActivityResume();
            }

            Log.e("adAvailable", String.valueOf(adAvailable));
            if (adAvailable) {
                if (adVideoView != null) {

                    adRL.setVisibility(View.VISIBLE);
                    loadingAdTV.setVisibility(View.VISIBLE);
                    adVideoView.setVisibility(View.VISIBLE);
                    adVideoView.seekTo(adStopPosition);
                    adVideoView.start();
                }
            }
        }
        if (isUpdate) {
            isUpdate = false;
            new GooglePlayStoreAppVersionNameLoader().execute();
        }

    }

    private void getPlanPriceAPI() {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GetQuizGamePaymentPlanResponse> getPlan = retroInterface.getQuizGamePaymentPlan();
        getPlan.enqueue(new Callback<GetQuizGamePaymentPlanResponse>() {
            @Override
            public void onResponse(Call<GetQuizGamePaymentPlanResponse> call, Response<GetQuizGamePaymentPlanResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {

                    for (int i = 0; i < response.body().getResponseData().size(); i++) {
                        if (response.body().getResponseData().get(i).getPlanName().equalsIgnoreCase("LIFELINE")) {
                            diamond_price = response.body().getResponseData().get(i).getPrice();
                        } else if (response.body().getResponseData().get(i).getPlanName().equalsIgnoreCase("QUIZSUBSCRIPTION")) {
                            subscription_price = response.body().getResponseData().get(i).getPrice();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetQuizGamePaymentPlanResponse> call, Throwable t) {

            }
        });

    }

    private void applyWinnerRewardAPI(int quizGameId, String QuizGameType, final long finalPrize, final boolean isStream) {
        String mobNo = mPreferenceSettings.getMobileNumber();
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<ApplyWinnerRewardResponse> applyReward = retroInterface.applyWinnerReward(mobNo, finalPrize, uId, quizGameId, QuizGameType);
        applyReward.enqueue(new Callback<ApplyWinnerRewardResponse>() {
            @Override
            public void onResponse(Call<ApplyWinnerRewardResponse> call, Response<ApplyWinnerRewardResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    Utility.getProfileDetails();
                    if (!isStream) {
                        if (response.body().getResponseID() == 1) {
                            showQuizWinnerDialog("You have win this quiz and earned $ ", String.valueOf(finalPrize), " JM", true);
                        } else {
                            finish();
                        }
                    } else {
                        if (response.body().getResponseID() == 1) {
                            if (dividePrize == 1) {
                                showQuizWinnerDialog("You have win this quiz and earned $ ", String.valueOf(finalPrize), "", false);
                            } else if (dividePrize == 0) {
                                showQuizWinnerDialog("You have win this quiz and earned $ ", String.valueOf(finalPrize), " JM", false);
                            }
                        } else if (response.body().getResponseID() == -1) {
                            showQuizWinnerDialog("You have earned $ ", String.valueOf(Utility.live_quiz_attend_reward), " JM", false);
                        }
                    }
                } else {
                    if (!isStream) {
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApplyWinnerRewardResponse> call, Throwable t) {
                finish();
            }
        });
    }

    private void applyMegaQuizWinnerRewardAPI(int quizGameId, String QuizGameType, final long finalPrize) {
        String mobNo = mPreferenceSettings.getMobileNumber();
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<ApplyWinnerRewardResponse> applyReward = retroInterface.applyMegaQuizWinnerReward(mobNo, finalPrize, uId, quizGameId, QuizGameType);
        applyReward.enqueue(new Callback<ApplyWinnerRewardResponse>() {
            @Override
            public void onResponse(Call<ApplyWinnerRewardResponse> call, Response<ApplyWinnerRewardResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    Utility.getProfileDetails();
                    if (response.body().getResponseID() == 1) {
                        showQuizWinnerDialog("You have win this quiz and earned $ ", String.valueOf(finalPrize), " JM", false);
                    } else if (response.body().getResponseID() == -1) {
                        showQuizWinnerDialog("You have earned $ ", String.valueOf(Utility.live_mega_quiz_winner_reward), " JM", false);
                    } else if (response.body().getResponseID() == -2) {
                        showQuizWinnerDialog("You have earned $ ", String.valueOf(Utility.live_mega_quiz_player_reward), " JM", false);
                    } else if (response.body().getResponseID() == -3) {
                        showQuizWinnerDialog("You have earned $ ", String.valueOf(Utility.live_quiz_attend_reward), " JM", false);
                    }

                }
            }

            @Override
            public void onFailure(Call<ApplyWinnerRewardResponse> call, Throwable t) {

            }
        });
    }

    private void getTopWinnersListAPI(final int quizgameid) {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<WinnerResponse> getTopWinner = retroInterface.getTopWinnersList(uId, quizgameid);
        getTopWinner.enqueue(new Callback<WinnerResponse>() {
            @Override
            public void onResponse(Call<WinnerResponse> call, Response<WinnerResponse> response) {

                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    if (!isMegaQuiz) {
                        long finalEarn = 0;
                        if (dividePrize == 1) {
                            if (response.body().getResponseData().getTotalWinners() > 0) {
                                finalEarn = mPreferenceSettings.getQuizPrize() / response.body().getResponseData().getTotalWinners();
                            }
                        } else if (dividePrize == 0) {
                            finalEarn = Utility.live_quiz_winner_reward_fix;
                        }
                        applyWinnerRewardAPI(quizgameid, "LIVESTREAM", finalEarn, true);
                    } else {
                        long finalEarn = 0;
                        if (response.body().getResponseData().getTotalWinners() > 0) {

                            finalEarn = mPreferenceSettings.getQuizPrize() / response.body().getResponseData().getTotalWinners();
                        }
                        applyMegaQuizWinnerRewardAPI(quizgameid, "LIVESTREAM", finalEarn);
                    }
                    if (response.body().getResponseData().getQuizGameWinnerList() != null &&
                            response.body().getResponseData().getQuizGameWinnerList().size() > 0) {
                        winnersLL.setVisibility(View.VISIBLE);
                        viewAllTV.setVisibility(View.VISIBLE);
                        winnersLL.startAnimation(sid);
                        viewAllTV.startAnimation(sid);
                        winnerList = response.body().getResponseData().getQuizGameWinnerList();
                        winnersAdapter = new WinnersAdapter(PredictionActivity.this, winnerList);
                        winnersRecyclerView.setAdapter(winnersAdapter);
                        winnersAdapter.notifyDataSetChanged();

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                winnersLL.startAnimation(sod);
                                winnersLL.setVisibility(View.GONE);
                            }
                        }, 10000);
                    } else {
                        noWinnerTV.setVisibility(View.VISIBLE);
                    }
                } else {
                    noWinnerTV.setVisibility(View.VISIBLE);
                    Log.e("error", response.body().getResponseMessage());
                }
            }

            @Override
            public void onFailure(Call<WinnerResponse> call, Throwable t) {
                Log.e("failure", t.getMessage());
                noWinnerTV.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        if (mPreviewSurface.getVisibility() != View.VISIBLE) {
        if (quizbackgroundRl.getVisibility() != View.VISIBLE) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(PredictionActivity.this, R.style.AlertDialogTheme);
            builder1.setTitle("Alert");
            builder1.setIcon(R.drawable.logo);
            builder1.setMessage("Are you sure to leave?");
            builder1.setCancelable(false);
            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (questionEventListener != null) {
                                databaseReference.child("MovieCashMillionaire").child("QuizQuestionNumber").removeEventListener(questionEventListener);
                            }
                            if (valueEventListener != null) {
                                databaseReference.child("MovieCashMillionaire").child("QuestionCountDown").removeEventListener(valueEventListener);
                            }
                            if (storyAvailableListener != null) {
                                databaseReference.child("MovieCashMillionaire").child("StoryAvailable").removeEventListener(storyAvailableListener);
                            }
                            if (storyUserIdListener != null) {
                                databaseReference.child("MovieCashMillionaire").child("StoryUserId").removeEventListener(storyUserIdListener);
                            }
                            if (multiAnchorListener != null) {
                                databaseReference.child("MovieCashMillionaire").child("MultiAnchor").removeEventListener(multiAnchorListener);
                            }
                            if (adUrlListener != null) {
                                databaseReference.child("MovieCashMillionaire").child("QuizAdUrl").removeEventListener(adUrlListener);
                            }
                            if (quizPLayer != null) {
                                quizPLayer.stop();
                            }
                            if (homePlayer != null) {
                                homePlayer.stop();
                            }

                            if (mBroadcastPlayer != null)
                                mBroadcastPlayer.close();
                            mBroadcastPlayer = null;

                            if (mBroadcastPlayerForStory != null)
                                mBroadcastPlayerForStory.close();
                            mBroadcastPlayerForStory = null;

                            if (mBroadcastPlayerForMultiAnchor != null)
                                mBroadcastPlayerForMultiAnchor.close();
                            mBroadcastPlayerForMultiAnchor = null;

                            dialog.dismiss();
                            stopTimer();
                            finish();

                        }
                    });
            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
            if (mTimer1 != null) {
                mTimer1.cancel();
                mTimer1.purge();
            }
            if (homePlayer != null) {
                homePlayer.stop();
            }
            stopCounrDownTimer();
            super.onBackPressed();
        }
//        } else {
//            mBroadcaster.stopBroadcast();
//            mPreviewSurface.setVisibility(View.GONE);
//        }
    }

    private void slidePager() {

        if (currentPage < (NUM_PAGES - 1)) {

//            predictionAdapter.notifyDataSetChanged();
            startTimer();
            currentPage++;
            countTV.setText((currentPage + 1) + "/" + NUM_PAGES);
            questionTV.setText("Q. " + quizList.get(currentPage).getQuestion());
            setCheckBox(currentPage);
            predictionAdapter.notifyDataSetChanged();
            predictionViewPager.setCurrentItem(currentPage, true);

//            setCheckBox();
            ansid = 0;
            isRight = false;
            quizQuestionId = quizList.get(currentPage).getID();
            anstxt = "";
            if (currentPage == (NUM_PAGES - 1)) {
                nextBtn.setText("DONE");
            }
        } else {
            //Claim dialog display
            applyWinnerRewardAPI(quizGameId, "VIDEO", Utility.offline_quiz_winner_prize, false);
            if (quizPLayer != null) {
                quizPLayer.stop();
            }

        }
    }

    private void showClaimDialog() {

        final Dialog dialog = new Dialog(PredictionActivity.this);
        dialog.setContentView(R.layout.custom_reward_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        final ProgressBar claimLoader = dialog.findViewById(R.id.claimLoader);
        CircleImageView closeDialogBtn = dialog.findViewById(R.id.closeDialogBtn);
        closeDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        final CustomTextViewBold claimBtn = dialog.findViewById(R.id.claimBtn);
        claimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isOnline(PredictionActivity.this)) {
                    claimLoader.setVisibility(View.VISIBLE);
                    claimBtn.setVisibility(View.GONE);
                    claimPrizeAPI(dialog, claimBtn, claimLoader);
                } else {
                    Utility.noInternetError(PredictionActivity.this);
                }

            }
        });

        dialog.show();
    }

    private void showPurchaseKeyDialog(final boolean isVideo) {

        final Dialog dialog = new Dialog(PredictionActivity.this);
        dialog.setContentView(R.layout.custom_key_purchase_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        final CustomEditText qtyET = dialog.findViewById(R.id.qtyET);
        final Button purchaseBtn = dialog.findViewById(R.id.purchaseBtn);
        CircleImageView closeDialogBtn = dialog.findViewById(R.id.closeDialogBtn);
        CustomTextViewBold life_descTV = dialog.findViewById(R.id.life_descTV);
        life_descTV.setText("PURCHASE LIFELINE\n($ " + diamond_price + " / life )");

        closeDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLifeLineClicked = false;
                dialog.dismiss();
                if (isVideo) {
                    slidePager();
                }
            }
        });

        qtyET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (Integer.parseInt(String.valueOf(s)) > 0) {
                        purchaseBtn.setText("BUY ( $ " + Integer.parseInt(String.valueOf(s)) * diamond_price + " )");
                    } else {
                        purchaseBtn.setText("BUY");
                    }
                } else {
                    purchaseBtn.setText("BUY");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        purchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qtyET.getText().toString().trim().length() > 0) {
                    int qty = Integer.parseInt(qtyET.getText().toString().trim());
                    if (qty > 0) {
                        dialog.dismiss();
                        if (mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID() != null &&
                                mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID().length() > 0) {
                            showFilledCardDetailsDialog(qty, true, isVideo);
                        } else {
                            showCardDetailDialog(qty, true, isVideo);
                        }
                    } else {
                        Toast.makeText(PredictionActivity.this, "Number of diamond must be atleast 1.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(PredictionActivity.this, "Please enter the no. of diamond(s) you want to purchase.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void showSubsDetailsDialog() {

        final Dialog dialog = new Dialog(PredictionActivity.this);
        dialog.setContentView(R.layout.custom_subscription_details_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        CustomTextViewBold okBtn = dialog.findViewById(R.id.okBtn),
                startDateTV = dialog.findViewById(R.id.startDateTV),
                endDateTV = dialog.findViewById(R.id.endDateTV);

        subscriptionStartDate = mPreferenceSettings.getUserDetails().getResponseData().getSubscriptionStartDate().split("T")[0];
        subscriptionEndDate = mPreferenceSettings.getUserDetails().getResponseData().getSubscriptionEndDate().split("T")[0];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat ndf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        try {
            Date endDate = sdf.parse(subscriptionEndDate),
                    startDate = sdf.parse(subscriptionStartDate);
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
        final boolean isPaidSubscription = MyApplication.getInstance().getPreferenceSettings().getUserDetails().getResponseData().isPaidSubscription();
        final Dialog dialog = new Dialog(PredictionActivity.this);
        dialog.setContentView(R.layout.custom_subscribe_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        CustomTextViewBold subscribeBtn = dialog.findViewById(R.id.subscribeBtn);
        CircleImageView closeBtn = dialog.findViewById(R.id.closeBtn);
        CustomTextView subs_descTV = dialog.findViewById(R.id.subs_descTV);
        final CustomEditText refferalCodeEditText = dialog.findViewById(R.id.refferalCodeEditText);

        if (isPaidSubscription) {
            refferalCodeEditText.setVisibility(View.VISIBLE);
            subs_descTV.setText("- Subscribe MovieCash Millionaire.\n- $ " + subscription_price + " / 3 month.");
        } else {
            refferalCodeEditText.setVisibility(View.GONE);
            subs_descTV.setText("- Subscribe MovieCash Millionaire.\n- 3 months subscription for FREE.");
        }
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        refCode = "";

        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isOnline(PredictionActivity.this)) {
                    dialog.dismiss();
                    if (!refferalCodeEditText.getText().toString().trim().isEmpty()) {
                        refCode = refferalCodeEditText.getText().toString().trim();
                    }
                    if (isPaidSubscription) {
                        if (stripeCusID != null && stripeCusID.length() > 0) {
                            showFilledCardDetailsDialog(0, false, false);
                        } else {
                            showCardDetailDialog(0, false, false);
                        }
                    } else {
                        dialog.dismiss();
                        quizFreeSubscriptionAPI();
                    }
                } else {
                    Utility.noInternetError(PredictionActivity.this);
                }

            }
        });

        dialog.show();
    }

    private void quizFreeSubscriptionAPI() {

        final ProgressDialog progressDialog = new ProgressDialog(PredictionActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait.");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<QuizGameFreeSubscribeUserResponse> subscribe = retroInterface.quizGameFreeSubscribeUser(uId);
        subscribe.enqueue(new Callback<QuizGameFreeSubscribeUserResponse>() {
            @Override
            public void onResponse(Call<QuizGameFreeSubscribeUserResponse> call, Response<QuizGameFreeSubscribeUserResponse> response) {
                progressDialog.dismiss();
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    successDialog("Your 3 months free subscription is activated.", "", false, false);
                    subsTv.setText("SUBSCRIBED");
                    subsIv.setImageResource(R.drawable.ic_correct);
                    Utility.getProfileDetails();
                } else {
                    Toast.makeText(PredictionActivity.this, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<QuizGameFreeSubscribeUserResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PredictionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void subscribeAPI(String stripeId, double amount, String email, String cardName, String cardNumber, int cardExpYear,
                              int cardExpMonth, String cvc, final ProgressBar progress, final CustomTextViewBold payNowBtn, final Dialog dialog) {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<ChargePurchaseDiamondsPaymentResponse> subscribe = retroInterface.subscribeUser(uId, stripeId, amount, "",
                email, cardName, cardNumber, cardExpYear, cardExpMonth, cvc, refCode);
        subscribe.enqueue(new Callback<ChargePurchaseDiamondsPaymentResponse>() {
            @Override
            public void onResponse(Call<ChargePurchaseDiamondsPaymentResponse> call, Response<ChargePurchaseDiamondsPaymentResponse> response) {
                progress.setVisibility(View.GONE);
                payNowBtn.setVisibility(View.VISIBLE);
                dialog.dismiss();
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    if (response.body().getResponseData().getPaymentStatus().equalsIgnoreCase("succeeded")) {
                        successDialog("Subscribed successfully.", response.body().getResponseData().getReceiptUrl(),
                                false, false);
                        subsTv.setText("SUBSCRIBED");
                        subsIv.setImageResource(R.drawable.ic_correct);
                        Utility.getProfileDetails();
                    } else {
                        Toast.makeText(PredictionActivity.this, response.body().getResponseData().getFailureMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(PredictionActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChargePurchaseDiamondsPaymentResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                payNowBtn.setVisibility(View.VISIBLE);
                dialog.dismiss();
                Toast.makeText(PredictionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showFilledCardDetailsDialog(final int qty, final boolean isPurchase, final boolean isVideo) {

        final Dialog dialog = new Dialog(PredictionActivity.this);
        dialog.setContentView(R.layout.custom_filled_card_details_dialog);
        dialog.setCancelable(false);
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
                showCardDetailDialog(qty, isPurchase, isVideo);
            }
        });

        closeDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isVideo) {
                    showPurchaseKeyDialog(isVideo);
                }
            }
        });

        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amount = 0;
                if (isPurchase) {
                    amount = qty * diamond_price;
                } else {
                    amount = subscription_price;
                }
                payNowBtn.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                stripeCusID = mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID();
                if (isPurchase) {
                    purchaseDiamondAPI(stripeCusID, amount, qty, email, "", "", 0, 0, "",
                            progress, payNowBtn, dialog, isVideo);
                } else {
                    subscribeAPI(stripeCusID, amount, email, "", "", 0, 0, "",
                            progress, payNowBtn, dialog);
                }
            }
        });

        dialog.show();
    }

    private void showCardDetailDialog(final int qty, final boolean purchase, final boolean isVideo) {

        final Dialog dialog = new Dialog(PredictionActivity.this);
        dialog.setContentView(R.layout.custom_card_details_dialog);
        dialog.setCancelable(false);
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
                if (isVideo) {
                    showPurchaseKeyDialog(isVideo);
                }
            }
        });

        payNowBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                double amount = 0;
                if (purchase) {
                    amount = qty * diamond_price;
                } else {
                    amount = subscription_price;
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
                                if (Utility.isOnline(PredictionActivity.this)) {
                                    progress.setVisibility(View.VISIBLE);
                                    payNowBTN.setVisibility(View.GONE);

                                    if (purchase) {
                                        purchaseDiamondAPI("", amount, qty, email, cardName, cardNumber, expYear, expMonth,
                                                cvc, progress, payNowBTN, dialog, isVideo);
                                    } else {
                                        subscribeAPI("", amount, email, cardName, cardNumber, expYear, expMonth,
                                                cvc, progress, payNowBTN, dialog);
                                    }
                                } else {
                                    Utility.noInternetError(PredictionActivity.this);
                                }
                            } else {
                                Toast.makeText(PredictionActivity.this, "Invalid card details.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PredictionActivity.this, "PLease enter card details.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PredictionActivity.this, "PLease enter email address.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PredictionActivity.this, "PLease enter card holder name.", Toast.LENGTH_SHORT).show();
                }


            }
        });


        dialog.show();
    }

    private void showApplyLifelineDialog(final int correctAnsId, final boolean isStream) {

        final Dialog dialog = new Dialog(PredictionActivity.this);
        dialog.setContentView(R.layout.custom_apply_lifeline_dialog);
        if (isStream) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        final Button useBtn = dialog.findViewById(R.id.useBtn);
        final ProgressBar progress = dialog.findViewById(R.id.progress);
        CircleImageView closeDialogBtn = dialog.findViewById(R.id.closeDialogBtn);

        closeDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (!isStream) {
                    slidePager();
                }
            }
        });

        useBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isOnline(PredictionActivity.this)) {
                    progress.setVisibility(View.VISIBLE);
                    useBtn.setVisibility(View.GONE);
                    applyLifeLineAPI(correctAnsId, progress, useBtn, dialog, isStream);
                } else {
                    Utility.noInternetError(PredictionActivity.this);
                }
            }
        });

        dialog.show();
    }

    private void showQuizWinnerDialog(String msg, String prize, String JM, final boolean isFinish) {

        final Dialog dialog = new Dialog(PredictionActivity.this);
        dialog.setContentView(R.layout.custom_win_dialog);
        dialog.setCancelable(false);

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        final TextView descTV = dialog.findViewById(R.id.descTV),
                okBTN = dialog.findViewById(R.id.okBTN);

        descTV.setText(msg + prize + JM);

        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isFinish) {
                    finish();
                }
            }
        });

        dialog.show();
    }

    private void purchaseDiamondAPI(String stripeId, double amount, int qty, String email, String cardName, String cardNumber,
                                    int expYear, int expMonth, String cvc, final ProgressBar progress,
                                    final CustomTextViewBold payNowBtn, final Dialog dialog, final boolean isVideo) {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<ChargePurchaseDiamondsPaymentResponse> purchaseDiamond = retroInterface.purchaseDiamond(uId, stripeId, amount,
                qty, "", email, cardName, cardNumber, expYear, expMonth, cvc);
        purchaseDiamond.enqueue(new Callback<ChargePurchaseDiamondsPaymentResponse>() {
            @Override
            public void onResponse(Call<ChargePurchaseDiamondsPaymentResponse> call, Response<ChargePurchaseDiamondsPaymentResponse> response) {
                progress.setVisibility(View.GONE);
                payNowBtn.setVisibility(View.VISIBLE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    if (response.body().getResponseData().getPaymentStatus().equalsIgnoreCase("succeeded")) {
                        dialog.dismiss();
                        Utility.getProfileDetails();
                        availableDiamondTV.setText(response.body().getResponseData().getAvailableDiamonds() + "");
                        availableStreamDiamondTV.setText(response.body().getResponseData().getAvailableDiamonds() + "");
                        successDialog("Life line purchased successfully.",
                                response.body().getResponseData().getReceiptUrl(), isVideo, true);
//                        Toast.makeText(PredictionActivity.this, "Purchased successfully.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PredictionActivity.this, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ChargePurchaseDiamondsPaymentResponse> call, Throwable t) {
                Log.e("failure", t.getMessage());
                progress.setVisibility(View.GONE);
                payNowBtn.setVisibility(View.VISIBLE);
            }
        });

    }

    private void showReminderDialog() {


        final Dialog dialog = new Dialog(PredictionActivity.this);
        dialog.setContentView(R.layout.custom_reminder_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        final CustomTextViewBold cancelBTN = dialog.findViewById(R.id.cancelBTN),
                cnfrmBTN = dialog.findViewById(R.id.cnfrmBTN);

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        cnfrmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizDateTime != null) {
                    dialog.dismiss();
                    reminderBTN.setVisibility(View.GONE);

//                //Get current timezone
//                values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
//                Log.e("TAG", "Timezone retrieved=>" + TimeZone.getDefault().getID());
//                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
//                Log.e("TAG", "Uri returned=>" + uri.toString());
//                // get the event ID that is the last element in the Uri
//                long eventID = Long.parseLong(uri.getLastPathSegment());
//
//
//                ContentValues reminders = new ContentValues();
//                reminders.put(CalendarContract.Reminders.EVENT_ID, eventID);
//                reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
//                reminders.put(CalendarContract.Reminders.MINUTES, 1);
//
//                Uri uri2 = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);

                    Calendar quizCalendar = Calendar.getInstance();
                    quizCalendar.setTime(quizDateTime);
                    Intent intent = new Intent(getBaseContext(), AlarmReceiver.class)
                            .putExtra("time", quizDateTime);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                    if (Build.VERSION.SDK_INT < 23) {
                        if (Build.VERSION.SDK_INT >= 19) {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, quizCalendar.getTimeInMillis() - (5 * 60 * 1000), pendingIntent);
                        } else {
                            alarmManager.set(AlarmManager.RTC_WAKEUP, quizCalendar.getTimeInMillis() - (5 * 60 * 1000), pendingIntent);
                        }
                    } else {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, quizCalendar.getTimeInMillis() - (5 * 60 * 1000), pendingIntent);
                    }

                    mPreferenceSettings.setReminder(true);
                    Toast.makeText(PredictionActivity.this, "Reminder set", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PredictionActivity.this, "Something went wrong, Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void showEndorsementDialog() {


        final Dialog dialog = new Dialog(PredictionActivity.this);
        dialog.setContentView(R.layout.custom_endorse_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        final CustomTextViewBold cancelBTN = dialog.findViewById(R.id.cancelBTN),
                cnfrmBTN = dialog.findViewById(R.id.cnfrmBTN);
        final CustomEditText refferalCodeEditText = dialog.findViewById(R.id.refferalCodeEditText);
        final ProgressBar progress = dialog.findViewById(R.id.progress);
        CustomTextView endorseCountTV = dialog.findViewById(R.id.endorseCountTV);

        endorseCountTV.setText("Your endorse count : " + mPreferenceSettings.getUserDetails().getResponseData().getEndorseCount());

        if (mPreferenceSettings.getUserDetails().getResponseData().isEnableEndorse()) {
            cnfrmBTN.setAlpha(0.5f);
        } else {
            cnfrmBTN.setAlpha(1f);
        }

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        cnfrmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ref = refferalCodeEditText.getText().toString().trim();
                if (!mPreferenceSettings.getUserDetails().getResponseData().isEnableEndorse()) {
                    if (ref.length() > 0) {
                        if (!ref.equalsIgnoreCase(mPreferenceSettings.getUserDetails().getResponseData().getReferCode())) {
                            if (Utility.isOnline(PredictionActivity.this)) {
                                applyEndorseAPI(ref, dialog, progress, cnfrmBTN);
                            } else {
                                Utility.noInternetError(PredictionActivity.this);
                            }
                        } else {
                            Toast.makeText(PredictionActivity.this, "Please enter other referral code.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PredictionActivity.this, "Please enter referral code", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PredictionActivity.this, "You can endorse one time only.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void applyEndorseAPI(final String refCode, final Dialog dialog, final ProgressBar progress,
                                 final CustomTextViewBold cnfrmBTN) {

        progress.setVisibility(View.VISIBLE);
        cnfrmBTN.setVisibility(View.GONE);

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<ApplyEndorseResponse> applyEndorse = retroInterface.applyEndorse(uId, refCode);
        applyEndorse.enqueue(new Callback<ApplyEndorseResponse>() {
            @Override
            public void onResponse(Call<ApplyEndorseResponse> call, Response<ApplyEndorseResponse> response) {
                progress.setVisibility(View.GONE);
                cnfrmBTN.setVisibility(View.VISIBLE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    Utility.getProfileDetails();
                    dialog.dismiss();
                } else {
                    Toast.makeText(PredictionActivity.this, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApplyEndorseResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                cnfrmBTN.setVisibility(View.VISIBLE);
                dialog.dismiss();
                Toast.makeText(PredictionActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showCounterDialog() {

        final Animation zic = AnimationUtils.loadAnimation(PredictionActivity.this, R.anim.zoom_in_counter);

        final Dialog dialog = new Dialog(PredictionActivity.this);
        dialog.setContentView(R.layout.custom_counter_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        final CustomTextViewBold counterTV = dialog.findViewById(R.id.counterTV);
        final Timer mTimer2 = new Timer();
        final Handler mTimerHandler2 = new Handler();
        TimerTask mTt2 = new TimerTask() {
            public void run() {
                mTimerHandler2.post(new Runnable() {
                    public void run() {
                        //TODO

                        countSec -= 1;

                        if (countSec == 3) {
                            counterTV.setText("Get");

                        } else if (countSec == 2) {
                            counterTV.setText("Set");
                        } else if (countSec == 1) {
                            counterTV.setText("Go");
                        } else {
                            counterTV.setText(String.valueOf(countSec));
                        }
                        counterTV.startAnimation(zic);
                        if (countSec == 0) {
                            if (mTimer2 != null) {
                                mTimer2.cancel();
                                mTimer2.purge();
                            }

                            quizPLayer.setLooping(true);
                            quizPLayer.start();
                            dialog.dismiss();
                            if (Utility.isOnline(PredictionActivity.this)) {
                                quizbackgroundRl.setVisibility(View.GONE);
                                videoViewRL.setVisibility(View.GONE);
                                predictionViewPager.setVisibility(View.VISIBLE);
                                startTimer();
                                nextBtn.setVisibility(View.GONE);
                                predictionAdapter = new PredictionAdapter(PredictionActivity.this, quizList, mainLL);
                                predictionViewPager.setAdapter(predictionAdapter);
                                predictionViewPager.setCurrentItem(0);
                                currentPage = 0;
//                                setCheckBox();
                                predictionAdapter.notifyDataSetChanged();

                                questionTV.setText("Q. " + quizList.get(0).getQuestion());


                                NUM_PAGES = quizList.size();
                                if (currentPage == (NUM_PAGES - 1)) {
                                    nextBtn.setText("DONE");
                                }
                                countTV.setText((currentPage + 1) + "/" + NUM_PAGES);
                            } else {
                                Utility.noInternetError(PredictionActivity.this);
                            }
                        }
                    }
                });
            }
        };

        mTimer2.schedule(mTt2, 0, 1000);

        dialog.show();
    }

    private void claimPrizeAPI(final Dialog dialog, final CustomTextViewBold claimBtn, final ProgressBar claimLoader) {
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<SaveQuizAnswerResponse> claimPrize = retroInterface.claimQuizPrize(uId, quizgameuserattendid);
        claimPrize.enqueue(new Callback<SaveQuizAnswerResponse>() {
            @Override
            public void onResponse(Call<SaveQuizAnswerResponse> call, Response<SaveQuizAnswerResponse> response) {
                claimLoader.setVisibility(View.GONE);
                claimBtn.setVisibility(View.VISIBLE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    dialog.dismiss();
                    finish();
                } else {
                    Toast.makeText(PredictionActivity.this, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SaveQuizAnswerResponse> call, Throwable t) {
                Toast.makeText(PredictionActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                claimLoader.setVisibility(View.GONE);
                claimBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void saveAnswerAPI() {
//        int curPos = predictionViewPager.getCurrentItem();
//        Prediction.ResponseData object = predictionArrayList.get(curPos);
//        stopTimer();
//        loaderTwo.setVisibility(View.VISIBLE);
//        nextBtn.setVisibility(View.GONE);
//        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
//        Call<SaveAnswer> saveAnswer = retroInterface.saveAnswer(uId, object.getID(), ansid, anstxt);
//        saveAnswer.enqueue(new Callback<SaveAnswer>() {
//            @Override
//            public void onResponse(Call<SaveAnswer> call, Response<SaveAnswer> response) {
//                loaderTwo.setVisibility(View.GONE);
//                nextBtn.setVisibility(View.VISIBLE);
//                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
//                    slidePager();
//                } else {
//                    loaderTwo.setVisibility(View.GONE);
//                    nextBtn.setVisibility(View.GONE);
//                    alertDialog(response.body().getResponseMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SaveAnswer> call, Throwable t) {
//                loaderTwo.setVisibility(View.GONE);
//                nextBtn.setVisibility(View.GONE);
//                alertDialog(t.getMessage());
//            }
//        });

    }

    private void getNextQuizDateTimeAPI() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GetNextQuizGameDateTimeResponse> getQuizDateTime = retroInterface.getNextQuizGameDateTime(countryCodeValue);
        getQuizDateTime.enqueue(new Callback<GetNextQuizGameDateTimeResponse>() {
            @Override
            public void onResponse(Call<GetNextQuizGameDateTimeResponse> call, final Response<GetNextQuizGameDateTimeResponse> response) {
                playLoader.setVisibility(View.GONE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    if (response.body().getResponseData() != null) {
                        isMegaQuiz = response.body().getResponseData().isMegaQuiz();
                        prizeTV.setVisibility(View.VISIBLE);

                        mPreferenceSettings.setQuizPrize((long) response.body().getResponseData().getQuizGamePrize());
                        quizTimeLL.setVisibility(View.VISIBLE);
                        final String dateStr = response.body().getResponseData().getReleaseDateUTCString();
                        Log.e("UTC", dateStr);
                        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mm a", Locale.US);
                        df.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date utcDate = null;
                        try {
                            utcDate = df.parse(dateStr);
                        } catch (ParseException e) {
                            Log.e("exc", e.getMessage());
                        }
                        Log.e("utcDate", utcDate.toString());
                        SimpleDateFormat ndf = new SimpleDateFormat("dd MMM yyyy - hh:mm a", Locale.US);
                        ndf.setTimeZone(TimeZone.getDefault());
                        String formattedDate = ndf.format(utcDate);
                        try {
                            utcDate = ndf.parse(formattedDate);
                        } catch (ParseException e) {
                            Log.e("exc", e.getMessage());
                        }
                        quizDateTime = utcDate;
                        startCountDownTimer(utcDate, ndf);

                        Log.e("LOCAL", formattedDate);
                        quizTimeTV.setText(formattedDate.replace("am", "AM").replace("pm", "PM"));


//                        Calendar alarmcalendar = Calendar.getInstance();
//                        alarmcalendar.setTime(date);
//
//                        Intent intent = new Intent(PredictionActivity.this, AlarmReceiver.class);
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                                PredictionActivity.this, 234324243, intent, 0);
//                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                        alarmManager.set(AlarmManager.RTC_WAKEUP, (alarmcalendar.getTimeInMillis() - 300000), pendingIntent);


                        if (!response.body().getResponseData().getQuizGameType().equalsIgnoreCase("LIVESTREAM")) {
                            prizeTV.setText("$ " + Utility.offline_quiz_winner_prize + " JM every winner");
                            Calendar calendar = Calendar.getInstance();
                            String date1 = ndf.format(calendar.getTime());
                            try {
                                final Date date2 = ndf.parse(date1),
                                        date3 = ndf.parse(formattedDate);
                                if (date2.before(date3)) {
                                    if (!mPreferenceSettings.getReminder()) {
                                        reminderBTN.setVisibility(View.VISIBLE);
                                    } else {
                                        reminderBTN.setVisibility(View.GONE);
                                    }
                                    playNowBtn.setVisibility(View.GONE);
                                    setPlayButton(ndf, formattedDate);

                                } else {
                                    mPreferenceSettings.setReminder(false);
                                    stopCounrDownTimer();
                                    reminderBTN.setVisibility(View.GONE);
                                    quizTimeLL.setVisibility(View.GONE);
                                    countDownLL.setVisibility(View.GONE);
                                    playNowBtn.setText("Play Now");
                                    playNowBtn.setVisibility(View.VISIBLE);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (response.body().getResponseData().isMegaQuiz()) {
                                prizeTV.setText("$ " + (int) response.body().getResponseData().getQuizGamePrize() + " JM" +
                                        " ( Round : " + response.body().getResponseData().getRoundNo() + " )");
                            } else {
                                if (response.body().getResponseData().getDividedByRule() == 0) {
                                    prizeTV.setText("$ " + (int) response.body().getResponseData().getQuizGamePrize() + " JM");
                                } else if (response.body().getResponseData().getDividedByRule() == 1) {
                                    prizeTV.setText("$ " + (int) response.body().getResponseData().getQuizGamePrize());
                                }
                            }

                            databaseReference.child("MovieCashMillionaire").child("QuizAvailable").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (response.body().getResponseData().getID() == currentquizid) {
                                        playNowBtn.setVisibility(View.VISIBLE);
                                        playLoader.setVisibility(View.GONE);
                                        if (Boolean.parseBoolean(dataSnapshot.getValue().toString())) {
                                            mPreferenceSettings.setReminder(false);
                                            stopCounrDownTimer();
                                            quizTimeLL.setVisibility(View.GONE);
                                            countDownLL.setVisibility(View.GONE);
                                            playNowBtn.setText("Play Now");
                                            playNowBtn.setVisibility(View.VISIBLE);
                                            reminderBTN.setVisibility(View.GONE);
                                            if (mTimer1 != null) {
                                                mTimer1.cancel();
                                                mTimer1.purge();
                                            }
                                        } else {
                                            if (!mPreferenceSettings.getReminder()) {
                                                reminderBTN.setVisibility(View.VISIBLE);
                                            } else {
                                                reminderBTN.setVisibility(View.GONE);
                                            }
                                            playNowBtn.setVisibility(View.GONE);


                                        }
                                    }
                                    if (!Boolean.parseBoolean(dataSnapshot.getValue().toString())) {
                                        playNowBtn.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    } else {
                        quizTimeLL.setVisibility(View.VISIBLE);
                        quizTimeTV.setText("Not available");
                    }
                } else {
                    Toast.makeText(PredictionActivity.this, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetNextQuizGameDateTimeResponse> call, Throwable t) {
                Toast.makeText(PredictionActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startCountDownTimer(final Date date, final SimpleDateFormat ndf) {
        countDownTImer = new Timer();
        countDownTImerTask = new TimerTask() {
            @Override
            public void run() {
                countDownHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Date current = null;
                        try {
                            current = ndf.parse(ndf.format(Calendar.getInstance().getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long difference = date.getTime() - current.getTime();

                        if (difference <= 0) {
                            stopCounrDownTimer();
                            countDownLL.setVisibility(View.GONE);
                        } else {
                            if (countDownLL.getVisibility() != View.VISIBLE) {
                                countDownLL.setVisibility(View.VISIBLE);
                            }
                        }

                        int days, hours, mins, seconds;
                        long secondsInMilli = 1000;
                        long minutesInMilli = secondsInMilli * 60;
                        long hoursInMilli = minutesInMilli * 60;
                        long daysInMilli = hoursInMilli * 24;

                        difference = difference - 60000;

                        days = (int) (difference / daysInMilli);
                        difference = difference % daysInMilli;

                        hours = (int) (difference / hoursInMilli);
                        difference = difference % hoursInMilli;

                        mins = (int) (difference / minutesInMilli);
                        difference = difference % minutesInMilli;

                        seconds = 60 - Calendar.getInstance().get(Calendar.SECOND);

                        Log.e("======= diff", date + " --- " + current + "====>" + days + ":" + hours + ":" + mins + ":" + seconds);
                        if (days > 0) {
                            daysLL.setVisibility(View.VISIBLE);
                            if (days < 10) {
                                daysTV.setText("0" + days);
                            } else {
                                daysTV.setText(days + "");
                            }
                        } else {
                            daysLL.setVisibility(View.GONE);
                        }
                        if (hours < 10) {
                            hoursTV.setText("0" + hours);
                        } else {
                            hoursTV.setText(hours + "");
                        }
                        if (mins < 10) {

                            minsTV.setText("0" + mins);

                        } else {
                            minsTV.setText(mins + "");
                        }
                        if (seconds < 10) {
                            secTV.setText("0" + seconds);
                        } else {
                            secTV.setText(seconds + "");
                        }

                        if (days == 0 && hours == 0 && mins <= 5) {
                            if (reminderBTN.getVisibility() == View.VISIBLE) {
                                reminderBTN.setVisibility(View.GONE);
                            }
                        }

                    }
                });
            }
        };

        countDownTImer.schedule(countDownTImerTask, 0, 1000);
    }

    private void stopCounrDownTimer() {

        if (countDownTImer != null) {
            countDownTImer.cancel();
            countDownTImer.purge();
        }

    }

    private void setPlayButton(final SimpleDateFormat ndf, final String formattedDate) {
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        //TODO
                        Calendar calendar = Calendar.getInstance();
                        String date1 = ndf.format(calendar.getTime());
                        Log.e("timerDate", new SimpleDateFormat("dd MMM yyyy - hh:mm:ss a", Locale.US).format(calendar.getTime()));
                        try {
                            Date date2 = ndf.parse(date1),
                                    date3 = ndf.parse(formattedDate);
                            if (date2.before(date3)) {

                            } else {
                                quizTimeLL.setVisibility(View.GONE);
                                playNowBtn.setText("Play Now");
                                playNowBtn.setVisibility(View.VISIBLE);
                                reminderBTN.setVisibility(View.GONE);
                                if (mTimer1 != null) {
                                    mTimer1.cancel();
                                    mTimer1.purge();
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 0, 1000);

    }

    ValueEventListener storyAvailableListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot != null && Boolean.parseBoolean(dataSnapshot.getValue().toString())) {
                Log.e("storyListener", dataSnapshot.getValue().toString() + "---storyUID: " + storyUserId + "---uID: " + uId);
                if (storyUserId > 0 && storyUserId != uId) {
                    Log.e("story", "available");
                    isStoryAvailable = true;
                    getLatestResourceUriForStory();
                }
            } else {

                Log.e("story", "gone");
                isStoryAvailable = false;
                videoSurfaceRL.setClickable(false);
                storySurfaceRL.setClickable(false);

                if (videoSurfaceView != null) {
                    videoSurfaceView.setBackground(null);
                }
                storySurfaceRL.setVisibility(View.GONE);
                if (storySurfaceView != null) {
                    storySurfaceView.setVisibility(View.GONE);
                }

                if (multiAnchor) {
                    videoSurfaceRL.setLayoutParams(multiViewParams1);
                    videoSurfaceRL2.setLayoutParams(multiViewParams2);
                } else {
                    videoSurfaceRL.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    videoSurfaceRL2.setVisibility(View.GONE);
                }
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener storyUserIdListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot != null) {
                storyUserId = Integer.parseInt(dataSnapshot.getValue().toString());
                Log.e("storyUserId", storyUserId + "");
                if (storyUserId == uId) {
                    viewAllTV.setVisibility(View.GONE);
                    showStoryBTN.setVisibility(View.VISIBLE);

                    streamSurfaceRL.setVisibility(View.VISIBLE);
                    streamSurfaceRL.setLayoutParams(smallViewParams);

                    videoSurfaceRL.setClickable(false);
                    streamSurfaceRL.setClickable(true);

                    if (!multiAnchor) {
                        videoSurfaceRL.setLayoutParams(mainVideoParams);

                    } else {
                        videoSurfaceRL.setLayoutParams(multiViewParams1);
                        videoSurfaceRL2.setLayoutParams(multiViewParams2);
                    }
                    streamSurfaceView = findViewById(R.id.streamSurfaceView);
                    streamSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    streamSurfaceView.setVisibility(View.VISIBLE);
                    streamSurfaceView.setBackground(getResources().getDrawable(R.drawable.circle));

                    mBroadcaster.setCameraSurface(streamSurfaceView);
                    mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
                    mBroadcaster.onActivityResume();
                } else if (storyUserId == 0) {
                    showStoryBTN.setVisibility(View.GONE);
                    streamSurfaceRL.setVisibility(View.GONE);

                    if (videoSurfaceView != null) {
                        videoSurfaceView.setBackground(null);
                    }

                    if (multiAnchor) {
                        videoSurfaceRL.setLayoutParams(multiViewParams1);
                        videoSurfaceRL2.setLayoutParams(multiViewParams2);
                    } else {
                        videoSurfaceRL2.setVisibility(View.GONE);
                        videoSurfaceRL.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener currentQuizIdListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.e("currQuizId", dataSnapshot.getValue().toString());
            currentquizid = Integer.parseInt(dataSnapshot.getValue().toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener adUrlListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.e("quizAdUrl", dataSnapshot.getValue().toString());
            String url = String.valueOf(dataSnapshot.getValue());

            if (url.trim().length() > 0) {
                adAvailable = true;
                if (mBroadcastPlayer != null) {
                    mBroadcastPlayer.setAudioVolume(0.0f);
                }
                if (mBroadcastPlayerForMultiAnchor != null) {
                    mBroadcastPlayerForMultiAnchor.setAudioVolume(0.0f);
                }
                if (mBroadcastPlayerForStory != null) {
                    mBroadcastPlayerForStory.setAudioVolume(0.0f);
                }
                parentRL.setVisibility(View.GONE);

                adRL.setVisibility(View.VISIBLE);
                adVideoView.setVisibility(View.VISIBLE);
                adTV.setVisibility(View.VISIBLE);
                loadingAdTV.setVisibility(View.VISIBLE);
                adVideoView.setBackgroundResource(R.color.grey);
                adVideoView.setVideoPath(url);
                adVideoView.setZOrderOnTop(true);
                adVideoView.start();

            } else {
                adAvailable = false;
                if (mBroadcastPlayer != null) {
                    mBroadcastPlayer.setAudioVolume(1.0f);
                }
                if (mBroadcastPlayerForMultiAnchor != null) {
                    mBroadcastPlayerForMultiAnchor.setAudioVolume(1.0f);
                }
                if (mBroadcastPlayerForStory != null) {
                    mBroadcastPlayerForStory.setAudioVolume(1.0f);
                }
                parentRL.setVisibility(View.VISIBLE);
                adRL.setVisibility(View.GONE);
                adVideoView.setVisibility(View.GONE);
                loadingAdTV.setVisibility(View.GONE);
                adVideoView.pause();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    ValueEventListener multiAnchorListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot != null && Boolean.parseBoolean(dataSnapshot.getValue().toString())) {

                Log.e("multiAnchor", "available");
                multiAnchor = true;
                getLatestResourceUriForMultiAnchor();
            } else {
                Log.e("multiAnchor", "not_available");
                multiAnchor = false;
                videoSurfaceRL2.setVisibility(View.GONE);
                if (videoSurfaceView2 != null) {
                    videoSurfaceView2.setVisibility(View.GONE);
                }
                if (storySurfaceRL.getVisibility() == View.VISIBLE ||
                        streamSurfaceRL.getVisibility() == View.VISIBLE) {
                    videoSurfaceRL.setLayoutParams(mainVideoParams);
                    videoSurfaceRL.setClickable(false);
                    if (videoSurfaceView != null) {
                        videoSurfaceView.setBackground(null);
                    }
                    if (storySurfaceRL.getVisibility() == View.VISIBLE) {
                        storySurfaceRL.setLayoutParams(smallViewParams);
                        storySurfaceRL.setClickable(true);
                        storySurfaceView.setBackground(getResources().getDrawable(R.drawable.circle));
                    }
                    if (streamSurfaceRL.getVisibility() == View.VISIBLE) {
                        streamSurfaceRL.setLayoutParams(smallViewParams);
                        streamSurfaceRL.setClickable(true);
                        streamSurfaceView.setBackground(getResources().getDrawable(R.drawable.circle));
                    }

                } else {
                    videoSurfaceRL.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener questionEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot != null && Integer.parseInt(dataSnapshot.getValue().toString()) > 0) {
                if (!quizStarted) {
                    final int queNumber = Integer.parseInt(dataSnapshot.getValue().toString());
                    if (queNumber <= quizList.size()) {
                        quePlayer.start();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            vibrator.vibrate(500);
                        }
                        questionLL.setVisibility(View.VISIBLE);
                        Utility.enableAllView(questionLL);
                        questionLL.startAnimation(sid);
                        streamQueTV.setText(quizList.get(queNumber - 1).getQuestion());
                        quizQuestionId = quizList.get(queNumber - 1).getID();
                        isRight = false;
                        ansid = 0;
                        ansPos = 10;

                        setOptions(quizList.get(queNumber - 1).getAnswersList().size(), queNumber - 1);

                        ansOneRB.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ansOneRB.setBackgroundResource(R.drawable.checked_answer);
                                Utility.disableAllView(questionLL);
                                ansid = quizList.get(queNumber - 1).getAnswersList().get(0).getID();
                                isRight = quizList.get(queNumber - 1).getAnswersList().get(0).isRight();
                                ansPos = 0;
                            }
                        });

                        ansTwoRB.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ansTwoRB.setBackgroundResource(R.drawable.checked_answer);
                                Utility.disableAllView(questionLL);
                                ansid = quizList.get(queNumber - 1).getAnswersList().get(1).getID();
                                isRight = quizList.get(queNumber - 1).getAnswersList().get(1).isRight();
                                ansPos = 1;
                            }
                        });

                        ansThreeRB.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ansThreeRB.setBackgroundResource(R.drawable.checked_answer);
                                Utility.disableAllView(questionLL);
                                ansid = quizList.get(queNumber - 1).getAnswersList().get(2).getID();
                                isRight = quizList.get(queNumber - 1).getAnswersList().get(2).isRight();
                                ansPos = 2;
                            }
                        });

                        ansFourRB.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ansFourRB.setBackgroundResource(R.drawable.checked_answer);
                                Utility.disableAllView(questionLL);
                                ansid = quizList.get(queNumber - 1).getAnswersList().get(3).getID();
                                isRight = quizList.get(queNumber - 1).getAnswersList().get(3).isRight();
                                ansPos = 3;
                            }
                        });

                        ansFiveRB.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ansFiveRB.setBackgroundResource(R.drawable.checked_answer);
                                Utility.disableAllView(questionLL);
                                ansid = quizList.get(queNumber - 1).getAnswersList().get(4).getID();
                                isRight = quizList.get(queNumber - 1).getAnswersList().get(4).isRight();
                                ansPos = 4;
                            }
                        });

                        startStreamTimer(sod, queNumber - 1);
                    }
                } else {
                    quizStarted = false;
                }
            } else {
                questionLL.startAnimation(sod);
                questionLL.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void getQuizAPI() {

        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<QuizResponse> getQuiz = retroInterface.getQuizList(uId, countryCodeValue, currentquizid);
        getQuiz.enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                databaseReference.child("MovieCashMillionaire").child("CurrentQuizID").removeEventListener(currentQuizIdListener);
                playLoader.setVisibility(View.GONE);
                playNowBtn.setVisibility(View.VISIBLE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    dividePrize = response.body().getResponseData().getDividedByRule();
                    quizList = new ArrayList<>();
                    quizList = response.body().getResponseData().getQuestionList();
                    if (quizList != null && quizList.size() > 0) {
                        databaseReference.child("MovieCashMillionaire").child("IsStartQuizGame").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                quizStarted = (boolean) dataSnapshot.getValue();
                                databaseReference.child("MovieCashMillionaire").child("IsStartQuizGame").removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        countTV.setText("MOVIECASH MILLIONAIRE");
                        quizGameId = response.body().getResponseData().getID();
                        gameLevel = response.body().getResponseData().getQuizGameLevel();
                        if (response.body().getResponseData().getQuizGameType().equalsIgnoreCase("VIDEO")) {

                            streamRL.setVisibility(View.GONE);

                            if (quizList != null && quizList.size() > 0) {
                                if (homePlayer != null) {
                                    homePlayer.stop();
                                }
                                quizQuestionId = response.body().getResponseData().getQuestionList().get(0).getID();
                                showCounterDialog();
                            }
                        } else {

                            databaseReference.child("MovieCashMillionaire").child("StoryUserId").addValueEventListener(storyUserIdListener);
                            databaseReference.child("MovieCashMillionaire").child("StoryAvailable").addValueEventListener(storyAvailableListener);
                            databaseReference.child("MovieCashMillionaire").child("MultiAnchor").addValueEventListener(multiAnchorListener);
                            databaseReference.child("MovieCashMillionaire").child("QuizAdUrl").addValueEventListener(adUrlListener);

                            quizbackgroundRl.setVisibility(View.GONE);
                            videoViewRL.setVisibility(View.GONE);
                            streamRL.setVisibility(View.VISIBLE);
                            if (homePlayer != null) {
                                homePlayer.stop();
                            }
                            availableStreamDiamondTV.setText(mPreferenceSettings.getUserDetails().getResponseData().getAvailableDiamonds() + "");
                            getLatestResourceUri();


                            databaseReference.child("MovieCashMillionaire").child("QuizQuestionNumber").addValueEventListener(questionEventListener);

                        }
                    } else {
                        alertDialog("No quiz found. Please wait for new one and try again.", true);
                    }
                } else {
                    alertDialog("No quiz found. Please wait for new one and try again.", true);
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                playLoader.setVisibility(View.GONE);
                playNowBtn.setVisibility(View.VISIBLE);
                alertDialog(t.getMessage(), true);
            }
        });

    }

    void getLatestResourceUri() {

        Request request = new Request.Builder()
                .url("https://api.bambuser.com/broadcasts")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Authorization", "Bearer " + BAMBUSER_API_KEY)
                .get()
                .build();

        mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(final okhttp3.Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Http exception: ", e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(final okhttp3.Call call, final okhttp3.Response response) throws IOException {
                String body = response.body().string();
                Log.e("Stream response: ", body);
                String resourceUri = null;
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray results = json.getJSONArray("results");
                    BroadcastResult.Result latestBroadcast = null;
                    ArrayList<BroadcastResult.Result> resultList = new ArrayList<>();
                    for (int i = 0; i < results.length(); i++) {
                        if (results.optJSONObject(i).optString("type").equalsIgnoreCase("live")) {
                            BroadcastResult.Result result = new BroadcastResult.Result(results.optJSONObject(i).optString("id"),
                                    results.optJSONObject(i).optString("resourceUri"),
                                    results.optJSONObject(i).optString("type"));
                            resultList.add(result);
                        }
                    }
                    if (streamId != null && streamId.length() > 0) {
                        for (int i = 0; i < resultList.size(); i++) {
                            if (streamId.equalsIgnoreCase(resultList.get(i).getId())) {
                                latestBroadcast = resultList.get(i);
                                break;
                            }
                        }
                    } else {

                        if (multiAnchor) {
                            if (isStoryAvailable) {
                                latestBroadcast = resultList.get(2);
                            } else {
                                latestBroadcast = resultList.get(1);
                            }
                        } else {
                            if (isStoryAvailable) {
                                latestBroadcast = resultList.get(1);
                            } else {
                                latestBroadcast = resultList.get(0);
                            }
                        }
                    }
                    streamId = latestBroadcast.getId();
                    Log.e("streamId", streamId);
//                    mPreferenceSettings.setStreamId(streamId);
                    resourceUri = latestBroadcast.getResourceUri();
                } catch (Exception ignored) {
                    Log.e("streamExc-", ignored.getMessage());
                }
                final String uri = resourceUri;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initPlayer(uri);
                    }
                });
            }
        });
    }

    void getLatestResourceUriForStory() {

        Request request = new Request.Builder()
                .url("https://api.bambuser.com/broadcasts")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Authorization", "Bearer " + BAMBUSER_API_KEY)
                .get()
                .build();

        mOkHttpClientForStory.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(final okhttp3.Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Http exception: ", e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(final okhttp3.Call call, final okhttp3.Response response) throws IOException {
                String body = response.body().string();
                Log.e("Story Stream: ", body);
                String resourceUri = null;
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray results = json.getJSONArray("results");
                    BroadcastResult.Result latestBroadcast = null;
                    ArrayList<BroadcastResult.Result> resultList = new ArrayList<>();
                    for (int i = 0; i < results.length(); i++) {
                        if (results.optJSONObject(i).optString("type").equalsIgnoreCase("live")) {
                            BroadcastResult.Result result = new BroadcastResult.Result(results.optJSONObject(i).optString("id"),
                                    results.optJSONObject(i).optString("resourceUri"),
                                    results.optJSONObject(i).optString("type"));
                            resultList.add(result);
                        }
                    }

                    if (storyId != null && storyId.length() > 0) {
                        for (int i = 0; i < resultList.size(); i++) {
                            if (storyId.equalsIgnoreCase(resultList.get(i).getId())) {
                                latestBroadcast = resultList.get(i);
                                break;
                            }
                        }
                    } else {
                        latestBroadcast = resultList.get(0);
                    }
                    storyId = latestBroadcast.getId();
                    Log.e("storyId", storyId);
                    resourceUri = latestBroadcast.getResourceUri();
                } catch (Exception ignored) {
                    Log.e("storyExc-", ignored.getMessage());
                }
                final String uri = resourceUri;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initPlayerForStory(uri);
                    }
                });
            }
        });
    }

    void getLatestResourceUriForMultiAnchor() {

        Request request = new Request.Builder()
                .url("https://api.bambuser.com/broadcasts")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Authorization", "Bearer " + BAMBUSER_API_KEY)
                .get()
                .build();

        mOkHttpClientForMultiAnchor.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(final okhttp3.Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Http exception: ", e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(final okhttp3.Call call, final okhttp3.Response response) throws IOException {
                String body = response.body().string();
                Log.e("MultiAnchor Stream: ", body);
                String resourceUri = null;
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray results = json.getJSONArray("results");
                    BroadcastResult.Result latestBroadcast = null;
                    ArrayList<BroadcastResult.Result> resultList = new ArrayList<>();
                    for (int i = 0; i < results.length(); i++) {
                        if (results.optJSONObject(i).optString("type").equalsIgnoreCase("live")) {
                            BroadcastResult.Result result = new BroadcastResult.Result(results.optJSONObject(i).optString("id"),
                                    results.optJSONObject(i).optString("resourceUri"),
                                    results.optJSONObject(i).optString("type"));
                            resultList.add(result);
                        }
                    }
                    if (multiAnchorId != null && multiAnchorId.length() > 0) {
                        for (int i = 0; i < resultList.size(); i++) {
                            if (multiAnchorId.equalsIgnoreCase(resultList.get(i).getId())) {
                                latestBroadcast = resultList.get(i);
                                break;
                            }
                        }
                    } else {
                        if (isStoryAvailable) {
                            latestBroadcast = resultList.get(1);
                        } else {
                            latestBroadcast = resultList.get(0);
                        }
                    }
                    multiAnchorId = latestBroadcast.getId();
                    Log.e("multiAnchorId", multiAnchorId);
//                    mPreferenceSettings.setMultiAnchorId(multiAnchorId);
                    resourceUri = latestBroadcast.getResourceUri();
                    final String multiUri = resourceUri;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.e("multiANchorUri", multiUri);
                                initPlayerForMultiAnchor(multiUri);
                            } catch (Exception e) {
                                Log.e("exc", e.getMessage());
                            }

                        }
                    });
                } catch (Exception ignored) {
                    Log.e("multiAnchorExc-", ignored.getMessage());
                }

            }
        });
    }

    void initPlayer(String resourceUri) {
        if (resourceUri == null) {
            Log.e("error", "Could not get info about latest broadcast");
            return;
        }
        if (videoSurfaceView == null) {
            // UI no longer active
            return;
        }

        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(this, resourceUri, BAMBUSER_APPLICATION_ID, mBroadcastPlayerObserver);
        mBroadcastPlayer.setSurfaceView(videoSurfaceView);
        mBroadcastPlayer.setAcceptType(BroadcastPlayer.AcceptType.LIVE);
        mBroadcastPlayer.setViewerCountObserver(viewerCountObserver);
        mBroadcastPlayer.load();
    }

    void initPlayerForStory(String resourceUri) {
        if (resourceUri == null) {
            Log.e("error", "Could not get info about latest broadcast for story");
            return;
        }
        if (storySurfaceView == null) {
            // UI no longer active
            return;
        }

        if (mBroadcastPlayerForStory != null)
            mBroadcastPlayerForStory.close();


        if (videoSurfaceView != null) {
            videoSurfaceView.setBackground(null);
        }

        if (storySurfaceView != null) {
            storySurfaceView.setBackground(getResources().getDrawable(R.drawable.circle));
        }

        storySurfaceRL.setVisibility(View.VISIBLE);
        storySurfaceView = findViewById(R.id.storySurfaceView);
        storySurfaceView.setVisibility(View.VISIBLE);
        videoSurfaceRL.setClickable(false);
        storySurfaceRL.setClickable(true);


        if (multiAnchor) {
            videoSurfaceRL.setLayoutParams(multiViewParams1);
            storySurfaceRL.setLayoutParams(smallViewParams);
            videoSurfaceRL2.setLayoutParams(multiViewParams2);
        } else {
            videoSurfaceRL.setLayoutParams(mainVideoParams);
            storySurfaceRL.setLayoutParams(smallViewParams);
        }

        mBroadcastPlayerForStory = new BroadcastPlayer(this, resourceUri, BAMBUSER_APPLICATION_ID, mBroadcastPlayerObserverForStory);
        mBroadcastPlayerForStory.setSurfaceView(storySurfaceView);
        mBroadcastPlayerForStory.setAcceptType(BroadcastPlayer.AcceptType.LIVE);
        mBroadcastPlayerForStory.load();


    }

    void initPlayerForMultiAnchor(String resourceUri) {
        try {
            if (resourceUri == null) {
                Log.e("error", "Could not get info about latest broadcast for story");
                return;
            }
            if (videoSurfaceView2 == null) {
                // UI no longer active
                return;
            }

            if (mBroadcastPlayerForMultiAnchor != null)
                mBroadcastPlayerForMultiAnchor.close();

            videoSurfaceRL2.setVisibility(View.VISIBLE);
            videoSurfaceRL.setLayoutParams(multiViewParams1);
            videoSurfaceRL2.setLayoutParams(multiViewParams2);


            if (videoSurfaceView != null) {
                videoSurfaceView.setBackground(null);
            }

            if (videoSurfaceView2 != null) {
                videoSurfaceView2.setBackground(null);
            }


            videoSurfaceView2 = findViewById(R.id.videoSurfaceView2);
            videoSurfaceView2.setVisibility(View.VISIBLE);
            videoSurfaceRL.setClickable(false);
            videoSurfaceRL2.setClickable(false);

            mBroadcastPlayerForMultiAnchor = new BroadcastPlayer(this, resourceUri, BAMBUSER_APPLICATION_ID, mBroadcastPlayerObserverForMultiAnchor);
            mBroadcastPlayerForMultiAnchor.setSurfaceView(videoSurfaceView2);
            mBroadcastPlayerForMultiAnchor.setAcceptType(BroadcastPlayer.AcceptType.LIVE);
            mBroadcastPlayerForMultiAnchor.load();
        } catch (Exception e) {
            Log.e("multiException", e.getMessage());
        }

    }

//    private void getPredictionQuestionsListAPI() {
//        loader.setVisibility(View.VISIBLE);
//        descTV.setVisibility(View.GONE);
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        String countryCodeValue = tm.getNetworkCountryIso();
//        Log.e("cCode", countryCodeValue);
//        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
//        Call<Prediction> predList = retroInterface.getPredictionList(uId, countryCodeValue.toUpperCase());
//        predList.enqueue(new Callback<Prediction>() {
//            @Override
//            public void onResponse(Call<Prediction> call, Response<Prediction> response) {
//                loader.setVisibility(View.GONE);
//                predictionViewPager.setVisibility(View.VISIBLE);
//                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
//
//                    if (response.body().getResponseDataList() != null && response.body().getResponseDataList().size() > 0) {
//                        startTimer();
//                        nextBtn.setVisibility(View.VISIBLE);
//                        predictionArrayList = response.body().getResponseDataList();
//                        PredictionAdapter predictionAdapter = new PredictionAdapter(PredictionActivity.this, predictionArrayList);
//                        predictionViewPager.setAdapter(predictionAdapter);
//                        predictionViewPager.setCurrentItem(0);
//                        currentPage = 0;
//                        predictionAdapter.notifyDataSetChanged();
//
//                        questionTV.setText("Q. " + predictionArrayList.get(0).getQuestion());
//
//
//                        NUM_PAGES = predictionArrayList.size();
//                        if (currentPage == (NUM_PAGES - 1)) {
//                            nextBtn.setText("DONE");
//                        }
//                        countTV.setText((currentPage + 1) + "/" + NUM_PAGES);
//                    } else {
//                        alertDialog("No new questions available.\nPlease wait and try again.");
//                        nextBtn.setVisibility(View.GONE);
//                    }
//                } else {
//                    alertDialog(response.body().getResponseMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Prediction> call, Throwable t) {
//                loader.setVisibility(View.GONE);
//                alertDialog(t.getMessage());
//            }
//        });
//    }

    private void startTimer() {
        timerSec = 10;
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        //TODO
                        if (tickPLayer != null) {
                            tickPLayer.start();
                        }
                        timerSec -= 1;
                        timeLoader.setProgress(timerSec);
                        timerTextView.setText(String.valueOf(timerSec));
                        Log.e("seconds", String.valueOf(timerSec));

                        if (timerSec == 0) {
                            Utility.disableAllView(mainLL);
                            stopTimer();
                            saveQuizAnswerAPI();
//                            saveAnswerAPI();
                        }
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 0, 1000);
    }


    private void stopTimer() {
        if (mTimer1 != null) {
            timerSec = 10;
            mTimer1.cancel();
            mTimer1.purge();
        }
    }


    private void startStreamTimer(final Animation sod, final int pos) {
//        timerSec = 10;
//        streamTimerRL.setVisibility(View.VISIBLE);
//        mTimer1 = new Timer();
//        mTt1 = new TimerTask() {
//            public void run() {
//                mTimerHandler.post(new Runnable() {
//                    public void run() {
//                        //TODO
//
//                        timerSec -= 1;
//                        streamTimeLoader.setProgress(timerSec);
//                        streamTimerTextView.setText(String.valueOf(timerSec));
//                        Log.e("streamSeconds", String.valueOf(timerSec));
//
//                        if (timerSec == 0) {
//                            questionLL.startAnimation(sod);
//                            questionLL.setVisibility(View.GONE);
//                            streamTimerRL.setVisibility(View.GONE);
//                            stopStreamTimer();
//                            saveStreamAnswerAPI(pos);
//                        }
//                    }
//                });
//            }
//        };
//
//        mTimer1.schedule(mTt1, 0, 1000);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (Integer.parseInt(dataSnapshot.getValue().toString()) > 0) {
                    streamTimer = Integer.parseInt(dataSnapshot.getValue().toString()) - 1;
                    streamTimeLoader.setProgress(streamTimer);
                    streamTimerTextView.setText(String.valueOf(streamTimer));
                    Log.e("streamSeconds", String.valueOf(streamTimer));
                    if (tickPLayer != null) {
                        tickPLayer.start();
                    }

                    if (streamTimer == 0) {
                        stopListener();
                        questionLL.startAnimation(sod);
                        questionLL.setVisibility(View.GONE);
                        streamTimerRL.setVisibility(View.GONE);
                        saveStreamAnswerAPI(pos);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        streamTimerRL.setVisibility(View.VISIBLE);
        streamTimeLoader.setProgress(timerSec);
        streamTimerTextView.setText(String.valueOf(timerSec));
        databaseReference.child("MovieCashMillionaire").child("QuestionCountDown").addValueEventListener(valueEventListener);

    }

    private void stopListener() {
        databaseReference.child("MovieCashMillionaire").child("QuestionCountDown").removeEventListener(valueEventListener);
    }


    private void stopStreamTimer() {
        if (mTimer1 != null) {
            timerSec = 10;
            mTimer1.cancel();
            mTimer1.purge();
        }
    }

    public void alertDialog(String msg, final boolean isFinish) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PredictionActivity.this, R.style.AlertDialogTheme);
        builder1.setTitle("Oops");
        builder1.setIcon(R.drawable.logo);
        builder1.setMessage(msg);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if (isFinish) {
                            finish();
                            if (homePlayer != null) {
                                homePlayer.stop();
                            }
                            if (quizPLayer != null) {
                                quizPLayer.stop();
                            }
                        } else {
                            slidePager();
                        }
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void showBuzzerDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PredictionActivity.this, R.style.AlertDialogTheme);
        builder1.setTitle("Golden Buzzer");
        builder1.setIcon(R.drawable.golden_buzzer);
        builder1.setMessage("Are you sure to apply for golden buzzer?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Utility.isOnline(PredictionActivity.this)) {
                            applyGoldenBuzzerAPI(dialog);
                        } else {
                            Utility.noInternetError(PredictionActivity.this);
                        }
                    }
                });
        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void applyGoldenBuzzerAPI(final DialogInterface dialog) {

        final ProgressDialog progress1 = new ProgressDialog(PredictionActivity.this);
        progress1.setMessage("Loading...");
        progress1.show();

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<ApplyGoldanBuzzerResponse> buzzer = retroInterface.applyGoldenBuzzer(uId);
        buzzer.enqueue(new Callback<ApplyGoldanBuzzerResponse>() {
            @Override
            public void onResponse(Call<ApplyGoldanBuzzerResponse> call, Response<ApplyGoldanBuzzerResponse> response) {
                progress1.dismiss();
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    dialog.dismiss();
                    Utility.getProfileDetails();
                    buzzerBtn.setVisibility(View.GONE);
                } else {
                    Toast.makeText(PredictionActivity.this, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApplyGoldanBuzzerResponse> call, Throwable t) {
                Toast.makeText(PredictionActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
                progress1.dismiss();
            }
        });
    }

    public void successDialog(String msg, final String receiptURL, final boolean isVideo, final boolean isPurchase) {
        final Dialog dialog = new Dialog(PredictionActivity.this);
        dialog.setContentView(R.layout.custom_success_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        TextView okBTN = dialog.findViewById(R.id.okBTN),
                receiptBTN = dialog.findViewById(R.id.receiptBTN),
                descTV = dialog.findViewById(R.id.descTV);

        descTV.setText(msg);
        if (receiptURL != null && receiptURL.length() > 0) {
            receiptBTN.setVisibility(View.VISIBLE);
        } else {
            receiptBTN.setVisibility(View.GONE);
        }
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
                if (isVideo) {
                    showApplyLifelineDialog(correctAnsId, false);
                } else {
                    if (isPurchase) {
                        if (!addLifeLineClicked) {
                            showApplyLifelineDialog(correctAnsId, true);
                        } else {
                            addLifeLineClicked = false;
                        }
                    }
                }
            }
        });

        dialog.show();
    }

    public void receiptDialog(final String receiptURL) {
        final Dialog dialog = new Dialog(PredictionActivity.this);
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

//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.e("on___Stop", "onStop");
//        if (predictionArrayList != null && predictionArrayList.size() > 0) {
//            saveAnswerAPI();
//            saveQuizAnswerAPI();
//        }
//    }


    private void saveQuizAnswerAPI() {
        loaderTwo.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Log.e("saveQuizAnswerAPI", "saveQuizAnswerAPI");
        Call<SaveQuizAnswerResponse> saveAns = retroInterface.saveQuizAnswer(uId, quizGameId, quizQuestionId, ansid, gameLevel, isRight);
        saveAns.enqueue(new Callback<SaveQuizAnswerResponse>() {
            @Override
            public void onResponse(Call<SaveQuizAnswerResponse> call, Response<SaveQuizAnswerResponse> response) {
                loaderTwo.setVisibility(View.GONE);
                Utility.enableAllView(mainLL);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {

                    quizgameuserattendid = response.body().getResponseID();
                    if (isRight) {
                        slidePager();
                    } else {
                        String answer = "";
                        for (int i = 0; i < quizList.get(currentPage).getAnswersList().size(); i++) {
                            if (quizList.get(currentPage).getAnswersList().get(i).isRight()) {
                                answer = quizList.get(currentPage).getAnswersList().get(i).getAnswer();
                                correctAnsId = quizList.get(currentPage).getAnswersList().get(i).getID();
                                break;
                            }
                        }
                        if (currentPage < (quizList.size() - 1)) {
                            if (lifeUsedCount < 2) {
                                if (Integer.parseInt(availableDiamondTV.getText().toString()) > 0) {
                                    showApplyLifelineDialog(correctAnsId, false);
                                } else {
                                    showPurchaseKeyDialog(true);
                                }
                            } else {
                                alertDialog("It's wrong answer.\nCorrect answer is " + answer + "\nBetter luck next time.", false);
                            }
                        } else {
                            if (quizPLayer != null) {
                                quizPLayer.stop();
                            }
                            alertDialog("It's wrong answer.\nCorrect answer is " + answer + "\nBetter luck next time.", true);
                        }
                    }


                } else {
                    Toast.makeText(PredictionActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaveQuizAnswerResponse> call, Throwable t) {
                loaderTwo.setVisibility(View.GONE);
                Utility.enableAllView(mainLL);
            }
        });
    }

    private void saveStreamAnswerAPI(final int pos) {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Log.e("saveStreamAnswerAPI", "saveStreamAnswerAPI");
        Call<SaveQuizAnswerResponse> saveAns = retroInterface.saveQuizAnswer(uId, quizGameId, quizQuestionId, ansid, gameLevel, isRight);
        saveAns.enqueue(new Callback<SaveQuizAnswerResponse>() {
            @Override
            public void onResponse(Call<SaveQuizAnswerResponse> call, Response<SaveQuizAnswerResponse> response) {

                Utility.enableAllView(mainLL);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getAnswerCountAPI(pos);
                        }
                    }, 3000);

                } else {
                    Toast.makeText(PredictionActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaveQuizAnswerResponse> call, Throwable t) {
                loaderTwo.setVisibility(View.GONE);
                Utility.enableAllView(mainLL);
            }
        });
    }

    private void getAnswerCountAPI(final int pos) {
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<AnswerCountResponse> getAnsCount = retroInterface.getAnswerCount(quizQuestionId);
        getAnsCount.enqueue(new Callback<AnswerCountResponse>() {
            @Override
            public void onResponse(Call<AnswerCountResponse> call, Response<AnswerCountResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {

                    questionLL.setVisibility(View.VISIBLE);
                    questionLL.startAnimation(sid);
                    Utility.disableAllView(questionLL);
                    ansCountLL.setVisibility(View.VISIBLE);
                    long correctAns = response.body().getResponseData().getTotalRightAnswer(),
                            wrongAns = response.body().getResponseData().getTotalWrongAnswer(),
                            totalAns = correctAns + wrongAns;
                    correctCountPB.setMax((int) totalAns);
                    wrongCountPB.setMax((int) totalAns);

                    correctCountPB.setProgress((int) correctAns);
                    correctCountTV.setText(String.valueOf(correctAns));

                    wrongCountPB.setProgress((int) wrongAns);
                    wrongCountTV.setText(String.valueOf(wrongAns));

//                    ansOneRB.setChecked(false);
//                    ansTwoRB.setChecked(false);
//                    ansThreeRB.setChecked(false);
//                    ansFourRB.setChecked(false);

                    ansOneRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                    ansTwoRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                    ansThreeRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                    ansFourRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                    ansFiveRB.setBackgroundResource(R.drawable.unchecked_stream_answer);

                    if (isRight) {
                        switch (ansPos) {
                            case 0:
                                ansOneRB.setBackgroundResource(R.drawable.correct_answer);
                                break;
                            case 1:
                                ansTwoRB.setBackgroundResource(R.drawable.correct_answer);
                                break;
                            case 2:
                                ansThreeRB.setBackgroundResource(R.drawable.correct_answer);
                                break;
                            case 3:
                                ansFourRB.setBackgroundResource(R.drawable.correct_answer);
                                break;
                            case 4:
                                ansFiveRB.setBackgroundResource(R.drawable.correct_answer);
                                break;
                        }
                    } else {

                        switch (ansPos) {
                            case 0:
                                ansOneRB.setBackgroundResource(R.drawable.wrong_answer);
                                break;
                            case 1:
                                ansTwoRB.setBackgroundResource(R.drawable.wrong_answer);
                                break;
                            case 2:
                                ansThreeRB.setBackgroundResource(R.drawable.wrong_answer);
                                break;
                            case 3:
                                ansFourRB.setBackgroundResource(R.drawable.wrong_answer);
                                break;
                            case 4:
                                ansFiveRB.setBackgroundResource(R.drawable.wrong_answer);
                                break;
                        }
                        for (int i = 0; i < quizList.get(pos).getAnswersList().size(); i++) {
                            if (quizList.get(pos).getAnswersList().get(i).isRight()) {
                                switch (i) {
                                    case 0:
                                        ansOneRB.setBackgroundResource(R.drawable.correct_answer);
                                        break;
                                    case 1:
                                        ansTwoRB.setBackgroundResource(R.drawable.correct_answer);
                                        break;
                                    case 2:
                                        ansThreeRB.setBackgroundResource(R.drawable.correct_answer);
                                        break;
                                    case 3:
                                        ansFourRB.setBackgroundResource(R.drawable.correct_answer);
                                        break;
                                    case 4:
                                        ansFiveRB.setBackgroundResource(R.drawable.correct_answer);
                                        break;
                                }
                                correctAnsId = quizList.get(pos).getAnswersList().get(i).getID();
                                break;
                            }
                        }

                    }


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            questionLL.startAnimation(sod);
                            questionLL.setVisibility(View.GONE);
                            Utility.enableAllView(questionLL);
                            ansCountLL.setVisibility(View.GONE);

                            ansOneRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                            ansTwoRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                            ansThreeRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                            ansFourRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                            ansFiveRB.setBackgroundResource(R.drawable.unchecked_stream_answer);


                            if (!isRight) {
                                if (pos < (quizList.size() - 1)) {
                                    if (lifeUsedCount < 2) {
                                        if (Integer.parseInt(availableDiamondTV.getText().toString()) > 0) {
                                            showApplyLifelineDialog(correctAnsId, true);
                                        } else {
                                            showPurchaseKeyDialog(false);
                                        }
                                    } else {
                                        Toast.makeText(PredictionActivity.this, "You can continue to win JM rewards.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

//                            if (!isRight) {
//                                if (pos < (quizList.size() - 1)) {
//                                    if (lifeUsedCount < 2) {
//                                        if (Integer.parseInt(availableDiamondTV.getText().toString()) > 0) {
//                                            showApplyLifelineDialog(correctAnsId, true);
//                                        }
//                                    }
//                                }
//                            }

                            if (pos == (quizList.size() - 1)) {
                                if (quizPLayer != null) {
                                    quizPLayer.stop();
                                }
                                databaseReference.child("MovieCashMillionaire").child("QuizQuestionNumber").removeEventListener(questionEventListener);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        getTopWinnersListAPI(quizList.get(pos).getQuizGameID());

                                    }
                                }, 10000);
                            }
                        }
                    }, 4000);


                }
            }

            @Override
            public void onFailure(Call<AnswerCountResponse> call, Throwable t) {

            }
        });

    }

    private void applyLifeLineAPI(int correctAnswerId, final ProgressBar progress, final Button useBtn, final Dialog dialog,
                                  final boolean isStream) {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<ApplyLifeLineResponse> applyLife = retroInterface.applyLifeLine(uId, Utility.life_to_apply, gameLevel,
                quizGameId, quizQuestionId, correctAnswerId);
        applyLife.enqueue(new Callback<ApplyLifeLineResponse>() {
            @Override
            public void onResponse(Call<ApplyLifeLineResponse> call, Response<ApplyLifeLineResponse> response) {
                progress.setVisibility(View.GONE);
                useBtn.setVisibility(View.VISIBLE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    dialog.dismiss();
                    correctAnsId = 0;
                    lifeUsedCount += 1;
                    Utility.getProfileDetails();
                    availableDiamondTV.setText(response.body().getResponseData());
                    if (!isStream) {
                        slidePager();
                    }
                } else {
                    Toast.makeText(PredictionActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApplyLifeLineResponse> call, Throwable t) {
                finish();
                progress.setVisibility(View.GONE);
                useBtn.setVisibility(View.VISIBLE);
                Toast.makeText(PredictionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setOptions(int size, int position) {

        switch (size) {
            case 1:
                ansOneRB.setVisibility(View.VISIBLE);
                ansTwoRB.setVisibility(View.GONE);
                ansThreeRB.setVisibility(View.GONE);
                ansFourRB.setVisibility(View.GONE);
                ansFiveRB.setVisibility(View.GONE);

                ansOneRB.setText(quizList.get(position).getAnswersList().get(0).getAnswer());

                ansOneRB.setBackgroundResource(R.drawable.unchecked_stream_answer);

                break;
            case 2:
                ansOneRB.setVisibility(View.VISIBLE);
                ansTwoRB.setVisibility(View.VISIBLE);
                ansThreeRB.setVisibility(View.GONE);
                ansFourRB.setVisibility(View.GONE);
                ansFiveRB.setVisibility(View.GONE);

                ansOneRB.setText(quizList.get(position).getAnswersList().get(0).getAnswer());
                ansTwoRB.setText(quizList.get(position).getAnswersList().get(1).getAnswer());

                ansOneRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                ansTwoRB.setBackgroundResource(R.drawable.unchecked_stream_answer);

                break;
            case 3:
                ansOneRB.setVisibility(View.VISIBLE);
                ansTwoRB.setVisibility(View.VISIBLE);
                ansThreeRB.setVisibility(View.VISIBLE);
                ansFourRB.setVisibility(View.GONE);
                ansFiveRB.setVisibility(View.GONE);

                ansOneRB.setText(quizList.get(position).getAnswersList().get(0).getAnswer());
                ansTwoRB.setText(quizList.get(position).getAnswersList().get(1).getAnswer());
                ansThreeRB.setText(quizList.get(position).getAnswersList().get(2).getAnswer());

                ansOneRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                ansTwoRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                ansThreeRB.setBackgroundResource(R.drawable.unchecked_stream_answer);

                break;
            case 4:
                ansOneRB.setVisibility(View.VISIBLE);
                ansTwoRB.setVisibility(View.VISIBLE);
                ansThreeRB.setVisibility(View.VISIBLE);
                ansFourRB.setVisibility(View.VISIBLE);
                ansFiveRB.setVisibility(View.GONE);

                ansOneRB.setText(quizList.get(position).getAnswersList().get(0).getAnswer());
                ansTwoRB.setText(quizList.get(position).getAnswersList().get(1).getAnswer());
                ansThreeRB.setText(quizList.get(position).getAnswersList().get(2).getAnswer());
                ansFourRB.setText(quizList.get(position).getAnswersList().get(3).getAnswer());

                ansOneRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                ansTwoRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                ansThreeRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                ansFourRB.setBackgroundResource(R.drawable.unchecked_stream_answer);

                break;
            case 5:
                ansOneRB.setVisibility(View.VISIBLE);
                ansTwoRB.setVisibility(View.VISIBLE);
                ansThreeRB.setVisibility(View.VISIBLE);
                ansFourRB.setVisibility(View.VISIBLE);
                ansFiveRB.setVisibility(View.VISIBLE);

                ansOneRB.setText(quizList.get(position).getAnswersList().get(0).getAnswer());
                ansTwoRB.setText(quizList.get(position).getAnswersList().get(1).getAnswer());
                ansThreeRB.setText(quizList.get(position).getAnswersList().get(2).getAnswer());
                ansFourRB.setText(quizList.get(position).getAnswersList().get(3).getAnswer());
                ansFiveRB.setText(quizList.get(position).getAnswersList().get(4).getAnswer());

                ansOneRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                ansTwoRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                ansThreeRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                ansFourRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                ansFiveRB.setBackgroundResource(R.drawable.unchecked_stream_answer);
                break;
        }

    }

}
