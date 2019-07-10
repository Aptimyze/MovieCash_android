package jonomoneta.juno.moviecash.Activity;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import jonomoneta.juno.moviecash.Adapter.QuizPrizeAdapter;
import jonomoneta.juno.moviecash.Model.Response.QuizPrizeListResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClaimPrizeActivity extends AppCompatActivity {

    private RecyclerView prizeRecyclerView;
    private ArrayList<QuizPrizeListResponse.ResponseData> quizPrizeList;
    private AVLoadingIndicatorView loader;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_prize);

        init();
    }

    private void init() {

        prizeRecyclerView = findViewById(R.id.prizeRecyclerView);
        loader = findViewById(R.id.loader);
        backBtn = findViewById(R.id.backBtn);

        loader.setVisibility(View.GONE);

        prizeRecyclerView.setLayoutManager(new LinearLayoutManager(ClaimPrizeActivity.this));
        prizeRecyclerView.setItemAnimator(new DefaultItemAnimator());

        quizPrizeList = new ArrayList<>();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Utility.isOnline(this)) {
            getQuizPrizeListAPI();
        } else {
            Utility.noInternetError(this);
        }


    }

    private void getQuizPrizeListAPI() {
        int uid = MyApplication.getInstance().getPreferenceSettings().getUserDetails().getResponseData().getID();
        loader.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<QuizPrizeListResponse> getPrizeList = retroInterface.getQuizPrizeList(uid);
        getPrizeList.enqueue(new Callback<QuizPrizeListResponse>() {
            @Override
            public void onResponse(Call<QuizPrizeListResponse> call, Response<QuizPrizeListResponse> response) {
                loader.setVisibility(View.GONE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    if (response.body().getResponseData() != null &&
                            response.body().getResponseData().size() > 0) {
                        quizPrizeList = response.body().getResponseData();

                        QuizPrizeAdapter quizPrizeAdapter = new QuizPrizeAdapter(ClaimPrizeActivity.this, quizPrizeList);
                        prizeRecyclerView.setAdapter(quizPrizeAdapter);
                        quizPrizeAdapter.notifyDataSetChanged();

                    } else {
                        alertDialog("You don't have any prize to claim.\nPlease try again later.");
                    }
                } else {
                    alertDialog(response.body().getResponseMessage());
                }
            }

            @Override
            public void onFailure(Call<QuizPrizeListResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });

    }

    public void alertDialog(String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ClaimPrizeActivity.this, R.style.AlertDialogTheme);
        builder1.setTitle("Oops");
        builder1.setIcon(R.drawable.logo);
        builder1.setMessage(msg);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        onBackPressed();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
