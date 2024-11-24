package com.eldroidfri730.extr.viewmodel.prof_and_set;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eldroidfri730.extr.data.ApiService;
import com.eldroidfri730.extr.utils.RetrofitClient;
import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> username = new MutableLiveData<>();
    private final MutableLiveData<String> emailError = new MutableLiveData<>();
    private final MutableLiveData<String> usernameError = new MutableLiveData<>();
    private final MutableLiveData<String> profileSuccessMessage = new MutableLiveData<>();
    private final MutableLiveData<String> profileErrorMessage = new MutableLiveData<>();
    private Application application;
    private ApiService apiService;

    public ProfileViewModel(Application application) {
        this.application = application;
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public LiveData<String> getEmailError() {
        return emailError;
    }

    public LiveData<String> getUsernameError() {
        return usernameError;
    }

    public LiveData<String> getProfileSuccessMessage() {
        return profileSuccessMessage;
    }

    public LiveData<String> getProfileErrorMessage() {
        return profileErrorMessage;
    }
}
