package com.jainnews.jainnewsapp.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jainnews.jainnewsapp.HomePage;
import com.jainnews.jainnewsapp.R;
import com.jainnews.jainnewsapp.activities.ReadBook;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        RecyclerView recyclerView = findViewById(R.id.booksRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        JSONArray bookArray = HomePage.getBooksArray();

        if(bookArray != null){
            RecyclerView.Adapter adapter = new BooksListAdapter(this, bookArray);
            recyclerView.setAdapter(adapter);
        }

        (findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookActivity.this, HomePage.class));
            }
        });
    }

    class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.MyViewHolder> {

        Context context;
        JSONArray jsonArray;

        BooksListAdapter(Context context, JSONArray jsonArray) {
            this.context = context;
            this.jsonArray = jsonArray;
        }

        @NonNull
        @Override
        public BooksListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.books_list_card, parent, false);
            return new BooksListAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BooksListAdapter.MyViewHolder holder, int position) {
            try {

                JSONObject object = jsonArray.getJSONObject(position);
                final String url = object.getString("book_iframe");
                holder.trendingTitle.setText(object.getString("book_name"));
                holder.trendingAuthor.setText("Author: " + object.getString("book_author"));
                holder.trendingDesc.setText("Description: " + object.getString("book_desc"));
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

            private RelativeLayout trendingRL;
            private ImageView trendingImage;
            private TextView trendingTitle, trendingAuthor, trendingDesc;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);

                trendingRL = itemView.findViewById(R.id.trendingRL);
                trendingImage = itemView.findViewById(R.id.trendingImage);
                trendingTitle = itemView.findViewById(R.id.trendingTitle);
                trendingAuthor = itemView.findViewById(R.id.trendingAuthor);
                trendingDesc = itemView.findViewById(R.id.trendingDesc);
            }
        }
    }
}
