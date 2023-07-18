package com.example.ridebuddy23;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaParser;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    TextView user_name_text;
    ImageView add_image;
    Bitmap bitmap;
    EditText user_name02, email, mobile_num, home_address, e_contact;
    RadioButton gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ImageView profile_image = findViewById(R.id.profile_image);
        add_image = findViewById(R.id.add_image);

        // Get basic details from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("User_Details", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Profile Image
        String dp_link = sharedPreferences.getString("DP", "No Image");
        if(!dp_link.equals("default"))
            new SetImageFromURL(profile_image, "https://ridebuddy.000webhostapp.com/" + dp_link);

        user_name_text = findViewById(R.id.user_name);
        String user_name = sharedPreferences.getString("user_name", null),
                user_email = sharedPreferences.getString("email", null);
        user_name_text.setText(user_name);

        // Displaying user details on the screen
        user_name02 = findViewById(R.id.user_name02);
        email = findViewById(R.id.email_address);
        mobile_num = findViewById(R.id.mobile_number);
        home_address = findViewById(R.id.home_address);
        e_contact = findViewById(R.id.e_contact);
        gender = findViewById(R.id.male);

        user_name02.setEnabled(true);
        email.setEnabled(true);
        mobile_num.setEnabled(true);
        home_address.setEnabled(true);
        e_contact.setEnabled(true);

        user_name02.setText(user_name);
        email.setText(sharedPreferences.getString("email", null));
        mobile_num.setText(sharedPreferences.getString("mobile_number", null));
        home_address.setText(sharedPreferences.getString("email", null));
        e_contact.setText(sharedPreferences.getString("email", null));
        gender.toggle();

        user_name02.setEnabled(false);
        email.setEnabled(false);
        mobile_num.setEnabled(false);
        home_address.setEnabled(false);
        e_contact.setEnabled(false);



        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                profile_image.setImageBitmap(bitmap);

                                // Sending bit map image to server
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                if(bitmap != null){
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                    byte[] bytes = byteArrayOutputStream.toByteArray();
                                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

                                    // Creating volley post request
                                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                    String url ="https://ridebuddy.000webhostapp.com/uploadImage.php";

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(getApplicationContext(), "Response: " + response, Toast.LENGTH_SHORT).show();
                                                    System.out.println("Response: " + response);
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        System.out.println(jsonObject.getString("image_name"));
                                                        editor.putString("DP", jsonObject.getString("image_name"));
                                                        editor.commit();
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getApplicationContext(), "Error: " + error, Toast.LENGTH_LONG).show();
                                            System.out.println("Error: " + error);
                                        }
                                    }){
                                        protected Map<String, String> getParams(){
                                            Map<String, String> paramV = new HashMap<>();
                                            paramV.put("image", base64Image);
                                            paramV.put("user_name", user_name);
                                            paramV.put("user_email", user_email);
                                            System.out.println(user_email + " " + user_name);
                                            return paramV;
                                        }
                                    };
                                    queue.add(stringRequest);

                                }else
                                    Toast.makeText(getApplicationContext(), "Image selection failed", Toast.LENGTH_LONG).show();

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });
    }


    public void close_edit(View view) {
        startActivity(new Intent(this, MyProfile.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

    }

}