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
import java.util.Collections;
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

        if (categoryTitle == null || categoryTitle.length() < 3) {
            categoryErrorMessage.postValue(application.getString(R.string.cat_title_short));
            return;
        }

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
                categoryErrorMessage.postValue(application.getString(R.string.cat_add_neterror));
            }
        });
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
                categoryErrorMessage.postValue(application.getString(R.string.cat_fetch_neterror));

            }
        });
    }

    public void updateCategoryTitle(String oldCategoryTitle, String newCategoryTitle, String userId) {
        if (newCategoryTitle == null || newCategoryTitle.length() < 3) {
            categoryErrorMessage.postValue(application.getString(R.string.cat_title_short));
            return;
        }
        Call<List<mCategory>> checkCategoryCall = apiService.getCategoriesByUserId(userId);
        checkCategoryCall.enqueue(new Callback<List<mCategory>>() {
            @Override
            public void onResponse(Call<List<mCategory>> call, Response<List<mCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Check if the new category title already exists
                    for (mCategory category : response.body()) {
                        if (category.getCategoryTitle().equals(newCategoryTitle)) {
                            categoryErrorMessage.postValue(application.getString(R.string.category_dupe));
                            return;
                        }
                    }

                    Call<mCategory> updateCall = apiService.patchCategory(oldCategoryTitle, userId, newCategoryTitle);
                    updateCall.enqueue(new Callback<mCategory>() {
                        @Override
                        public void onResponse(Call<mCategory> call, Response<mCategory> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                // Successfully updated the category
                                categorySuccessMessage.postValue(application.getString(R.string.cat_edit_success));
                                fetchCategoriesByUserId(userId);
                            } else {
                                // Handle the 404 response (Category not found)
                                if (response.code() == 404) {
                                    categoryErrorMessage.postValue(application.getString(R.string.cat_not_found));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<mCategory> call, Throwable t) {
                            categoryErrorMessage.postValue(application.getString(R.string.cat_upt_neterror));
                        }
                    });

                } else {
                    Log.e("CategoryViewModel", "Failed to fetch categories. Response code: " + response.code());
                    categoryErrorMessage.postValue(application.getString(R.string.cat_fetchdupe_neterror));
                }
            }
            @Override
            public void onFailure(Call<List<mCategory>> call, Throwable t) {
                categoryErrorMessage.postValue(application.getString(R.string.cat_checkdupe_neterror));
            }
        });
    }

    public void deleteCategory(String userId, String categoryTitle) {
        if (categoryTitle == null || categoryTitle.isEmpty()) {
            categoryErrorMessage.postValue(application.getString(R.string.cat_title_missing));
            return;
        }

        Call<mCategory> call = apiService.deleteCategory(categoryTitle, userId);

        call.enqueue(new Callback<mCategory>() {
            @Override
            public void onResponse(Call<mCategory> call, Response<mCategory> response) {
                if (response.isSuccessful()) {
                    // Successfully deleted category
                    categorySuccessMessage.postValue(application.getString(R.string.category_deleted_success));
                    fetchCategoriesByUserId(userId); // Refresh the categories list
                } else if (response.code() == 404) {
                    // Category not found
                    categoryErrorMessage.postValue(application.getString(R.string.category_not_found));
                } else {
                    // Other failure
                    categoryErrorMessage.postValue(application.getString(R.string.category_delete_fail));
                }
            }

            @Override
            public void onFailure(Call<mCategory> call, Throwable t) {
                // Network error or other failure
                categoryErrorMessage.postValue(application.getString(R.string.category_delete_neterror));
            }
        });
    }




}