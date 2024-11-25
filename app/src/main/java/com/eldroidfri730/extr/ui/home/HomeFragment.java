package com.eldroidfri730.extr.ui.home;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.ui.budgetplan.BudgetPlanningFragment;
import com.eldroidfri730.extr.ui.exp_and_cat.CategoryFragment;
import com.eldroidfri730.extr.ui.exp_and_cat.ExpenseFragment;
import com.eldroidfri730.extr.ui.prof_and_set.ProfileFragment;
import com.eldroidfri730.extr.utils.IntentUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private LinearLayout budgetOption, expenseOption, categoryOption;
    private TextSwitcher textSwitcher;
    private String[] texts = {"This week", "Last week", "Next week"};
    private int currentIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        String profileImageUrl = sharedPreferences.getString("profileImage", null);

        CircleImageView profileImageView = rootView.findViewById(R.id.user_profile_image_holder);

        if (profileImageUrl != null) {
            Glide.with(this)
                    .load(profileImageUrl)
                    .placeholder(R.drawable.budgetbgimg)
                    .error(R.drawable.budgetbgimg)
                    .into(profileImageView);
        }

        // Set click listener for profile image
        profileImageView.setOnClickListener(v -> {
            // Replace the current fragment with ProfileFragment
            IntentUtil.replaceFragment(
                    R.id.layout_content, // Container view ID
                    requireActivity(),
                    new ProfileFragment(), // Replace with your actual ProfileFragment class
                    "ProfileFragment" // Tag for backstack
            );
        });

        budgetOption = rootView.findViewById(R.id.opt_budget);
        expenseOption = rootView.findViewById(R.id.opt_expense);
        categoryOption = rootView.findViewById(R.id.opt_category);
        textSwitcher = rootView.findViewById(R.id.reportgenstatus);

        budgetOption.setOnClickListener(v -> {
            replaceFragment(new BudgetPlanningFragment());
        });

        expenseOption.setOnClickListener(v -> {
            replaceFragment(new ExpenseFragment());
        });

        categoryOption.setOnClickListener(v -> {
            IntentUtil.replaceFragment(R.id.layout_content, requireActivity(), new CategoryFragment(), "CategoryFragment");
        });

        textSwitcher.setFactory(() -> {
            TextView textView = new TextView(getContext());
            textView.setTextSize(10);
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setText(texts[currentIndex]);
            return textView;
        });

        textSwitcher.setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % texts.length;
            textSwitcher.setText(texts[currentIndex]);
        });

        // Add hide/show expense functionality
        TextView expenseCurrency = rootView.findViewById(R.id.expense_currency);
        TextView expenseTotal = rootView.findViewById(R.id.expense_total);
        View hideExpenseButton = rootView.findViewById(R.id.hide_expense);

        final boolean[] isExpenseHidden = {false}; // State to track toggle

        hideExpenseButton.setOnClickListener(v -> {
            if (isExpenseHidden[0]) {
                // Show the actual expense
                expenseCurrency.setVisibility(View.VISIBLE);
                expenseTotal.setText("200,000.00"); // Replace with actual amount
                isExpenseHidden[0] = false;
            } else {
                // Hide the expense
                expenseCurrency.setVisibility(View.INVISIBLE);
                expenseTotal.setText("******");
                isExpenseHidden[0] = true;
            }
        });

        return rootView;
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}