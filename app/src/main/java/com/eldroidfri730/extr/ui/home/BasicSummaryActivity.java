package com.eldroidfri730.extr.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.databinding.ActivityBasicSummaryBinding;
import com.eldroidfri730.extr.ui.auth.LoginActivity;
import com.eldroidfri730.extr.ui.exp_and_cat.ExpenseFragment;
import com.eldroidfri730.extr.ui.prof_and_set.ProfileFragment;
import com.eldroidfri730.extr.ui.prof_and_set.SettingsFragment;
import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;
import com.eldroidfri730.extr.viewmodel.auth.LoginViewModelFactory;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModelFactory;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.ExpenseViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.ExpenseViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.eldroidfri730.extr.utils.IntentUtil;

public class BasicSummaryActivity extends AppCompatActivity {

    private ActivityBasicSummaryBinding binding;
    private FloatingActionButton fabNavHome;
    private CategoryViewModel categoryViewModel;
    private ExpenseViewModel expenseViewModel;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Initialize ViewModel factories
        CategoryViewModelFactory categoryFactory = new CategoryViewModelFactory(getApplication());
        ExpenseViewModelFactory expenseFactory = new ExpenseViewModelFactory(getApplication());
        LoginViewModelFactory loginFactory = new LoginViewModelFactory(getApplication());

        // Create ViewModels scoped to the Activity
        categoryViewModel = new ViewModelProvider(this, categoryFactory).get(CategoryViewModel.class);
        expenseViewModel = new ViewModelProvider(this, expenseFactory).get(ExpenseViewModel.class);
        loginViewModel = new ViewModelProvider(this, loginFactory).get(LoginViewModel.class); // Corrected ViewModel type

        // Set up view binding
        binding = ActivityBasicSummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Adjust insets and navigation
        adjustSystemInsets();

        // Floating action button to navigate to home
        fabNavHome = findViewById(R.id.fab_nav_home);
        fabNavHome.setOnClickListener(v ->
                IntentUtil.replaceFragment(R.id.layout_content, this, new HomeFragment(), "HomeFragment")
        );

        // Load default home fragment
        IntentUtil.replaceFragment(R.id.layout_content, this, new HomeFragment(), "HomeFragment");

        // Setup bottom navigation
        setupBottomNavigation();

        // Check user login state
        checkLoginState();
    }

    public CategoryViewModel getCategoryViewModel() {
        return categoryViewModel;
    }

    public ExpenseViewModel getExpenseViewModel() {
        return expenseViewModel;
    }

    public LoginViewModel getLoginViewModel() {
        return loginViewModel;
    }

    // Adjust the system insets (status and navigation bars)
    private void adjustSystemInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            v.setPadding(0, insets.getInsets(WindowInsetsCompat.Type.systemBars()).top, 0, 0);
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigation, (v, insets) -> {
            v.setPadding(0, 0, 0, insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });
    }

    // Setup the bottom navigation menu
    private void setupBottomNavigation() {
        binding.bottomNavView.setSelectedItemId(R.id.nav_home);

        binding.bottomNavView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_settings) {
                IntentUtil.replaceFragment(R.id.layout_content, this, new SettingsFragment(), "SettingsFragment");
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                IntentUtil.replaceFragment(R.id.layout_content, this, new ProfileFragment(), "ProfileFragment");
                return true;
            } else if (item.getItemId() == R.id.nav_expense) {
                IntentUtil.replaceFragment(R.id.layout_content, this, new ExpenseFragment(), "ExpenseFragment");
                return true;
            } else if (item.getItemId() == R.id.nav_logout) {
                showLogoutDialogueFragment();
                return true;
            }
            return false;
        });
    }

    // Show the logout dialog fragment
    private void showLogoutDialogueFragment() {
        LogoutDialogueFragment dialogFragment = new LogoutDialogueFragment();
        dialogFragment.setLogoutDialogListener(this::logout); // Use method reference for clarity
        dialogFragment.show(getSupportFragmentManager(), "LogoutDialogFragment");
    }

    // Logout method
    private void logout() {
        clearLoginState();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Close BasicSummaryActivity
    }

    // Method to clear login state
    private void clearLoginState() {
        getSharedPreferences("app_prefs", MODE_PRIVATE)
                .edit()
                .putBoolean("is_logged_in", false)
                .remove("user_id") // Remove user ID to ensure a fresh login
                .apply();
    }

    // Check login state and redirect if not logged in
    private void checkLoginState() {
        boolean isLoggedIn = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getBoolean("is_logged_in", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
