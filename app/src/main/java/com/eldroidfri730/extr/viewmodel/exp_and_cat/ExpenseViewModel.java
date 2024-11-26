package com.eldroidfri730.extr.viewmodel.exp_and_cat;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Transformations;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.ApiService;
import com.eldroidfri730.extr.data.models.mCategory;
import com.eldroidfri730.extr.data.models.mExpense;
import com.eldroidfri730.extr.utils.ExpenseCategoryValidation;
import com.eldroidfri730.extr.utils.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseViewModel extends AndroidViewModel {

    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<List<mExpense>> expenses = new MutableLiveData<>();

    private final MutableLiveData<String> expenseName = new MutableLiveData<>();
    private final MutableLiveData<String> expenseAmount = new MutableLiveData<>();
    private final MutableLiveData<String> expenseDescription = new MutableLiveData<>();
    private final MutableLiveData<String> expenseCategory = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFormValid = new MutableLiveData<>(false);

    private final MutableLiveData<List<mExpense>> filteredExpenses = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<mExpense>> getFilteredExpenses() {
        return Transformations.distinctUntilChanged(filteredExpenses);
    }

    private final ApiService apiService;

    public ExpenseViewModel(Application application){
        super(application);
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    //Setter and Update if nullers
    public LiveData<List<mExpense>> getExpenses() {return expenses;}

    public LiveData<String> getSelectedDate() {return selectedDate;}

    public MutableLiveData<String> getExpenseName() {
        return expenseName;
    }

    public MutableLiveData<String> getExpenseAmount() {
        return expenseAmount;
    }

    public MutableLiveData<String> getExpenseDescription() {
        return expenseDescription;
    }

    public MutableLiveData<String> getExpenseCategory() {
        return expenseCategory;
    }

    public MutableLiveData<Boolean> isFormValid() {
        return isFormValid;
    }

    public void updateSelectedDate(String date) {
        selectedDate.setValue(date);
        validateForm();
    }

    public void updateExpenseName(String name) {
        expenseName.setValue(name != null ? name.trim() : "");
        validateForm();
    }


    public void updateExpenseAmount(String amount) {
        expenseAmount.setValue(amount);
        validateForm();
    }

    public void updateExpenseDescription(String description) {
        expenseDescription.setValue(description);
    }

    public void updateExpenseCategory(String category) {
        expenseCategory.setValue(category);
        validateForm();
    }

    private void validateForm() {
        boolean isValid = ExpenseCategoryValidation.isValidAmount(expenseAmount.getValue()) &&
                ExpenseCategoryValidation.isValidDate(selectedDate.getValue()) &&
                expenseName.getValue() != null &&
                !expenseName.getValue().trim().isEmpty() &&
                expenseCategory.getValue() != null &&
                !expenseCategory.getValue().trim().isEmpty();

        isFormValid.setValue(isValid);
    }

    public void clearExpenseValue(){
        updateExpenseName(null);
        updateExpenseAmount(null);
        updateExpenseCategory(null);
        updateExpenseDescription(null);
        updateSelectedDate(null);
    }

    public void addExpense(String name, String category, float amount, Date date, String desc, String userId) {
        // Convert Date to String in the required format for the API request
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(date);

        // Log the expense details before making the API call
        Log.d("ExpenseViewModel", "Adding expense with the following details:");
        Log.d("ExpenseViewModel", "Expense Name: " + name);
        Log.d("ExpenseViewModel", "Category: " + category);
        Log.d("ExpenseViewModel", "Amount: " + amount);
        Log.d("ExpenseViewModel", "Date: " + formattedDate);
        Log.d("ExpenseViewModel", "Description: " + desc);
        Log.d("ExpenseViewModel", "User ID: " + userId);

        // Create mExpense object with Date type (don't convert the date here)
        mExpense expense = new mExpense(name, category, amount, formattedDate, desc, userId);

        // Make the API call
        apiService.addExpense(expense).enqueue(new Callback<mExpense>() {
            @Override
            public void onResponse(Call<mExpense> call, Response<mExpense> response) {
                // Log response details
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ExpenseViewModel", "Expense added successfully: " + response.body());
                    Log.d("ExpenseViewModel", "Response Code: " + response.code());
                    Log.d("ExpenseViewModel", "Response Message: " + response.message());
                    Toast.makeText(getApplication(), "Expense added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ExpenseViewModel", "Server error: " + response.code() + ", " + response.message());
                    Log.e("ExpenseViewModel", "Response Body: " + (response.body() != null ? response.body().toString() : "null"));
                    Toast.makeText(getApplication(), "Failed to add expense: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<mExpense> call, Throwable t) {
                // Log failure details
                Log.e("ExpenseViewModel", "Network error: " + t.getMessage());
                Log.e("ExpenseViewModel", "Error: ", t);  // Log the full stack trace
                Toast.makeText(getApplication(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchExpensesByUserId(String userId) {
        Log.d("ExpenseViewModel", "Fetching expenses for user ID: " + userId);

        apiService.getExpensesByUserId(userId).enqueue(new Callback<List<mExpense>>() {
            @Override
            public void onResponse(Call<List<mExpense>> call, Response<List<mExpense>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<mExpense> fetchedExpenses = response.body();
                    // Update the MutableLiveData
                    expenses.setValue(fetchedExpenses);
                    // Log each expense
                    for (mExpense expense : fetchedExpenses) {
                        Log.d("ExpenseViewModel", "Fetched Expense: " + expense.toString());
                    }
                } else {
                    Log.e("ExpenseViewModel", "Server error: " + response.code() + ", " + response.message());
                    Log.e("ExpenseViewModel", "Response Body: " + (response.body() != null ? response.body().toString() : "null"));
                }
            }

            @Override
            public void onFailure(Call<List<mExpense>> call, Throwable t) {
                Log.e("ExpenseViewModel", "Network error while fetching expenses: " + t.getMessage());
                Log.e("ExpenseViewModel", "Error: ", t);
            }
        });
    }

    public void updateExpense(){

    }


    public void deleteExpense(String userId, String expenseName) {
        if (expenseName == null || expenseName.isEmpty()) {
            Log.e("deleteExpense", "Expense name is null or empty");
            return;
        }

        Log.d("deleteExpense", "Attempting to delete expense: " + expenseName + " for user: " + userId);

        Call<mExpense> call = apiService.deleteExpense(expenseName,userId);

        call.enqueue(new Callback<mExpense>() {
            @Override
            public void onResponse(Call<mExpense> call, Response<mExpense> response) {
                if (response.isSuccessful()) {
                    // Successfully deleted expense
                    Log.d("deleteExpense", "Expense deleted successfully for user: " + userId + ", expense: " + expenseName);
                    fetchExpensesByUserId(userId);
                } else if (response.code() == 404) {
                    // expense not found
                    Log.e("deleteExpense", "Expense not found: " + expenseName + " for user: " + userId + response.body().toString());
                } else {
                    // Other failure
                    Log.e("deleteExpense", "Failed to delete expense. Response code: " + response.code() + ", message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<mExpense> call, Throwable t) {
                // Network error or other failure
                Log.e("deleteExpense", "Error occurred while deleting expense: " + expenseName + " for user: " + userId, t);
            }
        });
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
                    updateSelectedDate(formattedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    public void filterExpensesByCategory(String category) {
        List<mExpense> allExpenses = expenses.getValue();
        if (allExpenses == null || category == null || category.isEmpty()) {
            filteredExpenses.postValue(new ArrayList<>());
            return;
        }

        List<mExpense> filteredList = new ArrayList<>();
        for (mExpense expense : allExpenses) {
            if (expense.getCategoryTitle() != null && expense.getCategoryTitle().equalsIgnoreCase(category)) {
                filteredList.add(expense);
            }
        }

        filteredExpenses.postValue(filteredList);
    }


    public Map<String, Float> calculateTotalExpensesByCategory() {
        Map<String, Float> categoryTotals = new HashMap<>();
        List<mExpense> allExpenses = expenses.getValue();

        if (allExpenses == null || allExpenses.isEmpty()) return categoryTotals;

        for (mExpense expense : allExpenses) {
            String category = expense.getCategoryTitle();
            float amount = expense.getAmount();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0f) + amount);
        }

        return categoryTotals;
    }



}
