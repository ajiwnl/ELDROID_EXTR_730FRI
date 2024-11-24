package com.eldroidfri730.extr.ui.prof_and_set;

import android.content.Intent;
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

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.ui.home.BasicSummaryActivity;
import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;
import com.eldroidfri730.extr.viewmodel.prof_and_set.ProfileViewModel;
import com.eldroidfri730.extr.viewmodel.prof_and_set.ProfileViewModelFactory;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private CircleImageView profileImageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText profileUsername = view.findViewById(R.id.profileusername);
        EditText profileEmail = view.findViewById(R.id.profileemail);
        EditText profileOldPassword = view.findViewById(R.id.profileoldpassword);
        EditText profileNewPassword = view.findViewById(R.id.profilenewpassword);
        EditText profileConfirmPassword = view.findViewById(R.id.profileconfirmpassword);
        profileImageView = view.findViewById(R.id.profileimage);
        Button submitButton = view.findViewById(R.id.profilesubmitbutton);


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
            String email = profileEmail.getText().toString();
            String oldPassword = profileOldPassword.getText().toString();
            String newPassword = profileNewPassword.getText().toString();
            String confirmPassword = profileConfirmPassword.getText().toString();

            if (!username.isEmpty()) {
                profileViewModel.updateDetails(userId, username, null, null , null);
                profileUsername.setText("");
            }
            else if(!email.isEmpty()) {
                profileViewModel.updateDetails(userId, null, email, null , null);
                profileEmail.setText("");
            }
            else if(!oldPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {
                if(newPassword.equals(confirmPassword)) {
                    profileViewModel.updateDetails(userId, null, null, newPassword, null);
                    profileOldPassword.setText("");
                    profileNewPassword.setText("");
                    profileConfirmPassword.setText("");
                }
                else if(!oldPassword.equals("Password From Logging In")) {
                    Toast.makeText(requireContext(), getString(R.string.password_incorrect), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(requireContext(), getString(R.string.password_not_equal), Toast.LENGTH_SHORT).show();
                }
            }
            else {
                //Toast.makeText(requireContext(), getString(R.string.details_not_empty), Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: Add more interaction logic to handle user input and call ViewModel methods
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireContext().getContentResolver().query(contentUri, proj, null, null, null); // Use requireContext() here
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                String imagePath = getRealPathFromURI(selectedImageUri);
                File profileImageFile = new File(imagePath);

                profileImageView.setImageURI(selectedImageUri);

                profileViewModel.uploadImage(profileImageFile, userId);
            }
        }
    }
}
