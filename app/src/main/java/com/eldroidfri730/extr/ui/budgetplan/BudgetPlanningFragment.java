package com.eldroidfri730.extr.ui.budgetplan;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.ExpenseViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.ExpenseViewModelFactory;

import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;

public class BudgetPlanningFragment extends Fragment {

    private ImageButton backButton;
    private TextSwitcher textSwitcher;
    private String[] texts = {"Day", "Month", "Year"};
    private int currentIndex = 0;
    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private ExpenseViewModel expenseViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_budget_planning, container, false);

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.expensivecostlistview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        expenseAdapter = new ExpenseAdapter();
        recyclerView.setAdapter(expenseAdapter);

        // Initialize LoginViewModel
        LoginViewModel loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        // Initialize ExpenseViewModel
        ExpenseViewModelFactory factory = new ExpenseViewModelFactory(requireActivity().getApplication());
        expenseViewModel = new ViewModelProvider(this, factory).get(ExpenseViewModel.class);

        // Observe expense data
        observeExpenseData();

        // TextSwitcher for filtering
        textSwitcher = rootView.findViewById(R.id.daymonthyearcategorybutton);
        setupTextSwitcher();

        // Back button functionality
        backButton = rootView.findViewById(R.id.budgetbackbutton);
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return rootView;
    }

    private void setupTextSwitcher() {
        textSwitcher.setFactory(() -> {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            textView.setTextColor(getResources().getColor(R.color.black));
            return textView;
        });
        textSwitcher.setText(texts[currentIndex]);

        textSwitcher.setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % texts.length;
            textSwitcher.setText(texts[currentIndex]);
            filterExpensesByCategory(texts[currentIndex]);
        });
    }

    private void observeExpenseData() {
        expenseViewModel.getExpenses().observe(getViewLifecycleOwner(), expenseList -> {
            if (expenseList != null) {
                expenseAdapter.setExpenseItems(expenseList);
            }
        });
    }

    public void filterExpensesByCategory(String category) {
        // Delegate filtering to ViewModel
        expenseViewModel.filterExpensesByCategory(category);
    }
}
