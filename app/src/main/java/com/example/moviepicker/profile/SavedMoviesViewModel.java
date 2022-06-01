package com.example.moviepicker.profile;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.moviepicker.FirebaseSource;
import com.example.moviepicker.Movies;

import java.util.List;

public class SavedMoviesViewModel extends AndroidViewModel {

    private FirebaseSource firebaseSource;
    private final LiveData<List<Movies.Movie>> movies;

    public SavedMoviesViewModel(@NonNull Application application) {
        super(application);
        firebaseSource = new FirebaseSource();
        movies = firebaseSource.getSavedMovies();
    }

    public LiveData<List<Movies.Movie>> getMovies() {
        return movies;
    }
}
