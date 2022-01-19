package com.example.moviepicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {


    ImageView iv_video;
    Animation anim_rotate;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash_screen);

        iv_video = findViewById(R.id.iv_video);
        anim_rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_rotate);

        iv_video.startAnimation(anim_rotate);
        //todo - action animation (anddddd action)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null)
                    intent = new Intent(SplashScreen.this, MainActivity.class);

                else
                    intent = new Intent(SplashScreen.this, LoginScreen.class);

                startActivity(intent);

            }
        }, 3000);

    }

}