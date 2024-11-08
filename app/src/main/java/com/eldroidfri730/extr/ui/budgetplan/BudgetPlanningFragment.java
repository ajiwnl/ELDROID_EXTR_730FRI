package com.eldroidfri730.extr.ui.budgetplan;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eldroidfri730.extr.R;

import java.util.ArrayList;
import java.util.List;

public class BudgetPlanningFragment extends Fragment {

    private ImageButton backButton;
    private TextSwitcher textSwitcher;
    private String[] texts = {"Day", "Month", "Year"};
    private int currentIndex = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_budget_planning, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.expensivecostlistview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ExpenseItem> expenseItemList = new ArrayList<>();
        expenseItemList.add(new ExpenseItem("Cake", "Php 500", "01-01-2024"));
        expenseItemList.add(new ExpenseItem("Water", "Php 400", "01-02-2024"));

        ExpenseAdapter expenseAdapter = new ExpenseAdapter(expenseItemList);
        recyclerView.setAdapter(expenseAdapter);

        textSwitcher = rootView.findViewById(R.id.daymonthyearcategorybutton);
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getContext());
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(12);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setText(texts[currentIndex]);
                return textView;
            }
        });

        textSwitcher.setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % texts.length;
            textSwitcher.setText(texts[currentIndex]);
        });

        backButton = rootView.findViewById(R.id.budgetbackbutton); // Adjust the ID if necessary

        backButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack(); // Go back to the previous fragment
        });

        return rootView;
    }
}