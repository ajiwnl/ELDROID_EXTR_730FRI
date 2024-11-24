package com.eldroidfri730.extr.viewmodel.prof_and_set;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static java.security.AccessController.getContext;

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
import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private final MutableLiveData<String> oldPasswordError = new MutableLiveData<>();

    private final MutableLiveData<String> newPasswordError = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPasswordError = new MutableLiveData<>();

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
    public LiveData<String> getOldPasswordError() {
        return oldPasswordError;
    }
    public LiveData<String> getNewPasswordError() {
        return newPasswordError;
    }
    public LiveData<String> getConfirmPasswordError() {
        return confirmPasswordError;
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

    public boolean validateEmail(String email) {
        boolean isValid = true;

        if (!InputValidator.isValidEmail(email)) {
            emailError.setValue(application.getString(R.string.invalid_email));
            isValid = false;
        } else {
            emailError.setValue(null); // Clear error
        }
        return isValid;

    }

    public boolean validateUsername(String updatedUsername) {
        boolean isValid = true;
        if (!InputValidator.isValidUsername(updatedUsername)) {
            usernameError.setValue(application.getString(R.string.username_check));
            isValid = false;
        }
        else {
            usernameError.setValue(null); // Clear error
        }

        return isValid;
    }

    public boolean validatePassword(String oldPassword, String inputOldPassword, String newPassword, String updatedConfirmPassword) {
        boolean isValid = true;
        if(!oldPassword.equals(inputOldPassword) || inputOldPassword.isEmpty()) {
            oldPasswordError.setValue(application.getString(R.string.password_incorrect));
            isValid = false;
        }
        else if (!InputValidator.isValidPassword(newPassword)) {
            newPasswordError.setValue(application.getString(R.string.password_min));
            isValid = false;
        }
        else if(!newPassword.equals(updatedConfirmPassword)) {
            newPasswordError.setValue(application.getString(R.string.password_not_matched));
            confirmPasswordError.setValue(application.getString(R.string.password_not_matched));
            isValid = false;
        }
        else {
            oldPasswordError.setValue(null);
            newPasswordError.setValue(null);
            confirmPasswordError.setValue(null);
        }
        return isValid;
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

        // Convert File to InputStream
        InputStream profileImageInputStream = null;
        if (profileImageFile != null) {
            try {
                profileImageInputStream = new FileInputStream(profileImageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Create the image part using the InputStream
        MultipartBody.Part imagePart = profileImageInputStream != null ?
                prepareFilePart("profileImage", profileImageInputStream) : null;

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

    public void uploadImage(InputStream imageInputStream, String userId) {
        if (imageInputStream != null) {
            try {
                File tempFile = File.createTempFile("image", ".jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = imageInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, length);
                }

                fileOutputStream.flush();
                fileOutputStream.close();

                MultipartBody.Part imagePart = prepareFilePart("profileImage", tempFile);

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
                        Log.e("ProfileUpdateError", "Failed to update profile", t);
                        profileErrorMessage.postValue(application.getString(R.string.update_network_error));
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                profileErrorMessage.postValue(application.getString(R.string.image_upload_error));
            }
        }
    }

    private MultipartBody.Part prepareFilePart(String partName, Object fileObject) {
        RequestBody requestFile = null;

        if (fileObject instanceof File) {
            File file = (File) fileObject;
            requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        }
        else if (fileObject instanceof InputStream) {
            InputStream inputStream = (InputStream) fileObject;
            try {
                File tempFile = File.createTempFile("image", ".jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, length);
                }

                fileOutputStream.flush();
                fileOutputStream.close();

                requestFile = RequestBody.create(MediaType.parse("image/*"), tempFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return requestFile != null ? MultipartBody.Part.createFormData(partName, ((File) fileObject).getName(), requestFile) : null;
    }
}
