package com.eldroidfri730.extr.viewmodel.budget;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.eldroidfri730.extr.data.ApiService;
import com.eldroidfri730.extr.data.models.mBudget;
import com.eldroidfri730.extr.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetViewModel extends AndroidViewModel {

    private final MutableLiveData<mBudget> addBudgetResponse;

    public BudgetViewModel(Application application) {
        super(application);
        addBudgetResponse = new MutableLiveData<>();
    }

    public LiveData<mBudget> getAddBudgetResponse() {
        return addBudgetResponse;
    }

    public void addBudget(mBudget budget) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
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
}
