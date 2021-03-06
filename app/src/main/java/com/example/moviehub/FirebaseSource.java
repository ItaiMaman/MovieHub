package com.example.moviehub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviehub.models.Event;
import com.example.moviehub.models.Movies;
import com.example.moviehub.models.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FirebaseSource {

    private static final DatabaseReference rooms = FirebaseDatabase.getInstance().getReference("rooms");
    private final DatabaseReference userdata;
    private final FirebaseAuth auth;
    private int id;

    public FirebaseSource() {
        auth = FirebaseAuth.getInstance();
        userdata = FirebaseDatabase.getInstance().getReference("UserData").child(auth.getUid());
    }

    public Task<Void> setUsername(String username) {
        return userdata.child("username").setValue(username);
    }

    public void saveMovie(@NonNull Movies.Movie movie) {
        String push = userdata.push().getKey();
        movie.setPush(push);
        userdata.child("watchlist").child(String.valueOf(movie.getId())).setValue(movie);
    }

    public void deleteMovie(int id) {
        userdata.child("watchlist").child(String.valueOf(id)).removeValue();
    }

    public LiveData<List<Movies.Movie>> getSavedMovies() {
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
                for (Movies.Movie i : list) {
                    if (i.getId().equals(snapshot.getValue(Movies.Movie.class).getId())) {
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



    public String createRoom() {
        DatabaseReference roomRef = rooms.push();
        Room room = new Room(roomRef.getKey(), auth.getUid());
        rooms.child(room.getRoomId()).setValue(room);
        return roomRef.getKey();
    }

    public DatabaseReference getRoom(String id) {
        return rooms.child(id);
    }

    public void deleteRoom(String roomId) {
        rooms.child(roomId).removeValue();
    }

    public LiveData<String> joinRoom(String code) {
        MutableLiveData<String> roomId = new MutableLiveData<>();

        rooms.orderByChild("code").equalTo(code).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DataSnapshot snapshot = task.getResult();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Room room = ds.getValue(Room.class);
                        room.addUser(auth.getUid(), rooms.push().getKey());
                        rooms.child(room.getRoomId()).setValue(room);
                        roomId.setValue(room.getRoomId());
                    }
                } else {
                    roomId.setValue(null);
                }
            }
        });
        return roomId;
    }

    public LiveData<String> getUsername(String uid) {
        if(uid == null)
            uid = auth.getUid();
        MutableLiveData<String> username = new MutableLiveData<>();
        FirebaseDatabase.getInstance().getReference("UserData").child(uid).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setValue(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return username;
    }

    public void startRoom(String roomId) {
        rooms.child(roomId).child("running").setValue(true);
    }

    public void leaveRoom(String roomId) {

        rooms.child(roomId).child("swiped").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        rooms.child(roomId).child("swiped").child(ds.getKey()).child(auth.getUid()).removeValue();
                    }
                }
                rooms.child(roomId).child("users").child(auth.getUid()).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public DatabaseReference getRoomState(String roomId) {
        return rooms.child(roomId).child("running");
    }

    public DatabaseReference getMatchState(String roomId) {
        return rooms.child(roomId).child("match");
    }

    public DatabaseReference getRoomUsers(String roomId) {
        return rooms.child(roomId).child("users");
    }

    public DatabaseReference getRoomMovies(String roomId) {
        return rooms.child(roomId).child("swiped");
    }

    public DatabaseReference getRoomDate(String roomId){
        return rooms.child(roomId).child("date");
    }

    public void swipeRightMatch(String roomId, int id) {
        rooms.child(roomId).child("swiped").child(String.valueOf(id)).child(auth.getUid()).setValue(id);
    }

    public void swipeLeftMatch(String roomId, int id) {
        rooms.child(roomId).child("swiped").child(String.valueOf(id)).child(auth.getUid()).removeValue();
    }

    public void match(String roomId, int id) {
        rooms.child(roomId).child("match").setValue(id);
    }

    public void clearMovieSwipes(String roomId, int id) {
        rooms.child(roomId).child("swiped").child(String.valueOf(id)).removeValue();
    }

    public void setActive(String roomId, Boolean bool) {
        rooms.child(roomId).child("running").setValue(bool);
    }

    public String getEmail() {
        return auth.getCurrentUser().getEmail();
    }


    public void addEvent(Date date, Movies.Movie movie, List<String> friends){
        Event event = new Event(date, movie, userdata.push().getKey(), friends);
        userdata.child("events").child(event.getId()).setValue(event);
    }

    public LiveData<List<Event>> getEvents(){
        MutableLiveData<List<Event>> events = new MutableLiveData<>();
        List<Event> list = new ArrayList<>();
        userdata.child("events").orderByChild("date/time").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(Event.class).getDate().getTime() - 1000*60*60*3 > System.currentTimeMillis()){
                    list.add(snapshot.getValue(Event.class));
                    events.setValue(list);
                }
                else
                    deleteEvent(snapshot.getValue(Event.class).getId());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for(Event e : list){
                    if(e.getId() == snapshot.getValue(Event.class).getId()){
                        list.remove(e);
                        events.setValue(list);
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
        return events;
    }

    private void deleteEvent(String id) {
        userdata.child("events").child(id).removeValue();
    }

    public MutableLiveData<List<String>> getUsernames(List<String> uids){
        MutableLiveData<List<String>> usernames = new MutableLiveData<>();
        List<String> list = new ArrayList<>();
        for(String s : uids){
            userdata.getParent().child(s).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.add(snapshot.getValue(String.class));
                    usernames.setValue(list);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        return usernames;
    }
}


