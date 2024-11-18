package com.eldroidfri730.extr.ui.exp_and_cat;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.ui.home.BasicSummaryActivity;
import com.eldroidfri730.extr.utils.ExpenseCategoryValidation;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModel;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    private ImageButton backButton;
    private Button submitButton;
    private RecyclerView categoryRecyclerView;
    private EditText categoryNameEditText, categoryDescEditText;
    private CategoryAdapter categoryAdapter;
    private CategoryViewModel categoryViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        //Views
        backButton = rootView.findViewById(R.id.categorybackbutton);
        submitButton = rootView.findViewById(R.id.categoriessubmitbutton);
        categoryRecyclerView = rootView.findViewById(R.id.CategoryRecyclerView);
        categoryNameEditText = rootView.findViewById(R.id.CategoryNameEditText);
        categoryDescEditText = rootView.findViewById(R.id.CategoryDescEditText);
        //ViewModels
        categoryViewModel = ((BasicSummaryActivity) getActivity()).getCategoryViewModel();
        //RecyclerView
        categoryAdapter = new CategoryAdapter(new ArrayList<>());
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);

        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.setCategoryList(categories);
        });

        submitButton.setOnClickListener(v -> {
            String name = ExpenseCategoryValidation.isNotNullEditText(categoryNameEditText);
            String desc = ExpenseCategoryValidation.isNotNullEditText(categoryDescEditText);

            if (name != null && desc != null) {
                categoryViewModel.createCategory(name, desc);

                categoryNameEditText.setText("");
                categoryDescEditText.setText("");
            }
        });


        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return rootView;
    }
}