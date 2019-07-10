package jonomoneta.juno.moviecash.Activity;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import jonomoneta.juno.moviecash.R;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import com.wang.avi.AVLoadingIndicatorView;

public class DemoVideoActivity extends AppCompatActivity {

    private VideoView demoVideoVIew;
    private CircleImageView closeBTN;
    AVLoadingIndicatorView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_video);

        init();
    }

    private void init() {


        loader = findViewById(R.id.loader);
        demoVideoVIew = findViewById(R.id.demoVideoVIew);
        closeBTN = findViewById(R.id.closeBTN);

//        String path = "android.resource://" + getPackageName() + "/" + R.raw.imo;
        Uri uri = Uri.parse("https://moviecashapi.junomoneta.io/worldsbest.mp4");
        demoVideoVIew.setVideoURI(uri);

        demoVideoVIew.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                demoVideoVIew.start();
            }
        });

        demoVideoVIew.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                loader.setVisibility(View.GONE);
                demoVideoVIew.start();
            }
        });

        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
