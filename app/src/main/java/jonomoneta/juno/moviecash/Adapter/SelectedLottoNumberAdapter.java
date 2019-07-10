package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import jonomoneta.juno.moviecash.R;

public class SelectedLottoNumberAdapter extends RecyclerView.Adapter<SelectedLottoNumberAdapter.ViewHolder> {

    Context context;
    ArrayList<String> numberList;

    public SelectedLottoNumberAdapter(Context context, ArrayList<String> numberList) {
        this.context = context;
        this.numberList = numberList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.lotto_number_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        if (i == 5) {
            viewHolder.numberCB.setBackgroundResource(R.drawable.number_selector_red);
        } else {
            viewHolder.numberCB.setBackgroundResource(R.drawable.number_selector_yellow);
        }
        viewHolder.numberCB.setText(numberList.get(i));
        viewHolder.numberCB.setChecked(true);
        viewHolder.numberCB.setEnabled(false);


    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return numberList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox numberCB;

        public ViewHolder(View itemView) {
            super(itemView);

            numberCB = itemView.findViewById(R.id.numberCB);
        }
    }
}
