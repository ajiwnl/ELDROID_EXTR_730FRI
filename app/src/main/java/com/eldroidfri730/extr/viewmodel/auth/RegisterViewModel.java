package com.eldroidfri730.extr.viewmodel.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eldroidfri730.extr.utils.InputValidator;

public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<String> emailError = new MutableLiveData<>();
    private final MutableLiveData<String> passwordError = new MutableLiveData<>();
    private final MutableLiveData<String> usernameError = new MutableLiveData<>();

    public LiveData<String> getEmailError() {
        return emailError;
    }

    public LiveData<String> getPasswordError() {
        return passwordError;
    }

    public LiveData<String> getUsernameError() {
        return usernameError;
    }

    public boolean validateInputs(String email, String password, String username) {
        boolean isValid = true;

        if (!InputValidator.isValidEmail(email)) {
            emailError.setValue("Invalid email address");
            isValid = false;
        } else {
            emailError.setValue(null); // Clear error
        }

        if (!InputValidator.isValidPassword(password)) {
            passwordError.setValue("Password must be at least 6 characters");
            isValid = false;
        } else {
            passwordError.setValue(null); // Clear error
        }

        if (!InputValidator.isValidUsername(username)) {
            usernameError.setValue("Username must be at least 3 characters");
            isValid = false;
        } else {
            usernameError.setValue(null); // Clear error
        }

        return isValid;
    }
}