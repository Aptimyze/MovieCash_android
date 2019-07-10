package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.CountryResponse;
import jonomoneta.juno.moviecash.Model.Response.MovieTypesResponse;
import jonomoneta.juno.moviecash.R;

public class CountryCustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    Context context;
    ArrayList<CountryResponse.ResponseData> countryList;
    LayoutInflater inflter;

    public CountryCustomSpinnerAdapter(Context applicationContext, ArrayList<CountryResponse.ResponseData> countryList) {
        this.context = applicationContext;
        this.countryList = countryList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryList.size();
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
        spnrTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
        spnrTextView.setText(countryList.get(i).getName());
        return view;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.custom_spinner_item, null);
        CustomTextView spnrTextView = convertView.findViewById(R.id.spnrTextView);
        spnrTextView.setText(countryList.get(position).getName());
        return convertView;
    }
}
