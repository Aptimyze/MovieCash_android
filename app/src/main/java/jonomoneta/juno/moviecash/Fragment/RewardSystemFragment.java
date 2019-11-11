package jonomoneta.juno.moviecash.Fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import jonomoneta.juno.moviecash.Activity.MapRewardActivity;
import jonomoneta.juno.moviecash.Activity.ProfileActivity;
import jonomoneta.juno.moviecash.CustomDialog;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.FullScreenPosterDialog;
import jonomoneta.juno.moviecash.Model.Response.GetAdvertisementResponse;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RewardSystemFragment extends Fragment {

    private TextView theaterIMG, sportsIMG, travelIMG, restaurantIMG, schoolIMG, mallIMG, gymIMG, posterEightIMG, posterNineIMG,
            cafeIMG, nightClubIMG, movOneIMG, movTwoIMG, movThreeIMG;
    private Animation zoomIn, zoomOut;
    private CustomTextViewBold earnRewardBTN;

    public RewardSystemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reward_system, container, false);

        init(view);
        return view;
    }

    private void init(View view) {

        zoomIn = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in_gift);
        zoomOut = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_out_gift);

        sportsIMG = view.findViewById(R.id.sportsIMG);
        travelIMG = view.findViewById(R.id.travelIMG);
        restaurantIMG = view.findViewById(R.id.restaurantIMG);
        schoolIMG = view.findViewById(R.id.schoolIMG);
        mallIMG = view.findViewById(R.id.mallIMG);
        gymIMG = view.findViewById(R.id.gymIMG);
        theaterIMG = view.findViewById(R.id.theaterIMG);
        posterEightIMG = view.findViewById(R.id.posterEightIMG);
        posterNineIMG = view.findViewById(R.id.posterNineIMG);
        movOneIMG = view.findViewById(R.id.movOneIMG);
        movTwoIMG = view.findViewById(R.id.movTwoIMG);
        movThreeIMG = view.findViewById(R.id.movThreeIMG);
        cafeIMG = view.findViewById(R.id.cafeIMG);
        nightClubIMG = view.findViewById(R.id.nightClubIMG);
        earnRewardBTN = view.findViewById(R.id.earnRewardBTN);

        earnRewardBTN.startAnimation(zoomOut);

        earnRewardBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MapRewardActivity.class));
            }
        });

        zoomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                earnRewardBTN.startAnimation(zoomOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        zoomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                earnRewardBTN.startAnimation(zoomIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        if (Utility.isOnline(getActivity())) {
            getAdvertisementAPI();
        }

        theaterIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.movie_theater_original);
                customDialog.setCancelable(true);
                customDialog.show();
            }
        });

        sportsIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.sports_original);
                customDialog.setCancelable(true);
                customDialog.show();
            }
        });

        travelIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.travel_original);
                customDialog.setCancelable(true);
                customDialog.show();
            }
        });

        restaurantIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.restaurant_original);
                customDialog.setCancelable(true);
                customDialog.show();
            }
        });

        schoolIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.school_original);
                customDialog.setCancelable(true);
                customDialog.show();
            }
        });

        mallIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.malls_original);
                customDialog.setCancelable(true);
                customDialog.show();
            }
        });

        gymIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.gym_original);
                customDialog.setCancelable(true);
                customDialog.show();
            }
        });

        posterEightIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.poster_eight_original);
                customDialog.setCancelable(true);
                customDialog.show();
            }
        });

        posterNineIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.poster_nine_original);
                customDialog.setCancelable(true);
                customDialog.show();
            }
        });

        cafeIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.cafe_original);
                customDialog.setCancelable(true);
                customDialog.show();
            }
        });

        nightClubIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.night_club_original, true);
                customDialog.setCancelable(true);
                customDialog.show();
            }
        });

        movOneIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.movie_one_original);
                customDialog.setCancelable(true);
                customDialog.show();
            }
        });

        movTwoIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.movie_two_original);
                customDialog.setCancelable(true);
                customDialog.show();
            }
        });

        movThreeIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenPosterDialog customDialog = new FullScreenPosterDialog((AppCompatActivity) getActivity(), R.drawable.movie_three_original,
                        true);
                customDialog.setCancelable(true);
                customDialog.show();
            }
        });
    }

    private void getAdvertisementAPI() {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<GetAdvertisementResponse> getAd = retroInterface.getAdvertisement(1);
        getAd.enqueue(new Callback<GetAdvertisementResponse>() {
            @Override
            public void onResponse(Call<GetAdvertisementResponse> call, Response<GetAdvertisementResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    showAdDialog(response.body().getResponseDataArrayList().get(0));
                }
            }

            @Override
            public void onFailure(Call<GetAdvertisementResponse> call, Throwable t) {

            }
        });
    }

    private void showAdDialog(final GetAdvertisementResponse.ResponseData data) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_advertisement_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.flag_transparent));

        CircleImageView closeBTN = dialog.findViewById(R.id.closeBTN);
        final ImageView adImageVIew = dialog.findViewById(R.id.adImageVIew);
        final RelativeLayout downloadBTN = dialog.findViewById(R.id.downloadBTN);
        final VideoView adVideoVIew = dialog.findViewById(R.id.adVideoVIew);
        final AVLoadingIndicatorView loader = dialog.findViewById(R.id.loader);

        if (data.getImageUrl() != null && data.getImageUrl().length() > 0) {
            adImageVIew.setVisibility(View.VISIBLE);

            Glide.with(getActivity()).load(data.getImageUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            loader.setVisibility(View.GONE);
                            Log.e("adLoadImgExc", e.getMessage() + "");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            loader.setVisibility(View.GONE);
                            downloadBTN.setVisibility(View.VISIBLE);
                            return false;
                        }
                    }).into(adImageVIew);
        }
        if (data.getVideoUrl() != null && data.getVideoUrl().length() > 0) {
            adVideoVIew.setVisibility(View.VISIBLE);
            loader.setVisibility(View.VISIBLE);

            adVideoVIew.setVideoPath(data.getVideoUrl());
            adVideoVIew.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    loader.setVisibility(View.GONE);
                    adVideoVIew.start();
                    downloadBTN.setVisibility(View.VISIBLE);
                }
            });

            adVideoVIew.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    adVideoVIew.start();
                }
            });

            adVideoVIew.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    dialog.dismiss();
                    return false;
                }
            });
        }

        downloadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getImageUrl() != null && data.getImageUrl().length() > 0) {
                    new DownloadFile().execute(data.getImageUrl(), "img");
                } else if (data.getVideoUrl() != null && data.getVideoUrl().length() > 0) {
                    new DownloadFile().execute(data.getVideoUrl(), "video");
                }
            }
        });

        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(getActivity());
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                String myfolder = Environment.getExternalStorageDirectory() + "/" + "Phala";
//                if (f_url[1].equalsIgnoreCase("img")) {
//                    myfolder = Environment.getExternalStorageDirectory() + "/" + "Phala" + "/" + "Media";
//                } else if (f_url[1].equalsIgnoreCase("video")) {
//                    myfolder = Environment.getExternalStorageDirectory() + "/" + "Phala" + "/" + "Videos";
//                }

                final File directory = new File(myfolder);
                if (!directory.exists()) {
                    directory.mkdir();
                }

                String subFolder = null;

                if (f_url[1].equalsIgnoreCase("img")) {
                    subFolder = Environment.getExternalStorageDirectory() + "/" + "Phala" + "/" + "Images";
                } else if (f_url[1].equalsIgnoreCase("video")) {
                    subFolder = Environment.getExternalStorageDirectory() + "/" + "Phala" + "/" + "Videos";
                }

                final File subDir = new File(subFolder);
                if (!subDir.exists()) {
                    subDir.mkdir();
                }


                String name = f_url[0].split("/")[f_url[0].split("/").length - 1];
                final File finalFile = new File(subDir + "/" + name);

                OutputStream output = new FileOutputStream(finalFile);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d("", "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded";

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getActivity(),
                    message, Toast.LENGTH_LONG).show();

            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{Environment.getExternalStorageDirectory().getAbsolutePath()}, new String[]{"video/mp4"},
                    null);


        }
    }


}
