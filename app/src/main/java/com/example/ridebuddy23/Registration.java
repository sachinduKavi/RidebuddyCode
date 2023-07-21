package com.example.ridebuddy23;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    Button reg_btn;
    String state;
    CheckBox checkBox;
    Bitmap bitmap_image;
    EditText registration_number, chassis_number, seats;
    TextView image_link;
    Spinner spinner;
    SharedPreferences sharedPreferences;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // Setting up progress bar

        sharedPreferences = getSharedPreferences("User_Details", MODE_PRIVATE);

        spinner = findViewById(R.id.spinner);
        checkBox = findViewById(R.id.checkBox);
        image_link = findViewById(R.id.imageLinks);
        registration_number = findViewById(R.id.registration_number);
        chassis_number = findViewById(R.id.chassis_number);
        seats = findViewById(R.id.seats);

        String colors[] = {"Car", "Van", "Bike/Scooter", "E-bike", "Bus"};
        ArrayAdapter<String> spinnerArrayAdapter;
        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colors);
        spinner.setAdapter(spinnerArrayAdapter);


        reg_btn = findViewById(R.id.register_btn);

        // Creating activity result launcher
        activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            image_link.setText("Image Selected");
                            try {
                                bitmap_image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });

    }

    public void checkBoxClick(View view) {
        if(checkBox.isChecked()){
            reg_btn.setEnabled(true);
        }else{
            reg_btn.setEnabled(false);
        }
    }

    public void onRegistration(View view) {
        // Sending bitmap image to Server
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if(bitmap_image != null){
            bitmap_image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

            // Creating Volley requests
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "https://ridebuddy.000webhostapp.com/vehicle_registration.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("Response" + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        state = jsonObject.getString("state");
                        System.out.println("Activity State: " + state);
                        // Check whether data inserted or not
                        if(state.equals("Data Inserted"))
                            Toast.makeText(getApplicationContext(), "Vehicle registered successfully", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "Failed vehicle registration", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MyProfile.class));

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error" + error);
                }
            }){
                protected Map<String, String> getParams(){
                    Map<String, String> paramV = new HashMap<>();
                    paramV.put("vehicle_image", base64Image);
                    paramV.put("vehicle_type", spinner.getSelectedItem().toString());
                    paramV.put("r_number", registration_number.getText().toString());
                    paramV.put("c_number", chassis_number.getText().toString());
                    paramV.put("seats", seats.getText().toString());
                    paramV.put("email", sharedPreferences.getString("email", "null"));
                    paramV.put("user_name", sharedPreferences.getString("user_name", "null"));

                    return paramV;
                }
            };
            queue.add(stringRequest);
        }

    }

    public void addImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);
    }

    public void crossBtn(View view) {

        startActivity(new Intent(this, MyProfile.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}