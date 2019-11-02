package com.jainnews.jainnewsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainnews.jainnewsapp.R;
import com.jainnews.jainnewsapp.activities.NewsDetails;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private Context context;
    private JSONArray jsonArray;

    public NewsAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card, parent, false);
        return new NewsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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
