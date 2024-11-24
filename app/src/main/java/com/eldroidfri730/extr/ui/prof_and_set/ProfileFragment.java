package com.eldroidfri730.extr.ui.prof_and_set;

import android.app.Activity;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.ui.home.BasicSummaryActivity;
import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;
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
        Button submitButton = view.findViewById(R.id.profilesubmitbutton);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        String profileImageUrl = sharedPreferences.getString("profileImage", null);

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
                // Handle email error (e.g., show a toast or set an error on an EditText)
            }
        });

        profileViewModel.getUsernameError().observe(getViewLifecycleOwner(), usernameError -> {
            if (usernameError != null) {
                // Handle username error (e.g., show a toast or set an error on an EditText)
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
            String updatedOldPassword = !oldPassword.isEmpty() ? oldPassword : null;
            String updatedNewPassword = !newPassword.isEmpty() ? newPassword : null;
            String updatedConfirmPassword = !confirmPassword.isEmpty() ? confirmPassword : null;

            if (updatedOldPassword != null && updatedNewPassword != null && updatedConfirmPassword != null) {
                if (updatedNewPassword.equals(updatedConfirmPassword)) {
                    profileViewModel.updateDetails(userId, null, null, updatedNewPassword, null);
                    clearPasswordFields();
                } else {
                    Toast.makeText(requireContext(), getString(R.string.password_not_equal), Toast.LENGTH_SHORT).show();
                }
            } else {
                profileViewModel.updateDetails(userId, updatedUsername, updatedEmail, null, null);
                clearTextFields(updatedUsername, updatedEmail);
            }

        });

        // TODO: Add more interaction logic to handle user input and call ViewModel methods
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
    }

    private void clearTextFields(String updatedUsername, String updatedEmail) {
        if (updatedUsername != null) profileUsername.setText("");
        if (updatedEmail != null) profileEmail.setText("");
    }
}
