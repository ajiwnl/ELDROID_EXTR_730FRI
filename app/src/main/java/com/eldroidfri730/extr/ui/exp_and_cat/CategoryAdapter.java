package com.eldroidfri730.extr.ui.exp_and_cat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.models.mCategory;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<mCategory> categoryList;

    public CategoryAdapter(List<mCategory> categoryList) {
        this.categoryList = categoryList;
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
}
