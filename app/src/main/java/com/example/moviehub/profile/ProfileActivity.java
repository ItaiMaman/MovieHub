package com.example.moviehub.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.moviehub.EventsFragment;
import com.example.moviehub.FragmentAdapter;
import com.example.moviehub.LikedMovies;
import com.example.moviehub.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    FragmentAdapter adapter;
    ActionBar actionBar;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setSupportActionBar(findViewById(R.id.toolbar_profile));
        actionBar = getSupportActionBar();
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new LikedMovies());
        fragments.add(new EventsFragment());

        adapter =  new FragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragments);

        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}