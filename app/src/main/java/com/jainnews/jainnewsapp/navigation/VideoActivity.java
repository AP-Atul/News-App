package com.jainnews.jainnewsapp.navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jainnews.jainnewsapp.HomePage;
import com.jainnews.jainnewsapp.R;
import com.jainnews.jainnewsapp.activities.VideoPlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        RecyclerView recyclerView = findViewById(R.id.videoRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        JSONArray videoArray = HomePage.getVideosArray();

        if(videoArray != null){
            RecyclerView.Adapter adapter = new VideosListAdapter(this, videoArray);
            recyclerView.setAdapter(adapter);
        }

        (findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VideoActivity.this, HomePage.class));
            }
        });
    }

    class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.MyViewHolder> {

        Context context;
        JSONArray jsonArray;

        VideosListAdapter(Context context, JSONArray jsonArray) {
            this.context = context;
            this.jsonArray = jsonArray;
        }

        @NonNull
        @Override
        public VideosListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_list_card, parent, false);
            return new VideosListAdapter.MyViewHolder(view);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onBindViewHolder(@NonNull VideosListAdapter.MyViewHolder holder, int position) {
            try {

                final JSONObject object = jsonArray.getJSONObject(position);
                final String url = object.getString("video_link");

                holder.trendingTitle.setText(object.getString("video_name"));

                WebSettings webSettings = holder.trendingVideo.getSettings();
                webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                holder.trendingVideo.loadData(url, "text/html", "utf-8");
                holder.trendingVideo.setWebChromeClient(new WebChromeClient());

                holder.trendingVideo.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });

                holder.trendingRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(context, VideoPlay.class).putExtra("url", url));
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            private LinearLayout trendingRL;
            private WebView trendingVideo;
            private TextView trendingTitle;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);

                trendingRL = itemView.findViewById(R.id.trendingRL);
                trendingVideo = itemView.findViewById(R.id.trendingImage);
                trendingTitle = itemView.findViewById(R.id.trendingTitle);
            }
        }
    }
}
