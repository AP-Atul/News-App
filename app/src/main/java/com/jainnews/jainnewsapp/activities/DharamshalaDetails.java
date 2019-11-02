package com.jainnews.jainnewsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jainnews.jainnewsapp.R;
import com.jainnews.jainnewsapp.navigation.DharamshalaActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DharamshalaDetails extends AppCompatActivity {

    private ImageView imageView;
    private TextView title, email, contact, address, desc;
    private WebView map;

    private JSONObject dharamshala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dharamshala_details);

        imageView = findViewById(R.id.imageNew);
        title = findViewById(R.id.title);
        email = findViewById(R.id.email);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.address);
        desc = findViewById(R.id.desc);
        map = findViewById(R.id.map);
        ScrollView scrollView = findViewById(R.id.scrollView);

        int id = getIntent().getIntExtra("id", -1);
        if(id != -1)
            dharamshala = DharamshalaActivity.getDharamshala(id);

        if(dharamshala != null)
            setDataToUI(dharamshala);

        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    private void setDataToUI(JSONObject dharamshala) {
        try {

            title.setText(dharamshala.getString("d_name"));
            email.setText("Email: " + dharamshala.getString("d_email"));
            contact.setText("Contact: " +dharamshala.getString("d_contact"));
            address.setText("Address: " +dharamshala.getString("d_address"));
            desc.setText("Description: " +dharamshala.getString("d_desc"));
            Picasso.get().load("http://jainnewsapp.000webhostapp.com/" + dharamshala.getString("d_img")).into(imageView);

            String url = dharamshala.getString("d_maps");

            WebSettings webSettings = map.getSettings();
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            map.loadData(url, "text/html", "utf-8");
            map.setWebChromeClient(new WebChromeClient());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
