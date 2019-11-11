package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.GetSearchLocationsTypesResponse;
import jonomoneta.juno.moviecash.Model.Response.MovieTypesResponse;
import jonomoneta.juno.moviecash.R;

public class LocationTypeSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    Context context;
    ArrayList<GetSearchLocationsTypesResponse.ResponseData> list;
    LayoutInflater inflter;

    public LocationTypeSpinnerAdapter(Context context, ArrayList<GetSearchLocationsTypesResponse.ResponseData> list) {
        this.context = context;
        this.list = list;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return list.size();
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
        View view1 = view.findViewById(R.id.view);

        view1.setVisibility(View.GONE);

        spnrTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);

        spnrTextView.setText(list.get(i).getName());
        return view;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.custom_spinner_item, null);
        CustomTextView spnrTextView = convertView.findViewById(R.id.spnrTextView);

        spnrTextView.setText(list.get(position).getName());
        return convertView;
    }
}
