package com.eldroidfri730.extr.viewmodel.exp_and_cat;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eldroidfri730.extr.data.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel extends ViewModel {
    private final MutableLiveData<List<CategoryModel>> categories = new MutableLiveData<>();

    public LiveData<List<CategoryModel>> getCategories() {
        return categories;
    }


    public void addCategory(String name, String desc) {
        List<CategoryModel> currentCategories = categories.getValue();
        if (currentCategories == null) {
            currentCategories = new ArrayList<>();
        }
        currentCategories.add(new CategoryModel(name, desc));
        categories.setValue(currentCategories);
    }

    public ArrayAdapter<CategoryModel> getCategoryAdapter(Context context) {
        List<CategoryModel> categoryList = categories.getValue();
        if (categoryList == null) {
            categoryList = new ArrayList<>();
        }
        ArrayAdapter<CategoryModel> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

}
