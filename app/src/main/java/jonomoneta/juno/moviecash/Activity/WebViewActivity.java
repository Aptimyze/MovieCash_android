package jonomoneta.juno.moviecash.Activity;

import androidx.appcompat.app.AppCompatActivity;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.R;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import static jonomoneta.juno.moviecash.Activity.PredictionActivity.homePlayer;

public class WebViewActivity extends AppCompatActivity {

    private ImageView backBtn;
    private CustomTextView titleTV;
    private WebView webView;
    private String url;
    private ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        init();
    }

    private void init() {

        url = getIntent().getStringExtra("url");

        backBtn = findViewById(R.id.backBtn);
        titleTV = findViewById(R.id.titleTV);
        webView = findViewById(R.id.webView);
        loader = findViewById(R.id.loader);

        titleTV.setText(getIntent().getStringExtra("title"));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);

        webView.setWebViewClient(new MyBrowser(url));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (homePlayer != null) {
            homePlayer.start();
        }
    }

    private class MyBrowser extends WebViewClient {
        private String currentUrl;

        public MyBrowser(String currentUrl) {
            this.currentUrl = currentUrl;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.equals(currentUrl)) {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            loader.setVisibility(View.GONE);
        }
    }
}
