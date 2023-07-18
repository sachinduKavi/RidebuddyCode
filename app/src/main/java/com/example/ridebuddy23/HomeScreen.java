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
import java.util.ArrayList;

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
        Thread thread = new LoadAdvertisements();
        thread.start();
    }

    public void create_ride(View view) {
    }

    public void go_myProfile(View view) {
        Intent intent = new Intent(this, MyProfile.class);
        startActivity(intent);
    }

    class CustomAdapter extends ArrayAdapter<String>{
        Context context;
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

    class LoadAdvertisements extends Thread{
        public void run(){
            try {
                URL get_image = new URL("https://ridebuddy.000webhostapp.com/get_image_lists.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) get_image.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                System.out.println("###################Testing");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String all_data = "", line;
                while((line = bufferedReader.readLine()) != null){
                    all_data += line;
                }
                System.out.println(all_data);
                if(all_data.length() > 35){
                    JSONObject jsonObject = new JSONObject(all_data);
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        ArrayList image_links_list = new ArrayList<String>();
                        JSONArray image_links = jsonObject.getJSONArray("image_links");

                        for(int i = 0; i < image_links.length(); i++){
                            JSONObject single_link = image_links.getJSONObject(i);
                            System.out.println(single_link.getString("image_link"));
                            image_links_list.add(single_link.getString("image_link"));
                        }
                        // Implementing Listview
                        HomeScreen.CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), image_links_list);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ListView listView = findViewById(R.id.list_view);
                                listView.setAdapter(customAdapter);

                            }
                        });
                    }
                }

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                System.out.println("Adds are not loading");
            }
        }
    }
}


