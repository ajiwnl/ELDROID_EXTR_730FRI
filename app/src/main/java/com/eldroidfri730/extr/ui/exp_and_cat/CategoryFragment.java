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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


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
    private SwipeRefreshLayout swipeRefreshLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        // Views initialization
        backButton = rootView.findViewById(R.id.categorybackbutton);
        submitButton = rootView.findViewById(R.id.categoriessubmitbutton);
        categoryRecyclerView = rootView.findViewById(R.id.CategoryRecyclerView);
        categoryNameEditText = rootView.findViewById(R.id.CategoryNameEditText);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);


        // Initialize CategoryAdapter and set it to the RecyclerView
        categoryViewModel = ((BasicSummaryActivity) getActivity()).getCategoryViewModel();
        categoryAdapter = new CategoryAdapter(categoryViewModel, new ArrayList<>(), getContext());  // Pass CategoryViewModel to the adapter
        categoryRecyclerView.setAdapter(categoryAdapter);

        // ViewModels
        LoginViewModel loginViewModel = ((BasicSummaryActivity) getActivity()).getLoginViewModel(); // Retrieve LoginViewModel
        String userId = loginViewModel.getUserId(); // Retrieve userId

        // RecyclerView setup
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 items per row

        // Observe categories LiveData and update the RecyclerView
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.setCategoryList(categories);
            swipeRefreshLayout.setRefreshing(false); // Stop refreshing when data is loaded

        });

        // Swipe-to-refresh listener
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (categoryViewModel != null && userId != null) {
                // Start the refreshing animation
                swipeRefreshLayout.setRefreshing(true);

                // Fetch categories when user pulls to refresh
                categoryViewModel.fetchCategoriesByUserId(userId);  // Replace with actual userId
            }
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
        } else {
            Toast.makeText(getContext(), getString(R.string.user_out), Toast.LENGTH_SHORT).show();
        }

        // Add category logic
        submitButton.setOnClickListener(v -> {
            String name = categoryNameEditText.getText().toString();
            //Fix this later
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