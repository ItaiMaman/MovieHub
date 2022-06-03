package com.example.moviehub.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.moviehub.home.FragmentAdapter;
import com.example.moviehub.R;
import com.example.moviehub.startScreens.SplashScreen;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    FragmentAdapter adapter;
    ActionBar actionBar;
    TabLayout tabLayout;
    TextView username, email;
    ProfileViewModel viewModel;
    MaterialAlertDialogBuilder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setSupportActionBar(findViewById(R.id.toolbar_profile));
        actionBar = getSupportActionBar();
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new SavedMoviesFragment());
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

        viewModel.getUsername().observe(this, s -> username.setText(s));

        email.setText(viewModel.getEmail());

        dialog = new MaterialAlertDialogBuilder(this).setTitle("Are you sure you want to log out?")
                .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ProfileActivity.this, SplashScreen.class));
                    }
                }).setNegativeButton("Cancel", (dialog, which) -> {});

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }else if(item.getItemId() == R.id.log_out){
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}