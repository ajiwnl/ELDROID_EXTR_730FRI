package com.eldroidfri730.extr.viewmodel.exp_and_cat;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;

public class ExpenseViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public ExpenseViewModelFactory(Application application) {
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ExpenseViewModel.class)) {
            // Pass both Application and LoginViewModel to the constructor
            return (T) new ExpenseViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
