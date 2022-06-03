package com.example.moviehub;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.Nullable;

public class VibrationService extends Service {

    VibrationEffect effect;
    Vibrator vibrator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            effect = (VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK));
        }
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        vibrator.cancel();
        vibrator.vibrate(effect);
        return super.onStartCommand(intent, flags, startId);

    }
}