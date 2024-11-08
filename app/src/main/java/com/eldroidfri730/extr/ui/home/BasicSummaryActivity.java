package com.eldroidfri730.extr.ui.home;

import android.os.Bundle;
import android.util.SparseArray;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.databinding.ActivityBasicSummaryBinding;
import com.eldroidfri730.extr.ui.exp_and_cat.ExpenseFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BasicSummaryActivity extends AppCompatActivity {

    ActivityBasicSummaryBinding binding;
    private FloatingActionButton fab;
    private boolean isFabVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityBasicSummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
           Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

           v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });


        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigation, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
           v.setPadding(0, 0, 0, systemBars.bottom);
            return insets;
        });


        SparseArray<Fragment> fragmentArray = new SparseArray<>();
        fragmentArray.put(R.id.nav_expense, new ExpenseFragment());
        // fragmentArray.put(R.id.nav_profile, new ProfileFragment());
        // fragmentArray.put(R.id.nav_settings, new SettingsFragment());

        FloatingActionButton fabNavHome = findViewById(R.id.fab_nav_home);

        binding.bottomNavView.setSelectedItemId(R.id.nav_home);

        fabNavHome.setOnClickListener(v -> {
           replaceFragment(new HomeFragment());
        });

        binding.bottomNavView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = fragmentArray.get(item.getItemId());
            if (fragment != null) {
                replaceFragment(fragment);
                return true;
            } else if (item.getItemId() == R.id.nav_logout) {
                showLogoutDialogueFragment();
                return true;
            }
            return false;
        });

        replaceFragment(new HomeFragment());

        binding.bottomNavView.setBackground(null);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_content, fragment);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }

    private void showLogoutDialogueFragment() {
        LogoutDialogueFragment dialogFragment = new LogoutDialogueFragment();

        dialogFragment.setLogoutDialogListener(() -> {
            finish();
        });

        dialogFragment.show(getSupportFragmentManager(), "LogoutDialogFragment");
    }
}
