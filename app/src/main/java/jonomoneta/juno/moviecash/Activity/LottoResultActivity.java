package jonomoneta.juno.moviecash.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import jonomoneta.juno.moviecash.Model.BroadcastResult;
import jonomoneta.juno.moviecash.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class LottoResultActivity extends AppCompatActivity {

    private static final String BAMBUSER_APPLICATION_ID = "IE208QHdUAaXXYlHMcwzlg"; // player
    private static final String BAMBUSER_API_KEY = "16lxikuz8g7dftv18ehin7qsw";
    final OkHttpClient mOkHttpClient = new OkHttpClient();
    String streamId = null;
    private SurfaceView videoSurfaceView;
    BroadcastPlayer mBroadcastPlayer;
    private TextView onlineUserTV;
    private boolean isPause = false;
    private CircleImageView closeBTN;
    private RelativeLayout adRL;
    DatabaseReference databaseReference;
    private VideoView adVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lotto_result);

        init();

    }

    private void init() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        adRL = findViewById(R.id.adRL);
        adVideoView = findViewById(R.id.adVideoView);
        videoSurfaceView = findViewById(R.id.videoSurfaceView);
        onlineUserTV = findViewById(R.id.onlineUserTV);
        closeBTN = findViewById(R.id.closeBTN);

        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        databaseReference.child("LottoAtm").child("LottoAdUrl").addValueEventListener(adUrlListener);

        getLatestResourceUri();

        adVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                adRL.setVisibility(View.GONE);
                adVideoView.setVisibility(View.GONE);
                if (videoSurfaceView != null) {
                    videoSurfaceView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    void getLatestResourceUri() {

        Request request = new Request.Builder()
                .url("https://api.bambuser.com/broadcasts")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Authorization", "Bearer " + BAMBUSER_API_KEY)
                .get()
                .build();

        mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(final okhttp3.Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Http exception: ", e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(final okhttp3.Call call, final okhttp3.Response response) throws IOException {
                String body = response.body().string();
                Log.e("Stream response: ", body);
                String resourceUri = null;
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray results = json.getJSONArray("results");
                    BroadcastResult.Result latestBroadcast = null;
                    ArrayList<BroadcastResult.Result> resultList = new ArrayList<>();
                    for (int i = 0; i < results.length(); i++) {
                        if (results.optJSONObject(i).optString("type").equalsIgnoreCase("live")) {
                            BroadcastResult.Result result = new BroadcastResult.Result(results.optJSONObject(i).optString("id"),
                                    results.optJSONObject(i).optString("resourceUri"),
                                    results.optJSONObject(i).optString("type"));
                            resultList.add(result);
                        }
                    }
                    if (streamId != null && streamId.length() > 0) {
                        for (int i = 0; i < resultList.size(); i++) {
                            if (streamId.equalsIgnoreCase(resultList.get(i).getId())) {
                                latestBroadcast = resultList.get(i);
                                break;
                            }
                        }
                    } else {
                        latestBroadcast = resultList.get(0);
                    }
                    streamId = latestBroadcast.getId();
                    Log.e("streamId", streamId);
//                    mPreferenceSettings.setStreamId(streamId);
                    resourceUri = latestBroadcast.getResourceUri();
                } catch (Exception ignored) {
                    Log.e("streamExc-", ignored.getMessage());
                }
                final String uri = resourceUri;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initPlayer(uri);
                    }
                });
            }
        });
    }

    ValueEventListener adUrlListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.e("lottoAdUrl", dataSnapshot.getValue().toString());
            String url = String.valueOf(dataSnapshot.getValue());

            if (url.trim().length() > 0) {
                databaseReference.child("MovieCashMillionaire").child("QuizAdUrl").removeEventListener(adUrlListener);
                if (videoSurfaceView != null) {
                    videoSurfaceView.setVisibility(View.GONE);
                }
                adRL.setVisibility(View.VISIBLE);
                adVideoView.setVisibility(View.VISIBLE);
                adVideoView.setVideoPath(url);
                adVideoView.setZOrderOnTop(true);
                adVideoView.start();

            } else {
                if (videoSurfaceView != null) {
                    videoSurfaceView.setVisibility(View.VISIBLE);
                }
                adRL.setVisibility(View.GONE);
                adVideoView.setVisibility(View.GONE);
                adVideoView.pause();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    void initPlayer(String resourceUri) {
        if (resourceUri == null) {
            Log.e("error", "Could not get info about latest broadcast");
            return;
        }
        if (videoSurfaceView == null) {
            // UI no longer active
            return;
        }

        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(this, resourceUri, BAMBUSER_APPLICATION_ID, mBroadcastPlayerObserver);
        mBroadcastPlayer.setSurfaceView(videoSurfaceView);
        mBroadcastPlayer.setAcceptType(BroadcastPlayer.AcceptType.LIVE);
        mBroadcastPlayer.setViewerCountObserver(viewerCountObserver);
        mBroadcastPlayer.load();
    }

    BroadcastPlayer.ViewerCountObserver viewerCountObserver = new BroadcastPlayer.ViewerCountObserver() {
        @Override
        public void onCurrentViewersUpdated(long l) {
            if (l >= 1000000) {
                double viewer = l / 1000000;
                onlineUserTV.setText(String.format("Value of a: %.2f", viewer) + "M");
            } else if (l >= 1000) {
                double viewer = l / 1000;
                onlineUserTV.setText(String.format("Value of a: %.2f", viewer) + "K");
            } else {
                onlineUserTV.setText(String.valueOf(l));
            }
        }

        @Override
        public void onTotalViewersUpdated(long l) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (adUrlListener != null) {
            databaseReference.child("MovieCashMillionaire").child("QuizAdUrl").removeEventListener(adUrlListener);
        }

        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = null;
    }

    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {
            Log.e("streamState", playerState.toString());
            if (playerState != null && playerState.toString().equalsIgnoreCase("COMPLETED")) {

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                streamId = null;
                finish();
            }
            if (playerState != null && playerState.toString().equalsIgnoreCase("ERROR")) {
                Log.e("streamERROR", playerState.toString());
                getLatestResourceUri();
            }
        }

        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("pause", String.valueOf(isPause));
        if (isPause) {
            isPause = false;
            videoSurfaceView = findViewById(R.id.videoSurfaceView);
            getLatestResourceUri();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("pause", "pause");
        isPause = true;
        mOkHttpClient.dispatcher().cancelAll();

        videoSurfaceView = null;
    }
}
