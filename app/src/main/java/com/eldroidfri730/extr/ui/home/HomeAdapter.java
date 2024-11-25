package com.eldroidfri730.extr.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.ui.home.HomeAdapter.HomeViewHolder;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private List<CategoryItem> categoryList;


    public HomeAdapter(List<CategoryItem> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        CategoryItem categoryItem = categoryList.get(position);
        holder.bind(categoryItem);
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        private TextView totalExpenseTextView;
        private TextView categoryTitleTextView;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            totalExpenseTextView = itemView.findViewById(R.id.TotalExpense);
            categoryTitleTextView = itemView.findViewById(R.id.CategoryTitle);
        }

        public void bind(CategoryItem categoryItem) {
            totalExpenseTextView.setText(String.valueOf(categoryItem.getTotalExpense()));
            categoryTitleTextView.setText(categoryItem.getCategoryTitle());
        }
    }

    public static class CategoryItem {
        private String categoryTitle;
        private double totalExpense;

        public CategoryItem(String categoryTitle, double totalExpense) {
            this.categoryTitle = categoryTitle;
            this.totalExpense = totalExpense;
        }

        public String getCategoryTitle() {
            return categoryTitle;
        }

        public double getTotalExpense() {
            return totalExpense;
        }
    }
}
