package com.jainnews.jainnewsapp.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jainnews.jainnewsapp.HomePage;
import com.jainnews.jainnewsapp.R;
import com.jainnews.jainnewsapp.activities.NewsDetails;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        RecyclerView recyclerView = findViewById(R.id.newsRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        JSONArray newsArray = HomePage.getNewsArray();

        if(newsArray != null){
            RecyclerView.Adapter adapter = new NewsListAdapter(this, newsArray);
            recyclerView.setAdapter(adapter);
        }

        (findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewsActivity.this, HomePage.class));
            }
        });
    }

    class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.MyViewHolder> {

        Context context;
        JSONArray jsonArray;

        NewsListAdapter(Context context, JSONArray jsonArray) {
            this.context = context;
            this.jsonArray = jsonArray;
        }

        @NonNull
        @Override
        public NewsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_card, parent, false);
            return new NewsListAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NewsListAdapter.MyViewHolder holder, int position) {
            try {
                final int id = position;
                JSONObject object = jsonArray.getJSONObject(position);
                holder.trendingTitle.setText(object.getString("news_title"));
                Picasso.get().load(object.getString("news_img_1")).into(holder.trendingImage);

                holder.trendingRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, NewsDetails.class).putExtra("id", id));
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
            private ImageView trendingImage;
            private TextView trendingTitle;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);

                trendingRL = itemView.findViewById(R.id.trendingRL);
                trendingImage = itemView.findViewById(R.id.trendingImage);
                trendingTitle = itemView.findViewById(R.id.trendingTitle);
            }
        }
    }
}
