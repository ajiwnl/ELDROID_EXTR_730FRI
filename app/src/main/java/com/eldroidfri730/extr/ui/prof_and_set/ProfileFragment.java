package com.eldroidfri730.extr.ui.prof_and_set;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.viewmodel.prof_and_set.ProfileViewModel;
import com.eldroidfri730.extr.viewmodel.prof_and_set.ProfileViewModelFactory;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                // Handle success message (e.g., show a toast or update the UI)
            }
        });

        profileViewModel.getProfileErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                // Handle general error message (e.g., show a toast or log the error)
            }
        });

        // TODO: Add more interaction logic to handle user input and call ViewModel methods
    }
}
