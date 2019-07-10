package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.WinnerListItem;
import jonomoneta.juno.moviecash.R;


public class WinnersAdapter extends RecyclerView.Adapter<WinnersAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<WinnerListItem> winnerList;

    public WinnersAdapter(Context mContext, ArrayList<WinnerListItem> winnerList) {
        this.mContext = mContext;
        this.winnerList = winnerList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.winners_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.nameTextView.setText(winnerList.get(i).getName());
        Picasso.get().load(winnerList.get(i).getProfilePicture()).placeholder(R.drawable.user).error(R.drawable.user).into(viewHolder.profileIV);

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return winnerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileIV;
        CustomTextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileIV = itemView.findViewById(R.id.profileIV);
            nameTextView = itemView.findViewById(R.id.nameTextView);

        }
    }
}
