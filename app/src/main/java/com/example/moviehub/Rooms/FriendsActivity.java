package com.example.moviehub.Rooms;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.moviehub.FirebaseSource;
import com.example.moviehub.R;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class FriendsActivity extends AppCompatActivity {

    ActionBar actionBar;
    MaterialButton generate, joinRoom;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        setSupportActionBar(findViewById(R.id.toolbar));
        generate = findViewById(R.id.generate);
        actionBar = getSupportActionBar();
        joinRoom = findViewById(R.id.join);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        setupDialog();

        generate.setOnClickListener(v -> {
            String id = new FirebaseSource().createRoom();
            startActivity(new Intent(FriendsActivity.this, RoomActivity.class).putExtra("id", id));
        });

    }

    private void setupDialog() {
        FirebaseSource firebaseSource = new FirebaseSource();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_code);
        EditText code = dialog.findViewById(R.id.code_et);
        MaterialButton join = dialog.findViewById(R.id.join);
        join.setOnClickListener(v -> {
            if (code.getText().toString().length() == 4)
                firebaseSource.joinRoom(code.getText().toString().toUpperCase(Locale.ROOT)).observe(FriendsActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s != null) {
                            Toast.makeText(getApplicationContext(), getString(R.string.room_joined), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(FriendsActivity.this, WaitingRoomActivity.class).putExtra("id", s));
                        }
                        else{
                            Toast.makeText(getApplicationContext(), getString(R.string.room_code_error), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            else
                Toast.makeText(getApplicationContext(), "Invalid room code", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        joinRoom.setOnClickListener(v -> {
            dialog.show();
            code.setText("");
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}