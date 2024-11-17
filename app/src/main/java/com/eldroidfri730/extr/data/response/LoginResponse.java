package com.eldroidfri730.extr.data.response;

import com.eldroidfri730.extr.data.models.mUser;

public class LoginResponse {
    private String message;
    private mUser user;  // The user object containing user details

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public mUser getUser() {
        return user;
    }

    public void setUser(mUser user) {
        this.user = user;
    }
}
