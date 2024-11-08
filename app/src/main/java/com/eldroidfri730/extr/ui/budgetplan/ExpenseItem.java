package com.eldroidfri730.extr.ui.budgetplan;

public class ExpenseItem {
    private String expenseName;
    private String expenseCost;
    private String datePurchased;

    public ExpenseItem(String expenseName, String expenseCost, String datePurchased) {
        this.expenseName = expenseName;
        this.expenseCost = expenseCost;
        this.datePurchased = datePurchased;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public String getExpenseCost() {
        return expenseCost;
    }

    public String getDatePurchased() {
        return datePurchased;
    }
}
