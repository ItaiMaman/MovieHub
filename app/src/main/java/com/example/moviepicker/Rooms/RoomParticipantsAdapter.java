package com.example.moviepicker.Rooms;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviepicker.R;
import com.example.moviepicker.Room;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class RoomParticipantsAdapter extends RecyclerView.Adapter<RoomParticipantsAdapter.ViewHolder> {

    private RoomInterface activity;
    private List<String> users;
    private Room room;

    public RoomParticipantsAdapter(RoomInterface activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        activity.getUsername(users.get(position)).observe(activity, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (users.get(position).equals(room.getOwnerId()))
                    s += " (host)";
                else if(users.get(position).equals(FirebaseAuth.getInstance().getUid()))
                    s += " (you)";
                holder.username.setText(s);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (users != null)
            return users.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
        }
    }

    public void setRoom(Room room) {
        this.room = room;
        if(room.getUsers() != null)
            this.users = new ArrayList<>(room.getUsers().keySet());
        else
            this.users = new ArrayList<>();
        this.users.add(0, room.getOwnerId());
        Log.d("tag", "users " + users.size());

        notifyDataSetChanged();
    }

    public List<String> getUsers() {
        return users;
    }
}
