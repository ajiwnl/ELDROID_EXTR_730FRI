package com.eldroidfri730.extr.ui.budgetplan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.models.mExpense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<mExpense> expenseList;

    public ExpenseAdapter() {
        this.expenseList = new ArrayList<>(); // Initialize with an empty list
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        mExpense expense = expenseList.get(position);
        holder.expenseCost.setText(String.valueOf(expense.getAmount()));
        //holder.datePurchased.setText(expense.getFormattedDate());
    }


    @Override
    public int getItemCount() {
        return expenseList != null ? expenseList.size() : 0;
    }

    public void setExpenseItems(List<mExpense> expenseList) {
        this.expenseList = expenseList; // Update the list
        notifyDataSetChanged();         // Notify RecyclerView to refresh
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseName, expenseCost, datePurchased;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseName = itemView.findViewById(R.id.expense_name);
            expenseCost = itemView.findViewById(R.id.expense_cost);
            datePurchased = itemView.findViewById(R.id.date_purchased);
        }
    }
}
