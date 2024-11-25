package com.eldroidfri730.extr.ui.budgetplan;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.ApiService;
import com.eldroidfri730.extr.data.models.mBudget;
import com.eldroidfri730.extr.data.models.mCategory;
import com.eldroidfri730.extr.ui.home.BasicSummaryActivity;
import com.eldroidfri730.extr.utils.RetrofitClient;
import com.eldroidfri730.extr.viewmodel.budget.BudgetViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModelFactory;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.ExpenseViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.ExpenseViewModelFactory;

import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetPlanningFragment extends Fragment {

    private ImageButton backButton;
    private TextSwitcher textSwitcher;
    private List<String> categoryList = new ArrayList<>();
    private int currentIndex = 0;
    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private ExpenseViewModel expenseViewModel;
    private BudgetViewModel budgetViewModel;
    private CategoryViewModel categoryViewModel;
    private String userId;
    private boolean isCategoriesFetched = false;
    private boolean isBudgetFetched = false;



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
        userId = loginViewModel.getUserId();

        // Initialize ExpenseViewModel
        ExpenseViewModelFactory factory = new ExpenseViewModelFactory(requireActivity().getApplication());
        expenseViewModel = new ViewModelProvider(this, factory).get(ExpenseViewModel.class);

        CategoryViewModelFactory categoryFactory = new CategoryViewModelFactory(requireActivity().getApplication());
        categoryViewModel = new ViewModelProvider(this, categoryFactory).get(CategoryViewModel.class);

        budgetViewModel = ((BasicSummaryActivity) getActivity()).getBudgetViewModel();

        if (userId != null && !isCategoriesFetched && !isBudgetFetched) {
            categoryViewModel.fetchCategoriesByUserId(userId);
            budgetViewModel.fetchBudgetByUserId(userId);
            isCategoriesFetched = true;
        } else {
            Toast.makeText(getContext(), getString(R.string.user_out), Toast.LENGTH_SHORT).show();
        }

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

        rootView.findViewById(R.id.add_budget).setOnClickListener(v -> showAddBudgetDialog());


        return rootView;
    }

    private void showAddBudgetDialog() {
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_budget, null);

        Spinner categorySpinner = dialogView.findViewById(R.id.category_spinner);
        EditText budgetInput = dialogView.findViewById(R.id.budget_input);
        TextView noCategory = dialogView.findViewById(R.id.add_category);

        // Observe and populate Spinner with categories
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

        // Create the AlertDialog
        new AlertDialog.Builder(requireContext())
                .setTitle("Add Budget")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String selectedCategory = categorySpinner.getSelectedItem().toString();
                    String budgetStr = budgetInput.getText().toString().trim();

                    if (budgetStr.isEmpty()) {
                        Toast.makeText(requireContext(), "Please enter a budget", Toast.LENGTH_SHORT).show();
                    } else {
                        double budget = Integer.parseInt(budgetStr);
                        addBudgetToCategory(selectedCategory, budget);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void addBudgetToCategory(String category, double budget) {
        mBudget newBudget = new mBudget(userId, category, budget);

        budgetViewModel.addBudget(newBudget);
        Toast.makeText(requireContext(), "Budget added successfully", Toast.LENGTH_SHORT).show();
    }


    private void setupTextSwitcher() {
        textSwitcher.setFactory(() -> {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            textView.setTextColor(getResources().getColor(R.color.black));
            return textView;
        });

        // Observe categories from ViewModel
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                categoryList.clear();
                for (mCategory category : categories) {
                    categoryList.add(category.getCategoryTitle()); // Assuming category has getCategoryTitle() method
                }

                // Update the TextSwitcher with the first category
                if (!categoryList.isEmpty()) {
                    textSwitcher.setText(categoryList.get(currentIndex));
                }

                textSwitcher.setOnClickListener(v -> {
                    // Update index to cycle through the categories
                    currentIndex = (currentIndex + 1) % categoryList.size();
                    textSwitcher.setText(categoryList.get(currentIndex));
                    filterExpensesByCategory(categoryList.get(currentIndex));
                });
            } else {
                textSwitcher.setText("No Categories Available");
            }
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
