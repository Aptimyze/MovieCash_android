package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.MovieTypesResponse;
import jonomoneta.juno.moviecash.R;

public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    Context context;
    ArrayList<MovieTypesResponse.ResponseData> categoryList;
    LayoutInflater inflter;
    private boolean isDropDown;

    public CustomSpinnerAdapter(Context applicationContext, ArrayList<MovieTypesResponse.ResponseData> categoryList, boolean isDropDown) {
        this.context = applicationContext;
        this.categoryList = categoryList;
        inflter = (LayoutInflater.from(applicationContext));
        this.isDropDown = isDropDown;
    }

    @Override
    public int getCount() {
        return categoryList.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_item, null);
        CustomTextView spnrTextView = view.findViewById(R.id.spnrTextView);
        if (isDropDown) {
            spnrTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
        } else {
            spnrTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add, 0);
        }
        spnrTextView.setText(categoryList.get(i).getName());
        return view;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.custom_spinner_item, null);
        CustomTextView spnrTextView = convertView.findViewById(R.id.spnrTextView);
        spnrTextView.setText(categoryList.get(position).getName());
        return convertView;
    }
}
