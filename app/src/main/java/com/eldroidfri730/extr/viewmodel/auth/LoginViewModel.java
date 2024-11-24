package com.eldroidfri730.extr.viewmodel.auth;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.ApiService;
import com.eldroidfri730.extr.data.models.mUser;
import com.eldroidfri730.extr.data.response.LoginResponse;
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
    private final SharedPreferences sharedPreferences;

    public LoginViewModel(Application application) {
        this.application = application;
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        this.sharedPreferences = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        isLoggedIn.setValue(getLoginState()); // Initialize login state
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

    public LiveData<String> getLoginSuccessMessage() {
        return loginSuccessMessage;
    }

    public LiveData<String> getLoginErrorMessage() {
        return loginErrorMessage;
    }

    // Save login state to SharedPreferences
    private void saveLoginState(boolean isLoggedIn) {
        sharedPreferences.edit()
                .putBoolean("is_logged_in", isLoggedIn)
                .apply();
    }

    // Retrieve login state from SharedPreferences
    private boolean getLoginState() {
        return sharedPreferences.getBoolean("is_logged_in", false);
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
            apiService.loginUser(user).enqueue(new Callback<LoginResponse>() {  // Updated to LoginResponse
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();
                        mUser loggedInUser = loginResponse.getUser();
                        String userId = loggedInUser.getId();
                        String username = loggedInUser.getUsername();
                        String email = loggedInUser.getEmail();
                        sharedPreferences.edit()
                                .putString("user_id", userId)
                                .putString("username", username)
                                .putString("email", email)
                                .apply();
                        isLoggedIn.setValue(true);
                        saveLoginState(true);
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
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(application, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public String getUserId() {
        return sharedPreferences.getString("user_id", null);
    }

}
