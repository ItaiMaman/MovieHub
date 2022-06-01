package com.example.moviepicker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddEventViewModel extends AndroidViewModel {

    FirebaseSource firebase;

    public AddEventViewModel(@NonNull Application application) {
        super(application);
        firebase = new FirebaseSource();
    }

    public LiveData<List<String>> getUserNames(String roomId){
        MutableLiveData<List<String>> usernames = new MutableLiveData<>();
        List<String> list = new ArrayList<>();
        firebase.getRoom(roomId).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    FirebaseDatabase.getInstance().getReference("UserData").child(ds.getKey()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return usernames;
    }
}
