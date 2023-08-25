package com.example.ridebuddy23;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
import java.util.HashMap;
import java.util.Map;

class FetchUserDetails extends Thread {
    String data, user_email;
    UserDetails userDetails = null;
    FetchUserDetails(String email){
        user_email = email;
    }

    @Override
    public void run() {
//        createConnection
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }
}
