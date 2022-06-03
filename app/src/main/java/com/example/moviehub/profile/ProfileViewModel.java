package com.example.moviehub.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.moviehub.FirebaseSource;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileViewModel extends AndroidViewModel {

    private LiveData<String> username;
    private String email;
    private FirebaseSource firebaseSource;
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        firebaseSource = new FirebaseSource();
        username = firebaseSource.getUsername(null);
        email = firebaseSource.getEmail();
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
