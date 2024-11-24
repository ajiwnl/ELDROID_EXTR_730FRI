package com.eldroidfri730.extr.ui.exp_and_cat;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.ui.home.BasicSummaryActivity;
import com.eldroidfri730.extr.utils.ExpenseCategoryValidation;
import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModel;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    private ImageButton backButton;
    private Button submitButton;
    private RecyclerView categoryRecyclerView;
    private EditText categoryNameEditText;
    private CategoryAdapter categoryAdapter;
    private CategoryViewModel categoryViewModel;
    private boolean isCategoriesFetched = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        // Views
        backButton = rootView.findViewById(R.id.categorybackbutton);
        submitButton = rootView.findViewById(R.id.categoriessubmitbutton);
        categoryRecyclerView = rootView.findViewById(R.id.CategoryRecyclerView);
        categoryNameEditText = rootView.findViewById(R.id.CategoryNameEditText);

        // ViewModels
        categoryViewModel = ((BasicSummaryActivity) getActivity()).getCategoryViewModel();
        LoginViewModel loginViewModel = ((BasicSummaryActivity) getActivity()).getLoginViewModel(); // Retrieve LoginViewModel
        String userId = loginViewModel.getUserId(); // Retrieve userId


        // RecyclerView setup
        categoryRecyclerView = rootView.findViewById(R.id.CategoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 items per row
        categoryAdapter = new CategoryAdapter(new ArrayList<>());
        categoryRecyclerView.setAdapter(categoryAdapter);


        // Observe categories LiveData and update the RecyclerView
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.setCategoryList(categories);
        });

        categoryViewModel.getCategorySuccessMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        categoryViewModel.getCategoryErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch categories for the logged-in user
        if (userId != null && !isCategoriesFetched) {
            categoryViewModel.fetchCategoriesByUserId(userId);
            isCategoriesFetched = true;  // Mark categories as fetched
        }else {
            Toast.makeText(getContext(), getString(R.string.user_out), Toast.LENGTH_SHORT).show();
        }

        // Add category logic
        submitButton.setOnClickListener(v -> {
            String name = ExpenseCategoryValidation.isNotNullEditText(categoryNameEditText);

            if (name != null) {
                if (userId != null) {
                    categoryViewModel.addCategory(userId, name); // Pass userId and name
                    categoryNameEditText.setText("");
                    categoryViewModel.fetchCategoriesByUserId(userId);
                } else {
                    Toast.makeText(getContext(), getString(R.string.user_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Back button logic
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return rootView;
    }
}