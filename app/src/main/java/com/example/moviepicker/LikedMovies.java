package com.example.moviepicker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviepicker.profile.SavedMoviesAdapter;
import com.example.moviepicker.profile.SavedMoviesViewModel;

import java.util.List;

public class LikedMovies extends Fragment {


    RecyclerView recyclerView;
    SavedMoviesAdapter adapter;
    SavedMoviesViewModel viewModel;

    public LikedMovies() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_liked_movies, container, false);


        viewModel = ViewModelProviders.of(this).get(SavedMoviesViewModel.class);
        recyclerView = view.findViewById(R.id.saved_movies_rv);
        adapter = new SavedMoviesAdapter(requireContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        viewModel.getMovies().observe(getViewLifecycleOwner(), movies -> adapter.setMovies(movies));

        return view;
    }
}