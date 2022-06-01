package com.example.moviepicker.api;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviepicker.FirebaseSource;
import com.example.moviepicker.Movies;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularViewModel extends AndroidViewModel {

    MovieApi movieApi;
    FirebaseSource firebaseSource;
    private final MutableLiveData<Movies> movies;
    int page = 1;

    public PopularViewModel(@NonNull Application application) {
        super(application);

        firebaseSource = new FirebaseSource();
        movieApi = MovieService.getMovieApi();
        movies = new MutableLiveData<>();
        newRequest();
    }

    private void addToMovies(Movies data){
        if(data != null){
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
        movieApi.getPopularMovies(page).enqueue(new Callback<Movies>() {
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

    public void saveMovie(Movies.Movie movie){
        firebaseSource.saveMovie(movie);
    }

    public void deleteMovie(int id){
        firebaseSource.deleteMovie(id);
    }
}
