package com.eldroidfri730.extr.ui.exp_and_cat;

import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.ui.home.BasicSummaryActivity;
import com.eldroidfri730.extr.utils.ExpenseCategoryValidation;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModelFactory;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.ExpenseViewModel;

public class ExpenseFragment extends Fragment {

    private EditText expenseNameEditText,  expenseAmountEditText, expenseDescEditText;
    private TextView datePurchasedEditText, noCategory;
    private Spinner categorySpinner;
    private ExpenseViewModel expenseViewModel;

    private CategoryViewModelFactory categoryViewModelFactory;
    private CategoryViewModel categoryViewModel;
    private ImageButton backButton;
    private Button submitButton;

    private Application app;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_expense, container, false);

        backButton = rootView.findViewById(R.id.expensebackbutton);
        submitButton = rootView.findViewById(R.id.expensesubmitbutton);
        categorySpinner = rootView.findViewById(R.id.expensecategoryspinner);
        datePurchasedEditText = rootView.findViewById(R.id.datepurchasededittext);
        expenseNameEditText = rootView.findViewById(R.id.expensenameedittext);
        expenseAmountEditText = rootView.findViewById(R.id.expenseamountedittext);
        expenseDescEditText = rootView.findViewById(R.id.expensedescedittext);
        noCategory = rootView.findViewById(R.id.AddInitialCat);

        expenseViewModel = ((BasicSummaryActivity) getActivity()).getExpenseViewModel();
        categoryViewModel = ((BasicSummaryActivity) getActivity()).getCategoryViewModel();

        datePurchasedEditText.setOnClickListener(v -> {
            expenseViewModel.showDatePickerDialog(this);
        });

        expenseViewModel.getSelectedDate().observe(getViewLifecycleOwner(), date -> {
            datePurchasedEditText.setText(date);
        });

        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            Log.d("Categories", categories.toString());
            if(!categories.isEmpty()){
                categorySpinner.setAdapter(categoryViewModel.getCategoryAdapter(getContext()));
                categorySpinner.setVisibility(View.VISIBLE);
                noCategory.setVisibility(View.GONE);
            }
        });

        submitButton.setOnClickListener(v -> {
            String name = ExpenseCategoryValidation.isNotNullEditText(expenseNameEditText);
            String amountStr = ExpenseCategoryValidation.isNotNullEditText(expenseAmountEditText);
            String dateStr = ExpenseCategoryValidation.isNotNullTextView(datePurchasedEditText);
            String category = ExpenseCategoryValidation.isNotNullSpinner(categorySpinner, rootView.getContext(), noCategory);

            if (name != null && amountStr != null && dateStr != null && category != null) {
                expenseViewModel.createExpense(name, amountStr, dateStr, category, expenseDescEditText.getText().toString(), rootView.getContext());
                expenseNameEditText.setText("");
                expenseAmountEditText.setText("");
                datePurchasedEditText.setText("");
                expenseDescEditText.setText("");
                categorySpinner.setSelection(0);
            }
        });

        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return rootView;
    }
}
