package com.example.moviepicker.api;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviepicker.Movies;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {

    MovieApi movieApi;
    private MutableLiveData<Movies> movies;



    public HomeViewModel(@NonNull Application application) {
        super(application);

        movieApi = MovieService.getMovieApi();
        movies = new MutableLiveData<>();
        newRequest();
    }

    public void newRequest(){
        Log.d("tag", "new Request");
        movieApi.getPopularMovies(1).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                movies.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

            }
        });
    }
    public LiveData<Movies> getMovies() {
        return movies;
    }
}
