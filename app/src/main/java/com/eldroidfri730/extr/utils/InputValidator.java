package com.eldroidfri730.extr.utils;

public class InputValidator {
    public static boolean isValidEmail(String email) {
        // Simple regex check for email validity (could be improved)
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 3;
    }
}
