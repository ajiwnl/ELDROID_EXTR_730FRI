// LoginViewModel.java
package com.eldroidfri730.extr.viewmodel.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();

    public LiveData<Boolean> getIsLoggedIn() {
        return isLoggedIn;
    }

    public void login(String username, String password) {
        // Implement login logic here, e.g., network call to authenticate
        // For now, we just set a successful login example
        isLoggedIn.setValue(true);
    }
}
