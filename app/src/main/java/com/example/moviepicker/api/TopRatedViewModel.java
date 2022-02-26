package com.example.moviepicker.api;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviepicker.Movies;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopRatedViewModel extends AndroidViewModel {

    MovieApi movieApi;
    private MutableLiveData<Movies> movies;
    int page = 1;

    public TopRatedViewModel(@NonNull Application application) {
        super(application);

        movieApi = MovieService.getMovieApi();
        movies = new MutableLiveData<>();
        newRequest();
    }

    public void addToMovies(Movies data){
        Log.d("tag", "add");
        if(data != null){
            Log.d("tag", "not null");
            page++;
            if(movies.getValue() != null){
                List<Movies.Movie> list = movies.getValue().getMovies();
                list.addAll(data.getMovies());
                movies.setValue(new Movies(list));
            }
            else{
                movies.setValue(data);
            }
        }
    }

    public void newRequest(){
        Log.d("tag", "request");
        movieApi.getTopRatedMovies(page).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                addToMovies(response.body());
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
