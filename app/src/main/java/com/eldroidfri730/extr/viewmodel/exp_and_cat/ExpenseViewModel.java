package com.eldroidfri730.extr.viewmodel.exp_and_cat;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.fragment.app.Fragment;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.ApiService;
import com.eldroidfri730.extr.data.models.mExpense;
import com.eldroidfri730.extr.utils.ExpenseCategoryValidation;
import com.eldroidfri730.extr.utils.RetrofitClient;
import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExpenseViewModel extends AndroidViewModel {

    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<List<mExpense>> expenses = new MutableLiveData<>();

    private final MutableLiveData<String> expenseName = new MutableLiveData<>();
    private final MutableLiveData<String> expenseAmount = new MutableLiveData<>();
    private final MutableLiveData<String> expenseDescription = new MutableLiveData<>();
    private final MutableLiveData<String> expenseCategory = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFormValid = new MutableLiveData<>(false);

    private final ApiService apiService;
    private final LoginViewModel loginViewModel; // Add LoginViewModel reference

    public ExpenseViewModel(Application application, LoginViewModel loginViewModel){
        super(application);
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        this.loginViewModel = loginViewModel;  // Initialize loginViewModel
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
        expenseName.setValue(name);
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

    //Crude Section

    public void createExpenseModel(String name, String amountStr, String dateStr, String category, String desc, Context context, String userId, int requestId) {
        try {
            float amount = Float.parseFloat(amountStr);
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateStr);

            mExpense newExpense = new mExpense(name, category, amount, date, desc, userId);

            if (requestId == 0) {
                addExpense(newExpense);
            } else {
                Log.e("ExpenseViewModel", "Invalid request ID");
            }
        } catch (Exception e) {
            Toast.makeText(context, "Invalid input data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    public void addExpense(mExpense expense) {
        apiService.addExpense(expense).enqueue(new Callback<mExpense>() {
            @Override
            public void onResponse(Call<mExpense> call, Response<mExpense> response) {
                if (response.isSuccessful()) {
                    Log.d("ExpenseViewModel", "Expense added successfully: " + response.body());
                    Toast.makeText(getApplication(), "Expense added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ExpenseViewModel", "Error: " + response.message());
                    Toast.makeText(getApplication(), "Failed to add expense.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<mExpense> call, Throwable t) {
                Log.e("ExpenseViewModel", "Network error: " + t.getMessage());
                Toast.makeText(getApplication(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void updateExpense(String userId, mExpense expense) {
        //Update Request
        Log.d("ExpenseAction", "Updating expense for user: " + userId + ", Expense: " + expense.getName()+" "+expense.getCategory()+" "+expense.getAmount()+" "+expense.getDesc()+" "+expense.getFormattedDate()+expense.getDesc());
    }

    public void deleteExpense(String userId, mExpense expense) {
        //Delete Request
        Log.d("ExpenseAction", "Deleting expense for user: " + userId + ", Expense " + " " + expense.getName()+" "+expense.getCategory()+" "+expense.getAmount()+" "+expense.getDesc()+" "+expense.getFormattedDate()+" "+expense.getDesc());
    }

    //Date

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
        // Get the current list of expenses
        List<mExpense> allExpenses = expenses.getValue();
        if (allExpenses == null) {
            return;
        }

        // Filter the expenses by the given category
        List<mExpense> filteredExpenses = new ArrayList<>();
        for (mExpense expense : allExpenses) {
            if (expense.getCategory().equalsIgnoreCase(category)) {
                filteredExpenses.add(expense);
            }
        }

        // Update the MutableLiveData with the filtered list
        expenses.setValue(filteredExpenses);
    }
}
