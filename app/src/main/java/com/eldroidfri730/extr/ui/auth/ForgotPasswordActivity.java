package com.eldroidfri730.extr.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.utils.IntentUtil;
import com.eldroidfri730.extr.utils.TextUtil;
import com.eldroidfri730.extr.viewmodel.auth.ForgotPasswordViewModel;
import com.eldroidfri730.extr.viewmodel.auth.ForgotPasswordViewModelFactory;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextView goback;
    private Button sendemail;
    private EditText forgotPasswordEmail;
    private ForgotPasswordViewModel forgotPasswordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        sendemail = findViewById(R.id.sendemailforgotpassbutton);
        goback = findViewById(R.id.gobacktextview);

        // Use ViewModelProvider.Factory to pass Application context
        ForgotPasswordViewModelFactory factory = new ForgotPasswordViewModelFactory(getApplication());
        forgotPasswordViewModel = new ViewModelProvider(this, factory).get(ForgotPasswordViewModel.class);

        forgotPasswordEmail = findViewById(R.id.forgotpasswordemail);

        // Observe email error to show validation error on the EditText field
        forgotPasswordViewModel.getEmailError().observe(this, error -> {
            if (error != null) {
                forgotPasswordEmail.setError(error);
            }
        });

        // Observe success or failure message for forgot password
        forgotPasswordViewModel.getForgotPasswordSuccessMessage().observe(this, successMessage -> {
            if (successMessage != null) {
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
                // You can navigate back to login page after success if needed
                IntentUtil.startActivity(ForgotPasswordActivity.this, LoginActivity.class);
            }
        });

        forgotPasswordViewModel.getForgotPasswordErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Use the TextUtil to underline the text
        String text = getString(R.string.go_back);
        goback.setText(TextUtil.getUnderlinedText(text));

        goback.setOnClickListener(v -> {
            IntentUtil.startActivity(ForgotPasswordActivity.this, LoginActivity.class);
        });

        sendemail.setOnClickListener(v -> {
            String email = forgotPasswordEmail.getText().toString();

            if (forgotPasswordViewModel.validateEmail(email)) {
                // Trigger the API call to reset the password
                forgotPasswordViewModel.forgotPassword(email);
            }
        });
    }
}