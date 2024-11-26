package com.eldroidfri730.extr.viewmodel.budget;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.ApiService;
import com.eldroidfri730.extr.data.models.mBudget;
import com.eldroidfri730.extr.data.models.mCategory;
import com.eldroidfri730.extr.utils.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetViewModel extends ViewModel {

    private final MutableLiveData<mBudget> addBudgetResponse;
    private final MutableLiveData<List<mBudget>> budget = new MutableLiveData<List<mBudget>>();
    private final MutableLiveData<String> budgetSuccessMessage = new MutableLiveData<>();
    private final MutableLiveData<String> budgetErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<Double> totalBudget = new MutableLiveData<>(0.0);

    private ApiService apiService;
    private final Application application;

    public BudgetViewModel(Application application) {
        this.application = application;
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        addBudgetResponse = new MutableLiveData<>();
    }
    public LiveData<String> getBudgetSuccessMessage() {
        return budgetSuccessMessage;
    }

    public LiveData<String> getBudgetErrorMessage() {
        return budgetErrorMessage;
    }
    public LiveData<List<mBudget>> getBudgets() {
        return budget;
    }
    public LiveData<mBudget> getAddBudgetResponse() {
        return addBudgetResponse;
    }

    public LiveData<Double> getTotalBudget() {
        return totalBudget;
    }

    public void addBudget(mBudget budget) {
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<mBudget> call = apiService.addBudget(budget);

        call.enqueue(new Callback<mBudget>() {
            @Override
            public void onResponse(Call<mBudget> call, Response<mBudget> response) {
                if (response.isSuccessful() && response.body() != null) {
                    budgetSuccessMessage.postValue(application.getString(R.string.budget_add));
                }
                else if (response.code() == 409) {
                    budgetErrorMessage.postValue(application.getString(R.string.budget_dupe));
                }
                else {
                    budgetErrorMessage.postValue(application.getString(R.string.category_fail));
                }
            }

            @Override
            public void onFailure(Call<mBudget> call, Throwable t) {
                addBudgetResponse.setValue(null); // Handle failure (e.g., network error)
            }
        });
    }

    public void updateBudget(mBudget budget) {
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<mBudget> call = apiService.patchBudget(
                budget.getCategoryTitle(),
                budget.getUserId(),
                String.valueOf(budget.getBudget())
        );

        call.enqueue(new Callback<mBudget>() {
            @Override
            public void onResponse(Call<mBudget> call, Response<mBudget> response) {
                if (response.isSuccessful()) {
                    Log.d("Budget Update", "Budget updated successfully: " + response.body());
                } else {
                    Log.e("Budget Update", "Failed to update budget. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<mBudget> call, Throwable t) {
                Log.e("Budget Update", "API call failed.", t);
            }
        });
    }

    public void fetchBudgetByUserId(String userId) {
        Log.d("BudgetViewModel", "Fetching Budget for userId: " + userId);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<mBudget>> call = apiService.getBudgetByUserId(userId);

        call.enqueue(new Callback<List<mBudget>>() {
            @Override
            public void onResponse(Call<List<mBudget>> call, Response<List<mBudget>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("BudgetViewModel", "Budgets fetched successfully. Count: " + response.body().size());
                    List<mBudget> budgets = response.body();

                    // Post the budgets to the LiveData
                    budget.postValue(budgets);

                    // Calculate the total budget
                    double total = 0.0;
                    for (mBudget budget : budgets) {
                        total += budget.getBudget(); // Assuming mBudget has a getAmount() method
                    }

                    // Update the total budget LiveData
                    totalBudget.postValue(total);
                } else {
                    Log.e("BudgetViewModel", "Failed to fetch budgets. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<mBudget>> call, Throwable t) {
                Log.e("BudgetViewModel", "Failed to fetch budgets due to network error or exception.", t);
                budgetErrorMessage.postValue(application.getString(R.string.cat_fetch_neterror));
            }
        });
    }
}
