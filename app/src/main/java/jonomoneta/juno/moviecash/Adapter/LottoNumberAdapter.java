package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import jonomoneta.juno.moviecash.R;

import static jonomoneta.juno.moviecash.Activity.SelectLottoNumbersActivity.cntinueBTN;
import static jonomoneta.juno.moviecash.Activity.SelectLottoNumbersActivity.selectedNumAdapter;
import static jonomoneta.juno.moviecash.Activity.SelectLottoNumbersActivity.selectedNumberList;

public class LottoNumberAdapter extends RecyclerView.Adapter<LottoNumberAdapter.ViewHolder> {

    Context context;
    ArrayList<String> numberList;
    boolean isLucky;

    public LottoNumberAdapter(Context context, ArrayList<String> numberList, boolean isLucky) {
        this.context = context;
        this.numberList = numberList;
        this.isLucky = isLucky;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.lotto_number_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        if (!isLucky) {
            viewHolder.numberCB.setBackgroundResource(R.drawable.number_selector_yellow);
        } else {
            viewHolder.numberCB.setBackgroundResource(R.drawable.number_selector_red);
        }
        viewHolder.numberCB.setText(numberList.get(i));

        viewHolder.numberCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isLucky) {
                    if (isChecked) {
                        if (selectedNumberList.size() < 5) {
                            selectedNumberList.add(numberList.get(i));
                            selectedNumAdapter.notifyDataSetChanged();
                        } else {
                            viewHolder.numberCB.setChecked(false);
                        }
                        if (selectedNumberList.size() == 5) {
                            cntinueBTN.setVisibility(View.VISIBLE);
                        }
                    } else {
                        selectedNumberList.remove(numberList.get(i));
                        selectedNumAdapter.notifyDataSetChanged();
                        cntinueBTN.setVisibility(View.GONE);

                    }
                } else {
                    if (isChecked) {
                        if (selectedNumberList.size() < 6) {
                            selectedNumberList.add(numberList.get(i));
                            selectedNumAdapter.notifyDataSetChanged();
                        } else {
                            viewHolder.numberCB.setChecked(false);
                        }
                        if (selectedNumberList.size() == 6) {
                            cntinueBTN.setVisibility(View.VISIBLE);
                        }
                    } else {
                        selectedNumberList.remove(selectedNumberList.size() - 1);
                        selectedNumAdapter.notifyDataSetChanged();
                        cntinueBTN.setVisibility(View.GONE);

                    }
                }
            }
        });

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
