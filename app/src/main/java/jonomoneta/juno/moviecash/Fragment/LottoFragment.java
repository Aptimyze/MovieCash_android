package jonomoneta.juno.moviecash.Fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.view.CardMultilineWidget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import jonomoneta.juno.moviecash.Activity.LottoHistoryActivity;
import jonomoneta.juno.moviecash.Activity.LottoResultActivity;
import jonomoneta.juno.moviecash.Activity.SelectLottoNumbersActivity;
import jonomoneta.juno.moviecash.Activity.WebViewActivity;
import jonomoneta.juno.moviecash.Adapter.LottoRewardPlanAdapter;
import jonomoneta.juno.moviecash.CustomEditText;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.Model.Response.GenerateLottoClubResultResponse;
import jonomoneta.juno.moviecash.Model.Response.GetCurrentLottoClubSelectedNoResponse;
import jonomoneta.juno.moviecash.Model.Response.GetQuizGamePaymentPlanResponse;
import jonomoneta.juno.moviecash.Model.Response.GettLottoClubRewardPlansResponse;
import jonomoneta.juno.moviecash.Model.Response.LottoClubFreeSubscribeUserResponse;
import jonomoneta.juno.moviecash.Model.Response.WorldsBestSubscriptionResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LottoFragment extends Fragment {

    private CustomTextViewBold playNowBtn, subsTv, hoursTV, minsTV, secTV;
    private CustomTextView retryBtn;
    private LinearLayout selectedNumLL, subscribeLL, countDownLL;
    private ImageView subsIv;
    private RecyclerView rewardPlanRV;
    private ArrayList<GettLottoClubRewardPlansResponse.ResponseData> rewardPlanList;
    private TextView no1TV, no2TV, no3TV, no4TV, no5TV, luckyNoTV;
    private ProgressBar loader;
    private String stripeCusID = "", email, lottoSubStartDate, lottoSubEndDate;
    private PreferenceSettings mPreferenceSettings;
    int uid;
    private Timer countDownTImer;
    private TimerTask countDownTImerTask;
    private Handler countDownHandler = new Handler();
    private CustomTextView waitTV, termsTV, privacyTV;
    DatabaseReference databaseReference;
    private ImageView historyBTN;
    Date resultDateUtc = null, lottoCreatedDate = null;
    long createdTimeInMillis;
    String resultDateStr;
    Response<GetCurrentLottoClubSelectedNoResponse> lottoResponse;
    private double lotto_subs_amount = 0;

    public LottoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lotto, container, false);


        init(view);

        return view;
    }

    ValueEventListener lottoResultListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
            if (dataSnapshot != null) {
                Log.e("resAvailable", dataSnapshot.toString());
                if (Boolean.parseBoolean(dataSnapshot.getValue().toString())) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                if (isAdded()) {
                                    databaseReference.child("LottoAtm").child("LottoResultAvailable").removeEventListener(lottoResultListener);
                                    waitTV.setVisibility(View.GONE);
                                    stopCounrDownTimer();
                                    startActivityForResult(new Intent(getActivity(), LottoResultActivity.class), 2);
                                }

                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 1000);
                }

//                else {
////                    waitTV.setVisibility(View.VISIBLE);
//                }
//                else if (!Boolean.parseBoolean(dataSnapshot.getValue().toString())) {
//
//                    if (lottoResponse.body().getResponseData().isResultDeclare()) {
//                        waitTV.setVisibility(View.GONE);
//                        playNowBtn.setVisibility(View.VISIBLE);
//                        stopCounrDownTimer();
//                        if (!lottoResponse.body().getResponseData().isResultChecked()) {
//
//                            playNowBtn.setText("Check Result");
//
//                        } else {
//                            selectedNumLL.setVisibility(View.GONE);
//                            playNowBtn.setText("Play Now");
//                        }
//                    } else {
//                        waitTV.setVisibility(View.VISIBLE);
//                    }
//                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

