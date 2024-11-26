package com.eldroidfri730.extr.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.models.mExpense;
import com.eldroidfri730.extr.ui.budgetplan.ExpenseAdapter;
import com.eldroidfri730.extr.ui.home.HomeAdapter.HomeViewHolder;
import com.eldroidfri730.extr.viewmodel.exp_and_cat.ExpenseViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private ExpenseViewModel expenseViewModel;
    private List<CategoryItem> categoryList;
    private Context context;


    public HomeAdapter(ExpenseViewModel expenseViewModel, List<CategoryItem> categoryList, Context context) {
        this.expenseViewModel = expenseViewModel;
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reportcatexp, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        CategoryItem categoryItem = categoryList.get(position);
        holder.bind(categoryItem);

        holder.itemView.setOnClickListener(v -> {
           showListExpense(categoryItem);
        });
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

    public void updateCategoryList(List<CategoryItem> newCategoryList) {
        this.categoryList = newCategoryList;
        notifyDataSetChanged();
    }


    private void showListExpense(CategoryItem categoryItem) {

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_listexpense, null);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(dialogView);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        RecyclerView expenseListRecyclerView = dialogView.findViewById(R.id.expenseList);
        ImageButton closeBtn = dialogView.findViewById(R.id.closeButton);

        dialogTitle.setText(categoryItem.getCategoryTitle());

        ExpenseAdapter expenseAdapter = new ExpenseAdapter(context,expenseViewModel);
        expenseListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        expenseListRecyclerView.setAdapter(expenseAdapter);

        expenseViewModel.getExpenses().observe((LifecycleOwner) context, expenses -> {
           Log.d("HomeFragment", "Expenses received: " + expenses.size());
        });

        expenseViewModel.filterExpensesByCategory(categoryItem.getCategoryTitle());

        // Observe filtered expenses
        expenseViewModel.getFilteredExpenses().observe((LifecycleOwner) context, filteredExpenses -> {
            if (filteredExpenses != null && !filteredExpenses.isEmpty()) {
                Log.d("HomeFragment", "Filtered expenses received: " + filteredExpenses.toString());
                expenseAdapter.setExpenseItems(filteredExpenses);
            } else {
                Log.d("HomeFragment", "Filtered expenses list is empty or null.");
            }
        });

        closeBtn.setOnClickListener(v -> {
           dialog.dismiss();
        });


        dialog.show();
    }
}
