package com.jainnews.jainnewsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jainnews.jainnewsapp.HomePage;
import com.jainnews.jainnewsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsDetails extends AppCompatActivity {

    private ImageView newsImage;
    private WebView newsVideo;
    private FloatingActionButton forward, backward;
    private TextView newsTitle, newsCity, newsDesc;

    private JSONObject news;
    private String[] newsUrls;
    private static int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        newsImage = findViewById(R.id.imageNew);
        forward = findViewById(R.id.floatingForward);
        backward = findViewById(R.id.floatingBackward);
        newsTitle = findViewById(R.id.newTitle);
        newsCity = findViewById(R.id.newsCity);
        newsDesc = findViewById(R.id.newsDesc);
        newsVideo = findViewById(R.id.newsVideo);

        int id = getIntent().getIntExtra("id", -1);
        if(id != -1)
            news = HomePage.getNewsObject(id);

        if(news != null)
            setDataToUI(news);

        (findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTextUrl();
            }
        });

        (findViewById(R.id.video)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(news.getString("news_video").isEmpty())
                        Toast.makeText(getApplicationContext(), "No Link found", Toast.LENGTH_SHORT).show();
                    else
                        startActivity(new Intent(NewsDetails.this, VideoPlay.class).putExtra("url", news.getString("news_video")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setDataToUI(JSONObject news) {
        try {
            newsUrls = new String[4];


            newsUrls[0] = news.getString("news_img_1");
            if(! news.getString("news_img_2").isEmpty())
                newsUrls[1] = news.getString("news_img_2");
            if(! news.getString("news_img_3").isEmpty())
                newsUrls[2] = news.getString("news_img_3");
            if(! news.getString("news_video").isEmpty())
                newsUrls[3] = news.getString("news_video");

            newsTitle.setText(news.getString("news_title"));
            newsCity.setText("Address: " + news.getString("news_place"));
            newsDesc.setText("Description: " + news.getString("news_content"));
            Picasso.get().load(news.getString("news_img_1")).into(newsImage);

            forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(counter == 0){
                        counter = 1;
                        Picasso.get().load(newsUrls[counter]).into(newsImage);
                    } else if(counter == 1){
                        counter = 2;
                        Picasso.get().load(newsUrls[counter]).into(newsImage);
                    } else if(counter == 2){
                        counter = 3;

                        newsImage.setVisibility(View.GONE);
                        WebSettings webSettings = newsVideo.getSettings();
                        webSettings.setJavaScriptEnabled(true);
                        webSettings.setAppCacheEnabled(true);
                        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                        newsVideo.loadData(newsUrls[counter], "text/html", "utf-8");
                        newsVideo.setWebChromeClient(new WebChromeClient());
                        newsVideo.setVisibility(View.VISIBLE);
                    }
                }
            });

            backward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(counter == 3){
                        counter = 2;
                        newsVideo.setVisibility(View.GONE);
                        newsImage.setVisibility(View.VISIBLE);
                        Picasso.get().load(newsUrls[counter]).into(newsImage);
                    } else if(counter == 2){
                        counter = 1;
                        Picasso.get().load(newsUrls[counter]).into(newsImage);
                    } else if(counter == 1){
                        counter = 0;
                        Picasso.get().load(newsUrls[counter]).into(newsImage);
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void shareTextUrl() {

        String url = "http://jainnewsapp.000webhostapp.com/api/home-page-loader.php";

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        share.putExtra(Intent.EXTRA_SUBJECT, "Website Link");
        share.putExtra(Intent.EXTRA_TEXT, url);

        startActivity(Intent.createChooser(share, "Share News!"));
    }
}
