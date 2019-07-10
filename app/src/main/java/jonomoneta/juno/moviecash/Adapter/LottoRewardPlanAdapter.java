package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.GettLottoClubRewardPlansResponse;
import jonomoneta.juno.moviecash.Model.Response.MovieTypesResponse;
import jonomoneta.juno.moviecash.R;


public class LottoRewardPlanAdapter extends RecyclerView.Adapter<LottoRewardPlanAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GettLottoClubRewardPlansResponse.ResponseData> planList;

    public LottoRewardPlanAdapter(Context context, ArrayList<GettLottoClubRewardPlansResponse.ResponseData> planList) {
        this.context = context;
        this.planList = planList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.lotto_reward_plan_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        if (i == 0) {
            viewHolder.rewadPlanTV.setText(planList.get(i).getNoOfCount() + " match = JM $ "+planList.get(i).getReward());
        } else {
            viewHolder.rewadPlanTV.setText(planList.get(i).getNoOfCount() + " matches = JM $ "+planList.get(i).getReward());
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CustomTextView rewadPlanTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rewadPlanTV = itemView.findViewById(R.id.rewadPlanTV);
        }
    }
}
