package jonomoneta.juno.moviecash.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jonomoneta.juno.moviecash.Adapter.LottoNumberAdapter;
import jonomoneta.juno.moviecash.Adapter.SelectedLottoNumberAdapter;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.Model.Response.CommonResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectLottoNumbersActivity extends AppCompatActivity {

    private RecyclerView numbersRV, luckyNumbersRV;
    private ImageView backBtn;
    public static RecyclerView selectedBallRV;
    private ArrayList<String> numberList;
    public static ArrayList<String> selectedNumberList;
    public static SelectedLottoNumberAdapter selectedNumAdapter;
    public static CustomTextViewBold cntinueBTN;
    private CustomTextView descTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lotto_numbers);

        init();
    }

    private void init() {

        backBtn = findViewById(R.id.backBtn);
        numbersRV = findViewById(R.id.numbersRV);
        luckyNumbersRV = findViewById(R.id.luckyNumbersRV);
        selectedBallRV = findViewById(R.id.selectedBallRV);
        cntinueBTN = findViewById(R.id.cntinueBTN);
        descTV = findViewById(R.id.descTV);

        selectedBallRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        selectedBallRV.setItemAnimator(new DefaultItemAnimator());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int multiViewWidth = (size.x) / 8;

        AutoFitGridLayoutManager gridLayoutManager = new AutoFitGridLayoutManager(this, multiViewWidth);
        numbersRV.setLayoutManager(gridLayoutManager);

        AutoFitGridLayoutManager luckyGridLayoutManager = new AutoFitGridLayoutManager(this, multiViewWidth);
        luckyNumbersRV.setLayoutManager(luckyGridLayoutManager);

        selectedNumberList = new ArrayList<>();
        selectedNumAdapter = new SelectedLottoNumberAdapter(SelectLottoNumbersActivity.this, selectedNumberList);
        selectedBallRV.setAdapter(selectedNumAdapter);
        selectedNumAdapter.notifyDataSetChanged();


        numberList = new ArrayList<>();
        for (int i = 1; i <= 64; i++) {
            numberList.add(String.valueOf(i));
        }

        final LottoNumberAdapter lottoNumberAdapter = new LottoNumberAdapter(SelectLottoNumbersActivity.this, numberList, false);
        numbersRV.setAdapter(lottoNumberAdapter);
        lottoNumberAdapter.notifyDataSetChanged();

        cntinueBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numbersRV.getVisibility() == View.VISIBLE) {
                    descTV.setText("Pick any 1 Lucky number.");
                    Animation sou = AnimationUtils.loadAnimation(SelectLottoNumbersActivity.this, R.anim.slide_out_up),
                            sid = AnimationUtils.loadAnimation(SelectLottoNumbersActivity.this, R.anim.slide_in_down);
                    numbersRV.startAnimation(sou);
                    luckyNumbersRV.startAnimation(sid);
                    cntinueBTN.setVisibility(View.GONE);
                    cntinueBTN.setText("Submit");
                    numbersRV.setVisibility(View.GONE);
                    luckyNumbersRV.setVisibility(View.VISIBLE);


                    LottoNumberAdapter luckyNumberAdapter = new LottoNumberAdapter(SelectLottoNumbersActivity.this, numberList, true);
                    luckyNumbersRV.setAdapter(luckyNumberAdapter);
                    lottoNumberAdapter.notifyDataSetChanged();
                } else if (luckyNumbersRV.getVisibility() == View.VISIBLE) {
                    if (Utility.isOnline(SelectLottoNumbersActivity.this)) {
                        submitLottoNumberAPI();
                    } else {
                        Utility.noInternetError(SelectLottoNumbersActivity.this);
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
    }

    private void submitLottoNumberAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(SelectLottoNumbersActivity.this);
        progressDialog.setTitle("Submitting");
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        int uid = MyApplication.getInstance().getPreferenceSettings().getUserDetails().getResponseData().getID();
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<CommonResponse> saveNumber = retroInterface.saveLottoClubSelectedNo(uid, selectedNumberList.get(0),
                selectedNumberList.get(1), selectedNumberList.get(2), selectedNumberList.get(3), selectedNumberList.get(4),
                selectedNumberList.get(5));
        saveNumber.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                progressDialog.dismiss();
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(SelectLottoNumbersActivity.this, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SelectLottoNumbersActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public class AutoFitGridLayoutManager extends GridLayoutManager {

        private int columnWidth;
        private boolean columnWidthChanged = true;

        public AutoFitGridLayoutManager(Context context, int columnWidth) {
            super(context, 1);

            setColumnWidth(columnWidth);
        }

        public void setColumnWidth(int newColumnWidth) {
            if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
                columnWidth = newColumnWidth;
                columnWidthChanged = true;
            }
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            if (columnWidthChanged && columnWidth > 0) {
                int totalSpace;
                if (getOrientation() == RecyclerView.VERTICAL) {
                    totalSpace = getWidth() - getPaddingRight() - getPaddingLeft();
                } else {
                    totalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
                }
                int spanCount = Math.max(1, totalSpace / columnWidth);
                setSpanCount(spanCount);
                columnWidthChanged = false;
            }
            super.onLayoutChildren(recycler, state);
        }
    }
}
