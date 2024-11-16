package com.eldroidfri730.extr.ui.exp_and_cat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.ui.home.HomeFragment;
import com.eldroidfri730.extr.utils.IntentUtil;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModel;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    private ImageButton backButton;
    private Button submitButton;
    private RecyclerView categoryRecyclerView;
    private EditText categoryNameTextView,categoryDescTextView;
    private CategoryAdapter categoryAdapter;
    private CategoryViewModel categoryViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        backButton = rootView.findViewById(R.id.categorybackbutton);
        submitButton = rootView.findViewById(R.id.categoriessubmitbutton);
        categoryRecyclerView = rootView.findViewById(R.id.CategoryRecyclerView);
        categoryNameTextView = rootView.findViewById(R.id.CategoryNameEditText);
        categoryDescTextView = rootView.findViewById(R.id.CategoryDescEditText);

        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        categoryAdapter = new CategoryAdapter(new ArrayList<>());
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);

        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.setCategoryList(categories);
        });

        submitButton.setOnClickListener(v -> {
            String name = categoryNameTextView.getText().toString();
            String desc = categoryDescTextView.getText().toString();

            if (!name.isEmpty() && !desc.isEmpty()) {
                categoryViewModel.addCategory(name, desc);

                categoryNameTextView.setText("");
                categoryDescTextView.setText("");
            }
        });

        backButton.setOnClickListener(v -> {
            IntentUtil.replaceFragment(R.id.layout_content, getActivity(), new HomeFragment(), "HomeFragment");
        });

        return rootView;
    }
}