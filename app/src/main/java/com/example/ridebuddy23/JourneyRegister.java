package com.example.ridebuddy23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class JourneyRegister extends AppCompatActivity {
    Dialog getDateCalender;
    String currentDate;
    Button dateBtn;
    int layoutCount = 0, layoutIDs[] = {R.layout.activity_journey_register, R.layout.journey_register02};
    JourneyData journeyData;
    EditText hours, minutes, startCity, endCity, description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutIDs[layoutCount]);

        getDateCalender = new Dialog(this);
        getDateCalender.setContentView(R.layout.trip_form01);

        // Creating new Journey details object
        journeyData = new JourneyData();
        // Page 01
        initiatePage01();



    }
    public void nextBtn(View view) {
        journeyData.status = "Active";

        if(layoutCount == 0){
            setContentView(layoutIDs[++layoutCount]);
            journeyData.setPage01(currentDate + " " + hours.getText() + ":" + minutes.getText(), startCity.getText().toString(), endCity.getText().toString());
        } else {
            initiatePage02();
            journeyData.setDescription(description.getText().toString());
            Intent intent = new Intent(this, Map.class);
            intent.putExtra("journeyData", journeyData);
            startActivity(intent);
        }
        System.out.println(journeyData);
    }

    public void goBackBtn(View view) {
        startActivity(new Intent(this, HomeScreen.class));
    }

    public void openMap(View view) {
        startActivity(new Intent(this, Map.class));
    }

    public void initiatePage02() {
        description = findViewById(R.id.description);
    }
    public void initiatePage01() {
        dateBtn = findViewById(R.id.dateBtn);
        hours = findViewById(R.id.hours);
        minutes = findViewById(R.id.minis);
        startCity = findViewById(R.id.pickupCity);
        endCity = findViewById(R.id.desCity);
    }

    public void changeDate(View view) {
        getDateCalender.show();
        CalendarView calendarView = getDateCalender.findViewById(R.id.calendarView);
        Button button = getDateCalender.findViewById(R.id.button3);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String monthStr = String.valueOf(month), dayStr = String.valueOf(dayOfMonth);
                if (monthStr.length() < 2)
                    monthStr = "0" + monthStr;
                if (dayStr.length() < 2)
                    dayStr = "0" + dayStr;
                currentDate = String.valueOf(year + "-" + monthStr + "-" + dayStr);
            }
        });

        // Selecting the date from the calender and close it
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Date: " + currentDate);
                dateBtn.setText(currentDate);
                getDateCalender.dismiss();
            }
        });


    }
}