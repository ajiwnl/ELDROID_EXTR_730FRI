package com.eldroidfri730.extr.viewmodel.auth;

import android.app.Application;
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

public class ForgotPasswordViewModel extends ViewModel {
    private final MutableLiveData<String> emailError = new MutableLiveData<>();
    private final MutableLiveData<String> forgotPasswordSuccessMessage = new MutableLiveData<>();
    private final MutableLiveData<String> forgotPasswordErrorMessage = new MutableLiveData<>();
    private Application application;

    private ApiService apiService;


    public ForgotPasswordViewModel(Application application) {
        this.application = application;
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public LiveData<String> getEmailError() {
        return emailError;
    }

    public LiveData<String> getForgotPasswordSuccessMessage() {
        return forgotPasswordSuccessMessage;
    }

    public LiveData<String> getForgotPasswordErrorMessage() {
        return forgotPasswordErrorMessage;
    }

    public boolean validateEmail(String email) {
        boolean isValid = true;

        if (!InputValidator.isValidEmail(email)) {
            emailError.setValue(application.getString(R.string.invalid_email));
            isValid = false;
        } else {
            emailError.setValue(null);
        }

        return isValid;
    }

    // Make the API call to forgot password
    public void forgotPassword(String email) {
        mUser fUser = new mUser(email);
        Call<mUser> call = apiService.forgotPassword(fUser);

        call.enqueue(new Callback<mUser>() {
            @Override
            public void onResponse(Call<mUser> call, Response<mUser> response) {
                if (response.isSuccessful()) {
                    forgotPasswordSuccessMessage.setValue("Check your email to change your password");
                } else {
                    forgotPasswordErrorMessage.setValue(application.getString(R.string.no_user));
                }
            }

            @Override
            public void onFailure(Call<mUser> call, Throwable t) {
                forgotPasswordErrorMessage.setValue(application.getString(R.string.network_err) + t.getMessage());
            }
        });
    }

}
