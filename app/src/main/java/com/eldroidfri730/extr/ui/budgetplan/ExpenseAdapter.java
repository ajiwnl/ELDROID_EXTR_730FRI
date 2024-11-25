package com.eldroidfri730.extr.ui.budgetplan;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.models.mExpense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<mExpense> expenseList;
    private Context context;

    public ExpenseAdapter(Context context) {
        this.context = context;
        this.expenseList = new ArrayList<>();
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

        // Bind data to the views
        holder.expenseName.setText(expense.getExpenseName());
        holder.expenseCost.setText(String.format("₱ %.2f", expense.getAmount()));
        holder.datePurchased.setText(expense.getDate());

        // Edit button logic
        holder.editButton.setOnClickListener(v -> {
            showEditExpenseDialog(expense);
        });

        // Delete button logic
        holder.deleteButton.setOnClickListener(v -> {
            showDeleteConfirmationDialog(expense);
        });
    }

    @Override
    public int getItemCount() {
        return expenseList != null ? expenseList.size() : 0;
    }

    public void setExpenseItems(List<mExpense> expenseList) {
        this.expenseList = expenseList;
        notifyDataSetChanged();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseName, expenseCost, datePurchased;
        ImageButton editButton, deleteButton;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseName = itemView.findViewById(R.id.expense_name);
            expenseCost = itemView.findViewById(R.id.expense_cost);
            datePurchased = itemView.findViewById(R.id.date_purchased);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    public void updateExpenses(List<mExpense> newExpenses) {
        this.expenseList.clear();
        this.expenseList.addAll(newExpenses);
        notifyDataSetChanged();
    }

    // Method to display edit dialog
    private void showEditExpenseDialog(mExpense expense) {
//        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_expense, null);
//        Dialog dialog = new Dialog(context);
//        dialog.setContentView(dialogView);
//        dialog.getWindow().setLayout(800, WindowManager.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.setCancelable(true);
//
//        TextView expenseName = dialogView.findViewById(R.id.edit_expense_name);
//        TextView expenseAmount = dialogView.findViewById(R.id.edit_expense_amount);
//
//        expenseName.setText(expense.getExpenseName());
//        expenseAmount.setText(String.valueOf(expense.getAmount()));
//
//        // Example save button logic
//        dialogView.findViewById(R.id.save_button).setOnClickListener(v -> {
//            Toast.makeText(context, "Expense Updated!", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        });
//
//        dialog.show();
    }

    // Method to display delete confirmation dialog
    private void showDeleteConfirmationDialog(mExpense expense) {
//        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_confirmation, null);
//        Dialog dialog = new Dialog(context);
//        dialog.setContentView(dialogView);
//        dialog.getWindow().setLayout(800, WindowManager.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.setCancelable(true);
//
//        TextView confirmationText = dialogView.findViewById(R.id.confirmation_text);
//        confirmationText.setText("Are you sure you want to delete " + expense.getExpenseName() + "?");
//
//        // Delete button logic
//        dialogView.findViewById(R.id.delete_button).setOnClickListener(v -> {
//            expenseList.remove(expense);
//            notifyDataSetChanged();
//            Toast.makeText(context, "Expense Deleted!", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        });
//
//        // Cancel button logic
//        dialogView.findViewById(R.id.cancel_button).setOnClickListener(v -> dialog.dismiss());
//
//        dialog.show();
    }
}
