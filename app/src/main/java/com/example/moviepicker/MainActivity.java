package com.example.moviepicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import com.example.moviepicker.profile.ProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;


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
        adapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());

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
            }
        });

        //todo - store in realtime database
        //todo - connect with other user
        //todo - match (calender service)
        //todo - extra details onclick of card
        //todo - matched movie with menu (fab) (cancel, message, set event)

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FriendsActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

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
}