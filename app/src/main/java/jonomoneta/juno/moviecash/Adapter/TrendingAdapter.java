package jonomoneta.juno.moviecash.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import jonomoneta.juno.moviecash.Activity.TalentUserProfileActivity;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.GetAllBestTalentBankVideosResponse;
import jonomoneta.juno.moviecash.Model.Response.TrailerDetailsResponse;
import jonomoneta.juno.moviecash.R;

public class TrendingAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GetAllBestTalentBankVideosResponse.ResponseData> trendingList;

    public TrendingAdapter(Context context, ArrayList<GetAllBestTalentBankVideosResponse.ResponseData> trendingList) {
        this.context = context;
        this.trendingList = trendingList;
    }

    @Override
    public int getCount() {

        return trendingList.size();

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
            grid = inflater.inflate(R.layout.trend_list_item, null);


        } else {
            grid = view;
        }

        ImageView thumbIV = grid.findViewById(R.id.thumbIV);
        CustomTextView nameTV = grid.findViewById(R.id.nameTV),
                mobNoTV = grid.findViewById(R.id.mobNoTV),
                likeCountTV = grid.findViewById(R.id.likeCountTV),
                disLikeCountTV = grid.findViewById(R.id.disLikeCountTV);

        Picasso.get().load(trendingList.get(i).getThumbUrl()).into(thumbIV);
        nameTV.setText(trendingList.get(i).getUserName());
        mobNoTV.setText(trendingList.get(i).getMobileNo());
        likeCountTV.setText(trendingList.get(i).getLikeVideoCount() + "");
        disLikeCountTV.setText(trendingList.get(i).getDislikeVideoCount() + "");

        thumbIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoDialog(context, trendingList.get(i).getVideoUrl());
            }
        });

        nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, TalentUserProfileActivity.class)
                        .putExtra("userid", trendingList.get(i).getUserID())
                        .putExtra("name", trendingList.get(i).getUserName())
                        .putExtra("profImage", trendingList.get(i).getProfilePicture())
                        .putExtra("mobno", trendingList.get(i).getMobileNo())
                        .putExtra("isJudge", true)
                        .putExtra("formUrl", trendingList.get(i).getFormUrl()));
            }
        });


        return grid;
    }

    public void videoDialog(final Context context, final String videoURL) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_videoview_dialog);
        dialog.setCancelable(true);

        final AVLoadingIndicatorView loader = dialog.findViewById(R.id.loader);
        LinearLayout nameLL = dialog.findViewById(R.id.nameLL);
        final VideoView videoView = dialog.findViewById(R.id.videoView);

        videoView.setVideoPath(videoURL);


        nameLL.setVisibility(View.GONE);


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                loader.setVisibility(View.GONE);
                videoView.start();
            }
        });
        dialog.show();
    }
}
