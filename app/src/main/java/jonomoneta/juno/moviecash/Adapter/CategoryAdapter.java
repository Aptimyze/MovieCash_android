package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jonomoneta.juno.moviecash.Model.Response.MovieTypesResponse;
import jonomoneta.juno.moviecash.R;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MovieTypesResponse.ResponseData> movieTypesList;
    private String selectedType;
    public static List<String> selectedTypeArray ;
    private boolean checkable;

    public CategoryAdapter(Context mContext, ArrayList<MovieTypesResponse.ResponseData> movieTypesList,
                           String selectedType, boolean checkable) {
        this.mContext = mContext;
        this.movieTypesList = movieTypesList;
        this.selectedType = selectedType;
        selectedTypeArray = new ArrayList<>();
        this.checkable = checkable;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        if (checkable){
            viewHolder.categoryCheckBox.setEnabled(true);
        } else {
            viewHolder.categoryCheckBox.setEnabled(false);
        }
        viewHolder.categoryCheckBox.setText(movieTypesList.get(i).getName().trim());



        if (selectedType != null && selectedType.length() > 0) {

            if (selectedType.contains(",")) {
                selectedTypeArray = new ArrayList<>(Arrays.asList(selectedType.split(",")));
            } else {
                selectedTypeArray = new ArrayList<>();
                selectedTypeArray.add(selectedType);
            }

            for (int m = 0; m < selectedTypeArray.size(); m++) {

                if (selectedTypeArray.get(m).equalsIgnoreCase(String.valueOf(movieTypesList.get(i).getID()))) {
                    viewHolder.categoryCheckBox.setChecked(true);
                    viewHolder.categoryCheckBox.setTextColor(mContext.getResources().getColor(R.color.black));
                    break;
                }

            }
        }

        viewHolder.categoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    viewHolder.categoryCheckBox.setTextColor(mContext.getResources().getColor(R.color.black));
                    selectedTypeArray.add(String.valueOf(movieTypesList.get(i).getID()));
                } else {
                    viewHolder.categoryCheckBox.setTextColor(mContext.getResources().getColor(R.color.white));
                    selectedTypeArray.remove(String.valueOf(movieTypesList.get(i).getID()));
                }

                Log.e("selectedType", String.valueOf(selectedTypeArray));
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return movieTypesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox categoryCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryCheckBox = itemView.findViewById(R.id.categoryCheckBox);

        }
    }
}
