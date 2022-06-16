package com.example.moviehub.startScreens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviehub.R;
import com.example.moviehub.home.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    ImageView gif;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        gif = findViewById(R.id.video);

        Glide.with(this).load(R.raw.clapboard).into(gif);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null)
                    intent = new Intent(SplashScreen.this, MainActivity.class);

                else
                    intent = new Intent(SplashScreen.this, LoginScreen.class);

                startActivity(intent);

            }
        }, 2500);

    }



}