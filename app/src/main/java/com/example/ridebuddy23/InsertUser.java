package com.example.ridebuddy23;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InsertUser extends Thread{
    UserDetails userDetails;
    String data;
    InsertUser(UserDetails userDetails){
        this.userDetails = userDetails;
    }
    public void run(){
        try {
            URL insert_url = new URL("https://ridebuddy.000webhostapp.com/add_user_details.php?" +
                    "user_name=" + userDetails.getUser_name() +
                    "&email=" + userDetails.getEmail() +
                    "&mobile_num=" + userDetails.getMobile() +
                    "&password=" + userDetails.getPassword() +
                    "&DP=" + userDetails.getDP() +
                    "&profile_state=" + userDetails.getProfile_state() +
                    "&vehicle_state=" + 1 +
                    "&user_dob=" + userDetails.getUser_dob() +
                    "&gender=" + userDetails.getGender());
            HttpURLConnection httpURLConnection1 = (HttpURLConnection) insert_url.openConnection();
            InputStream inputStream1 = httpURLConnection1.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream1));
                String line;
                while((line = bufferedReader.readLine()) != null){
                    this.data += line;
                }
                data = data.substring(4);
                System.out.println("Inserted");
            JSONObject jsonObject = new JSONObject(data);
            boolean status = jsonObject.getBoolean("status");
            System.out.println(status);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
