package com.example.moviehub.Rooms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

public abstract class RoomInterface extends AppCompatActivity {

    public abstract LiveData<String> getUsername(String uid);
}
