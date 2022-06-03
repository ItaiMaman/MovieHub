package com.example.moviehub.Rooms;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviehub.FirebaseSource;
import com.example.moviehub.models.Movies;
import com.example.moviehub.api.MovieApi;
import com.example.moviehub.api.MovieService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwipeMatchViewModel extends AndroidViewModel {

    private final MovieApi movieApi;
    private final FirebaseSource firebaseSource;
    private final MutableLiveData<Movies> movies;
    private int page = 1;
    private final String roomId;
    private final MutableLiveData<List<String>> users;
    private final MutableLiveData<Integer> match;
    private final MutableLiveData<Boolean> active;
    private final MutableLiveData<HashMap<String, HashMap<String, Long>>> swiped;
    private final MutableLiveData<Date> date;

    public SwipeMatchViewModel(@NonNull Application application, String roomId) {
        super(application);

        this.roomId = roomId;
        firebaseSource = new FirebaseSource();
        movieApi = MovieService.getMovieApi();
        movies = new MutableLiveData<>();
        users = new MutableLiveData<>();
        active = new MutableLiveData<>();
        match = new MutableLiveData<>();
        swiped = new MutableLiveData<>();
        date = new MutableLiveData<>();

        newRequest();
        fetchStuff();
    }

    private void addToMovies(Movies data) {
        if (data != null) {
            page++;
            if (movies.getValue() != null) {
                List<Movies.Movie> list = movies.getValue().getMovies();
                list.addAll(data.getMovies());
                movies.setValue(new Movies(list));
            } else {
                movies.setValue(data);
            }
        }
    }

    public void newRequest() {
        movieApi.getPopularMovies(page).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {
                addToMovies(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {

            }
        });
    }

    public LiveData<Movies> getMovies() {
        return movies;
    }

    public LiveData<List<String>> getUsers() {
        return users;
    }

    public LiveData<Boolean> getActive() {
        return active;
    }

    public LiveData<Integer> getMatch() {
        return match;
    }

    public MutableLiveData<HashMap<String, HashMap<String, Long>>> getSwiped() {
        return swiped;
    }


    public void fetchStuff() {
        List<String> list = new ArrayList<>();
        firebaseSource.getRoomUsers(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    list.add(ds.getKey());
                }
                users.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        firebaseSource.getRoom(roomId).child("ownerId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.add(snapshot.getValue(String.class));
                users.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebaseSource.getRoomState(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    active.setValue(snapshot.getValue(Boolean.class));
                else
                    active.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        firebaseSource.getMatchState(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    match.setValue(snapshot.getValue(Integer.class));
                else
                    match.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        GenericTypeIndicator<HashMap<String, HashMap<String, Long>>> indicator = new GenericTypeIndicator<HashMap<String, HashMap<String, Long>>>() {
        };

        firebaseSource.getRoomMovies(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                swiped.setValue(snapshot.getValue(indicator));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebaseSource.getRoomDate(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                date.setValue(snapshot.getValue(Date.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void swipeRight(int id) {
        firebaseSource.swipeRightMatch(roomId, id);
    }

    public void swipeLeft(int id) {
        firebaseSource.swipeLeftMatch(roomId, id);
    }

    public void match(int id) {
        firebaseSource.match(roomId, id);
    }

    public void clearMatch(){
        firebaseSource.clearMovieSwipes(roomId, match.getValue());
        firebaseSource.match(roomId, 0);
    }

    public void closeRoom(){
        firebaseSource.deleteRoom(roomId);
    }

    public void leaveRoom(){
        firebaseSource.leaveRoom(roomId);
    }

    public void setActive(Boolean bool){
        firebaseSource.setActive(roomId, bool);
    }

    public MutableLiveData<Date> getDate() {
        return date;
    }

    public void addEvent(Date date, Movies.Movie movie, List<String> friends){
        firebaseSource.addEvent(date, movie, friends );
    }

    public void deleteRoom(){
        firebaseSource.deleteRoom(roomId);
    }
}
