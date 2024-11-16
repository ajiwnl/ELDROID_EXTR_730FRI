package com.eldroidfri730.extr.viewmodel.auth;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.utils.InputValidator;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    private final MutableLiveData<String> usernameError = new MutableLiveData<>();
    private final MutableLiveData<String> passwordError = new MutableLiveData<>();
    private final Application application;

    public LoginViewModel(Application application) {
        this.application = application;
    }

    public LiveData<Boolean> getIsLoggedIn() {
        return isLoggedIn;
    }

    public LiveData<String> getUsernameError() {
        return usernameError;
    }

    public LiveData<String> getPasswordError() {
        return passwordError;
    }

    public void login(String username, String password) {
        boolean isValid = true;

        // Validate username
        if (!InputValidator.isValidUsername(username)) {
            usernameError.setValue(application.getString(R.string.invalid_username));
            isValid = false;
        } else {
            usernameError.setValue(null);
        }

        // Validate password
        if (!InputValidator.isValidPassword(password)) {
            passwordError.setValue(application.getString(R.string.invalid_password));
            isValid = false;
        } else {
            passwordError.setValue(null);
        }

        // Only proceed with login if both inputs are valid
        if (isValid) {
            isLoggedIn.setValue(true);
        }
    }
}
