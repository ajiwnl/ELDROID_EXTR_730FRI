package com.eldroidfri730.extr.ui.exp_and_cat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.ui.home.HomeFragment;
import com.eldroidfri730.extr.utils.IntentUtil;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.ExpenseViewModel;

public class ExpenseFragment extends Fragment {

    private TextView expenseNameEditText, datePurchasedEditText, expenseAmountEditText, expenseDescEditText,noCategory;
    private Spinner categorySpinner;
    private ExpenseViewModel expenseViewModel;
    private CategoryViewModel categoryViewModel;
    private ImageButton backButton;
    private Button submitButton;

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

        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);


        datePurchasedEditText.setOnClickListener(v -> {
            expenseViewModel.showDatePickerDialog(this);
        });

        expenseViewModel.getSelectedDate().observe(getViewLifecycleOwner(), date -> {
            datePurchasedEditText.setText(date);
        });

        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if(!categories.isEmpty()){
                categorySpinner.setAdapter(categoryViewModel.getCategoryAdapter(getContext()));
                categorySpinner.setVisibility(View.VISIBLE);
                noCategory.setVisibility(View.GONE);
            }

        });



        submitButton.setOnClickListener(v -> {
            String name = expenseNameEditText.getText().toString();
            String amountStr = expenseAmountEditText.getText().toString();
            String desc = expenseDescEditText.getText().toString();
            String dateStr = datePurchasedEditText.getText().toString();
            String category = categorySpinner.getSelectedItem().toString();

            expenseViewModel.createExpense(name, amountStr, dateStr, category, desc, rootView.getContext());


        });

        backButton.setOnClickListener(v -> {
            IntentUtil.replaceFragment(R.id.layout_content, getActivity(), new HomeFragment(), "HomeFragment");
        });

        return rootView;
    }
}
