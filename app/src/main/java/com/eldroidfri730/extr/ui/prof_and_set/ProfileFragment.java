package com.eldroidfri730.extr.ui.prof_and_set;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.ui.auth.LoginActivity;
import com.eldroidfri730.extr.ui.home.BasicSummaryActivity;
import com.eldroidfri730.extr.utils.IntentUtil;
import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;
import com.eldroidfri730.extr.viewmodel.auth.RegisterViewModel;
import com.eldroidfri730.extr.viewmodel.auth.RegisterViewModelFactory;
import com.eldroidfri730.extr.viewmodel.prof_and_set.ProfileViewModel;
import com.eldroidfri730.extr.viewmodel.prof_and_set.ProfileViewModelFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private CircleImageView profileImageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String userId;
    private EditText profileOldPassword;
    private EditText profileNewPassword;
    private EditText profileConfirmPassword;
    private EditText profileEmail;
    private EditText profileUsername;

    private ImageButton backButton;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileUsername = view.findViewById(R.id.profileusername);
        profileEmail = view.findViewById(R.id.profileemail);
        profileOldPassword = view.findViewById(R.id.profileoldpassword);
        profileNewPassword = view.findViewById(R.id.profilenewpassword);
        profileConfirmPassword = view.findViewById(R.id.profileconfirmpassword);
        profileImageView = view.findViewById(R.id.profileimage);
        profileUsername = view.findViewById(R.id.profileusername);

        backButton = view.findViewById(R.id.profilebackbutton);

        Button submitButton = view.findViewById(R.id.profilesubmitbutton);

        backButton.setOnClickListener(v -> IntentUtil.popBackStack(requireActivity()));

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        String profileImageUrl = sharedPreferences.getString("profileImage", null);
        String usernamePref = sharedPreferences.getString("username", null);
        String emailPref = sharedPreferences.getString("email", null);
        String passRef = sharedPreferences.getString("password", null);

        profileUsername.setHint(usernamePref);
        profileEmail.setHint(emailPref);

        CircleImageView profileImageView = view.findViewById(R.id.profileimage);

        if (profileImageUrl != null) {
            Glide.with(this)
                    .load(profileImageUrl)
                    .placeholder(R.drawable.budgetbgimg)
                    .error(R.drawable.budgetbgimg)
                    .into(profileImageView);
        }

        LoginViewModel loginViewModel = ((BasicSummaryActivity) getActivity()).getLoginViewModel(); // Retrieve LoginViewModel
        userId = loginViewModel.getUserId(); // Retrieve userId

        // Initialize ProfileViewModel using the factory
        ProfileViewModelFactory factory = new ProfileViewModelFactory(requireActivity().getApplication());
        profileViewModel = new ViewModelProvider(this, factory).get(ProfileViewModel.class);

        // Observe LiveData for changes and update the UI accordingly
        profileViewModel.getEmailError().observe(getViewLifecycleOwner(), emailError -> {
            if (emailError != null) {
                profileEmail.setError(emailError);
            }
        });

        profileViewModel.getUsername().observe(getViewLifecycleOwner(), updatedUsername -> {
            if (updatedUsername != null) {
                profileUsername.setText(updatedUsername);
            }
        });


        profileViewModel.getUsernameError().observe(getViewLifecycleOwner(), usernameError -> {
            if (usernameError != null) {
                profileUsername.setError(usernameError);
            }
        });

        profileViewModel.getOldPasswordError().observe(getViewLifecycleOwner(), passwordError -> {
            if(passwordError != null) {
                profileOldPassword.setError(passwordError);
            }
        });

        profileViewModel.getNewPasswordError().observe(getViewLifecycleOwner(), passwordError -> {
            if(passwordError != null) {
                profileNewPassword.setError(passwordError);
            }
        });

        profileViewModel.getConfirmPasswordError().observe(getViewLifecycleOwner(), passwordError -> {
            if(passwordError != null) {
                profileConfirmPassword.setError(passwordError);
            }
        });

        profileViewModel.getProfileSuccessMessage().observe(getViewLifecycleOwner(), successMessage -> {
            if (successMessage != null) {
                Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getProfileErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        profileImageView.setOnClickListener(v -> openImagePicker());

        submitButton.setOnClickListener(v -> {
            String username = profileUsername.getText().toString().trim();
            String email = profileEmail.getText().toString().trim();
            String oldPassword = profileOldPassword.getText().toString().trim();
            String newPassword = profileNewPassword.getText().toString().trim();
            String confirmPassword = profileConfirmPassword.getText().toString().trim();

            String updatedUsername = !username.isEmpty() ? username : null;
            String updatedEmail = !email.isEmpty() ? email : null;
            String updatedOldPassword = !oldPassword.isEmpty() ? oldPassword : "";
            String updatedNewPassword = !newPassword.isEmpty() ? newPassword : "";
            String updatedConfirmPassword = !confirmPassword.isEmpty() ? confirmPassword : "";

            if (!updatedOldPassword.isEmpty() && profileViewModel.validatePassword(updatedOldPassword, passRef , newPassword, updatedConfirmPassword)) {
                if (updatedNewPassword.equals(updatedConfirmPassword)) {
                    profileViewModel.updateDetails(userId, null, null, updatedNewPassword, null);
                    clearPasswordFields();

                }
            } else {
                if(updatedEmail != null && profileViewModel.validateEmail(updatedEmail)) {
                    profileViewModel.updateDetails(userId, null, updatedEmail, null, null);
                    clearTextFields(updatedUsername, updatedEmail);
                    clearLoginState();
                }
                else if(updatedUsername != null && profileViewModel.validateUsername(updatedUsername)) {
                    profileViewModel.updateDetails(userId, updatedUsername, null, null, null);
                    clearTextFields(updatedUsername, updatedEmail);
                    updateSharedPreferences(updatedUsername, updatedEmail);  // Update SharedPreferences with new username

                }
            }
        });
        // TODO: Add more interaction logic to handle user input and call ViewModel methods
    }

    private void updateSharedPreferences(String username, String email) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (username != null) {
            editor.putString("username", username);  // Update username
        }

        if (email != null) {
            editor.putString("email", email);  // Update email
        }
        // Commit the changes
        editor.apply();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    InputStream inputStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
                    if (inputStream != null) {
                        profileImageView.setImageURI(selectedImageUri);

                        profileViewModel.uploadImage(inputStream, userId);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void clearPasswordFields() {
        profileOldPassword.setText("");
        profileNewPassword.setText("");
        profileConfirmPassword.setText("");
        profileNewPassword.setError(null);
    }

    private void clearTextFields(String updatedUsername, String updatedEmail) {
        if (updatedUsername != null) profileUsername.setText("");
        if (updatedEmail != null) profileEmail.setText("");
    }

    private void clearLoginState() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_logged_in", false); // Mark user as logged out
        editor.remove("user_id"); // Remove user ID to ensure a fresh login
        editor.apply();

        Toast.makeText(requireContext(), getString(R.string.email_verification_message), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish(); // Close the ProfileFragment and return to the LoginActivity
    }

}
