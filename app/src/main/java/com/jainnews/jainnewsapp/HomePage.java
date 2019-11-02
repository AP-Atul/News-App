package com.jainnews.jainnewsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.jainnews.jainnewsapp.adapters.BookAdapter;
import com.jainnews.jainnewsapp.adapters.NewsAdapter;
import com.jainnews.jainnewsapp.adapters.VideoAdapter;
import com.jainnews.jainnewsapp.navigation.AudioActivity;
import com.jainnews.jainnewsapp.navigation.BookActivity;
import com.jainnews.jainnewsapp.navigation.ContactActivity;
import com.jainnews.jainnewsapp.navigation.DharamshalaActivity;
import com.jainnews.jainnewsapp.navigation.MemberActivity;
import com.jainnews.jainnewsapp.navigation.NewsActivity;
import com.jainnews.jainnewsapp.navigation.VideoActivity;
import com.jainnews.jainnewsapp.utils.MySingleton;
import com.jainnews.jainnewsapp.utils.ipUtil;
import com.google.android.material.navigation.NavigationView;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static android.Manifest.permission.INTERNET;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerViewNews, recyclerViewVideo ,recyclerViewBook;
    private RecyclerView.Adapter adapter;
    private ProgressBar progressBar;

    private String news_url = ipUtil.getIp() + "android_api/routes/getNews.php";
    private String videos_url = ipUtil.getIp() + "android_api/routes/getVideo.php";
    private String books_url = ipUtil.getIp() + "android_api/routes/getBooks.php";

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.zero, R.drawable.one, R.drawable.two, R.drawable.three };

    private static final int PERMISSION_REQUEST_CODE = 200;
    public static JSONArray newsArray, videosArray, booksArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressBar = findViewById(R.id.progressHome);
        progressBar.setVisibility(View.VISIBLE);

        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        });

        recyclerViewNews = findViewById(R.id.trendingNewsRV);
        recyclerViewNews.setHasFixedSize(true);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerViewVideo = findViewById(R.id.trendingVideosRV);
        recyclerViewVideo.setHasFixedSize(true);
        recyclerViewVideo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerViewBook = findViewById(R.id.trendingBooksRV);
        recyclerViewBook.setHasFixedSize(true);
        recyclerViewBook.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        if (!checkPermission()) {

            requestPermission();

        } else {
            addTrendingNews();
            addTrendingVideos();
            addTrendingBooks();
        }

        (findViewById(R.id.newsMore)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, NewsActivity.class));
            }
        });

        (findViewById(R.id.videosMore)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, VideoActivity.class));
            }
        });

        (findViewById(R.id.booksMore)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, BookActivity.class));
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);

        return result == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{INTERNET}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && PERMISSION_REQUEST_CODE == requestCode) {

            boolean linternetAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (linternetAccepted)
                Toast.makeText(getApplicationContext(), "Permission Granted.", Toast.LENGTH_LONG).show();
            else {

                Toast.makeText(getApplicationContext(), "Permission denied.", Toast.LENGTH_LONG).show();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(INTERNET)) {
                        showMessageOKCancel(
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{INTERNET},
                                                PERMISSION_REQUEST_CODE);
                                    }
                                });
                    }
                }

            }
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomePage.this)
                .setMessage("You need to allow access to the permissions")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void addTrendingNews() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, news_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            newsArray = new JSONArray();
                            for(int i = 0; i < 5 && i != response.length(); i++){
                                    newsArray.put(response.get(i));
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter = new NewsAdapter(HomePage.this, response);
                        recyclerViewNews.setAdapter(adapter);
                        Log.d("JSON RESPONSE: ", response.toString());
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(getApplicationContext(), "Problem connecting to the server.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void addTrendingVideos() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, videos_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            videosArray = new JSONArray();
                            for(int i = 0; i < 5 && i != response.length(); i++){
                                videosArray.put(response.get(i));
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter = new VideoAdapter(HomePage.this, response);
                        recyclerViewVideo.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred

                    }
                }
        );

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void addTrendingBooks() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, books_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            booksArray = new JSONArray();
                            for(int i = 0; i < 5 && i != response.length(); i++){
                                booksArray.put(response.get(i));
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter = new BookAdapter(HomePage.this, response);
                        recyclerViewBook.setAdapter(adapter);

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


    public static JSONArray getNewsArray(){
        return newsArray;
    }

    public static JSONArray getVideosArray(){
        return videosArray;
    }

    public static JSONArray getBooksArray(){
        return booksArray;
    }

    public static JSONObject getNewsObject(int id){
        try {
            return newsArray.getJSONObject(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

        @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(HomePage.this, HomePage.class));
        } else if (id == R.id.nav_news){
            startActivity(new Intent(HomePage.this, NewsActivity.class));
        } else if (id == R.id.nav_audio) {
            startActivity(new Intent(HomePage.this, AudioActivity.class));
        } else if (id == R.id.nav_video){
            startActivity(new Intent(HomePage.this, VideoActivity.class));
        }else if (id == R.id.nav_books){
            startActivity(new Intent(HomePage.this, BookActivity.class));
        }else if (id == R.id.nav_dharmashala){
            startActivity(new Intent(HomePage.this, DharamshalaActivity.class));
        }else if (id == R.id.nav_member){
            startActivity(new Intent(HomePage.this, MemberActivity.class));
        }else if (id == R.id.nav_contact){
            startActivity(new Intent(HomePage.this, ContactActivity.class));
        }
//        else if(id == R.id.nav_exit){
//            closeAppAndLogOut();
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void closeAppAndLogOut() {
//        SharedPreferences sharedPref = getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
//        sharedPref.edit().clear().commit();
//        Toast.makeText(getApplicationContext(), "Logout successfull", Toast.LENGTH_SHORT).show();
//    }
}
