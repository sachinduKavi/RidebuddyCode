package com.example.ridebuddy23;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Displaying user name in the home screen
        SharedPreferences sharedPreferences = getSharedPreferences("User_Details", MODE_PRIVATE);
        String user_name = sharedPreferences.getString("user_name", " ");
        TextView user_name_text = findViewById(R.id.user_name);
        user_name_text.setText("Welcome,\n" + user_name);

        ImageView profile_image = findViewById(R.id.profile_image);

        // Fetching profile picture from database
        String dp_link = sharedPreferences.getString("DP", "No Image");
        System.out.println("Image Link: " + dp_link);
        if(!dp_link.equals("default"))
            new SetImageFromURL(profile_image, "https://ridebuddy.000webhostapp.com/" + dp_link);

        // Loading advertisements
        new LoadAdvertisements();

    }

    public void create_ride(View view) {
        startActivity(new Intent(this, JourneyRegister.class));
    }

    public void myActivities(View view) {

        System.out.println("Function is working");
        startActivity(new Intent(this, OnActivities.class));
    }

    public void go_myProfile(View view) {
        Intent intent = new Intent(this, MyProfile.class);
        startActivity(intent);
    }

    class CustomAdapter extends ArrayAdapter<String>{
        ArrayList link_list;
        public CustomAdapter(@NonNull Context context, ArrayList link_list){
            super(context, R.layout.feature_single_row, link_list);
            this.link_list = link_list;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, ViewGroup parent){
            if(convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.feature_single_row, parent, false);

            ImageView image = convertView.findViewById(R.id.load_image);
            try {
                Picasso.get()
                        .load(String.valueOf(new URL((String) link_list.get(position))))
                        .into(image);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return convertView;
        }
    }

    class LoadAdvertisements {
        LoadAdvertisements(){
            CreateConnection connection = new CreateConnection("banners/getBanners", new HashMap<>(), HttpURLConnection.HTTP_OK, "GET");
            connection.start();
            try {
                connection.join();
                JSONArray bannerArray = connection.getJsonResponse().getJSONArray("data");
                if (bannerArray != null) {
                    ArrayList<String> bannerLinks = new ArrayList<>();
                    for (int i = 0; i < bannerArray.length(); i++) {
                        System.out.println(bannerArray.getJSONObject(i).getString("bannerLink"));
                        bannerLinks.add(bannerArray.getJSONObject(i).getString("bannerLink"));
                    }
                    HomeScreen.CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), bannerLinks);
                    runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ListView listView = findViewById(R.id.list_view);
                                listView.setAdapter(customAdapter);
                            }
                        });
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


        }
    }
}


