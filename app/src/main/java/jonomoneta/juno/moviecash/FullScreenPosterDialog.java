package jonomoneta.juno.moviecash;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class FullScreenPosterDialog extends Dialog {

    public AppCompatActivity activity;
    public Dialog dialog;
    public TouchImageView prevImageView;
    public int image;
    private CircleImageView closeBTN;
    private boolean isLand = false;

    public FullScreenPosterDialog(@NonNull AppCompatActivity activity, int image) {
        super(activity);
        this.activity = activity;
        this.image = image;
    }

    public FullScreenPosterDialog(@NonNull AppCompatActivity activity, int image, boolean isLand) {
        super(activity);
        this.activity = activity;
        this.image = image;
        this.isLand = isLand;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.full_screen_poster_dialog);

        prevImageView = findViewById(R.id.prevImageView);
        closeBTN = findViewById(R.id.closeBTN);

        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        prevImageView.setImageResource(image);

        if (!isLand) {
            prevImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

}
