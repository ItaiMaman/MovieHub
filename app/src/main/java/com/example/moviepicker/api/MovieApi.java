package com.example.moviepicker.api;

import com.example.moviepicker.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("movie/popular")
    Call<Movies> getPopularMovies(@Query("page") int page);


}
