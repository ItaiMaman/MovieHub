package com.example.moviehub.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.moviehub.FirebaseSource;
import com.example.moviehub.models.Movies;

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

    public void deleteMovie(int id){
        firebaseSource.deleteMovie(id);
    }
}
