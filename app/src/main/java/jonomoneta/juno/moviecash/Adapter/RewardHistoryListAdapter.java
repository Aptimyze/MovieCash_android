package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.Model.Response.LottoHistoryResponse;
import jonomoneta.juno.moviecash.Model.Response.RewardHistoryResponse;
import jonomoneta.juno.moviecash.R;


public class RewardHistoryListAdapter extends BaseAdapter {

    Context context;
    ArrayList<RewardHistoryResponse.ResponseData> historyList;

    public RewardHistoryListAdapter(Context context, ArrayList<RewardHistoryResponse.ResponseData> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @Override
    public int getCount() {

        return historyList.size();

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {

            grid = new View(context);
            grid = inflater.inflate(R.layout.lotto_history_list_item, null);


        } else {
            grid = (View) view;
        }

        CustomTextViewBold dayTV = grid.findViewById(R.id.dayTV),
                amountTV = grid.findViewById(R.id.amountTV),
                reasonTV = grid.findViewById(R.id.reasonTV);
        CustomTextView monthTV = grid.findViewById(R.id.monthTV),
                numCountTV = grid.findViewById(R.id.numCountTV);

        numCountTV.setVisibility(View.GONE);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            calendar.setTime(sdf.parse(historyList.get(i).getCreatedDate().split("T")[0]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat ndf = new SimpleDateFormat("dd-MMM-yyyy");
        String[] date = ndf.format(calendar.getTime()).split("-");
        String day = date[0],
                month = date[1];

        dayTV.setText(day);
        monthTV.setText(month);
        amountTV.setText("+ JM $ " + historyList.get(i).getReward());
        reasonTV.setText(historyList.get(i).getReason());
        return grid;
    }

}
