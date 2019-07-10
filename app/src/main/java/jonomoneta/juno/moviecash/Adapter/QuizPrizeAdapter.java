package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Arrays;

import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.Model.Response.QuizPrizeListResponse;
import jonomoneta.juno.moviecash.Model.Response.SaveQuizAnswerResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizPrizeAdapter extends RecyclerView.Adapter<QuizPrizeAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<QuizPrizeListResponse.ResponseData> quizPrizeList;
    private ArrayList<Integer> bgList, colorList;

    public QuizPrizeAdapter(Context context, ArrayList<QuizPrizeListResponse.ResponseData> quizPrizeList) {
        this.context = context;
        this.quizPrizeList = quizPrizeList;
        bgList = new ArrayList<>(Arrays.asList(
                R.drawable.btn_background_yellow,
                R.drawable.btn_background,
                R.drawable.btn_background_sky));
        colorList = new ArrayList<>(Arrays.asList(R.color.yellow, R.color.btn_color, R.color.colorAccent));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomTextViewBold levelTV, quizNameTV, claimBTN;
        public View myView;
        public AVLoadingIndicatorView loader;
        public RelativeLayout loaderRL;

        public MyViewHolder(View view) {
            super(view);
            levelTV = view.findViewById(R.id.levelTV);
            quizNameTV = view.findViewById(R.id.quizNameTV);
            claimBTN = view.findViewById(R.id.claimBTN);
            myView = view.findViewById(R.id.view);
            loader = view.findViewById(R.id.loader);
            loaderRL = view.findViewById(R.id.loaderRL);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.prize_list_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        myViewHolder.levelTV.setText(quizPrizeList.get(i).getGameLevel() + "");
        myViewHolder.quizNameTV.setText(quizPrizeList.get(i).getQuizGameName());
        myViewHolder.claimBTN.setBackgroundResource(bgList.get(i % bgList.size()));
        myViewHolder.myView.setBackgroundResource(colorList.get(i % colorList.size()));
        myViewHolder.loader.setIndicatorColor(context.getResources().getColor(colorList.get(i % colorList.size())));
        if (quizPrizeList.get(i).isClaim()) {
            myViewHolder.claimBTN.setVisibility(View.GONE);
        } else {
            myViewHolder.claimBTN.setVisibility(View.VISIBLE);
        }

        myViewHolder.claimBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isOnline(context)) {
                    myViewHolder.claimBTN.setVisibility(View.GONE);
                    myViewHolder.loaderRL.setVisibility(View.VISIBLE);

                    claimPrizeAPI(myViewHolder.loaderRL, myViewHolder.claimBTN, quizPrizeList.get(i).getID(), i);
                } else {
                    Utility.noInternetError(context);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return quizPrizeList.size();
    }

    private void claimPrizeAPI(final RelativeLayout loaderRL, final CustomTextViewBold claimBTN, int quizgameuserattendid, final int pos) {
        int uId = MyApplication.getInstance().getPreferenceSettings().getUserDetails().getResponseData().getID();
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<SaveQuizAnswerResponse> claimPrize = retroInterface.claimQuizPrize(uId, quizgameuserattendid);
        claimPrize.enqueue(new Callback<SaveQuizAnswerResponse>() {
            @Override
            public void onResponse(Call<SaveQuizAnswerResponse> call, Response<SaveQuizAnswerResponse> response) {
                loaderRL.setVisibility(View.GONE);
                claimBTN.setVisibility(View.VISIBLE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    quizPrizeList.remove(pos);
                    notifyDataSetChanged();
                    if (quizPrizeList != null && quizPrizeList.size() == 0) {
                        ((AppCompatActivity) context).onBackPressed();
                    }
                } else {
                    Toast.makeText(context, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SaveQuizAnswerResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                loaderRL.setVisibility(View.GONE);
                claimBTN.setVisibility(View.VISIBLE);
            }
        });
    }
}
