package com.example.ridebuddy23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.HttpURLConnection;
import java.util.LinkedHashMap;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    LatLng[] staEnd = {null, null};
    int coordinateCounter = -1;
    JourneyData journeyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        journeyData = getIntent().getParcelableExtra("journeyData");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng sriLanka = new LatLng(7.8731, 80.7718);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(sriLanka, 7));

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                map.clear();
                if(coordinateCounter < 1)
                    coordinateCounter++;
                else {
                    staEnd[0] = null;
                    staEnd[1] = null;
                    coordinateCounter = 0;
                }

                // Updating the list
                staEnd[coordinateCounter] = latLng;


                System.out.println(coordinateCounter);

                for(int i = 0; i < 2; i++){
                    if(staEnd[i] != null) {
                        MarkerOptions markerOptions = new MarkerOptions().position(staEnd[i]);
                        if (i == 0) {
                            markerOptions.title("Staring Point");
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        } else {
                            markerOptions.title("Destination Point");
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        }
                        map.addMarker(markerOptions);
                    }
                }

                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Your Selected Position");
                map.addMarker(markerOptions);

            }
        });

    }

    public void submitData(View view) {
        boolean filed = true;
        for(LatLng l: staEnd) {
            if (l == null) {
                filed = false;
                break;
            }
        }

        if(filed) {
            journeyData.setCoordinates(staEnd[0].latitude, staEnd[0].longitude, staEnd[1].latitude, staEnd[1].longitude);
            URLConnection(journeyData);
        }
    }

    public void URLConnection(JourneyData journeyData) {
        SharedPreferences sharedPreferences = getSharedPreferences("User_Details", MODE_PRIVATE);
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("userID", sharedPreferences.getString("userID", "null"));
        params.put("venue", journeyData.getDateTime());
        params.put("startingCity", journeyData.getStartCity());
        params.put("endCity", journeyData.getEndCity());
        params.put("sLongitudes", journeyData.getsLog());
        params.put("sLatitudes", journeyData.getsLat());
        params.put("eLongitudes", journeyData.geteLog());
        params.put("eLatitudes", journeyData.geteLat());
        System.out.println("Description Eka koo : " + journeyData.getDescription());
        params.put("description", journeyData.getDescription());

        System.out.println(journeyData);

        CreateConnection connection = new CreateConnection("journey/newJourney", params, HttpURLConnection.HTTP_CREATED, "POST");
        connection.start();

        try {
            connection.join();
            if(connection.responseCode == HttpURLConnection.HTTP_CREATED)
                Toast.makeText(this, "Journey Created Successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Unable to Create new Journey", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeScreen.class));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void addLocation(View view) {
        boolean filed = true;
        for(LatLng l: staEnd) {
            if (l == null) {
                filed = false;
                break;
            }
        }

        if(filed) {
            TextView startingCor = findViewById(R.id.startCor);
            TextView endCor = findViewById(R.id.endCor);

            startingCor.setText("S Lat:" + Double.toString(staEnd[0].latitude) + "\n   Lon:" + Double.toString(staEnd[0].longitude));
            endCor.setText("E Lat:" + Double.toString(staEnd[1].latitude) + "\n   Lon:" + Double.toString(staEnd[1].longitude));
        } else {
            Toast.makeText(this, "Please add two pointers", Toast.LENGTH_LONG).show();
        }

    }
}