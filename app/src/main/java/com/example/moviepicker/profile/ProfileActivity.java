package com.example.moviepicker.profile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.moviepicker.Movies;
import com.example.moviepicker.R;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SavedMoviesAdapter adapter;
    SavedMoviesViewModel viewModel;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setSupportActionBar(findViewById(R.id.toolbar));
        actionBar = getSupportActionBar();
        viewModel = ViewModelProviders.of(this).get(SavedMoviesViewModel.class);
        recyclerView = findViewById(R.id.saved_movies_rv);
        adapter = new SavedMoviesAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        viewModel.getMovies().observe(this, new Observer<List<Movies.Movie>>() {
            @Override
            public void onChanged(List<Movies.Movie> movies) {
                adapter.setMovies(movies);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}