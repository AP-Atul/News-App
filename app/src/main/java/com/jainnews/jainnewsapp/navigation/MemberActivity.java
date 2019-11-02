package com.jainnews.jainnewsapp.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.jainnews.jainnewsapp.utils.MySingleton;
import com.jainnews.jainnewsapp.utils.ipUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MemberActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private String member_url = ipUtil.getIp() + "android_api/routes/getMembers.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        recyclerView = findViewById(R.id.membersRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        (findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MemberActivity.this, HomePage.class));
            }
        });

        getMemberList();
    }

    private void getMemberList() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, member_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        adapter = new MemberListAdapter(MemberActivity.this, response);
                        recyclerView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MyViewHolder>{
        Context context;
        JSONArray jsonArray;

        MemberListAdapter(Context context, JSONArray jsonArray) {
            this.context = context;
            this.jsonArray = jsonArray;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_list_card, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            try {
                JSONObject object = jsonArray.getJSONObject(position);

                holder.name.setText(object.getString("member_name"));
                holder.email.setText(object.getString("member_email"));
                holder.contact.setText(object.getString("member_contact"));
                holder.description.setText(object.getString("member_desc"));
                Picasso.get().load("http://jainnewsapp.000webhostapp.com/img/" + object.getString("member_image")).into(holder.imageView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            private ImageView imageView;
            private TextView name, email, contact, description;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.trendingImage);
                name = itemView.findViewById(R.id.trendingName);
                email = itemView.findViewById(R.id.trendingEmail);
                contact = itemView.findViewById(R.id.trendingContact);
                description = itemView.findViewById(R.id.trendingDesc);
            }
        }
    }
}
