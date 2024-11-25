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
import com.eldroidfri730.extr.data.models.mExpense;
import com.eldroidfri730.extr.ui.home.BasicSummaryActivity;
import com.eldroidfri730.extr.utils.RetrofitClient;
import com.eldroidfri730.extr.viewmodel.budget.BudgetViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModelFactory;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.ExpenseViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.ExpenseViewModelFactory;

import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private TextView totalBudgetView, userbudgetdisplay, userexpensedisplay;
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

        budgetViewModel.getTotalBudget().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                totalBudgetView.setText(String.format(Locale.getDefault(), "%.2f", total));
            } else {
                totalBudgetView.setText("0.00");
            }
        });

        budgetViewModel.getBudgetSuccessMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        budgetViewModel.getBudgetErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

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

        totalBudgetView = rootView.findViewById(R.id.userbudgettotaldisplay);
        userbudgetdisplay = rootView.findViewById(R.id.userbudgetdisplay);
        userexpensedisplay = rootView.findViewById(R.id.userexpensedisplay);

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
    }


    private void setupTextSwitcher() {
        textSwitcher.setFactory(() -> {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            textView.setTextColor(getResources().getColor(R.color.black));
            return textView;
        });

        // Observe categories, budgets, and expenses
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            budgetViewModel.getBudgets().observe(getViewLifecycleOwner(), budgets -> {
                expenseViewModel.getExpenses().observe(getViewLifecycleOwner(), expenses -> {
                    if (categories != null && !categories.isEmpty() && budgets != null && expenses != null) {
                        // Map budgets to categories
                        Map<String, Double> categoryBudgetMap = new HashMap<>();
                        for (mBudget budget : budgets) {
                            categoryBudgetMap.put(budget.getCategoryTitle(), budget.getBudget());
                        }

                        // Accumulate expenses by category
                        Map<String, Double> categoryExpenseTotals = new HashMap<>();
                        for (mExpense expense : expenses) {
                            String categoryTitle = expense.getCategoryTitle();
                            double amount = expense.getAmount(); // Assuming getAmount() exists
                            categoryExpenseTotals.put(
                                    categoryTitle,
                                    categoryExpenseTotals.getOrDefault(categoryTitle, 0.0) + amount
                            );
                        }

                        // Populate category and budget lists
                        List<String> categoryList = new ArrayList<>();
                        List<Double> budgetList = new ArrayList<>();
                        List<Double> expenseTotalsList = new ArrayList<>();
                        for (mCategory category : categories) {
                            String categoryTitle = category.getCategoryTitle();
                            categoryList.add(categoryTitle);
                            budgetList.add(categoryBudgetMap.getOrDefault(categoryTitle, 0.0));
                            expenseTotalsList.add(categoryExpenseTotals.getOrDefault(categoryTitle, 0.0));
                        }

                        // Display the first category, budget, and accumulated expenses
                        if (!categoryList.isEmpty()) {
                            updateUIForCategory(
                                    categoryList.get(currentIndex),
                                    budgetList.get(currentIndex),
                                    expenseTotalsList.get(currentIndex)
                            );

                            textSwitcher.setOnClickListener(v -> {
                                // Cycle through the categories
                                currentIndex = (currentIndex + 1) % categoryList.size();
                                updateUIForCategory(
                                        categoryList.get(currentIndex),
                                        budgetList.get(currentIndex),
                                        expenseTotalsList.get(currentIndex)
                                );
                            });
                        }
                    } else {
                        textSwitcher.setText("No Categories Available");
                        userbudgetdisplay.setText("0.00");
                        userexpensedisplay.setText("0.00");
                    }
                });
            });
        });
    }

    // Helper method to update UI for the selected category
    private void updateUIForCategory(
            String categoryTitle,
            double budget,
            double expenseTotal
    ) {
        textSwitcher.setText(categoryTitle);
        userbudgetdisplay.setText(String.format(Locale.getDefault(), "%.2f", budget));
        userexpensedisplay.setText(String.format(Locale.getDefault(), "%.2f", expenseTotal));
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
