package com.eldroidfri730.extr.ui.exp_and_cat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.CategoryModel;
import com.eldroidfri730.extr.ui.home.HomeFragment;
import com.eldroidfri730.extr.utils.IntentUtil;
import com.eldroidfri730.extr.viewmodel.expense.ExpenseViewModel;

public class ExpenseFragment extends Fragment {

    private TextView datePurchasedEditText;
    private Spinner categorySpinner;
    private ExpenseViewModel expenseViewModel;
    private ImageButton backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_expense, container, false);

        backButton = rootView.findViewById(R.id.expensebackbutton);
        categorySpinner = rootView.findViewById(R.id.expensecategoryspinner);
        datePurchasedEditText = rootView.findViewById(R.id.datepurchasededittext);

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        datePurchasedEditText.setOnClickListener(v -> {
            expenseViewModel.showDatePickerDialog(this);
        });

        expenseViewModel.getSelectedDate().observe(getViewLifecycleOwner(), date -> {
            datePurchasedEditText.setText(date);
        });

        expenseViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            ArrayAdapter<CategoryModel> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);
        });

        expenseViewModel.loadCategories();


        backButton.setOnClickListener(v -> {
            IntentUtil.replaceFragment(R.id.layout_content, getActivity(), new HomeFragment(), "HomeFragment");
        });

        return rootView;
    }
}