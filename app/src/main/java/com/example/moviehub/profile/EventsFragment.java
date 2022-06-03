package com.example.moviehub.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviehub.FirebaseSource;
import com.example.moviehub.R;

import java.util.List;

public class EventsFragment extends Fragment {

    RecyclerView recyclerView;
    EventsAdapter adapter;
    FirebaseSource firebaseSource;
    public EventsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        firebaseSource = new FirebaseSource();
        recyclerView = view.findViewById(R.id.events_recyclerview);
        adapter = new EventsAdapter(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        firebaseSource.getEvents().observe(getViewLifecycleOwner(), events -> adapter.setEvents(events));



        return view;
    }

    public LiveData<List<String>> getUsername(List<String> uids){
        return firebaseSource.getUsernames(uids);
    }
}