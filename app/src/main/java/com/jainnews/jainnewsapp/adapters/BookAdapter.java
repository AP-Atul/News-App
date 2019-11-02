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
import com.jainnews.jainnewsapp.activities.ReadBook;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private Context context;
    private JSONArray jsonArray;

    public BookAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card, parent, false);
        return new BookAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {

            JSONObject object = jsonArray.getJSONObject(position);
            final String url = object.getString("book_iframe");
            holder.trendingTitle.setText(object.getString("book_name"));
            Picasso.get().load("http://jainnewsapp.000webhostapp.com/" + object.getString("book_cover")).into(holder.trendingImage);

            holder.trendingRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReadBook.class).putExtra("url", url));
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

            trendingRL = itemView.findViewById(R.id.trendingBookRL);
            trendingImage = itemView.findViewById(R.id.trendingBookImage);
            trendingTitle = itemView.findViewById(R.id.trendingBookTitle);
        }
    }
}
