package com.example.ridebuddy23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Scanner;


public class CreateUserAccount extends AppCompatActivity {
    EditText f_name, l_name, email, mobile_num, password, re_password;
    TextView error_box;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        f_name =  findViewById(R.id.first_name);
        l_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email_address);
        mobile_num = findViewById(R.id.mobile_number);
        password = findViewById(R.id.password);
        re_password = findViewById(R.id.re_password);
        error_box = findViewById(R.id.error_box);
    }

    public void create_account(View view) {
        String user_name = f_name.getText().toString() + " " + l_name.getText().toString();
        if(password.getText().toString().equals(re_password.getText().toString())) {
            UserDetails userDetails = new UserDetails(
                    user_name,
                    email.getText().toString(),
                    mobile_num.getText().toString(),
                    password.getText().toString(),
                    "default",
                    2,
                    "2001-10-12",
                    (byte)1, "U0000006");

            LinkedHashMap<String, Object> params = new LinkedHashMap<>();
            params.put("fName", f_name.getText());
            params.put("LName", l_name.getText());
            params.put("userEmail", email.getText());
            params.put("pass", password.getText());
            params.put("mobile", mobile_num.getText());
            CreateConnection connection = new CreateConnection("userDetails/newUser", params, 201, "POST");
            connection.start();

            startActivity(new Intent(this, MainActivity.class));

//            InsertUser insertUser = new InsertUser(userDetails);
//            insertUser.start();
        }else{
            error_box.setText("Password mismatch");
            error_box.setVisibility(View.VISIBLE);
        }
    }

    public void GoBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}