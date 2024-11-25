package com.eldroidfri730.extr.ui.exp_and_cat;

import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.models.mCategory;
import com.eldroidfri730.extr.data.models.mExpense;
import com.eldroidfri730.extr.ui.home.BasicSummaryActivity;
import com.eldroidfri730.extr.utils.ExpenseCategoryValidation;
import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModelFactory;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.ExpenseViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpenseFragment extends Fragment {

    private EditText expenseNameEditText,  expenseAmountEditText, expenseDescEditText;
    private TextView datePurchasedEditText, noCategory;
    private Spinner categorySpinner;
    private ImageButton backButton;
    private Button submitButton;

    private ExpenseViewModel expenseViewModel;
    private CategoryViewModel categoryViewModel;

    private String userId;
    private boolean isCategoriesFetched = false;

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

        LoginViewModel loginViewModel = ((BasicSummaryActivity) getActivity()).getLoginViewModel();
        userId = loginViewModel.getUserId();

        expenseViewModel = ((BasicSummaryActivity) getActivity()).getExpenseViewModel();
        categoryViewModel = ((BasicSummaryActivity) getActivity()).getCategoryViewModel();

        if (userId != null && !isCategoriesFetched) {
            categoryViewModel.fetchCategoriesByUserId(userId);
            expenseViewModel.fetchExpensesByUserId(userId);

            isCategoriesFetched = true;
        } else {
            Toast.makeText(getContext(), getString(R.string.user_out), Toast.LENGTH_SHORT).show();
        }

        expenseViewModel.clearExpenseValue();

        setupBtnListeners();

        setupObservers();

        return rootView;
    }

    private void setupBtnListeners() {
        datePurchasedEditText.setOnClickListener(v -> expenseViewModel.showDatePickerDialog(this));

        submitButton.setOnClickListener(v -> {
            try {
                // Get input values
                String name = expenseNameEditText.getText().toString();
                float amount = Float.parseFloat(expenseAmountEditText.getText().toString());
                String dateStr = datePurchasedEditText.getText().toString();
                String category = expenseViewModel.getExpenseCategory().getValue();
                String desc = expenseDescEditText.getText().toString();

                // Parse date from string
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = sdf.parse(dateStr);

                Log.d("ExpenseFragment", "User ID: " + userId);  // Log userId
                expenseViewModel.addExpense(name, category, amount, date, desc, userId);

                clearEditTextField();
                Toast.makeText(getContext(), "Expense added successfully.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Log the exception to find out what went wrong
                Log.e("ExpenseFragment", "Error in submit button: " + e.getMessage(), e);
                Toast.makeText(getContext(), "Invalid input. Please check your fields.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });


        backButton.setOnClickListener(v -> {
            expenseViewModel.clearExpenseValue();
            clearEditTextField();
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setupObservers() {
        expenseViewModel.getSelectedDate().observe(getViewLifecycleOwner(), date -> datePurchasedEditText.setText(date));

        expenseViewModel.isFormValid().observe(getViewLifecycleOwner(), isValid -> submitButton.setEnabled(isValid));

        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            List<String> categoryList = new ArrayList<>();
            if (categories != null && !categories.isEmpty()) {
                for (mCategory category : categories) {
                    categoryList.add(category.getCategoryTitle());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, categoryList);
                categorySpinner.setAdapter(adapter);
                categorySpinner.setVisibility(View.VISIBLE);
                noCategory.setVisibility(View.GONE);
            } else {
                categorySpinner.setVisibility(View.GONE);
                noCategory.setVisibility(View.VISIBLE);
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = parentView.getItemAtPosition(position).toString();
                expenseViewModel.updateExpenseCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        expenseNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                expenseViewModel.updateExpenseName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        expenseAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                expenseViewModel.updateExpenseAmount(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        expenseDescEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                expenseViewModel.updateExpenseDescription(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    public void clearEditTextField(){
        expenseNameEditText.setText(null);
        expenseAmountEditText.setText(null);
        expenseDescEditText.setText(null);
        categorySpinner.setSelection(0);
        datePurchasedEditText.setText(null);
    }

}
