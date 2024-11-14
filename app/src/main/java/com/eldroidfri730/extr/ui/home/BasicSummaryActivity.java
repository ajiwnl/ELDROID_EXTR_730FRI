package com.eldroidfri730.extr.ui.home;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.databinding.ActivityBasicSummaryBinding;
import com.eldroidfri730.extr.ui.exp_and_cat.ExpenseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.eldroidfri730.extr.utils.IntentUtil;

public class BasicSummaryActivity extends AppCompatActivity {

    private ActivityBasicSummaryBinding binding;
    private FloatingActionButton fabNavHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityBasicSummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adjustSystemInsets();

        fabNavHome = findViewById(R.id.fab_nav_home);
        fabNavHome.setOnClickListener(v ->
                IntentUtil.replaceFragment(R.id.layout_content,this, new HomeFragment(), "HomeFragment")
        );

        IntentUtil.replaceFragment(R.id.layout_content,this, new HomeFragment(), "HomeFragment");

        setupBottomNavigation();
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
            if (item.getItemId() == R.id.nav_expense) {
                IntentUtil.replaceFragment(R.id.layout_content,this, new ExpenseFragment(), "ExpenseFragment");
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
        dialogFragment.setLogoutDialogListener(() -> finish());
        dialogFragment.show(getSupportFragmentManager(), "LogoutDialogFragment");
    }
}
