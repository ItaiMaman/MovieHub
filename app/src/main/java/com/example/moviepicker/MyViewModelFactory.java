package com.example.moviepicker;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviepicker.Rooms.RoomViewModel;
import com.example.moviepicker.Rooms.SwipeMatchViewModel;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;
    private int type;


    public MyViewModelFactory(Application application, String param, int type) {
        mApplication = application;
        mParam = param;
        this.type = type;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if(type == 0)
        return (T) new SwipeMatchViewModel(mApplication, mParam);
        else
            return (T) new RoomViewModel(mApplication, mParam);
    }
}