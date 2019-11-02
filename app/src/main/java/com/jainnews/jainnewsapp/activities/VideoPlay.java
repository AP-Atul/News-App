package com.jainnews.jainnewsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jainnews.jainnewsapp.R;

public class VideoPlay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        WebView webView = findViewById(R.id.videoPlay);

        String url = getIntent().getStringExtra("url");

        if(url != null && (!url.isEmpty())){
            WebSettings webSettings = webView.getSettings();
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.loadData(url, "text/html", "utf-8");
            webView.setWebChromeClient(new WebChromeClient());
        }

    }
}
