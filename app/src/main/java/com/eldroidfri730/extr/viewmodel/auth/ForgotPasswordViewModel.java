package com.eldroidfri730.extr.viewmodel.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eldroidfri730.extr.utils.InputValidator;

public class ForgotPasswordViewModel extends ViewModel {
    private final MutableLiveData<String> emailError = new MutableLiveData<>();

    public LiveData<String> getEmailError() {
        return emailError;
    }
    public boolean validateEmail(String email) {
        boolean isValid = true;

        if (!InputValidator.isValidEmail(email)) {
            emailError.setValue("Invalid email address");
            isValid = false;
        } else {
            emailError.setValue(null);
        }

        return isValid;
    }
}