//    ValueEventListener resultDeclaredListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//            if (dataSnapshot != null) {
//                Log.e("resDeclared", dataSnapshot.toString());
//                if (Boolean.parseBoolean(dataSnapshot.getValue().toString())) {
//                    databaseReference.child("LottoAtm").child("ResultDeclared").removeEventListener(resultDeclaredListener);
//                    waitTV.setVisibility(View.GONE);
//                    stopCounrDownTimer();
//                    Log.e("listener", "Listener");
//                    playNowBtn.setText("Check Result");
//                    playNowBtn.setVisibility(View.VISIBLE);
//                } else {
//                    waitTV.setVisibility(View.VISIBLE);
//                }
//            }
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//    };

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isVisibleToUser) {
                    if (Utility.isOnline(getActivity())) {
//                        drop_animation_view.startAnimation();
                        retryBtn.setVisibility(View.GONE);
                        getPlanPriceAPI();
                        databaseReference.child("LottoAtm").child("LottoResultTime").addValueEventListener(getResultDateForPlayNow);
                        if (mPreferenceSettings.getUserDetails().getResponseData() != null) {
                            uid = mPreferenceSettings.getUserDetails().getResponseData().getID();
                            if (mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID() != null &&
                                    mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID().length() > 0) {
                                stripeCusID = mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID();
                            }
                            if (mPreferenceSettings.getUserDetails().getResponseData().getEmail() != null &&
                                    mPreferenceSettings.getUserDetails().getResponseData().getEmail().length() > 0) {
                                email = mPreferenceSettings.getUserDetails().getResponseData().getEmail();
                            }
                            if (mPreferenceSettings.getUserDetails().getResponseData().getLottoClubSubscriptionEndDate() != null) {
                                lottoSubEndDate = mPreferenceSettings.getUserDetails().getResponseData().getLottoClubSubscriptionEndDate().split("T")[0];
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                                SimpleDateFormat ndf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                ndf.setTimeZone(TimeZone.getDefault());


                                try {
                                    Date endDate = sdf.parse(lottoSubEndDate),
                                            todayDate = ndf.parse(ndf.format(Calendar.getInstance().getTime()));

                                    String endDateLocal = ndf.format(endDate);

                                    try {
                                        endDate = ndf.parse(endDateLocal);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (todayDate.after(endDate)) {
                                        subsTv.setText("SUBSCRIBE");
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
                        getCurrentLottoNumberPAI();
                    } else {
                        loader.setVisibility(View.GONE);
                        selectedNumLL.setVisibility(View.GONE);
                        retryBtn.setVisibility(View.VISIBLE);
                        Utility.noInternetError(getActivity());
                    }
                }
            }
        }, 1000);
    }

    private void init(View view) {
        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        termsTV = view.findViewById(R.id.termsTV);
        privacyTV = view.findViewById(R.id.privacyTV);
        subscribeLL = view.findViewById(R.id.subscribeLL);
        subsTv = view.findViewById(R.id.subsTv);
        subsIv = view.findViewById(R.id.subsIv);
        loader = view.findViewById(R.id.loader);
        playNowBtn = view.findViewById(R.id.playNowBtn);
        selectedNumLL = view.findViewById(R.id.selectedNumLL);
        no1TV = view.findViewById(R.id.no1TV);
        no2TV = view.findViewById(R.id.no2TV);
        no3TV = view.findViewById(R.id.no3TV);
        no4TV = view.findViewById(R.id.no4TV);
        no5TV = view.findViewById(R.id.no5TV);
        luckyNoTV = view.findViewById(R.id.luckyNoTV);
        hoursTV = view.findViewById(R.id.hoursTV);
        minsTV = view.findViewById(R.id.minsTV);
        secTV = view.findViewById(R.id.secTV);
        countDownLL = view.findViewById(R.id.countDownLL);
        waitTV = view.findViewById(R.id.waitTV);
        historyBTN = view.findViewById(R.id.historyBTN);
        retryBtn = view.findViewById(R.id.retryBtn);

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isOnline(getActivity())) {
                    retryBtn.setVisibility(View.GONE);
                    getPlanPriceAPI();
                    databaseReference.child("LottoAtm").child("LottoResultTime").addValueEventListener(getResultDateForPlayNow);
                    if (mPreferenceSettings.getUserDetails().getResponseData() != null) {
                        uid = mPreferenceSettings.getUserDetails().getResponseData().getID();
                        if (mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID() != null &&
                                mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID().length() > 0) {
                            stripeCusID = mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID();
                        }
                        if (mPreferenceSettings.getUserDetails().getResponseData().getEmail() != null &&
                                mPreferenceSettings.getUserDetails().getResponseData().getEmail().length() > 0) {
                            email = mPreferenceSettings.getUserDetails().getResponseData().getEmail();
                        }
                        if (mPreferenceSettings.getUserDetails().getResponseData().getLottoClubSubscriptionEndDate() != null) {
                            lottoSubEndDate = mPreferenceSettings.getUserDetails().getResponseData().getLottoClubSubscriptionEndDate().split("T")[0];
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                            SimpleDateFormat ndf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            ndf.setTimeZone(TimeZone.getDefault());


                            try {
                                Date endDate = sdf.parse(lottoSubEndDate),
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
                    subsTv.setText("FREE SUBSCRIBE");
                    getCurrentLottoNumberPAI();
                } else {
                    loader.setVisibility(View.GONE);
                    selectedNumLL.setVisibility(View.GONE);
                    retryBtn.setVisibility(View.VISIBLE);
                    Utility.noInternetError(getActivity());
                }
            }
        });

        privacyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Utility.privacy_link_lotto;
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("url", url)
                        .putExtra("title", "Privacy"));
            }
        });

        termsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Utility.terms_link_lotto;
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("url", url)
                        .putExtra("title", "Terms"));
            }
        });


        historyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LottoHistoryActivity.class));
            }
        });

        playNowBtn.setText("Play Now");


        playNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPreferenceSettings.getUserDetails().getResponseData().getLottoClubSubscriptionEndDate() != null) {
                    lottoSubEndDate = mPreferenceSettings.getUserDetails().getResponseData().getLottoClubSubscriptionEndDate().split("T")[0];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    SimpleDateFormat ndf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    ndf.setTimeZone(TimeZone.getDefault());


                    try {
                        Date endDate = sdf.parse(lottoSubEndDate),
                                todayDate = ndf.parse(ndf.format(Calendar.getInstance().getTime()));

                        String endDateLocal = ndf.format(endDate);

                        try {
                            endDate = ndf.parse(endDateLocal);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (todayDate.after(endDate)) {
                            Toast.makeText(getActivity(), "Please subscribe to play Lotto.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (playNowBtn.getText().toString().trim().equalsIgnoreCase("Play Now")) {

                                SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                                Calendar curCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                                Calendar limitCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

                                try {
                                    Date curTime = utcFormat.parse(utcFormat.format(curCalendar.getTime())),
                                            limitTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(resultDateStr);
                                    limitCal.setTime(limitTime);
                                    limitCal.add(Calendar.MINUTE, -30);
                                    String curUtcStr = utcFormat.format(curTime);
                                    Log.e("curTime", " " + curUtcStr + "\nLimitTime: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(limitCal.getTime()));

                                    Date finalCurDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(curUtcStr);

                                    if (finalCurDate.after(limitCal.getTime())) {
                                        Log.e("curTime", " " + finalCurDate + "\nLimitTime: " + limitCal.getTime());
                                        playNowBtn.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Oops, you are late to participate.\nPlease try after 1 hour.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (Utility.isOnline(getActivity())) {
                                            startActivityForResult(new Intent(getActivity(), SelectLottoNumbersActivity.class), 1);
                                        } else {
                                            Utility.noInternetError(getActivity());
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e("EXC", e.getMessage());
                                }


                            } else if (playNowBtn.getText().toString().trim().equalsIgnoreCase("Check Result")) {
                                if (Utility.isOnline(getActivity())) {
                                    getResultAPI();
                                } else {
                                    Utility.noInternetError(getActivity());
                                }
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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

    }

    private void checkHidePlayButton() {
        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar curCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Calendar limitCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        try {
            Date curTime = utcFormat.parse(utcFormat.format(curCalendar.getTime())),
                    limitTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(resultDateStr);
            limitCal.setTime(limitTime);
            limitCal.add(Calendar.MINUTE, -30);
            String curUtcStr = utcFormat.format(curTime);
            Log.e("curTime", " " + curUtcStr + "\nLimitTime: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(limitCal.getTime()));

            Date finalCurDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(curUtcStr);

            if (finalCurDate.after(limitCal.getTime())) {
                playNowBtn.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("EXC", e.getMessage());
        }
    }

    private void showResultDialog(GenerateLottoClubResultResponse.ResponseData resultData) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_lotto_result_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        CheckBox resNo1CB, resNo2CB, resNo3CB, resNo4CB, resNo5CB, resLuckyNoCB,
                no1CB, no2CB, no3CB, no4CB, no5CB, luckyNoCB;
        CustomTextViewBold rewardTV = dialog.findViewById(R.id.rewardTV),
                okBTN = dialog.findViewById(R.id.okBTN);

        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                selectedNumLL.setVisibility(View.GONE);
                playNowBtn.setText("Play Now");
                playNowBtn.setVisibility(View.VISIBLE);
            }
        });

        resNo1CB = dialog.findViewById(R.id.resNo1CB);
        resNo2CB = dialog.findViewById(R.id.resNo2CB);
        resNo3CB = dialog.findViewById(R.id.resNo3CB);
        resNo4CB = dialog.findViewById(R.id.resNo4CB);
        resNo5CB = dialog.findViewById(R.id.resNo5CB);
        resLuckyNoCB = dialog.findViewById(R.id.resLuckyNoCB);
        no1CB = dialog.findViewById(R.id.no1CB);
        no2CB = dialog.findViewById(R.id.no2CB);
        no3CB = dialog.findViewById(R.id.no3CB);
        no4CB = dialog.findViewById(R.id.no4CB);
        no5CB = dialog.findViewById(R.id.no5CB);
        luckyNoCB = dialog.findViewById(R.id.luckyNoCB);

        no1CB.setText(resultData.getNo1());
        no2CB.setText(resultData.getNo2());
        no3CB.setText(resultData.getNo3());
        no4CB.setText(resultData.getNo4());
        no5CB.setText(resultData.getNo5());
        luckyNoCB.setText(resultData.getLuckyNo());

        String[] result = resultData.getDeclareResultNo().split(",");

        resNo1CB.setText(result[0]);
        resNo2CB.setText(result[1]);
        resNo3CB.setText(result[2]);
        resNo4CB.setText(result[3]);
        resNo5CB.setText(result[4]);
        resLuckyNoCB.setText(result[5]);

        if (resultData.isMatchNo1()) {
            no1CB.setChecked(true);
        } else {
            no1CB.setChecked(false);
        }
        if (resultData.isMatchNo2()) {
            no2CB.setChecked(true);
        } else {
            no2CB.setChecked(false);
        }
        if (resultData.isMatchNo3()) {
            no3CB.setChecked(true);
        } else {
            no3CB.setChecked(false);
        }
        if (resultData.isMatchNo4()) {
            no4CB.setChecked(true);
        } else {
            no4CB.setChecked(false);
        }
        if (resultData.isMatchNo5()) {
            no5CB.setChecked(true);
        } else {
            no5CB.setChecked(false);
        }
        if (resultData.isMatchLuckyNo()) {
            luckyNoCB.setChecked(true);
        } else {
            luckyNoCB.setChecked(false);
        }

        if (resultData.getTotalMatchNoCount() > 0) {
            rewardTV.setText("Congratulations!\nYou have won JM $ " + resultData.getReward() + ".");

        } else {
            rewardTV.setText("Oops!\nBetter luck next time.");
        }


        dialog.show();

    }

    private void getResultAPI() {
        loader.setVisibility(View.VISIBLE);
        playNowBtn.setVisibility(View.GONE);
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GenerateLottoClubResultResponse> getResult = retroInterface.generateLottoClubResult(uid);
        getResult.enqueue(new Callback<GenerateLottoClubResultResponse>() {
            @Override
            public void onResponse(Call<GenerateLottoClubResultResponse> call, Response<GenerateLottoClubResultResponse> response) {
                loader.setVisibility(View.GONE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    if (response.body().getResponseData() != null) {
                        showResultDialog(response.body().getResponseData());
                        Utility.getProfileDetails();
                    }
                }
            }

            @Override
            public void onFailure(Call<GenerateLottoClubResultResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });

    }

    ValueEventListener getResultDateForPlayNow = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot != null) {
                databaseReference.child("LottoAtm").child("LottoResultTime").removeEventListener(getResultDateForPlayNow);
                resultDateStr = dataSnapshot.getValue().toString().replace("T", " ");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener resultDateListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot != null) {
                databaseReference.child("LottoAtm").child("LottoResultTime").removeEventListener(resultDateListener);
                resultDateStr = dataSnapshot.getValue().toString().replace("T", " ");
                if (resultDateStr != null && resultDateStr.length() > 0) {
                    startCountDownTimer();

                }

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void startCountDownTimer() {

        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-M-d HH:mm:ss", Locale.US);
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            resultDateUtc = utcFormat.parse(resultDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm", Locale.US);
        String lottoCreatedDateStr = lottoResponse.body().getResponseData().getCurrentUTCDateTime().replace("T", " ");

        try {
            lottoCreatedDate = utcFormat.parse(lottoCreatedDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        createdTimeInMillis = lottoCreatedDate.getTime();

        countDownTImer = new Timer();
        countDownTImerTask = new TimerTask() {
            @Override
            public void run() {
                countDownHandler.post(new Runnable() {
                    @Override
                    public void run() {


                        Calendar curCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));


                        String curDateString = sdf.format(curCalendar.getTime());
                        Date curDate = null;
                        try {
                            curDate = sdf.parse(curDateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        long difference = resultDateUtc.getTime() - createdTimeInMillis;
                        createdTimeInMillis += 1000;
                        if (difference <= 0) {
                            stopCounrDownTimer();
                            waitTV.setVisibility(View.VISIBLE);
                        } else {
                            if (waitTV.getVisibility() == View.VISIBLE) {
                                waitTV.setVisibility(View.GONE);
                            }
                            if (countDownLL.getVisibility() != View.VISIBLE) {
                                countDownLL.setVisibility(View.VISIBLE);
                            }
                        }

                        int days, hours, mins, seconds;
                        long secondsInMilli = 1000;
                        long minutesInMilli = secondsInMilli * 60;
                        long hoursInMilli = minutesInMilli * 60;
                        long daysInMilli = hoursInMilli * 24;

                        days = (int) (difference / daysInMilli);
                        difference = difference % daysInMilli;

                        hours = (int) (difference / hoursInMilli);
                        difference = difference % hoursInMilli;

                        mins = (int) (difference / minutesInMilli);
                        seconds = 60 - Calendar.getInstance().get(Calendar.SECOND);

                        Log.e("======= diff", resultDateUtc + " --- " + curDate + "====>" + days + ":" + hours + ":" + mins + ":" + seconds);

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


                    }
                });
            }
        };

        countDownTImer.schedule(countDownTImerTask, 0, 1000);
    }

    private void stopCounrDownTimer() {
        countDownLL.setVisibility(View.GONE);
        if (countDownTImer != null) {
            countDownTImer.cancel();
            countDownTImer.purge();
        }

    }

    private void showSubsDetailsDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_subscription_details_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        CustomTextViewBold okBtn = dialog.findViewById(R.id.okBtn),
                startDateTV = dialog.findViewById(R.id.startDateTV),
                endDateTV = dialog.findViewById(R.id.endDateTV);

        lottoSubStartDate = mPreferenceSettings.getUserDetails().getResponseData().getLottoClubSubscriptionStartDate().split("T")[0];
        lottoSubEndDate = mPreferenceSettings.getUserDetails().getResponseData().getLottoClubSubscriptionEndDate().split("T")[0];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat ndf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        try {
            Date endDate = sdf.parse(lottoSubEndDate),
                    startDate = sdf.parse(lottoSubStartDate);
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
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_subscribe_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        CustomTextViewBold subscribeBtn = dialog.findViewById(R.id.subscribeBtn);
        CircleImageView closeBtn = dialog.findViewById(R.id.closeBtn);
        CustomTextView subs_descTV = dialog.findViewById(R.id.subs_descTV);
        final CustomEditText refferalCodeEditText = dialog.findViewById(R.id.refferalCodeEditText);

        refferalCodeEditText.setVisibility(View.GONE);

        if (isPaidSubscription) {
            subs_descTV.setText("- Play lotto daily and have fun.\n- Monthly subscription at just $ " + lotto_subs_amount + ".");
        } else {
            subs_descTV.setText("- Play lotto daily and have fun.\n- Free subscription for one month.");
        }
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isOnline(getActivity())) {
                    dialog.dismiss();
                    if (isPaidSubscription) {
                        if (stripeCusID != null && stripeCusID.length() > 0) {
                            showFilledCardDetailsDialog();
                        } else {
                            showCardDetailDialog();
                        }
                    } else {
                        lottoClubFreeSubscribeUserAPI();
                    }
                } else {
                    Utility.noInternetError(getActivity());
                }

            }
        });

        dialog.show();
    }

    private void lottoClubFreeSubscribeUserAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait.");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<LottoClubFreeSubscribeUserResponse> freeSubscribe = retroInterface.lottoClubFreeSubscribeUser(uid);
        freeSubscribe.enqueue(new Callback<LottoClubFreeSubscribeUserResponse>() {
            @Override
            public void onResponse(Call<LottoClubFreeSubscribeUserResponse> call, Response<LottoClubFreeSubscribeUserResponse> response) {
                progressDialog.dismiss();
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    successDialog("Your one month free subscription is activated.", "");
                    subsTv.setText("SUBSCRIBED");
                    subsIv.setImageResource(R.drawable.ic_correct);
                    Utility.getProfileDetails();
                } else {
                    Toast.makeText(getActivity(), response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LottoClubFreeSubscribeUserResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showFilledCardDetailsDialog() {

        final Dialog dialog = new Dialog(getActivity());
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
                showCardDetailDialog();
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
                double amount;

                amount = lotto_subs_amount;


                payNowBtn.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                stripeCusID = mPreferenceSettings.getUserDetails().getResponseData().getStripeCustomerID();


                subscribeAPI(stripeCusID, amount, email, "", "", 0, 0, "",
                        progress, payNowBtn, dialog);

            }
        });

        dialog.show();
    }

    private void getPlanPriceAPI() {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GetQuizGamePaymentPlanResponse> getPlan = retroInterface.getQuizGamePaymentPlan();
        getPlan.enqueue(new Callback<GetQuizGamePaymentPlanResponse>() {
            @Override
            public void onResponse(Call<GetQuizGamePaymentPlanResponse> call, Response<GetQuizGamePaymentPlanResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {

                    for (int i = 0; i < response.body().getResponseData().size(); i++) {
                        if (response.body().getResponseData().get(i).getPlanName().equalsIgnoreCase("LOTTOSUBSCRITPION")) {
                            lotto_subs_amount = response.body().getResponseData().get(i).getPrice();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetQuizGamePaymentPlanResponse> call, Throwable t) {

            }
        });

    }

    private void showCardDetailDialog() {

        final Dialog dialog = new Dialog(getActivity());
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
                double amount;

                amount = lotto_subs_amount;

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
                                if (Utility.isOnline(getActivity())) {
                                    progress.setVisibility(View.VISIBLE);
                                    payNowBTN.setVisibility(View.GONE);


                                    subscribeAPI("", amount, email, cardName, cardNumber, expYear, expMonth,
                                            cvc, progress, payNowBTN, dialog);


                                } else {
                                    Utility.noInternetError(getActivity());
                                }
                            } else {
                                Toast.makeText(getActivity(), "Invalid card details.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please enter card details.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please enter email address.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter card holder name.", Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                getCurrentLottoNumberPAI();
            }
        } else if (requestCode == 2) {
            if (resultCode == getActivity().RESULT_OK) {
                waitTV.setVisibility(View.GONE);
                Log.e("activityRes", "activityRes");
                playNowBtn.setText("Check Result");
                playNowBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    private void subscribeAPI(String stripeId, double amount, String email, String
            cardName, String cardNumber, int cardExpYear,
                              int cardExpMonth, String cvc, final ProgressBar progress, final CustomTextViewBold payNowBtn,
                              final Dialog dialog) {
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<WorldsBestSubscriptionResponse> subscribe = retroInterface.lottoClubSubscribeUser(uid, stripeId, amount, "",
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
                        Utility.getProfileDetails();
                    } else {
                        Toast.makeText(getActivity(), response.body().getResponseData().getFailureMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WorldsBestSubscriptionResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                payNowBtn.setVisibility(View.VISIBLE);
                dialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void successDialog(String msg, final String receiptURL) {
        final Dialog dialog = new Dialog(getActivity());
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
        final Dialog dialog = new Dialog(getActivity());
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

    private void getLottoPlanAPI() {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GettLottoClubRewardPlansResponse> getPlans = retroInterface.gettLottoClubRewardPlans();
        getPlans.enqueue(new Callback<GettLottoClubRewardPlansResponse>() {
            @Override
            public void onResponse(Call<GettLottoClubRewardPlansResponse> call, Response<GettLottoClubRewardPlansResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    rewardPlanList = new ArrayList<>();
                    rewardPlanList = response.body().getResponseData();

                    showRewardPlanDialog();
                }
            }

            @Override
            public void onFailure(Call<GettLottoClubRewardPlansResponse> call, Throwable t) {

            }
        });
    }

    private void getCurrentLottoNumberPAI() {
        loader.setVisibility(View.VISIBLE);
        selectedNumLL.setVisibility(View.GONE);
        playNowBtn.setVisibility(View.GONE);

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GetCurrentLottoClubSelectedNoResponse> getNumber = retroInterface.getCurrentLottoClubSelectedNo(uid);
        getNumber.enqueue(new Callback<GetCurrentLottoClubSelectedNoResponse>() {
            @Override
            public void onResponse(Call<GetCurrentLottoClubSelectedNoResponse> call, Response<GetCurrentLottoClubSelectedNoResponse> response) {
                lottoResponse = response;
                loader.setVisibility(View.GONE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    if (response.body().getResponseData() != null) {
                        playNowBtn.setVisibility(View.GONE);
                        waitTV.setVisibility(View.GONE);
                        selectedNumLL.setVisibility(View.VISIBLE);
                        no1TV.setText(response.body().getResponseData().getNo1());
                        no2TV.setText(response.body().getResponseData().getNo2());
                        no3TV.setText(response.body().getResponseData().getNo3());
                        no4TV.setText(response.body().getResponseData().getNo4());
                        no5TV.setText(response.body().getResponseData().getNo5());
                        luckyNoTV.setText(response.body().getResponseData().getLuckyNo());

                        stopCounrDownTimer();


                        if (!response.body().getResponseData().isResultDeclare()) {

                            databaseReference.child("LottoAtm").child("LottoResultTime").addValueEventListener(resultDateListener);
                            if (!response.body().getResponseData().isResultChecked()) {
                                if (lottoResultListener != null) {
                                    databaseReference.child("LottoAtm").child("LottoResultAvailable").removeEventListener(lottoResultListener);
                                }
                                databaseReference.child("LottoAtm").child("LottoResultAvailable").addValueEventListener(lottoResultListener);
                            } else {
                                selectedNumLL.setVisibility(View.GONE);
                                playNowBtn.setText("Play Now");
                                playNowBtn.setVisibility(View.VISIBLE);
                            }
                        } else {
                            waitTV.setVisibility(View.GONE);
                            playNowBtn.setVisibility(View.VISIBLE);
                            if (!response.body().getResponseData().isResultChecked()) {

                                playNowBtn.setText("Check Result");

                            } else {
                                selectedNumLL.setVisibility(View.GONE);
                                playNowBtn.setText("Play Now");
                            }
                        }
                    } else {
                        selectedNumLL.setVisibility(View.GONE);
                        playNowBtn.setText("Play Now");
                        playNowBtn.setVisibility(View.VISIBLE);

                        checkHidePlayButton();
                        getLottoPlanAPI();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCurrentLottoClubSelectedNoResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });

    }

    private void showRewardPlanDialog() {
        final Dialog dialog;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            dialog = new Dialog(Objects.requireNonNull(getActivity()));

            dialog.setContentView(R.layout.custom_lotto_plan_dialog);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

            CustomTextViewBold cntinueBTN = dialog.findViewById(R.id.cntinueBTN);

            cntinueBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            rewardPlanRV = dialog.findViewById(R.id.rewardPlanRV);

            rewardPlanRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            rewardPlanRV.setItemAnimator(new DefaultItemAnimator());


            LottoRewardPlanAdapter lottoRewardPlanAdapter = new LottoRewardPlanAdapter(getActivity(), rewardPlanList);
            rewardPlanRV.setAdapter(lottoRewardPlanAdapter);
            lottoRewardPlanAdapter.notifyDataSetChanged();

            dialog.show();
        }
    }

}
