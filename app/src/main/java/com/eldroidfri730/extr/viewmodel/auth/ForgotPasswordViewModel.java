package com.eldroidfri730.extr.viewmodel.auth;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.eldroidfri730.extr.utils.InputValidator;
import com.eldroidfri730.extr.R;

public class ForgotPasswordViewModel extends ViewModel {
    private final MutableLiveData<String> emailError = new MutableLiveData<>();
    private Application application;

    public ForgotPasswordViewModel(Application application) {
        this.application = application;
    }

    public LiveData<String> getEmailError() {
        return emailError;
    }

    public boolean validateEmail(String email) {
        boolean isValid = true;

        if (!InputValidator.isValidEmail(email)) {
            emailError.setValue(application.getString(R.string.invalid_email));  // Use application context to access strings
            isValid = false;
        } else {
            emailError.setValue(null);
        }

        return isValid;
    }
}