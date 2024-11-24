package com.eldroidfri730.extr.data.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class mExpense {
    @SerializedName("expenseName")
    private String name;

    @SerializedName("expenseDescription")
    private String desc;

    @SerializedName("amount")
    private float amount;

    @SerializedName("date")
    private Date date;

    @SerializedName("userId")
    private String userId;

    // Default constructor
    public mExpense() {
    }

    // Constructor with all fields
    public mExpense(String name, String category, float amount, Date date, @Nullable String desc, String userId) {
        this.name = name;
        //this.category = category;
        this.amount = amount;
        this.date = date;
        this.desc = desc;
        this.userId = userId;
    }

    // Add the setter for userId
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter for userId
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    /*public String getCategory() {
        return category;
    }*/

    public float getAmount() {
        return amount;
    }

    public String getDesc() {
        return desc != null ? desc : "";  // Return empty string if desc is null
    }

    public Date getDate() {
        return date;
    }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    // Optional: A method to display a readable version of the expense
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
               /* ", category='" + category*/ + '\'' +
                ", amount=" + amount +
                ", date=" + getFormattedDate() +
                ", desc='" + getDesc() + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
