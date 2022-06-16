package com.example.moviehub;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.moviehub.startScreens.SplashScreen;
import com.example.moviehub.utils.Utils;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean in15min = intent.getBooleanExtra("15min", false);
        String movieName = intent.getStringExtra("movieName");
        if(movieName != null){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                    .setContentTitle("Movie time!")
                    .setSmallIcon(R.drawable.ic_movie)
                    .setContentText(in15min? "Dont forget you are watching " + movieName + " with your friends in 15 min" : "Grab your popcorn, " + movieName + " is starting!")
                    .setChannelId(Utils.NOTIFICATION_CHANNEL)
                    .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, SplashScreen.class), PendingIntent.FLAG_IMMUTABLE))
                    .setPriority(NotificationCompat.PRIORITY_MAX);

            NotificationManager managerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            managerCompat.notify(200, builder.build());
        }

    }
}
