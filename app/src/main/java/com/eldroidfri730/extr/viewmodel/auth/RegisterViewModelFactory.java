package com.eldroidfri730.extr.viewmodel.auth;


import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.eldroidfri730.extr.viewmodel.auth.RegisterViewModel;

public class RegisterViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public RegisterViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}