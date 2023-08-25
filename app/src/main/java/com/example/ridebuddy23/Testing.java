package com.example.ridebuddy23;

import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Testing {
    public static void main(String[] args) {
        CreateConnection connection = new CreateConnection("vehicle/registerVehicle", new HashMap<>(), HttpURLConnection.HTTP_OK, "POST");
        connection.start();


    }
}
