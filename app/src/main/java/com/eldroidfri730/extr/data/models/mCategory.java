package com.eldroidfri730.extr.data.models;

public class mCategory {
    private String userId;        // Should match "userId" in JSON
    private String categoryTitle; // Should match "categoryTitle" in JSON

    // Constructor
    public mCategory(String userId, String categoryTitle) {
        this.userId = userId;
        this.categoryTitle = categoryTitle;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCategoryTitle() { return categoryTitle; }
    public void setCategoryTitle(String categoryTitle) { this.categoryTitle = categoryTitle; }
}