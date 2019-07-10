package jonomoneta.juno.moviecash;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class FullScreenPosterDialog extends Dialog {

    public AppCompatActivity activity;
    public Dialog dialog;
    public TouchImageView prevImageView;
    public int image;
    private AVLoadingIndicatorView progressBar;

    public FullScreenPosterDialog(@NonNull AppCompatActivity activity, int image) {
        super(activity);
        this.activity = activity;
        this.image = image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.full_screen_poster_dialog);

        prevImageView = findViewById(R.id.prevImageView);
        progressBar = findViewById(R.id.progressBar);

        prevImageView.setImageResource(image);
    }

}
