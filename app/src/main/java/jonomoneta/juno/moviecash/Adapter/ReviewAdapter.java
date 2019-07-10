package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.TrailerDetailsResponse;
import jonomoneta.juno.moviecash.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<TrailerDetailsResponse.ResponseData.UserCommentList> reviewsArrayList;

    public ReviewAdapter(Context context, ArrayList<TrailerDetailsResponse.ResponseData.UserCommentList> reviewsArrayList) {
        this.context = context;
        this.reviewsArrayList = reviewsArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView timeTextView, reviewTextView;
        public CustomTextView userNameTextView;
        public CircleImageView profileImageView;

        public MyViewHolder(View view) {
            super(view);
            userNameTextView = view.findViewById(R.id.userNameTextView);
            timeTextView = view.findViewById(R.id.timeTextView);
            reviewTextView = view.findViewById(R.id.reviewTextView);
            profileImageView = view.findViewById(R.id.profileImageView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_list_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.userNameTextView.setText(reviewsArrayList.get(i).getUserName());
        myViewHolder.reviewTextView.setText(reviewsArrayList.get(i).getComments());
        Picasso.get().load(reviewsArrayList.get(i).getProfilePicture()).error(R.drawable.user).into(myViewHolder.profileImageView);
        myViewHolder.timeTextView.setText(reviewsArrayList.get(i).getCommentDate());
//        Date date = new Date();
//        String createdate = reviewsArrayList.get(i).getCreatedDate().split("T")[0],
//                today = null;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        today = sdf.format(date);
//        if (createdate.equals(date)) {
//            myViewHolder.timeTextView.setText(reviewsArrayList.get(i).getCreatedDate().split("T")[1]);
//        }

    }

    @Override
    public int getItemCount() {
        return reviewsArrayList.size();
    }
}
