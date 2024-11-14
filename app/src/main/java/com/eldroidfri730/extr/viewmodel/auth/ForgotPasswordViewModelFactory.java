package com.eldroidfri730.extr.viewmodel.auth;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ForgotPasswordViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public ForgotPasswordViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel.class)) {
            return (T) new ForgotPasswordViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
