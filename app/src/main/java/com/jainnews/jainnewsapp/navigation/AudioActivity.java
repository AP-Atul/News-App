package com.jainnews.jainnewsapp.navigation;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AudioActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ProgressBar progressBar;

    private MediaPlayer mediaPlayer = null;
    private String audio_url = ipUtil.getIp() + "android_api/routes/getAudio.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        progressBar = findViewById(R.id.progressAudio);
        recyclerView = findViewById(R.id.audioRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar.setVisibility(View.VISIBLE);
        addTrendingBooks();

        (findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null)
                    mediaPlayer.release();
                startActivity(new Intent(AudioActivity.this, HomePage.class));
            }
        });
    }

    private void addTrendingBooks() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, audio_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        adapter = new AudioListAdapter(AudioActivity.this, response);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null)
            mediaPlayer.release();
    }

    public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.MyViewHolder> {

        JSONArray jsonArray;
        Context context;

        AudioListAdapter(Context context, JSONArray jsonArray) {
            this.jsonArray = jsonArray;
            this.context = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_list_card, parent, false);
            return new AudioListAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
            try {
                JSONObject object = jsonArray.getJSONObject(position);
                final String url = "http://jainnewsapp.000webhostapp.com/audios/" + object.getString("audio_url");

                holder.audioTitle.setText(object.getString("audio_name"));
                holder.audioDuration.setText(object.getString("audio_duration"));
                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if(mediaPlayer != null && mediaPlayer.isPlaying())
                            {
//                                Toast.makeText(context, "stopped", Toast.LENGTH_SHORT).show();
                                stopAudio(holder);
                            }else{
                                playAudio(url, holder);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

        private void playAudio(String url, final MyViewHolder holder) throws Exception {
//            Toast.makeText(context, "start", Toast.LENGTH_SHORT).show();

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Drawable myDrawable = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
            holder.playButton.setImageDrawable(myDrawable);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_play_black_24dp);
                    holder.playButton.setImageDrawable(myDrawable);
                }
            });
        }

        private void stopAudio(final  MyViewHolder holder){
            if(mediaPlayer!=null) {
                try {
                    mediaPlayer.reset();
                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_play_black_24dp);
                    holder.playButton.setImageDrawable(myDrawable);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            private TextView audioTitle, audioDuration;
            private RelativeLayout relativeLayout;
            private ImageView playButton;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);

                audioTitle = itemView.findViewById(R.id.audioTitle);
                audioDuration = itemView.findViewById(R.id.audioDur);
                relativeLayout = itemView.findViewById(R.id.trendingARL);
                playButton = itemView.findViewById(R.id.playButton);
            }
        }
    }
}
