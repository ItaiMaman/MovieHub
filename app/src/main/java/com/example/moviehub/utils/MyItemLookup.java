package com.example.moviehub.utils;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviehub.profile.SavedMoviesAdapter;

public class MyItemLookup<L extends Number> extends ItemDetailsLookup<Long> {

    private final RecyclerView recyclerView;

    public MyItemLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof SavedMoviesAdapter.ViewHolder) {
                return ((SavedMoviesAdapter.ViewHolder) viewHolder).getItemDetails();
            }
        }
        return null;
    }
}