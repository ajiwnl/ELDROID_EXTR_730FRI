package com.eldroidfri730.extr.data.models;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class mExpense {
    private String userId;
    private String expenseName;
    private String categoryTitle;
    private String expenseDescription;
    private float amount;
    private Date date;

    // Default constructor
    public mExpense() {
    }

    // Constructor with all fields
    public mExpense(String expenseName, String categoryTitle, float amount, Date date, @Nullable String expenseDescription, String userId) {
        this.expenseName = expenseName;
        this.categoryTitle = categoryTitle;
        this.amount = amount;
        this.date = date;
        this.expenseDescription = expenseDescription;
        this.userId = userId;
    }

    // Getter and Setter methods
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getExpenseDescription() {
        return expenseDescription != null ? expenseDescription : ""; // Return empty string if desc is null
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Method to get formatted date
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}
