package com.example.ridebuddy23;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

interface UrlConnection {
    String DOMAIN = "https://impossible-tan-top-hat.cyclic.cloud/";
//    String DOMAIN = "http://localhost:3000/";
    String getResponse();
    JSONObject getJsonResponse();
}
public class CreateConnection extends Thread implements UrlConnection{
    String method, baseURL = "";
    private StringBuffer stringResponse;
    HashMap<String, Object> params;
    public int responseCode;
    private int checkCode, attempts = 0;

    CreateConnection(String pathURL, HashMap<String, Object> params, int checkCode, String method) {
        this.baseURL = pathURL;
        this.params = params;
        this.checkCode = checkCode;
        this.method = method;
    }

    @Override
    public String getResponse() {
        return this.stringResponse.toString();
    }

    @Override
    public JSONObject getJsonResponse() {
        if (this.stringResponse != null) {
            try {
                JSONObject jsonObject = new JSONObject(stringResponse.toString());
                return jsonObject;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public void run() {
        // Creating connection with the URL
        try{
            for (String key: this.params.keySet()) {
                this.baseURL += "/" + params.get(key).toString().replace(" ", "+").replace(":", "s").replace(".", "d");
            }
            this.baseURL = DOMAIN + this.baseURL;
            URL url = new URL(baseURL);
            while (attempts < 3) {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(this.method);
                System.out.println("URL: "  + this.baseURL);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                this.responseCode = conn.getResponseCode();
                System.out.println("Response Code: " + this.responseCode);
                // Check response code is matched or not
                if (checkCode == responseCode) {
                    System.out.println("Connection Code matched");
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String singleLine;
                    stringResponse = new StringBuffer();
                    while ((singleLine = in.readLine()) != null)
                        stringResponse.append(singleLine);
                    in.close();
                    System.out.println("Sever response: " + stringResponse);
                    break;
                } else {
                    System.out.println("Sever response " + ++attempts + " : " + null);
                }
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
