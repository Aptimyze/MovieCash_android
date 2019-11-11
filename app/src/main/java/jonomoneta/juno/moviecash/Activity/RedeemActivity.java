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

    private LinearLayout basicLL, goldLL, premiumLL, luxuryLL, dreamsLL, dreamBasicLL, dreamGoldLL,
            dreamPremiumLL, dreamLuxLL, movTicketLL, dinnerLL;
    private ImageView backBtn;
    private CustomTextView basicReqTV, goldReqTV, premiumReqTV, luxuryReqTV, dreamsReqTV, dreamBasicReqTV, dreamGoldReqTV,
            dreamPremiumReqTV, dreamLuxReqTV, movTicketReqTV, dinnerReqTV;
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
        dreamsLL = findViewById(R.id.dreamsLL);
        dreamBasicLL = findViewById(R.id.dreamBasicLL);
        dreamGoldLL = findViewById(R.id.dreamGoldLL);
        dreamPremiumLL = findViewById(R.id.dreamPremiumLL);
        dreamLuxLL = findViewById(R.id.dreamLuxLL);
        movTicketLL = findViewById(R.id.movTicketLL);
        dinnerLL = findViewById(R.id.dinnerLL);
        dreamsReqTV = findViewById(R.id.dreamsReqTV);
        dreamBasicReqTV = findViewById(R.id.dreamBasicReqTV);
        dreamGoldReqTV = findViewById(R.id.dreamGoldReqTV);
        dreamPremiumReqTV = findViewById(R.id.dreamPremiumReqTV);
        dreamLuxReqTV = findViewById(R.id.dreamLuxReqTV);
        movTicketReqTV = findViewById(R.id.movTicketReqTV);
        dinnerReqTV = findViewById(R.id.dinnerReqTV);

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
                    dreamsReqTV.setText("$" + planList.get(4).getClaimPrice() + " JM");
                    dreamBasicReqTV.setText("$" + planList.get(5).getClaimPrice() + " JM");
                    dreamGoldReqTV.setText("$" + planList.get(6).getClaimPrice() + " JM");
                    dreamPremiumReqTV.setText("$" + planList.get(7).getClaimPrice() + " JM");
                    dreamLuxReqTV.setText("$" + planList.get(8).getClaimPrice() + " JM");
                    movTicketReqTV.setText("$" + planList.get(9).getClaimPrice() + " JM");
                    dinnerReqTV.setText("$" + planList.get(10).getClaimPrice() + " JM");

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

                    dreamsLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utility.claimDialog(RedeemActivity.this, "DREAMS",
                                    "Requirement: $" + planList.get(4).getClaimPrice() + " JM\nReward: Basic Economy Leptop",
                                    R.drawable.ic_laptop, Integer.parseInt(planList.get(4).getClaimPrice()), "DREAMS");
                        }
                    });

                    dreamBasicLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utility.claimDialog(RedeemActivity.this, "DREAMS-BASIC",
                                    "Requirement: $" + planList.get(5).getClaimPrice() + " JM\nReward: Tesla Economy Car ( 30 Installments )",
                                    R.drawable.ic_car, Integer.parseInt(planList.get(5).getClaimPrice()), "DREAMS-BASIC");
                        }
                    });

                    dreamGoldLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utility.claimDialog(RedeemActivity.this, "DREAMS-GOLD",
                                    "Requirement: $" + planList.get(6).getClaimPrice() + " JM\nReward: 70% of dorm Rental for 1 year",
                                    R.drawable.ic_dorm, Integer.parseInt(planList.get(6).getClaimPrice()), "DREAMS-GOLD");
                        }
                    });

                    dreamPremiumLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utility.claimDialog(RedeemActivity.this, "DREAMS-PREMIUM",
                                    "Requirement: $" + planList.get(7).getClaimPrice() + " JM\nReward: 70% of Tuition fees for 1 Year",
                                    R.drawable.ic_fees, Integer.parseInt(planList.get(7).getClaimPrice()), "DREAMS-PREMIUM");
                        }
                    });

                    dreamLuxLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utility.claimDialog(RedeemActivity.this, "DREAMS-LUXURY",
                                    "Requirement: $" + planList.get(8).getClaimPrice() + " JM\nReward: 1 Bed Room Apartment (30 years Mortgage) The most Basic in city We approve",
                                    R.drawable.ic_bedroom, Integer.parseInt(planList.get(8).getClaimPrice()), "DREAMS-LUXURY");
                        }
                    });

                    movTicketLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utility.claimDialog(RedeemActivity.this, "MOVIETICKET",
                                    "Requirement: $" + planList.get(9).getClaimPrice() + " JM\nReward: Movie Ticket",
                                    R.drawable.ic_ticket, Integer.parseInt(planList.get(9).getClaimPrice()), "MOVIETICKET");
                        }
                    });

                    dinnerLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utility.claimDialog(RedeemActivity.this, "DINNER",
                                    "Requirement: $" + planList.get(10).getClaimPrice() + " JM\nReward: DINNER",
                                    R.drawable.ic_dinner, Integer.parseInt(planList.get(10).getClaimPrice()), "MOVIETICKET");
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
