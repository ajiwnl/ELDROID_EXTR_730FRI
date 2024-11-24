package com.eldroidfri730.extr.viewmodel.prof_and_set;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    // Updated constructor to accept LoginViewModel
    public ProfileViewModelFactory(Application application) {
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            // Pass loginViewModel to the ProfileViewModel constructor
            return (T) new ProfileViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
