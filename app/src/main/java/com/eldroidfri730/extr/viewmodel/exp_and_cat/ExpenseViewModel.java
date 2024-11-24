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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
        final int ADD_EXP_REQ = 0;
        final int UP_EXP_REQ = 1;
        final int DEL_EXP_REQ = 2;

        mExpense newExpense = new mExpense(
                name,
                category,
                Float.parseFloat(amountStr),
                ExpenseCategoryValidation.parseDate(dateStr),
                desc == null ? "" : desc
        );

        switch (requestId) {
            case ADD_EXP_REQ:
                addExpense(userId, newExpense);
                break;

            case UP_EXP_REQ:
                updateExpense(userId, newExpense);
                break;

            case DEL_EXP_REQ:
                deleteExpense(userId, newExpense);
                break;

            default:
                Toast.makeText(context, R.string.vmExpenseReqTypeErr, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void addExpense(String userId, mExpense expense) {
        //Add Request
        Log.d("ExpenseAction", "Adding expense for user: " + userId + ", Expense " +expense.getName()+" "+expense.getCategory()+" "+expense.getAmount()+" "+expense.getDesc()+" "+expense.getFormattedDate()+expense.getDesc());
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


}
