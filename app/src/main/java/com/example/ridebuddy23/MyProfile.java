package com.example.ridebuddy23;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyProfile extends AppCompatActivity {
    TextView user_name_t, user_email, mobile_num, profile_state;
    Dialog confirm_dialog;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        user_name_t = findViewById(R.id.name_user);
        user_email = findViewById(R.id.user_email);
        mobile_num = findViewById(R.id.mobile_number);
        profile_state = findViewById(R.id.profile_state);
        ImageView profile_image = findViewById(R.id.profile_image);

        // Opening shared preferences
        sharedPreferences = getSharedPreferences("User_Details", MODE_PRIVATE);
        user_name_t.setText(sharedPreferences.getString("user_name", ""));
        user_email.setText(sharedPreferences.getString("email", "null@gmail.com"));
        mobile_num.setText(sharedPreferences.getString("mobile_num", "null"));
        profile_state.setText("Profile state: " + sharedPreferences.getInt("profile_state", 0) + "/5");

        // Fetching profile picture from database
        String dp_link = sharedPreferences.getString("DP", "No Image");
        if(!dp_link.equals("default"))
            new SetImageFromURL(profile_image, "https://ridebuddy.000webhostapp.com/" + dp_link);

    }

    public void back_home(View view) {
        startActivity(new Intent(this, MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void EditProfile(View view) {
        startActivity(new Intent(this, EditProfile.class));
    }

    public void logOut(View view) {
        Runnable runnable01 = new Runnable() {
            @Override
            public void run() {
                sharedPreferences.edit().clear().apply();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        };
        ConfirmDialogBox confirmDialogBox = new ConfirmDialogBox(MyProfile.this, "Logout", "Are you sure you want to log out? you can login any time again.", runnable01);
        confirmDialogBox.showDialogBox();
    }

    public void manage_vehicle(View view) {
        startActivity(new Intent(this, VehicleInformation.class));
    }
}