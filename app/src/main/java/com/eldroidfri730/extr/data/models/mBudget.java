package com.eldroidfri730.extr.data.models;

public class mBudget {
    private String userId;         // Should match "userId" in JSON
    private String categoryTitle;  // Should match "categoryTitle" in JSON
    private double budget;            // Should match "budget" in JSON
    private String id;

    // Constructor
    public mBudget(String userId, String categoryTitle, double budget) {
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

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

}
