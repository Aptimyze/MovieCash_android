package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.R;

public class UpcomingMovieSynopsisAdapter extends PagerAdapter {

    ArrayList<Integer> posterImg;
    ArrayList<String> synopsisDetails;
    ArrayList<String> movNameList;
    Context context;
    private LayoutInflater inflater;

    public UpcomingMovieSynopsisAdapter(Context context, ArrayList<Integer> posterImg, ArrayList<String> synopsisDetails,
                                        ArrayList<String> movNameList) {
        this.context = context;
        this.posterImg = posterImg;
        this.movNameList = movNameList;
        this.synopsisDetails = synopsisDetails;
    }

    @Override
    public int getCount() {
        return synopsisDetails.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.upcoming_movies_slider_item, container, false);

        ImageView posterIV = viewLayout.findViewById(R.id.posterIV);
        CustomTextView synopsisTV = viewLayout.findViewById(R.id.synopsisTV);
        CustomTextViewBold movNameTV = viewLayout.findViewById(R.id.movNameTV);

        movNameTV.setText(movNameList.get(position));
        posterIV.setImageResource(posterImg.get(position));
        synopsisTV.setText(synopsisDetails.get(position));

        container.addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
