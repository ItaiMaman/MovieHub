package com.example.moviehub;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Utils {

    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";

    public static void loadImage(Context context, String url, ImageView imageView){
        Glide.with(context).load(url).centerCrop().into(imageView);
    }
}
