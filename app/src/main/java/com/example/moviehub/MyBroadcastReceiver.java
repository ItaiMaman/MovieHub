package com.example.moviehub;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.example.moviehub.home.MainActivity;
import com.example.moviehub.utils.Utils;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean in15min = intent.getBooleanExtra("15min", false);
        String movieName = intent.getStringExtra("movieName");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                .setContentTitle("Movie time!")
                .setSmallIcon(R.drawable.ic_movie)
                .setContentText(in15min? "Dont forget you are watching " + movieName + " with your friends in 15 min" : "Grab your popcorn, " + movieName + " is starting!")
                .setChannelId(Utils.NOTIFICATION_CHANNEL)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManager managerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        managerCompat.notify(200, builder.build());
    }
}
