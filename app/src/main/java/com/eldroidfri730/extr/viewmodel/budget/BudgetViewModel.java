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
import com.eldroidfri730.extr.utils.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetViewModel extends ViewModel {

    private final MutableLiveData<mBudget> addBudgetResponse;
    private final MutableLiveData<List<mBudget>> budget = new MutableLiveData<List<mBudget>>();
    private final MutableLiveData<String> budgetErrorMessage = new MutableLiveData<>();
    private ApiService apiService;
    private final Application application;

    public BudgetViewModel(Application application) {
        this.application = application;
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        addBudgetResponse = new MutableLiveData<>();
    }

    public LiveData<mBudget> getAddBudgetResponse() {
        return addBudgetResponse;
    }

    public void addBudget(mBudget budget) {
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<mBudget> call = apiService.addBudget(budget);

        call.enqueue(new Callback<mBudget>() {
            @Override
            public void onResponse(Call<mBudget> call, Response<mBudget> response) {
                if (response.isSuccessful() && response.body() != null) {
                    addBudgetResponse.setValue(response.body()); // Set the response as the budget
                } else {
                    addBudgetResponse.setValue(null); // Handle failure
                }
            }

            @Override
            public void onFailure(Call<mBudget> call, Throwable t) {
                addBudgetResponse.setValue(null); // Handle failure (e.g., network error)
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
                    Log.d("CategoryViewModel", "Budgets fetched successfully. Count: " + response.body().size());
                    budget.postValue(response.body());
                } else {
                    Log.e("CategoryViewModel", "Failed to fetch budgets. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<mBudget>> call, Throwable t) {
                Log.e("CategoryViewModel", "Failed to fetch categories due to network error or exception.", t);
                budgetErrorMessage.postValue(application.getString(R.string.cat_fetch_neterror));
            }
        });
    }
}
