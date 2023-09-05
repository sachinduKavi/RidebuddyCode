package com.example.ridebuddy23;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ridebuddy23.databinding.ActivitiesSingleRowBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnActivities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_activities);

        // Fetching journey data from the API
        SharedPreferences sharedPreferences = getSharedPreferences("User_Details", MODE_PRIVATE);
        HashMap<String, Object> params = new HashMap<>();
        params.put("userID", sharedPreferences.getString("userID", "null"));
        CreateConnection connection = new CreateConnection("journey/getJourney", params, HttpURLConnection.HTTP_OK, "GET");
        connection.start();

        try {
            connection.join();
            if(connection.responseCode == 200) {
                JSONArray jsonArray = connection.getJsonResponse().getJSONArray("data");
                // Creating ArrayList with journeyIDs
                String[] journeyIDs = new String[jsonArray.length()];
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    journeyIDs[i] = jsonObject.getString("_id");
                    System.out.println(journeyIDs[i]);
                }

                // Creating custom adapter
                JourneyAdapter journeyAdapter = new JourneyAdapter(this, jsonArray, journeyIDs);
                ListView listView = findViewById(R.id.activitiesListView);
                listView.setAdapter(journeyAdapter);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    class JourneyAdapter extends ArrayAdapter<String> {
        JSONArray journeyJson;
        JourneyAdapter(@NonNull Context context, @Nullable JSONArray journeyJson, String[] arrayList) {
            super(context, R.layout.activities_single_row, arrayList);
            this.journeyJson = journeyJson;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View singleView, ViewGroup parent){
            System.out.println(singleView);
            if(singleView == null)
                singleView = getLayoutInflater().inflate(R.layout.activities_single_row, parent, false);

            TextView journeyID = singleView.findViewById(R.id.journeyID);
            TextView sCity = singleView.findViewById(R.id.startingCityText);
            TextView eCity = singleView.findViewById(R.id.endingCityID);
            TextView summitedOm = singleView.findViewById(R.id.time);
            TextView status = singleView.findViewById(R.id.statusText);
            JSONObject jsonObject = null;
            try {
                jsonObject = journeyJson.getJSONObject(position);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                journeyID.setText("Journey ID : " + jsonObject.getString("_id"));
            } catch (JSONException e) {
                System.out.println("Error json handling : " + e);
            }

            try {
                sCity.setText("From : " + jsonObject.getString("startingCity"));
            } catch (JSONException e) {
                System.out.println("Error json handling : " + e);
            }
            try {
                eCity.setText("To   : " + jsonObject.getString("endCity"));
            } catch (JSONException e) {
                System.out.println("Error json handling : " + e);
            }
            try {
                summitedOm.setText("Posted on : " + jsonObject.getString("postedOn"));
            } catch (JSONException e) {
                System.out.println("Error json handling : " + e);
            }
            try {
                status.setText("Trip Status : " + jsonObject.getString("status"));
                if(jsonObject.getString("status").equals("Active"))
                    status.setTextColor(Color.GREEN);
            } catch (JSONException e) {
                System.out.println("Error json handling : " + e);
            }

            return singleView;
        }

    }
}

