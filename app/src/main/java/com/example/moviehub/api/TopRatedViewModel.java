package com.example.moviehub.api;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviehub.FirebaseSource;
import com.example.moviehub.models.Movies;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopRatedViewModel extends AndroidViewModel {

    MovieApi movieApi;
    FirebaseSource firebaseSource;
    private MutableLiveData<Movies> movies;
    int page = 1;

    public TopRatedViewModel(@NonNull Application application) {
        super(application);

        firebaseSource = new FirebaseSource();
        movieApi = MovieService.getMovieApi();
        movies = new MutableLiveData<>();
        newRequest();
    }

    public void addToMovies(Movies data){
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

    public void saveMovie(Movies.Movie movie){
        firebaseSource.saveMovie(movie);
    }

    public void deleteMovie(int id){
        firebaseSource.deleteMovie(id);
    }
}
