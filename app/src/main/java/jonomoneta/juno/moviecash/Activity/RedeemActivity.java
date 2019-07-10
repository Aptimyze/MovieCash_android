package jonomoneta.juno.moviecash.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.ClaimPlanResponse;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemActivity extends AppCompatActivity {

    private LinearLayout basicLL, goldLL, premiumLL, luxuryLL;
    private ImageView backBtn;
    private CustomTextView basicReqTV, goldReqTV, premiumReqTV, luxuryReqTV;
    ArrayList<ClaimPlanResponse.ResponseData> planList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        initialize();
    }

    private void initialize() {
        planList = new ArrayList<>();
        basicLL = findViewById(R.id.basicLL);
        goldLL = findViewById(R.id.goldLL);
        premiumLL = findViewById(R.id.premiumLL);
        luxuryLL = findViewById(R.id.luxuryLL);
        backBtn = findViewById(R.id.backBtn);
        basicReqTV = findViewById(R.id.basicReqTV);
        goldReqTV = findViewById(R.id.goldReqTV);
        premiumReqTV = findViewById(R.id.premiumReqTV);
        luxuryReqTV = findViewById(R.id.luxuryReqTV);

        getClaimPlans();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getClaimPlans() {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<ClaimPlanResponse> getPlan = retroInterface.getClaimPlan();
        getPlan.enqueue(new Callback<ClaimPlanResponse>() {
            @Override
            public void onResponse(Call<ClaimPlanResponse> call, Response<ClaimPlanResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    planList = response.body().getResponseData();
                    basicReqTV.setText("$" + planList.get(0).getClaimPrice() + " JM");
                    goldReqTV.setText("$" + planList.get(1).getClaimPrice() + " JM");
                    premiumReqTV.setText("$" + planList.get(2).getClaimPrice() + " JM");
                    luxuryReqTV.setText("$" + planList.get(3).getClaimPrice() + " JM");

                    basicLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utility.claimDialog(RedeemActivity.this, "BASIC",
                                    "Requirement: $" + planList.get(0).getClaimPrice() + " JM\nReward: T-shirt",
                                    R.drawable.ic_tshirt, Integer.parseInt(planList.get(0).getClaimPrice()), "BASIC");
                        }
                    });

                    goldLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utility.claimDialog(RedeemActivity.this, "GOLD",
                                    "Requirement: $" + planList.get(1).getClaimPrice() + " JM\nReward: Cologne/Perfume of our choice",
                                    R.drawable.ic_perfume, Integer.parseInt(planList.get(1).getClaimPrice()), "GOLD");
                        }
                    });

                    premiumLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utility.claimDialog(RedeemActivity.this, "PREMIUM",
                                    "Requirement: $" + planList.get(2).getClaimPrice() + " JM\nReward:\nAmericans:Economy cruise from Miami to Bahamas\nNepalese and Indians: One economy ticket on Angriya – India’s first cruise ship to Goa\nRomanians: Two night stay at Constanta beach hotel",
                                    R.drawable.ic_ship, Integer.parseInt(planList.get(2).getClaimPrice()), "PREMIUM");
                        }
                    });

                    luxuryLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utility.claimDialog(RedeemActivity.this, "LUXURY",
                                    "Requirement: $" + planList.get(3).getClaimPrice() + " JM\nReward: One 50 inch TV anywhere in world",
                                    R.drawable.ic_tv, Integer.parseInt(planList.get(3).getClaimPrice()), "LUXURY");
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ClaimPlanResponse> call, Throwable t) {

            }
        });

    }


}
