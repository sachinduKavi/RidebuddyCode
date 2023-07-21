package com.example.ridebuddy23;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VehicleInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_information);


        String[] names = {"BHM2476", "YN3487", "CGH762", "JCB4586"};
        new VehicleFetch().start();
        System.out.println("Testing....");


    }

    public void createVehicle(String regNum, String imageLink){
        LinearLayout linearLayout = findViewById(R.id.container_v);
        View view = getLayoutInflater().inflate(R.layout.vehicle_card, null);

        TextView reg_num = view.findViewById(R.id.reg_number);

        ImageView veh_image = view.findViewById(R.id.vehicle_image);
        if(imageLink != null) {
            new SetImageFromURL(veh_image, "https://ridebuddy.000webhostapp.com/" + imageLink);
            reg_num.setText(regNum);
        }else {
            veh_image.setImageResource(R.drawable.add_icon);
            reg_num.setText("");
        }
        veh_image.setClickable(true);
        veh_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Registration.class));
            }
        });

        linearLayout.addView(view);
    }

    class VehicleFetch extends Thread{
        public void run(){
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String url = "https://ridebuddy.000webhostapp.com/pullVehicles.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("Response:" + response);
                    // Converting string to json.

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("Status");
                        if(status.equals("Success")){
                            JSONArray vehicleDetails = jsonObject.getJSONArray("data");
                            System.out.println(vehicleDetails.length());

//                            VehicleDetails[] vehicleDetails1 = new VehicleDetails[vehicleDetails.length()];
                            for(int i = 0; i < vehicleDetails.length(); i++){
                                JSONObject singleVehicle = vehicleDetails.getJSONObject(i);
                                createVehicle(singleVehicle.getString("reg_num"), singleVehicle.getString("image_link"));
                            }

                        }
                        createVehicle(null, null);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error: " + error);
                }
            }){
                protected Map<String, String> getParams(){
                    SharedPreferences sharedPreferences = getSharedPreferences("User_Details", MODE_PRIVATE);
                    String email = sharedPreferences.getString("email", "null");
                    Map<String, String> paramV = new HashMap<>();
                    paramV.put("user_email", email);

                    return paramV;
                }

            };
            requestQueue.add(stringRequest);
        }
    }
}
