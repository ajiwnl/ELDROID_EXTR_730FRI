package com.eldroidfri730.extr.data.models;

public class mBudget {
    private String userId;         // Should match "userId" in JSON
    private String categoryTitle;  // Should match "categoryTitle" in JSON
    private int budget;            // Should match "budget" in JSON

    // Constructor
    public mBudget(String userId, String categoryTitle, int budget) {
        this.userId = userId;
        this.categoryTitle = categoryTitle;
        this.budget = budget;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }
}
