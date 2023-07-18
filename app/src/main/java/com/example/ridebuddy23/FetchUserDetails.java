package com.example.ridebuddy23;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class FetchUserDetails extends Thread {
    String data, user_email;
    UserDetails userDetails = null;
    FetchUserDetails(String email){
        user_email = email;
    }

    @Override
    public void run() {
        try {
            URL login_url = new URL("https://ridebuddy.000webhostapp.com/login_api.php?email=" + user_email);
            HttpURLConnection httpURLConnection = (HttpURLConnection)login_url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                data += line;
            }

            data = data.substring(4);
            System.out.println(data);
            if (data.length() > 25) {
                JSONObject jsonObject = new JSONObject(data);
                Boolean status = jsonObject.getBoolean("status");
                System.out.println(status);
                if(status) {
                    JSONArray user_data = jsonObject.getJSONArray("user_data");

                    for (int i = 0; i < user_data.length(); i++) {
                        JSONObject user_values = user_data.getJSONObject(i);
                        // Fetching user details values
                        System.out.println(user_values.getString("user_dob"));
                        UserDetails userDetails_values = new UserDetails(
                                user_values.getString("user_name"),
                                user_values.getString("email"),
                                user_values.getString("mobile_num"),
                                user_values.getString("password"),
                                user_values.getString("DP"),
                                Integer.parseInt(user_values.getString("profile_state")),
                                (byte)Integer.parseInt(user_values.getString("vehicle_state")),
                                user_values.getString("user_dob"),
                                (byte) Integer.parseInt(user_values.getString("gender")));

                        userDetails = userDetails_values;
                        System.out.println("Thread 01 finished");
                    }
                }
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }
}
