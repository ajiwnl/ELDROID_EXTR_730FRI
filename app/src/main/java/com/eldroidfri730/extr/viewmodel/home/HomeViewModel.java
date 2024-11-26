package com.eldroidfri730.extr.viewmodel.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eldroidfri730.extr.viewmodel.budget.BudgetViewModel;

public class HomeViewModel extends ViewModel {

    private BudgetViewModel budgetViewModel;

    public HomeViewModel(BudgetViewModel budgetViewModel) {
        this.budgetViewModel = budgetViewModel;
    }

    // Expose the LiveData of the total budget
    public LiveData<Double> getTotalBudget() {
        return budgetViewModel.getTotalBudget();
    }
}
