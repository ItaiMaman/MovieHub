package com.example.moviepicker.Rooms;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.moviepicker.R;
import com.example.moviepicker.Room;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class WaitingRoomActivity extends RoomInterface{

    RoomViewModel viewModel;
    RecyclerView users;
    RoomParticipantsAdapter adapter;
    MaterialAlertDialogBuilder roomDeletedDialog, leaveRoomDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        String id = getIntent().getStringExtra("id");
        toolbar = findViewById(R.id.toolbar);
        users = findViewById(R.id.users);
        viewModel = new RoomViewModel(getApplication(), id);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        adapter = new RoomParticipantsAdapter(this);
        users.setLayoutManager(new LinearLayoutManager(this));
        users.setAdapter(adapter);

        roomDeletedDialog = new MaterialAlertDialogBuilder(this).setMessage("The host has ended the session").setPositiveButton("Ok", (dialog1, which) -> finish()).setCancelable(false);
        leaveRoomDialog = new MaterialAlertDialogBuilder(this).setMessage("Are you sure you want to leave the room?")
                .setPositiveButton("Leave", (dialog, which) -> {
                    finish();
                    viewModel.leaveRoom();
                })
                .setNegativeButton("Stay", (dialog, which) -> dialog.dismiss());


        viewModel.getRoom().observe(this, new Observer<Room>() {
            @Override
            public void onChanged(Room room) {
                if(room == null){
                    roomDeletedDialog.show();
                }
                else
                    adapter.setRoom(room);
            }
        });
    }


    @Override
    public LiveData<String> getUsername(String uid) {
        return viewModel.getUsername(uid);
    }

    @Override
    public void onBackPressed() {
        leaveRoomDialog.show();
    }
}