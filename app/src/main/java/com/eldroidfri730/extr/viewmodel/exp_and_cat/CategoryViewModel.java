package com.eldroidfri730.extr.viewmodel.exp_and_cat;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;

import com.eldroidfri730.extr.data.ApiService;
import com.eldroidfri730.extr.data.models.mCategory;
import com.eldroidfri730.extr.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel extends AndroidViewModel  {

    private final MutableLiveData<List<mCategory>> categories = new MutableLiveData<>();

    public CategoryViewModel(Application application) {
        super(application);
    }

    public LiveData<List<mCategory>> getCategories() {
        return categories;
    }

    public void addCategory(String userId, String categoryTitle) {
        Log.d("CategoryViewModel", "addCategory called with userId: " + userId + ", categoryTitle: " + categoryTitle);

        // Create the category object with userId and categoryTitle
        mCategory newCategory = new mCategory(userId, categoryTitle);

        // Log the payload being sent
        Log.d("CategoryViewModel", "Created mCategory object: " + newCategory);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<mCategory> call = apiService.addCategory(newCategory);

        Log.d("CategoryViewModel", "Initiating API call to addCategory...");

        call.enqueue(new Callback<mCategory>() {
            @Override
            public void onResponse(Call<mCategory> call, Response<mCategory> response) {
                Log.d("CategoryViewModel", "API call successful. Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("CategoryViewModel", "Category added successfully. Response body: " + response.body());
                    // Handle success as before
                } else {
                    Log.e("CategoryViewModel", "API call failed. Response message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<mCategory> call, Throwable t) {
                Log.e("CategoryViewModel", "API call failed due to network error or exception.", t);
            }
        });
    }


    public ArrayAdapter<mCategory> getCategoryAdapter(Context context) {
        List<mCategory> categoryList = categories.getValue();
        if (categoryList == null) {
            categoryList = new ArrayList<>();
        }
        ArrayAdapter<mCategory> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public void fetchCategoriesByUserId(String userId) {
        Log.d("CategoryViewModel", "Fetching categories for userId: " + userId);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<mCategory>> call = apiService.getCategoriesByUserId(userId);

        call.enqueue(new Callback<List<mCategory>>() {
            @Override
            public void onResponse(Call<List<mCategory>> call, Response<List<mCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("CategoryViewModel", "Categories fetched successfully. Count: " + response.body().size());
                    categories.postValue(response.body());
                } else {
                    Log.e("CategoryViewModel", "Failed to fetch categories. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<mCategory>> call, Throwable t) {
                Log.e("CategoryViewModel", "Failed to fetch categories due to network error or exception.", t);
            }
        });
    }
}