package com.eldroidfri730.extr.viewmodel.home;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.eldroidfri730.extr.viewmodel.budget.BudgetViewModel;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final BudgetViewModel budgetViewModel;

    public HomeViewModelFactory(BudgetViewModel budgetViewModel) {
        this.budgetViewModel = budgetViewModel;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(budgetViewModel);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
