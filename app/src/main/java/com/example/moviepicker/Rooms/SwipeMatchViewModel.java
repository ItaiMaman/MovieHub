package com.example.moviepicker.Rooms;

import android.app.Application;
import android.widget.MultiAutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviepicker.FirebaseSource;
import com.example.moviepicker.Movies;
import com.example.moviepicker.Room;
import com.example.moviepicker.api.MovieApi;
import com.example.moviepicker.api.MovieService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwipeMatchViewModel extends AndroidViewModel {

    MutableLiveData<Room> room;
    MovieApi movieApi;
    FirebaseSource firebaseSource;
    private MutableLiveData<Movies> movies;
    int page = 1;
    String roomId;
    MutableLiveData<Integer> users;
    MutableLiveData<Boolean> active, match;
    MutableLiveData<HashMap<String, Integer>> swiped;


    public SwipeMatchViewModel(@NonNull Application application, String roomId) {
        super(application);

        this.roomId = roomId;
        firebaseSource = new FirebaseSource();
        movieApi = MovieService.getMovieApi();
        movies = new MutableLiveData<>();
        room = new MutableLiveData<>();
        users = new MutableLiveData<>();
        active = new MutableLiveData<>();
        match = new MutableLiveData<>();
        swiped = new MutableLiveData<>();

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

    public LiveData<Integer> getUsers() {
        return users;
    }

    public LiveData<Boolean> getActive() {
        return active;
    }

    public LiveData<Boolean> getMatch() {
        return match;
    }

    public LiveData<HashMap<String, Integer>> getSwiped() {
        return swiped;
    }

    public void fetchStuff() {
        firebaseSource.getRoom(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                room.setValue(snapshot.getValue(Room.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        firebaseSource.getRoomUsers(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.setValue((int) snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebaseSource.getRoomState(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                active.setValue(snapshot.getValue(Boolean.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        firebaseSource.getMatchState(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                match.setValue(snapshot.getValue(Boolean.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        GenericTypeIndicator<HashMap<String, Integer>> indicator = new GenericTypeIndicator<HashMap<String, Integer>>() {
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
    }


    public void swipeRight(int id) {
        firebaseSource.getRoom(roomId).child("swiped").child(String.valueOf(id)).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot i) {
                int value = 1;
                if (i.exists())
                    value += i.getValue(Integer.class);
                firebaseSource.swipeRightMatch(roomId, id, value );
            }
        });

    }


}
