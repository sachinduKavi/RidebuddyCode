package com.example.ridebuddy23;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class VehicleInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_information);


        LinearLayout linearLayout = findViewById(R.id.vehicle_layout);
        CardView cardView = new CardView(this);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)cardView.getLayoutParams();
        layoutParams.height = 100;
        layoutParams.width = 100;

        cardView.setLayoutParams(layoutParams);
        cardView.setMinimumHeight(200);
        linearLayout.addView(cardView);
        System.out.println("Height" + cardView.getHeight());

    }
}