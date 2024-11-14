package com.eldroidfri730.extr.viewmodel.auth;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.utils.InputValidator;

public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<String> emailError = new MutableLiveData<>();
    private final MutableLiveData<String> passwordError = new MutableLiveData<>();
    private final MutableLiveData<String> usernameError = new MutableLiveData<>();
    private Application application;

    public RegisterViewModel(Application application) {
        this.application = application;
    }

    // Getter methods for error LiveData
    public LiveData<String> getEmailError() {
        return emailError;
    }

    public LiveData<String> getPasswordError() {
        return passwordError;
    }

    public LiveData<String> getUsernameError() {
        return usernameError;
    }

    // Input validation logic
    public boolean validateInputs(String email, String password, String username) {
        boolean isValid = true;

        if (!InputValidator.isValidEmail(email)) {
            emailError.setValue(application.getString(R.string.invalid_email));
            isValid = false;
        } else {
            emailError.setValue(null); // Clear error
        }

        if (!InputValidator.isValidPassword(password)) {
            passwordError.setValue(application.getString(R.string.password_min));
            isValid = false;
        } else {
            passwordError.setValue(null); // Clear error
        }

        if (!InputValidator.isValidUsername(username)) {
            usernameError.setValue(application.getString(R.string.username_check));
            isValid = false;
        } else {
            usernameError.setValue(null); // Clear error
        }

        return isValid;
    }
}