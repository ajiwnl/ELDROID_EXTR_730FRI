package com.eldroidfri730.extr.ui.home;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.models.mCategory;
import com.eldroidfri730.extr.ui.budgetplan.BudgetPlanningFragment;
import com.eldroidfri730.extr.ui.exp_and_cat.CategoryFragment;
import com.eldroidfri730.extr.ui.exp_and_cat.ExpenseFragment;
import com.eldroidfri730.extr.utils.IntentUtil;
import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.ExpenseViewModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private LinearLayout budgetOption, expenseOption, categoryOption;
    private TextSwitcher textSwitcher;
    private String[] texts = {"This week", "Last week", "Next week"};
    private int currentIndex = 0;
    private ExpenseViewModel expenseViewModel;
    private CategoryViewModel categoryViewModel;
    private String userId;
    private boolean isCategoriesFetched;
    private boolean isExpensesFetched;
    private HomeAdapter homeAdapter;
    private RecyclerView repRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        String profileImageUrl = sharedPreferences.getString("profileImage", null);

        CircleImageView profileImageView = rootView.findViewById(R.id.userprofileimageholder);


        expenseViewModel = ((BasicSummaryActivity) getActivity()).getExpenseViewModel();
        categoryViewModel = ((BasicSummaryActivity) getActivity()).getCategoryViewModel();

        repRecyclerView = rootView.findViewById(R.id.report_recycler_view);
        repRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        homeAdapter = new HomeAdapter(expenseViewModel ,new ArrayList<>(), getContext());
        repRecyclerView.setAdapter(homeAdapter);


        LoginViewModel loginViewModel = ((BasicSummaryActivity) getActivity()).getLoginViewModel();
        userId = loginViewModel.getUserId();
        //fetchiung
        if (userId != null && !isCategoriesFetched) {
            categoryViewModel.fetchCategoriesByUserId(userId);
            isCategoriesFetched = true;
        } else {
            Toast.makeText(getContext(), getString(R.string.user_out), Toast.LENGTH_SHORT).show();
        }
        if (userId != null && !isExpensesFetched) {
            expenseViewModel.fetchExpensesByUserId(userId);
            isExpensesFetched = true;
        } else {
            Toast.makeText(getContext(), getString(R.string.user_out), Toast.LENGTH_SHORT).show();
        }
        //observer
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                List<HomeAdapter.CategoryItem> categoryItems = new ArrayList<>();
                for (mCategory category : categories) {
                    categoryItems.add(new HomeAdapter.CategoryItem(
                            category.getCategoryTitle(),
                            0 // Placeholder for total amount not done
                    ));
                }
                // Update the adapter
                homeAdapter.updateCategoryList(categoryItems);
            }
        });

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
            IntentUtil.replaceFragment(R.id.layout_content, requireActivity(), new BudgetPlanningFragment(), "BudgetPlanningFragment");
        });

        expenseOption.setOnClickListener(v -> {
            IntentUtil.replaceFragment(R.id.layout_content, requireActivity(), new ExpenseFragment(), "ExpenseFragment");
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

        return rootView;
    }


}