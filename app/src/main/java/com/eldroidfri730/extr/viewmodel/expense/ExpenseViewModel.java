package com.eldroidfri730.extr.viewmodel.expense;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.fragment.app.Fragment;
import android.app.DatePickerDialog;

import com.eldroidfri730.extr.data.CategoryModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExpenseViewModel extends ViewModel {

    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<List<CategoryModel>> categories = new MutableLiveData<>();

    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }

    public LiveData<List<CategoryModel>> getCategories() {
        return categories;
    }

    public void loadCategories() {
        List<CategoryModel> categoryList = new ArrayList<>();

        // Example categories
        categoryList.add(new CategoryModel("1", "Food"));
        categoryList.add(new CategoryModel("2", "Transport"));
        categoryList.add(new CategoryModel("3", "Bills"));
        categoryList.add(new CategoryModel("4", "Entertainment"));

        categories.setValue(categoryList);
    }


    public void showDatePickerDialog(Fragment fragment) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                fragment.requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());
                    selectedDate.setValue(formattedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}
