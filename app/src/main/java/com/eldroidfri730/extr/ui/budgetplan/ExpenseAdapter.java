package com.eldroidfri730.extr.ui.budgetplan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.eldroidfri730.extr.viewmodel.exp_and_cat.ExpenseViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private ExpenseViewModel expenseViewModel;
    private List<mExpense> expenseList;
    private Context context;

    public ExpenseAdapter(Context context,ExpenseViewModel expenseViewModel) {
        this.context = context;
        this.expenseList = new ArrayList<>();
        this.expenseViewModel = expenseViewModel;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete " + expense.getExpenseName() + " ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        expenseViewModel.deleteExpense(expense.getUserId(), expense.getExpenseName());
                        Toast.makeText(context, "Expense Deleted!" , Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Dismiss the dialog without doing anything
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
