package jonomoneta.juno.moviecash.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import jonomoneta.juno.moviecash.Adapter.AllWinnersAdapter;
import jonomoneta.juno.moviecash.Model.Response.AllWinnerResponse;
import jonomoneta.juno.moviecash.Model.WinnerListItem;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WinnersActivity extends AppCompatActivity {

    private ImageView backBtn;
    private GridView winnersGridView;
    private AVLoadingIndicatorView loader;
    private boolean isLoading = false, hasMoreItems = true;
    private int currPage = 1;
    private ArrayList<WinnerListItem> winnersList;
    private AllWinnersAdapter winnersAdapter;
    private int quizgameid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winners);

        init();
    }

    private void init() {


        quizgameid = getIntent().getIntExtra("quizgameid", 0);

        backBtn = findViewById(R.id.backBtn);
        winnersGridView = findViewById(R.id.winnersGridView);
        loader = findViewById(R.id.loader);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        winnersList = new ArrayList<>();
//        winnersList.add(new WinnerListItem(0, "Chintan Chevli", ""));
//        winnersList.add(new WinnerListItem(0, "Vishal Pandav", ""));
//        winnersList.add(new WinnerListItem(0, "Vaibhav Narse", ""));
//        winnersList.add(new WinnerListItem(0, "Vishal Pandav", ""));
//        winnersList.add(new WinnerListItem(0, "Vaibhav Narse", ""));
//        winnersList.add(new WinnerListItem(0, "Chintan Chevli", ""));
//        winnersList.add(new WinnerListItem(0, "Vishal Pandav", ""));
//        winnersList.add(new WinnerListItem(0, "Vaibhav Narse", ""));
//        winnersList.add(new WinnerListItem(0, "Vishal Pandav", ""));
//        winnersList.add(new WinnerListItem(0, "Vaibhav Narse", ""));
//        winnersList.add(new WinnerListItem(0, "Chintan Chevli", ""));
//        winnersList.add(new WinnerListItem(0, "Vishal Pandav", ""));
//        winnersList.add(new WinnerListItem(0, "Vaibhav Narse", ""));
//        winnersList.add(new WinnerListItem(0, "Vishal Pandav", ""));
//        winnersList.add(new WinnerListItem(0, "Vaibhav Narse", ""));
        winnersAdapter = new AllWinnersAdapter(WinnersActivity.this, winnersList);
        winnersGridView.setAdapter(winnersAdapter);
        winnersAdapter.notifyDataSetChanged();

        if (Utility.isOnline(WinnersActivity.this)) {
            getAllWinnersAPI();
        } else {
            Utility.noInternetError(WinnersActivity.this);
        }

        winnersGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0) {
                    int lastVisibleItem = firstVisibleItem + visibleItemCount;
                    if (!isLoading && hasMoreItems && (lastVisibleItem == totalItemCount)) {
                        //load more items--
                        if (Utility.isOnline(WinnersActivity.this)) {
                            isLoading = true;
                            currPage += 1;
                            getAllWinnersAPI();

                        }
                    }
                }
            }
        });
    }

    private void getAllWinnersAPI() {
        loader.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<AllWinnerResponse> getWinnerList = retroInterface.getQuizWinnersList(quizgameid, currPage);
        getWinnerList.enqueue(new Callback<AllWinnerResponse>() {
            @Override
            public void onResponse(Call<AllWinnerResponse> call, Response<AllWinnerResponse> response) {
                loader.setVisibility(View.GONE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    isLoading = false;
                    if (response.body().getResponseDataList() != null &&
                            response.body().getResponseDataList().size() > 0) {
                        hasMoreItems = true;
                        winnersList.addAll(response.body().getResponseDataList());

                    } else {
                        hasMoreItems = false;
                        if (currPage == 1) {
                            Toast.makeText(WinnersActivity.this, "No winners found for this quiz.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                    winnersAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<AllWinnerResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(WinnersActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
