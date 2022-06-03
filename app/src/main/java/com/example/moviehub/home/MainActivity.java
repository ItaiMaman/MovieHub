package com.example.moviehub.home;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.moviehub.MyBroadcastReceiver;
import com.example.moviehub.Rooms.FriendsActivity;
import com.example.moviehub.R;
import com.example.moviehub.profile.AddEventActivity;
import com.example.moviehub.profile.ProfileActivity;
import com.example.moviehub.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    FragmentAdapter adapter;
    FloatingActionButton fab;
    ImageView friends, profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySnackBar();

        friends = findViewById(R.id.friends);
        profile = findViewById(R.id.profile);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.TabLayout);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_fire);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_top_rated);
        fab = findViewById(R.id.fab);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new PopularFragment());
        fragments.add(new TopRatedFragment());
        adapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        createNotificationChannel();

        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);

        String[] title = {"Popular", "Top Rated"};
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab.setText(title[tab.getPosition()]);
                tab.setContentDescription(title[tab.getPosition()]);
                new Handler(Looper.getMainLooper()).postDelayed(() -> tab.setText(null), 1000);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setText(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewPager.getCurrentItem();
                if(pos == 0){
                    ((PopularFragment) adapter.getFragment(pos)).rewind();
                }
                else if(pos == 1){
                    ((TopRatedFragment) adapter.getFragment(pos)).rewind();
                }
                Intent intent = new Intent(MainActivity.this, MyBroadcastReceiver.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast
                        (MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE );
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis() + 3000, pendingIntent);

            }
        });

        //todo - match (calender service)
        //todo - extra details onclick of card
        //todo - matched movie with menu (fab) (cancel, message, set event)

        friends.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FriendsActivity.class)));

        profile.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ProfileActivity.class)));

    }

    public void mySnackBar(){
        Intent i = getIntent();
        String message = i.getStringExtra("snackbar");
        if(message != null){
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
        }


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            String name = Utils.NOTIFICATION_CHANNEL;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(name,name,importance);
            channel.setDescription("temp");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
    }
}