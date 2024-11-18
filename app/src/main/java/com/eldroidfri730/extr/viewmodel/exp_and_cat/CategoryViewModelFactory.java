package com.eldroidfri730.extr.viewmodel.exp_and_cat;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CategoryViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public CategoryViewModelFactory(Application application) {
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CategoryViewModel.class)) {
            // Return an instance of CategoryViewModel
            return (T) new CategoryViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
