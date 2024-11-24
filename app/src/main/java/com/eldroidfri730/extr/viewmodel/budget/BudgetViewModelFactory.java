package com.eldroidfri730.extr.viewmodel.budget;



import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class BudgetViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public BudgetViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BudgetViewModel.class)) {
            return (T) new BudgetViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}