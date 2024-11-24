package com.eldroidfri730.extr.data.models;

public class mUser {
    private String username;
    private String email;
    private String password;
    private boolean verified;
    private String id;
    private String profileImage;

    // Constructor
    public mUser(String username, String email, String password, boolean verified) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.verified = verified;
    }

    public mUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public mUser(String email) {
        this.email = email;
    }

    public mUser(String id, String username, String email, String password, boolean verified) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.verified = verified;
    }

    public mUser(String id, String username, String email, String password, boolean verified,  String profileImage ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.verified = verified;
        this.profileImage = profileImage;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
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

    public void setProfileImage(String profileImage) {
     this.profileImage = profileImage;
    }

    public String getProfileImage() {
        return profileImage;
    }
}

