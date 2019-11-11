package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.Fragment.MapRewardFragment;
import jonomoneta.juno.moviecash.Model.Response.GetGoogleSearchLocationResponse;
import jonomoneta.juno.moviecash.R;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {

    Context context;
    ArrayList<GetGoogleSearchLocationResponse.ResponseData> list;
    ArrayList<Marker> markerList;
    GoogleMap mMap;
    MapRewardFragment mapRewardFragment;

    public LocationListAdapter(Context context, ArrayList<GetGoogleSearchLocationResponse.ResponseData> list, ArrayList<Marker> markerList,
                               GoogleMap mMap, MapRewardFragment mapRewardFragment) {
        this.context = context;
        this.list = list;
        this.markerList = markerList;
        this.mMap = mMap;
        this.mapRewardFragment = mapRewardFragment;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        if (list.get(i).getIcon() != null) {
            Picasso.get().load(list.get(i).getIcon()).error(R.drawable.marker_large).into(viewHolder.iconIV);
        }
        viewHolder.nameTV.setText(list.get(i).getName());
        viewHolder.distanceTV.setText(list.get(i).getDistance() + " km away");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapRewardFragment.showPlaceDetailDialog(markerList.get(i));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(markerList.get(i).getPosition()).zoom(16).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition
                        (cameraPosition));
            }
        });
        if (list.get(i).getLastUsedInSecond() > 0) {
            viewHolder.mainLL.setBackgroundColor(context.getResources().getColor(R.color.grey_trans));
            viewHolder.nameTV.setTextColor(context.getResources().getColor(R.color.light_grey));
            viewHolder.iconIV.setAlpha(0.5f);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iconIV;
        CustomTextViewBold nameTV;
        CustomTextView distanceTV;
        LinearLayout mainLL;

        public ViewHolder(View itemView) {
            super(itemView);

            iconIV = itemView.findViewById(R.id.iconIV);
            nameTV = itemView.findViewById(R.id.nameTV);
            distanceTV = itemView.findViewById(R.id.distanceTV);
            mainLL = itemView.findViewById(R.id.mainLL);
        }
    }
}
