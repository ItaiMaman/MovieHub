package com.example.moviehub.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviehub.Rooms.RoomViewModel;
import com.example.moviehub.Rooms.SwipeMatchViewModel;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;
    private final String mParam;
    private final int type;


    public MyViewModelFactory(Application application, String param, int type) {
        mApplication = application;
        mParam = param;
        this.type = type;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if(type == 0)
        return (T) new SwipeMatchViewModel(mApplication, mParam);
        else
            return (T) new RoomViewModel(mApplication, mParam);
    }
}