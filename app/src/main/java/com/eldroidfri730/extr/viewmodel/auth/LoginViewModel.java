package com.eldroidfri730.extr.viewmodel.auth;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.ApiService;
import com.eldroidfri730.extr.data.models.mUser;
import com.eldroidfri730.extr.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    private final MutableLiveData<String> usernameError = new MutableLiveData<>();
    private final MutableLiveData<String> passwordError = new MutableLiveData<>();
    private final MutableLiveData<String> loginSuccessMessage = new MutableLiveData<>();
    private final MutableLiveData<String> loginErrorMessage = new MutableLiveData<>();
    private final Application application;
    private final ApiService apiService;

    public LoginViewModel(Application application) {
        this.application = application;
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
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
    public LiveData<String> getLoginSuccessMessage() {return loginSuccessMessage;}

    public LiveData<String> getLoginErrorMessage() {
        return loginErrorMessage;
    }

    public void login(String username, String password) {
        boolean isValid = true;

        // Validate inputs
        if (username.isEmpty()) {
            usernameError.setValue(application.getString(R.string.invalid_username));
            isValid = false;
        } else {
            usernameError.setValue(null);
        }

        if (password.isEmpty()) {
            passwordError.setValue(application.getString(R.string.invalid_password));
            isValid = false;
        } else {
            passwordError.setValue(null);
        }

        // Proceed with login if inputs are valid
        if (isValid) {
            mUser user = new mUser(username, password);
            apiService.loginUser(user).enqueue(new Callback<mUser>() {
                @Override
                public void onResponse(Call<mUser> call, Response<mUser> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        loginSuccessMessage.setValue(application.getString(R.string.login_success));
                        isLoggedIn.setValue(true);
                    } else if (response.code() == 404) {
                        usernameError.setValue(application.getString(R.string.no_user));
                    } else if (response.code() == 401) {
                        passwordError.setValue(application.getString(R.string.wrong_pass));
                    } else if (response.code() == 403) {
                        loginErrorMessage.setValue(application.getString(R.string.not_verified));
                    } else {
                        loginErrorMessage.setValue(application.getString(R.string.login_fail));
                    }
                }

                @Override
                public void onFailure(Call<mUser> call, Throwable t) {
                    Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
