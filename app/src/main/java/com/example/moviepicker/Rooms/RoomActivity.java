package com.example.moviepicker.Rooms;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.moviepicker.R;
import com.example.moviepicker.Room;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class RoomActivity extends RoomInterface {

    RoomViewModel viewModel;
    EditText code;
    RecyclerView recyclerView;
    MaterialButton start;
    Toolbar toolbar;
    RoomParticipantsAdapter adapter;
    MaterialAlertDialogBuilder dialog, startDialog;
    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);


        String id = getIntent().getStringExtra("id");
        viewModel = new RoomViewModel(getApplication(), id);
        code = findViewById(R.id.code);
        recyclerView = findViewById(R.id.users);
        start = findViewById(R.id.start_btn);
        toolbar = findViewById(R.id.toolbar);
        adapter = new RoomParticipantsAdapter(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> this.onBackPressed());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel.getRoom().observe(this, new Observer<Room>() {
            @Override
            public void onChanged(Room room) {
                if (room != null) {
                    code.setText(room.getCode());
                    adapter.setRoom(room);
                    RoomActivity.this.room = room;
                }
                else
                    finish();
            }
        });
        startDialog = new MaterialAlertDialogBuilder(this).setTitle("Room needs at least two participants")
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        ).setCancelable(false);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(room != null && room.getUsers().size() > 0){
                    startActivity(new Intent(RoomActivity.this, SwipeMatchActivity.class).putExtra("id", room.getRoomId()));
                }
                else{
                    startDialog.show();
                    viewModel.startRoom();
                }
            }
        });

        dialog = new MaterialAlertDialogBuilder(this).setMessage("Are you sure you want to end current room session")
                .setPositiveButton("End session", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        viewModel.deleteRoom();
                        finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

    }

    @Override
    public LiveData<String> getUsername(String uid) {
        return viewModel.getUsername(uid);
    }

    @Override
    public void onBackPressed() {
        if (adapter.getUsers() != null && adapter.getUsers().size() > 1) {
            dialog.show();
        } else {
            viewModel.deleteRoom();
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.deleteRoom();
    }


}