package com.eldroidfri730.extr.ui.exp_and_cat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoryModel> categoryList;

    public CategoryAdapter(List<CategoryModel> categoryList) {
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
        CategoryModel category = categoryList.get(position);

        holder.categoryNameTextView.setText(category.getName());
        holder.categoryDescTextView.setText(category.getDesc());

        holder.categoryImageView.setImageResource(R.drawable.ic_profile_24);//change to dynamic getIcon()
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImageView;
        TextView categoryNameTextView;
        TextView categoryDescTextView;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            categoryImageView = itemView.findViewById(R.id.CategoryImageView);
            categoryNameTextView = itemView.findViewById(R.id.CategoryNameTextView);
            categoryDescTextView = itemView.findViewById(R.id.CategoryDescTextView);
        }
    }

    public void setCategoryList(List<CategoryModel> categories) {
        this.categoryList = categories;
        notifyDataSetChanged();
    }
}

