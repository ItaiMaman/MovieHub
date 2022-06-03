package com.example.moviehub.Rooms;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviehub.FirebaseSource;
import com.example.moviehub.models.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class RoomViewModel extends AndroidViewModel {

    FirebaseSource firebaseSource;
    MutableLiveData<Room> room;
    String id;

    public RoomViewModel(@NonNull Application application, String id) {
        super(application);
        this.id = id;
        firebaseSource = new FirebaseSource();
        room = new MutableLiveData<>();
        firebaseSource.getRoom(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                room.setValue(snapshot.getValue(Room.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void startRoom() {
        firebaseSource.startRoom(id);
    }

    public LiveData<Room> getRoom() {
        return room;
    }

    public LiveData<String> getUsername(String uid){
        return firebaseSource.getUsername(uid);
    }

    public LiveData<Boolean> getRoomState() {
        MutableLiveData<Boolean> run = new MutableLiveData<>();
        firebaseSource.getRoomState(id).addValueEventListener(new ValueEventListener() {
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

    public void deleteRoom(){
        firebaseSource.deleteRoom(id);
    }

    public void leaveRoom(){
        firebaseSource.leaveRoom(id);
    }
}

