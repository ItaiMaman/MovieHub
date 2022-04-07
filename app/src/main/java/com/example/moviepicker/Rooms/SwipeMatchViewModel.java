package com.example.moviepicker.Rooms;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviepicker.FirebaseSource;
import com.example.moviepicker.Movies;
import com.example.moviepicker.Room;
import com.example.moviepicker.api.MovieApi;
import com.example.moviepicker.api.MovieService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwipeMatchViewModel extends AndroidViewModel {

    List<Integer> swiped;
    LiveData<Room> room;
    MovieApi movieApi;
    FirebaseSource firebaseSource;
    private MutableLiveData<Movies> movies;
    int page = 1;
    String roomId;

    public SwipeMatchViewModel(@NonNull Application application, String roomId) {
        super(application);

        this.roomId = roomId;
        firebaseSource = new FirebaseSource();
        movieApi = MovieService.getMovieApi();
        movies = new MutableLiveData<>();
        swiped = new ArrayList<>();
        firebaseSource.getRoom(roomId).;
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

    public void addSwipedMovieToList(int pos, int id){

    }

    public LiveData<Movies> getMovies() {
        return movies;
    }

    public LiveData<Boolean> getRoomState(){
        MutableLiveData<Boolean> run = new MutableLiveData<>();
        firebaseSource.getRoomState(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                run.setValue(snapshot.getValue(Boolean.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return run;
    }

    public void swipeRight(){

    }


}
