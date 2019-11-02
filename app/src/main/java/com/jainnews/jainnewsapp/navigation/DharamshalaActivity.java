package com.jainnews.jainnewsapp.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.jainnews.jainnewsapp.HomePage;
import com.jainnews.jainnewsapp.R;
import com.jainnews.jainnewsapp.activities.DharamshalaDetails;
import com.jainnews.jainnewsapp.utils.MySingleton;
import com.jainnews.jainnewsapp.utils.ipUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DharamshalaActivity extends AppCompatActivity {

    private String hostel_url = ipUtil.getIp() + "android_api/routes/getStateList.php";

    private EditText searchBox;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private static JSONArray dharamshalas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dharamshala);

        searchBox = findViewById(R.id.searchBox);
        recyclerView = findViewById(R.id.dharamRV);
        progressBar = findViewById(R.id.progressDharam);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        (findViewById(R.id.search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = searchBox.getText().toString().trim();
                if(city.length() > 0){
                    progressBar.setVisibility(View.VISIBLE);
                    getAllHostelList(city);
                }
            }
        });

        (findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DharamshalaActivity.this, HomePage.class));
            }
        });
    }

    private void getAllHostelList(String city) {
        String new_hostel_url = hostel_url + "?city=" + city;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, new_hostel_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length() > 0) {
                            dharamshalas = response;
                            adapter = new DharamshalaListAdapter(DharamshalaActivity.this, response);
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
                        } else{
                            recyclerView.setAdapter(null);
                            Toast.makeText(getApplicationContext(), "No result found", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
        );
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    public static JSONObject getDharamshala(int id){
        try {
            return dharamshalas.getJSONObject(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public class DharamshalaListAdapter extends RecyclerView.Adapter<DharamshalaListAdapter.MyViewHolder>{

        Context context;
        JSONArray jsonArray;

        DharamshalaListAdapter(Context context, JSONArray jsonArray){
            this.context = context;
            this.jsonArray = jsonArray;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dharamshala_list_card, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            try {
                final int id = position;
                JSONObject object = jsonArray.getJSONObject(position);

                holder.name.setText(object.getString("d_name"));
                holder.address.setText(object.getString("d_city") + " " + object.getString("d_state"));
                holder.description.setText(object.getString("d_desc"));
                Picasso.get().load("http://jainnewsapp.000webhostapp.com/" + object.getString("d_img")).into(holder.imageView);

                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, DharamshalaDetails.class).putExtra("id", id));
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

            private RelativeLayout relativeLayout;
            private ImageView imageView;
            private TextView name, address, description;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);

                relativeLayout = itemView.findViewById(R.id.trendingRL);
                imageView = itemView.findViewById(R.id.trendingImage);
                name = itemView.findViewById(R.id.trendingTitle);
                address = itemView.findViewById(R.id.trendingAddress);
                description = itemView.findViewById(R.id.trendingDesc);
            }
        }
    }
}
