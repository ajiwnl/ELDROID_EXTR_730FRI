package com.eldroidfri730.extr.viewmodel.auth;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.ApiService;
import com.eldroidfri730.extr.data.models.mUser;
import com.eldroidfri730.extr.utils.InputValidator;
import com.eldroidfri730.extr.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<String> emailError = new MutableLiveData<>();
    private final MutableLiveData<String> passwordError = new MutableLiveData<>();
    private final MutableLiveData<String> usernameError = new MutableLiveData<>();
    private final MutableLiveData<String> registerSuccessMessage = new MutableLiveData<>();
    private final MutableLiveData<String> registerErrorMessage = new MutableLiveData<>();
    private Application application;

    private ApiService apiService;

    public RegisterViewModel(Application application) {
        this.application = application;
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
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

    public LiveData<String> getRegisterSuccessMessage() {return registerSuccessMessage;}

    public LiveData<String> getRegisterErrorMessage() {
        return registerErrorMessage;
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

    // Register user API call
    public void registerUser(String email, String password, String username) {
        mUser newUser = new mUser(username, email, password, false);
        Call<mUser> call = apiService.registerUser(newUser);

        call.enqueue(new Callback<mUser>() {
            @Override
            public void onResponse(Call<mUser> call, Response<mUser> response) {
                if (response.isSuccessful()) {
                    registerSuccessMessage.setValue(application.getString(R.string.reg_success));
                } else {

                    if (response.code() ==409){
                        registerErrorMessage.setValue(application.getString(R.string.reg_409));

                    }else {
                    registerErrorMessage.setValue(application.getString(R.string.reg_fail));
                    }
                }
            }

            @Override
            public void onFailure(Call<mUser> call, Throwable t) {
                registerErrorMessage.setValue(application.getString(R.string.network_err) + t.getMessage());
            }
        });
    }

}
