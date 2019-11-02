package com.jainnews.jainnewsapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.jainnews.jainnewsapp.R;
import com.jainnews.jainnewsapp.activities.VideoPlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    private Context context;
    private JSONArray jsonArray;

    public VideoAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_card, parent, false);
        return new VideoAdapter.MyViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {

            JSONObject object = jsonArray.getJSONObject(position);
            final String url = object.getString("video_link");
            holder.trendingTitle.setText(object.getString("video_name"));

            WebSettings webSettings = holder.trendingVideo.getSettings();
            webSettings.setAppCacheEnabled(true);
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
                    context.startActivity(new Intent(context, VideoPlay.class).putExtra("url", url));
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

            trendingRL = itemView.findViewById(R.id.trendingVRL);
            trendingVideo = itemView.findViewById(R.id.trendingVideo);
            trendingTitle = itemView.findViewById(R.id.trendingVideoTitle);
        }
    }
}
