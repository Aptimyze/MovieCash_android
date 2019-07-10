package jonomoneta.juno.moviecash.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import jonomoneta.juno.moviecash.Activity.TalentUserProfileActivity;
import jonomoneta.juno.moviecash.Activity.WorldsBestActivity;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.Model.Response.CommonResponse;
import jonomoneta.juno.moviecash.Model.Response.GetAllTalentBankVideosByUserResponse;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Response;

import static jonomoneta.juno.moviecash.Activity.WorldsBestActivity.totalVideoTV;


public class MyVideoListAdapter extends BaseAdapter {

    Context context;
    ArrayList<GetAllTalentBankVideosByUserResponse.ResponseData> myVideoList;
    boolean self, isReview;

    public MyVideoListAdapter(Context context, ArrayList<GetAllTalentBankVideosByUserResponse.ResponseData> myVideoList,
                              boolean self, boolean isReview) {
        this.context = context;
        this.myVideoList = myVideoList;
        this.self = self;
        this.isReview = isReview;
    }

    @Override
    public int getCount() {

        return myVideoList.size();

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
            grid = inflater.inflate(R.layout.profile_video_grid_item, null);


        } else {
            grid = view;
        }

        final ImageView gridImageView = grid.findViewById(R.id.gridImageView);
        final AVLoadingIndicatorView loader = grid.findViewById(R.id.loader);
        final CircleImageView remvBTN = grid.findViewById(R.id.remvBTN);
        CustomTextView categoryTV = grid.findViewById(R.id.categoryTV);
        remvBTN.setVisibility(View.GONE);

        remvBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(i);
            }
        });

        categoryTV.setText("#" + myVideoList.get(i).getCategoryName());

        Picasso.get().load(myVideoList.get(i).getThumbUrl()).error(R.drawable.ic_image_not_found)
                .into(gridImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        loader.setVisibility(View.GONE);
                        if (self) {
                            remvBTN.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        loader.setVisibility(View.GONE);
                        if (self) {
                            remvBTN.setVisibility(View.VISIBLE);
                        }
                    }
                });

        gridImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                videoDialog(context, i, myVideoList.get(i).getVideoUrl(), isReview);

            }
        });
        return grid;
    }

    private void showAlertDialog(final int i) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder1.setTitle("Alert");
        builder1.setIcon(R.drawable.logo);
        builder1.setMessage("Are you sure to remove this video?");
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if (Utility.isOnline(context)) {
                            removeVideoAPI(myVideoList.get(i).getID(), i);
                        } else {
                            Utility.noInternetError(context);
                        }
                    }
                });
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void removeVideoAPI(int videoId, final int pos) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Deleting...");
        progressDialog.setMessage("Please wait.");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<CommonResponse> remvVideo = retroInterface.removeVideo(videoId);
        remvVideo.enqueue(new retrofit2.Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                progressDialog.dismiss();
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    WorldsBestActivity.myVideosList.remove(pos);
                    notifyDataSetChanged();
                    totalVideoTV.setText((Long.parseLong(String.valueOf(totalVideoTV.getText())) - 1) + "");
                } else {
                    Toast.makeText(context, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void videoDialog(final Context context, final int pos, final String videoURL, boolean isReview) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_videoview_dialog);
        dialog.setCancelable(true);
//        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.flag_transparent));

        final AVLoadingIndicatorView loader = dialog.findViewById(R.id.loader);
        LinearLayout nameLL = dialog.findViewById(R.id.nameLL);
        final VideoView videoView = dialog.findViewById(R.id.videoView);
        CircleImageView userImgView = dialog.findViewById(R.id.userImgView);
        CustomTextView userNameTV = dialog.findViewById(R.id.userNameTV);

        videoView.setVideoPath(videoURL);

        if (isReview) {
            nameLL.setVisibility(View.VISIBLE);
            Picasso.get().load(myVideoList.get(pos)
                    .getProfilePicture())
                    .error(R.drawable.user)
                    .placeholder(R.drawable.user)
                    .into(userImgView);
            userNameTV.setText(myVideoList.get(pos).getUserName());

            nameLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    context.startActivity(new Intent(context, TalentUserProfileActivity.class)
                            .putExtra("userid", myVideoList.get(pos).getUserID())
                            .putExtra("name", myVideoList.get(pos).getUserName())
                            .putExtra("profImage", myVideoList.get(pos).getProfilePicture())
                            .putExtra("mobno", myVideoList.get(pos).getMobileNo())
                            .putExtra("formUrl", myVideoList.get(pos).getFormUrl()));
                }
            });
        } else {
            nameLL.setVisibility(View.GONE);
        }

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
