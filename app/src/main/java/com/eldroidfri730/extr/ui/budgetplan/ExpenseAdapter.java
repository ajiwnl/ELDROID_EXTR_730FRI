package com.eldroidfri730.extr.ui.budgetplan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eldroidfri730.extr.R;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<ExpenseItem> expenseList;

    public ExpenseAdapter(List<ExpenseItem> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        ExpenseItem expenseItem = expenseList.get(position);
        holder.expenseName.setText(expenseItem.getExpenseName());
        holder.expenseCost.setText(expenseItem.getExpenseCost());
        holder.datePurchased.setText(expenseItem.getDatePurchased());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
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
