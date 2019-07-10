package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.WinnerListItem;
import jonomoneta.juno.moviecash.R;


public class AllWinnersAdapter extends BaseAdapter {

    Context context;
    ArrayList<WinnerListItem> winnerList;

    public AllWinnersAdapter(Context context, ArrayList<WinnerListItem> winnerList) {
        this.context = context;
        this.winnerList = winnerList;
    }

    @Override
    public int getCount() {

        return winnerList.size();

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
            grid = inflater.inflate(R.layout.all_winners_list_item, null);


        } else {
            grid = (View) view;
        }

        CustomTextView nameTextView = grid.findViewById(R.id.nameTextView);
        CircleImageView profileIV = grid.findViewById(R.id.profileIV);
        if (winnerList != null && winnerList.size() > 0) {
            nameTextView.setText(winnerList.get(i).getName());
            Picasso.get().load(winnerList.get(i).getProfilePicture()).placeholder(R.drawable.user).error(R.drawable.user).into(profileIV);
        }

        return grid;
    }

}
