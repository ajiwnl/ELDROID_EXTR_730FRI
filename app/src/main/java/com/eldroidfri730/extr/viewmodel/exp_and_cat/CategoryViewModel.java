package com.eldroidfri730.extr.viewmodel.exp_and_cat;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.ApiService;
import com.eldroidfri730.extr.data.models.mCategory;
import com.eldroidfri730.extr.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel extends ViewModel {

    private final MutableLiveData<List<mCategory>> categories = new MutableLiveData<>();
    private final MutableLiveData<String> categorySuccessMessage = new MutableLiveData<>();
    private final MutableLiveData<String> categoryErrorMessage = new MutableLiveData<>();
    private final Application application;
    private final ApiService apiService;


    public CategoryViewModel(Application application) {
        this.application = application;
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

    }

    public LiveData<List<mCategory>> getCategories() {
        return categories;
    }

    public LiveData<String> getCategorySuccessMessage() {
        return categorySuccessMessage;
    }

    public LiveData<String> getCategoryErrorMessage() {
        return categoryErrorMessage;
    }

    public void addCategory(String userId, String categoryTitle) {
        mCategory newCategory = new mCategory(userId, categoryTitle);

        Call<mCategory> call = apiService.addCategory(newCategory);

        call.enqueue(new Callback<mCategory>() {
            @Override
            public void onResponse(Call<mCategory> call, Response<mCategory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categorySuccessMessage.postValue(application.getString(R.string.category_add));
                } else if (response.code() == 409) {
                    categoryErrorMessage.postValue(application.getString(R.string.category_dupe));
                } else {
                    categoryErrorMessage.postValue(application.getString(R.string.category_fail));
                }
            }

            @Override
            public void onFailure(Call<mCategory> call, Throwable t) {
                categoryErrorMessage.postValue("Failed to add category due to network error.");
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