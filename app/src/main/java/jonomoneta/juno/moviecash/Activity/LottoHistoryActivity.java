package jonomoneta.juno.moviecash.Activity;

import androidx.appcompat.app.AppCompatActivity;
import jonomoneta.juno.moviecash.Adapter.HistoryListAdapter;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.LottoHistoryResponse;
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

public class LottoHistoryActivity extends AppCompatActivity {

    private GridView historyGV;
    HistoryListAdapter historyListAdapter;
    private static boolean isLoading = false, hasMoreItems = true;
    public static int currPage;
    private ArrayList<LottoHistoryResponse.ResponseData> historyList;
    private CustomTextView noDataTV;
    private AVLoadingIndicatorView loader;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lotto_history);

        init();
    }

    private void init() {
        currPage = 1;
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
        historyListAdapter = new HistoryListAdapter(LottoHistoryActivity.this, historyList);
        historyGV.setAdapter(historyListAdapter);
        historyListAdapter.notifyDataSetChanged();

        if (Utility.isOnline(LottoHistoryActivity.this)) {
            getHistoryListAPI();
        } else {
            Utility.noInternetError(LottoHistoryActivity.this);
            noDataTV.setVisibility(View.VISIBLE);
        }

        historyGV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0) {
                    int lastVisibleItem = firstVisibleItem + visibleItemCount;
                    if (!isLoading && hasMoreItems && (lastVisibleItem == totalItemCount)) {
                        //load more items--
                        if (Utility.isOnline(LottoHistoryActivity.this)) {
                            isLoading = true;
                            currPage += 1;

                            getHistoryListAPI();

                        }
                    }
                }
            }
        });
    }

    private void getHistoryListAPI() {
        loader.setVisibility(View.VISIBLE);
        int uid = MyApplication.getInstance().getPreferenceSettings().getUserDetails().getResponseData().getID();
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<LottoHistoryResponse> getHistory = retroInterface.gettLottoClubSelectedNoListByUser(uid, currPage, 10);
        getHistory.enqueue(new Callback<LottoHistoryResponse>() {
            @Override
            public void onResponse(Call<LottoHistoryResponse> call, Response<LottoHistoryResponse> response) {
                loader.setVisibility(View.GONE);
                try {
                    if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {

                        isLoading = false;
//                    releasedMovieArrayList = response.body().getResponseDataArrayList();
                        if (response.body().getResponseDataArrayList() != null && response.body().getResponseDataArrayList().size() > 0) {
                            hasMoreItems = true;
                            historyList.addAll(response.body().getResponseDataArrayList());
                            if (historyList != null && historyList.size() > 0) {
                                noDataTV.setVisibility(View.GONE);
                            } else {
                                noDataTV.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (currPage == 1) {
                                noDataTV.setVisibility(View.VISIBLE);
                            }
                            hasMoreItems = false;
                        }
                        if (currPage == 1) {
                            historyGV.smoothScrollToPosition(0);
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
            public void onFailure(Call<LottoHistoryResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });

    }
}
