package com.example.ridebuddy23;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    EditText user_email, user_password;
    public Dialog progressbar_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Defining xml elements
        user_email = findViewById(R.id.email_address);
        user_password = findViewById(R.id.password);

        SharedPreferences sharedPreferences = getSharedPreferences("User_Details", MODE_PRIVATE);
        String email_sh = sharedPreferences.getString("email", null);


        // By Passing login screen
        if(email_sh != null){
            Intent intent = new Intent(this, HomeScreen.class);
            startActivity(intent);
        }

    }

    public void login_click(View view) {
        String user_email_final = user_email.getText().toString();

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("userID", "null");
        params.put("userEmail", user_email_final);
        CreateConnection connection = new CreateConnection("userDetails/getUserDetails", params, HttpURLConnection.HTTP_OK, "GET");
        connection.start();

        try {
            connection.join();
            JSONObject data = connection.getJsonResponse().getJSONArray("data").getJSONObject(0);
            System.out.println(data);

            // Creating userDetails object
            UserDetails userDetails = new UserDetails(
                    data.getString("f_name") + " " + data.getString("l_name"),
                    data.getString("user_email"),
                    data.getString("mobileNumber"),
                    data.getString("password"),
                    data.getString("dp_image"),
                    data.getInt("rating"),
                    data.getString("dob"),
                    (byte)data.getInt("gender"),
                    data.getString("userID")
            );

            startActivity(new Intent(this, MainActivity.class));

            // Passing for authentication
            System.out.println("User Details: " + userDetails);
            authentication(userDetails);

        } catch (InterruptedException | JSONException e) {
            Toast.makeText(this, "Incorrect User Name", Toast.LENGTH_SHORT).show();
        }





    }

    public void authentication(UserDetails userDetails){
        System.out.println(userDetails);
//        progressbar_layout.dismiss();
        if(userDetails == null) {
            Toast.makeText(this, "Invalid email address, Click link to create an account", Toast.LENGTH_LONG).show();
            user_password.setText("");
        }
        else if(user_password.getText().toString().equals(userDetails.getPassword())){
            // Saving account in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("User_Details", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_name", userDetails.getUser_name());
            editor.putString("email", userDetails.getEmail());
            editor.putString("mobile_num", userDetails.getMobile());
            editor.putString("password", userDetails.getPassword());
            editor.putString("DP", userDetails.getDP());
            editor.putInt("profile_state", userDetails.getProfile_state());
            editor.putString("userID", userDetails.getUserID());
            editor.putString("user_dob", userDetails.getUser_dob());
            editor.putInt("gender", userDetails.getGender());
            editor.apply();

            Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show();
            System.out.println("Login success");
            Intent login = new Intent(this, HomeScreen.class);
            startActivity(login);
        }else{
            Toast.makeText(this, "Incorrect Password", Toast.LENGTH_LONG).show();
            user_password.setText("");
            user_email.setText("");
        }
//        System.out.println(user_password.getText().toString() + " - " + userDetails.getPassword() + userDetails);
    }

    public void Sign_up(View view) {
        Intent i = new Intent(this, CreateUserAccount.class);
        startActivity(i);
    }
}




