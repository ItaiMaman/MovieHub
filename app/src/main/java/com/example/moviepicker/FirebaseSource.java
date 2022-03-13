package com.example.moviepicker;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FirebaseSource {

    private static DatabaseReference rooms = FirebaseDatabase.getInstance().getReference("rooms");
    private DatabaseReference userdata;
    private FirebaseAuth auth;

    public FirebaseSource() {
        auth = FirebaseAuth.getInstance();
        userdata = FirebaseDatabase.getInstance().getReference("UserData").child(auth.getUid());
    }

    public Task<Void> setUsername(String username){
        return userdata.child("username").setValue(username);
    }

    public void saveMovie(Movies.Movie movie){
        String push = userdata.push().getKey();
        movie.setPush(push);
        userdata.child("watchlist").child(String.valueOf(movie.getId())).setValue(movie);
    }

    public void deleteMovie(int id){
        userdata.child("watchlist").child(String.valueOf(id)).removeValue();
    }

    public LiveData<List<Movies.Movie>> getSavedMovies(){
        MutableLiveData<List<Movies.Movie>> movies = new MutableLiveData<>();
        List<Movies.Movie> list = new ArrayList<>();
        userdata.child("watchlist").orderByChild("push").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                list.add(snapshot.getValue(Movies.Movie.class));
                movies.setValue(list);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for(Movies.Movie i : list){
                    if(i.getId() == snapshot.getValue(Movies.Movie.class).getId()){
                        list.remove(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return movies;

    }

    public void createRoom(){
        DatabaseReference roomRef = rooms.push();
        Room room = new Room(roomRef.getKey(), auth.getUid());
        roomRef.setValue(room);
    }
}

