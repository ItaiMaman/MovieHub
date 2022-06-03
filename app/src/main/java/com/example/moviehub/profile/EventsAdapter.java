package com.example.moviehub.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviehub.FirebaseSource;
import com.example.moviehub.R;
import com.example.moviehub.models.Event;
import com.example.moviehub.utils.FirebaseApp;
import com.example.moviehub.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private final Context context;
    private List<Event> events;
    private final EventsFragment fragment;

    public EventsAdapter(EventsFragment fragment) {
        this.fragment = fragment;
        this.context =fragment.requireContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        Glide.with(context).load(Utils.IMAGE_URL + event.getMovie().getPosterPath()).centerCrop().into(holder.image);
        holder.title.setText(event.getMovie().getTitle());
        SimpleDateFormat localDateFormat = new SimpleDateFormat("EEE, MMMM dd @ HH:mm");
        localDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String time = localDateFormat.format(event.getDate());
        holder.time.setText(time);

        event.getFriends().remove(FirebaseAuth.getInstance().getUid());

        fragment.getUsername(event.getFriends()).observe(fragment.getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                String friends = "With " + prettyPrint(strings);
                holder.friends.setText(friends);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (events != null)
            return events.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView image;
        TextView title, time, friends;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.txt);
            time = itemView.findViewById(R.id.date);
            friends = itemView.findViewById(R.id.friends);
        }
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        Collections.reverse(events);
        notifyDataSetChanged();
    }

    private String prettyPrint(List<String> strings) {
        String tmp = "";
        for (int i = 0; i < strings.size(); i++) {
            tmp += strings.get(i);
            if (strings.size() - i > 2)
                tmp += ", ";
            else if (i < strings.size() - 1)
                tmp += " and ";
        }
        return tmp;
    }
}
