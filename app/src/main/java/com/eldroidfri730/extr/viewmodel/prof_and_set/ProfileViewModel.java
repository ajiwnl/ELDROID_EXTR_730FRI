package com.eldroidfri730.extr.viewmodel.prof_and_set;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.data.ApiService;
import com.eldroidfri730.extr.data.models.mUser;
import com.eldroidfri730.extr.utils.RetrofitClient;
import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    public void updateDetails(String userId, String newUsername, String email, String newPassword, File profileImageFile) {
        if (newUsername != null && newUsername.length() < 3) {
            usernameError.postValue(application.getString(R.string.username_too_short));
            return;
        }

        if (newUsername != null) {
            Call<List<mUser>> checkUsernameCall = apiService.getUsers();
            checkUsernameCall.enqueue(new Callback<List<mUser>>() {
                @Override
                public void onResponse(Call<List<mUser>> call, Response<List<mUser>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        for (mUser user : response.body()) {
                            if (user.getUsername().equals(newUsername)) {
                                usernameError.postValue(application.getString(R.string.username_already_exists));
                                return;
                            }
                        }
                        performUpdate(userId, newUsername, email, newPassword, profileImageFile);
                    } else {
                        profileErrorMessage.postValue(application.getString(R.string.check_network_error));
                    }
                }

                @Override
                public void onFailure(Call<List<mUser>> call, Throwable t) {
                    profileErrorMessage.postValue(application.getString(R.string.check_network_error));
                }
            });
        } else {
            performUpdate(userId, null, email, newPassword, profileImageFile);
        }
    }


    private void performUpdate(String userId, String newUsername, String email, String newPassword, File profileImageFile) {
        RequestBody usernameBody = newUsername != null ? RequestBody.create(MediaType.parse("text/plain"), newUsername) : null;
        RequestBody emailBody = email != null ? RequestBody.create(MediaType.parse("text/plain"), email) : null;
        RequestBody passwordBody = newPassword != null ? RequestBody.create(MediaType.parse("text/plain"), newPassword) : null;
        MultipartBody.Part imagePart = profileImageFile != null ? prepareFilePart("profileImage", profileImageFile) : null;

        Call<mUser> updateCall = apiService.updateUser(userId, usernameBody, imagePart, emailBody, passwordBody, passwordBody);
        updateCall.enqueue(new Callback<mUser>() {
            @Override
            public void onResponse(Call<mUser> call, Response<mUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    profileSuccessMessage.postValue(application.getString(R.string.update_success));
                } else {
                    if (response.code() == 404) {
                        profileErrorMessage.postValue(application.getString(R.string.user_not_found));
                    } else {
                        profileErrorMessage.postValue(application.getString(R.string.update_failed));
                    }
                }
            }

            @Override
            public void onFailure(Call<mUser> call, Throwable t) {
                profileErrorMessage.postValue(application.getString(R.string.update_network_error));
            }
        });
    }

    public void uploadImage(File profileImageFile, String userId) {
        if (profileImageFile != null) {
            MultipartBody.Part imagePart = prepareFilePart("profileImage", profileImageFile);

            Call<mUser> updateCall = apiService.updateUser(userId, null, imagePart, null, null, null);
            updateCall.enqueue(new Callback<mUser>() {
                @Override
                public void onResponse(Call<mUser> call, Response<mUser> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        profileSuccessMessage.postValue(application.getString(R.string.update_success));
                    } else {
                        profileErrorMessage.postValue(application.getString(R.string.update_failed));
                    }
                }

                @Override
                public void onFailure(Call<mUser> call, Throwable t) {
                    profileErrorMessage.postValue(application.getString(R.string.update_network_error));
                }
            });
        }
    }

    private MultipartBody.Part prepareFilePart(String partName, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
