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

import com.eldroidfri730.extr.data.models.mExpense;
import com.eldroidfri730.extr.utils.ExpenseCategoryValidation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpenseViewModel extends AndroidViewModel {

    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<List<mExpense>> expenses = new MutableLiveData<>();

    public ExpenseViewModel(Application application){
        super(application);
    }

    public LiveData<List<mExpense>> getExpenses() {return expenses;}
    public LiveData<String> getSelectedDate() {return selectedDate;}


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

    public void createExpense(String name, String amountStr, String dateStr, String category, String desc, Context context) {

        mExpense newExpense;

        if (!ExpenseCategoryValidation.isValidAmount(amountStr)) {
            Toast.makeText(context, "Invalid Number", Toast.LENGTH_SHORT).show();
        }

        if (!ExpenseCategoryValidation.isValidDate(dateStr)) {
            Toast.makeText(context, "Invalid Date Format use (dd/mm/yyyy)", Toast.LENGTH_SHORT).show();
        }

        float amount = Float.parseFloat(amountStr);
        Date date = ExpenseCategoryValidation.parseDate(dateStr);

        newExpense = new mExpense(name, category, amount, date, desc);

        displayDataOnSubmit(newExpense);
    }

    //Change to add() Crude later
    public void displayDataOnSubmit(mExpense newExpense){
        Log.d("ExpenseData", "Expense Name: " + newExpense.getName());
        Log.d("ExpenseData", "Category: " + newExpense.getCategory());
        Log.d("ExpenseData", "Amount: " + newExpense.getAmount());
        Log.d("ExpenseData", "Date: " + newExpense.getFormattedDate());
        Log.d("ExpenseData", "Description: " + newExpense.getDesc());
    }


}
