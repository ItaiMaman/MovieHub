package com.example.moviehub.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviehub.R;
import com.example.moviehub.models.Movies;
import com.example.moviehub.utils.MyItemKeyProvider;
import com.example.moviehub.utils.MyItemLookup;

import java.util.List;
import java.util.Objects;

public class SavedMoviesFragment extends Fragment {


    RecyclerView recyclerView;
    SavedMoviesAdapter adapter;
    SavedMoviesViewModel viewModel;
    SelectionTracker<Long> selectionTracker;
    ActionMode actionMode;
    ActionMode.Callback callback;
    MyItemKeyProvider<Long> itemKeyProvider;

    public SavedMoviesFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view =  inflater.inflate(R.layout.fragment_liked_movies, container, false);


        viewModel = ViewModelProviders.of(this).get(SavedMoviesViewModel.class);
        recyclerView = view.findViewById(R.id.saved_movies_rv);
        adapter = new SavedMoviesAdapter(requireContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        itemKeyProvider = new MyItemKeyProvider<Long>(0);

        viewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<List<Movies.Movie>>() {
            @Override
            public void onChanged(List<Movies.Movie> movies) {
                itemKeyProvider.setItemList(movies);
                adapter.setMovies(movies);
                viewModel.getMovies().removeObserver(this);
            }
        });

        selectionTracker = new SelectionTracker.Builder<>("id", recyclerView, itemKeyProvider, new MyItemLookup<Long>(recyclerView), StorageStrategy.createLongStorage()).build();

        callback = new androidx.appcompat.view.ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.profile_actionmode_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(androidx.appcompat.view.ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.select_all) {
                    boolean allSelected = selectionTracker.getSelection().size() == adapter.getMovies().size();
                    selectionTracker.setItemsSelected(itemKeyProvider.getKeyIterable(), !allSelected);
                    return true;
                } else if (item.getItemId() == R.id.delete) {
                    for (Long aLong : (Iterable<Long>) selectionTracker.getSelection()) {
                        int id = aLong.intValue();
                        if (id != -1){
                            adapter.notifyItemRemoved(itemKeyProvider.getPosition(aLong));
                            adapter.getMovies().remove(itemKeyProvider.getPosition(aLong));
                            viewModel.deleteMovie(id);
                        }
                    }
                    if (actionMode != null)
                        actionMode.finish();
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(androidx.appcompat.view.ActionMode mode) {
                selectionTracker.clearSelection();
                adapter.setMovies(adapter.getMovies());
                actionMode = null;
            }
        };

        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onItemStateChanged(@NonNull Long key, boolean selected) {
                super.onItemStateChanged(key, selected);
            }

            @Override
            public void onSelectionChanged() {
                boolean hasSelection = selectionTracker.hasSelection();
                if (hasSelection) {
                    if (actionMode == null) {
                        actionMode = ((AppCompatActivity) requireActivity()).startSupportActionMode(callback);
                        adapter.setMovies(adapter.getMovies());
                    }
                    actionMode.setTitle(selectionTracker.getSelection().size() + " selected");
                } else if (actionMode != null) {
                    actionMode.finish();
                }
            }
        });
        adapter.setSelectionTracker(selectionTracker);



        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            if (adapter.getMovies() != null && !adapter.getMovies().isEmpty())
                selectionTracker.select(Objects.requireNonNull(((SavedMoviesAdapter.ViewHolder) recyclerView.findViewHolderForLayoutPosition(0)).getItemDetails().getSelectionKey()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}