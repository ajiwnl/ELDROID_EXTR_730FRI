package com.eldroidfri730.extr.utils;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseCategoryValidation {

    public static boolean isValidAmount(String amountStr) {
        try {
            Float.parseFloat(amountStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            sdf.setLenient(false);
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static Date parseDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String isNotNullEditText(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError("This field is required");
            return null;
        } else {
            return editText.getText().toString().trim();
        }
    }

    public static String isNotNullTextView(TextView textView) {
        if (textView.getText().toString().trim().isEmpty()) {
            textView.setError("This field is required");
            return null;
        } else {
            return textView.getText().toString().trim();
        }
    }

    public static String isNotNullSpinner(Spinner spinner, Context context, TextView noCategory) {
        if (noCategory.getVisibility() == View.VISIBLE) {
            Toast.makeText(context, "Please create a category", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            if (spinner.getSelectedItem() == null || spinner.getSelectedItem().toString().trim().isEmpty()) {
                Toast.makeText(context, "Please select a category", Toast.LENGTH_SHORT).show();
                return null;
            } else {
                return spinner.getSelectedItem().toString().trim();
            }
        }
    }

}
