package com.eldroidfri730.extr.viewmodel.exp_and_cat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.fragment.app.Fragment;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.eldroidfri730.extr.data.ExpenseModel;
import com.eldroidfri730.extr.utils.ExpenseValidation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExpenseViewModel extends ViewModel {

    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();

    public LiveData<String> getSelectedDate() {
        return selectedDate;
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

    public void createExpense(String name, String amountStr, String dateStr, String category, String desc, Context context) {

        ExpenseModel newExpense;

        if (!ExpenseValidation.isValidAmount(amountStr)) {
            Toast.makeText(context, "Invalid Number", Toast.LENGTH_SHORT).show();
        }

        if (!ExpenseValidation.isValidDate(dateStr)) {
            Toast.makeText(context, "Invalid Date Format use (dd/mm/yyyy)", Toast.LENGTH_SHORT).show();
        }

        float amount = Float.parseFloat(amountStr);
        Date date = ExpenseValidation.parseDate(dateStr);

        newExpense = new ExpenseModel(name, category, amount, date, desc);

         displayDataOnSubmit(newExpense);
    }

    //Change to add() Crude later
    public void displayDataOnSubmit(ExpenseModel newExpense){
        Log.d("ExpenseData", "Expense Name: " + newExpense.getName());
        Log.d("ExpenseData", "Category: " + newExpense.getCategory());
        Log.d("ExpenseData", "Amount: " + newExpense.getAmount());
        Log.d("ExpenseData", "Date: " + newExpense.getFormattedDate());
        Log.d("ExpenseData", "Description: " + newExpense.getDesc());
    }


}
