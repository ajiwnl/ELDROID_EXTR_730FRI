package com.eldroidfri730.extr.ui.exp_and_cat;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.models.mCategory;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.CategoryViewModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private final CategoryViewModel categoryViewModel;
    private List<mCategory> categoryList;
    private Context context;

    // Constructor with categoryViewModel and categoryList
    public CategoryAdapter(CategoryViewModel categoryViewModel, List<mCategory> categoryList, Context context) {
        this.categoryViewModel = categoryViewModel;  // Initialize categoryViewModel
        this.categoryList = categoryList;  // Initialize categoryList
        this.context = context;  // Initialize context
    }
    public CategoryAdapter (CategoryViewModel categoryViewModel){
        this.categoryViewModel = categoryViewModel;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categories, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        mCategory category = categoryList.get(position);

        holder.categoryNameTextView.setText(category.getCategoryTitle());

        holder.itemView.setOnClickListener(v -> {
            showEditDialog(category);
        });


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameTextView;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            categoryNameTextView = itemView.findViewById(R.id.CategoryNameTextView);
        }
    }

    public void setCategoryList(List<mCategory> categories) {
        this.categoryList = categories;
        notifyDataSetChanged();
    }

    private void showEditDialog(mCategory category) {
        // Inflate the custom dialog view
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_category, null);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(dialogView);
        dialog.getWindow().setLayout(800, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        // Get references to the dialog views
        EditText editCategoryTitle = dialog.findViewById(R.id.editCategoryTitle);
        Button saveEditButton = dialog.findViewById(R.id.saveButton);
        Button deleteEditButton = dialog.findViewById(R.id.deleteButton);
        ImageButton dialogCloseButton = dialog.findViewById(R.id.closeButton);


        // Pre-fill the EditText with the current category title
        editCategoryTitle.setText(category.getCategoryTitle());

        // Close button action
        dialogCloseButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // Save button action
        saveEditButton.setOnClickListener(v -> {
            String newCategoryTitle = editCategoryTitle.getText().toString().trim();

            if (!newCategoryTitle.isEmpty()) {
                // Get the old category title (the one currently being edited)
                String oldCategoryTitle = category.getCategoryTitle();

                // Call your ViewModel to update the category title
                categoryViewModel.updateCategoryTitle(oldCategoryTitle, newCategoryTitle, category.getUserId());
                dialog.dismiss();
            }
        });

        //Delete Button
        deleteEditButton.setOnClickListener(v -> {
            String categoryTitle = category.getCategoryTitle();
            String userId = category.getUserId();

            categoryViewModel.deleteCategory(userId, categoryTitle);
            dialog.dismiss();
        });

        dialog.show();
    }




}
