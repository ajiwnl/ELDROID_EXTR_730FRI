package com.eldroidfri730.extr.viewmodel.exp_and_cat;

import android.app.Application;
import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eldroidfri730.extr.data.models.mCategory;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel extends ViewModel {
    private final MutableLiveData<List<mCategory>> categories = new MutableLiveData<>();

    private Application application;

    public CategoryViewModel(Application application) {
        this.application = application;
    }
    public LiveData<List<mCategory>> getCategories() {
        return categories;
    }


    public void addCategory(String name, String desc) {
        List<mCategory> currentCategories = categories.getValue();
        if (currentCategories == null) {
            currentCategories = new ArrayList<>();
        }
        currentCategories.add(new mCategory(name, desc));
        categories.setValue(currentCategories);
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

}
