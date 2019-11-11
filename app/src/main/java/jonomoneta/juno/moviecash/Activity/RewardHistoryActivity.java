package jonomoneta.juno.moviecash.Activity;

import androidx.appcompat.app.AppCompatActivity;
import jonomoneta.juno.moviecash.Adapter.HistoryListAdapter;
import jonomoneta.juno.moviecash.Adapter.RewardHistoryListAdapter;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.LottoHistoryResponse;
import jonomoneta.juno.moviecash.Model.Response.RewardHistoryResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class RewardHistoryActivity extends AppCompatActivity {

    private GridView historyGV;
    RewardHistoryListAdapter historyListAdapter;
    private ArrayList<RewardHistoryResponse.ResponseData> historyList;
    private CustomTextView noDataTV;
    private AVLoadingIndicatorView loader;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_history);

        init();
    }

    private void init() {

        historyGV = findViewById(R.id.historyGV);
        noDataTV = findViewById(R.id.noDataTV);
        loader = findViewById(R.id.loader);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        historyList = new ArrayList<>();
        historyListAdapter = new RewardHistoryListAdapter(RewardHistoryActivity.this, historyList);
        historyGV.setAdapter(historyListAdapter);
        historyListAdapter.notifyDataSetChanged();

        if (Utility.isOnline(RewardHistoryActivity.this)) {
            getHistoryListAPI();
        } else {
            Utility.noInternetError(RewardHistoryActivity.this);
            noDataTV.setVisibility(View.VISIBLE);
        }
    }

    private void getHistoryListAPI() {
        loader.setVisibility(View.VISIBLE);
        int uid = MyApplication.getInstance().getPreferenceSettings().getUserDetails().getResponseData().getID();
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<RewardHistoryResponse> getHistory = retroInterface.getRewardHistory(uid);
        getHistory.enqueue(new Callback<RewardHistoryResponse>() {
            @Override
            public void onResponse(Call<RewardHistoryResponse> call, Response<RewardHistoryResponse> response) {
                loader.setVisibility(View.GONE);
                try {
                    if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {

//                    releasedMovieArrayList = response.body().getResponseDataArrayList();
                        if (response.body().getResponseDataArrayList() != null && response.body().getResponseDataArrayList().size() > 0) {
                            historyList.addAll(response.body().getResponseDataArrayList());
                            if (historyList != null && historyList.size() > 0) {
                                noDataTV.setVisibility(View.GONE);
                            } else {
                                noDataTV.setVisibility(View.VISIBLE);
                            }
                        }
                        historyListAdapter.notifyDataSetChanged();
                    } else {
                        noDataTV.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.e("exc", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<RewardHistoryResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });

    }
}
