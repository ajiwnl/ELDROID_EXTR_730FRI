package com.eldroidfri730.extr.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.utils.IntentUtil;
import com.eldroidfri730.extr.viewmodel.auth.RegisterViewModel;
import com.eldroidfri730.extr.viewmodel.auth.RegisterViewModelFactory;
import com.eldroidfri730.extr.utils.TextUtil;

public class RegisterActivity extends AppCompatActivity {
    private TextView existingAccountTextView;
    private Button registerAccountButton;
    private EditText registerEmail;
    private EditText registerUsername;
    private EditText registerPassword;
    private RegisterViewModel registerViewModel;

    private static final String TAG = "RegisterActivity"; // Tag for logs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.d(TAG, "onCreate: RegisterActivity started");

        // Initialize ViewModel with Application context using the Factory
        RegisterViewModelFactory factory = new RegisterViewModelFactory(getApplication());
        registerViewModel = new ViewModelProvider(this, factory).get(RegisterViewModel.class);

        // UI setup
        existingAccountTextView = findViewById(R.id.existingaccounttextview);
        registerAccountButton = findViewById(R.id.registeraccountbutton);
        registerEmail = findViewById(R.id.registeremail);
        registerUsername = findViewById(R.id.loginusername);
        registerPassword = findViewById(R.id.loginpassword);

        // Set underlined text
        existingAccountTextView.setText(TextUtil.getUnderlinedText(getString(R.string.existing_acct)));

        // Observe validation errors
        registerViewModel.getEmailError().observe(this, error -> {
            if (error != null) {
                registerEmail.setError(error);
            }
        });

        registerViewModel.getPasswordError().observe(this, error -> {
            if (error != null) {
                registerPassword.setError(error);
            }
        });

        registerViewModel.getUsernameError().observe(this, error -> {
            if (error != null) {
                registerUsername.setError(error);
            }
        });

        // Set up click listeners
        existingAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(RegisterActivity.this, LoginActivity.class);
            }
        });

        registerAccountButton.setOnClickListener(v -> {
            String email = registerEmail.getText().toString().trim();
            String username = registerUsername.getText().toString().trim();
            String password = registerPassword.getText().toString().trim();


            if (registerViewModel.validateInputs(email, password, username)) {
                registerViewModel.registerUser(email, password, username);
            } else {}
        });

        registerViewModel.getRegisterSuccessMessage().observe(this, successMessage -> {
            if (successMessage != null) {
                Toast.makeText(RegisterActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                IntentUtil.startActivity(RegisterActivity.this, VerifyEmailActivity.class);
            }
        });

        registerViewModel.getRegisterErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}