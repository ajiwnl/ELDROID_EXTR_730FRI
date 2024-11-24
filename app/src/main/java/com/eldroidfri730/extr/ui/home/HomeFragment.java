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

        CircleImageView profileImageView = rootView.findViewById(R.id.userprofileimageholder);

        if (profileImageUrl != null) {
            Glide.with(this)
                    .load(profileImageUrl)
                    .placeholder(R.drawable.budgetbgimg)
                    .error(R.drawable.budgetbgimg)
                    .into(profileImageView);
        }

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

        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getContext());
                textView.setTextSize(10);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setText(texts[currentIndex]);
                return textView;
            }
        });

        textSwitcher.setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % texts.length;
            textSwitcher.setText(texts[currentIndex]);
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