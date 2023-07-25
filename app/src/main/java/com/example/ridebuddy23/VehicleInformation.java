package com.example.ridebuddy23;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VehicleInformation extends AppCompatActivity {
    VehicleDetails[] vehicleObjList;
    BottomSheetDialog vehicle_information;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_information);

        // PopUp
        vehicle_information = new BottomSheetDialog(this);
        vehicle_information.setContentView(R.layout.vehicle_details);

        new VehicleFetch().start();
    }

    public void myProfile(){startActivity(new Intent(this, MyProfile.class));}

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
        if(imageLink == null) {
            veh_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), Registration.class));
                }
            });
        }else{
            veh_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Click " + regNum, Toast.LENGTH_SHORT).show();
                    VehicleDetails thisVehicle = null;
                    for(VehicleDetails singleObj: vehicleObjList){
                        if(regNum.equals(singleObj.getRegNum())){
                            thisVehicle = singleObj;
                        }
                    }
                    System.out.println(thisVehicle);
                    bottomSheet(thisVehicle);
                    vehicle_information.show();
                }
            });
        }

        linearLayout.addView(view);
    }

    public void bottomSheet(VehicleDetails vehicle) {
        TextView vehicle_type = vehicle_information.findViewById(R.id.type_v);
        TextView reg_num = vehicle_information.findViewById(R.id.reg_number);
        TextView chassis_number = vehicle_information.findViewById(R.id.chassis_num);
        TextView seats = vehicle_information.findViewById(R.id.seats);
        TextView status = vehicle_information.findViewById(R.id.vehicle_level);
        ImageView image = (ImageView) vehicle_information.findViewById(R.id.vehicle_image);

        vehicle_type.setText(vehicle.getVehicleType());
        reg_num.setText(vehicle.getRegNum());
        chassis_number.setText(vehicle.getChassisNumber());
        seats.setText("" +vehicle.getSeats());
        status.setText(vehicle.getLevel());

        new SetImageFromURL(image, "https://ridebuddy.000webhostapp.com/" + vehicle.getImageLink());
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

                            vehicleObjList = new VehicleDetails[vehicleDetails.length()];
                            for(int i = 0; i < vehicleDetails.length(); i++){
                                JSONObject singleVehicle = vehicleDetails.getJSONObject(i);
                                createVehicle(singleVehicle.getString("reg_num"), singleVehicle.getString("image_link"));
                                // Add values to separate obj
                                vehicleObjList[i] = new VehicleDetails(
                                        singleVehicle.getString("vehicle_type"),
                                        singleVehicle.getString("image_link"),
                                        singleVehicle.getString("reg_num"),
                                        singleVehicle.getString("chassis_num"),
                                        singleVehicle.getString("level"),
                                        Integer.parseInt(singleVehicle.getString("seats"))
                                );
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
