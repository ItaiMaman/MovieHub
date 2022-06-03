package com.example.moviehub.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import com.example.moviehub.models.Movies;

import java.util.ArrayList;
import java.util.List;

public class MyItemKeyProvider<L extends Number> extends ItemKeyProvider<Long> {

    private List<Movies.Movie> itemList;

    public MyItemKeyProvider(int scope) {
        super(scope);
        this.itemList = new ArrayList<>();
    }

    @Nullable
    @Override
    public Long getKey(int position) {
        return Long.valueOf(itemList.get(position).getId());
    }

    @Override
    public int getPosition(@NonNull Long key) {
        for (Movies.Movie i : itemList)
            if (i.getId() == key.intValue())
                return itemList.indexOf(i);
        return -1;
    }

    public void setItemList(List<Movies.Movie> movies) {
        this.itemList = movies;
    }

    public Iterable<Long> getKeyIterable(){
        ArrayList<Long> iterable = new ArrayList<>();
        for(Movies.Movie i : itemList)
            iterable.add(Long.valueOf(i.getId()));
        return iterable;
    }
}