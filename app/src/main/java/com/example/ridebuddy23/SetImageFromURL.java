package com.example.ridebuddy23;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class SetImageFromURL {
    SetImageFromURL(ImageView image_id, String image_url){
        Picasso.get()
                .load(image_url)
                .into(image_id);
    }
}
