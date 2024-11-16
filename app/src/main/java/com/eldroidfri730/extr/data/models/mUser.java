package com.eldroidfri730.extr.data.models;

public class mUser {
    private String username;
    private String email;
    private String password;
    private boolean verified;

    // Constructor
    public mUser(String username, String email, String password, boolean verified) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.verified = verified;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
