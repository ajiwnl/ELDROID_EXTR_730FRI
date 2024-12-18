package com.eldroidfri730.extr.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.ui.home.BasicSummaryActivity;
import com.eldroidfri730.extr.utils.IntentUtil;
import com.eldroidfri730.extr.viewmodel.auth.LoginViewModel;
import com.eldroidfri730.extr.viewmodel.auth.LoginViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private EditText usernameLogin, passwordLogin;
    private Button loginButton;
    private TextView createAccountTxtView, forgotPasswordTxtView;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if user is already logged in
        if (getLoginState()) {
            IntentUtil.startActivity(this, BasicSummaryActivity.class);
            finish(); // Close the login activity
            return; // Exit onCreate early
        }

        sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);


        // Initialize ViewModel
        LoginViewModelFactory factory = new LoginViewModelFactory(getApplication());
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        // Setup UI components
        usernameLogin = findViewById(R.id.loginusername);
        passwordLogin = findViewById(R.id.loginpassword);
        loginButton = findViewById(R.id.loginuserbutton);
        createAccountTxtView = findViewById(R.id.createnewaccounttextview);
        forgotPasswordTxtView = findViewById(R.id.forgotpasswordtextview);

        // Set up login button click listener
        loginButton.setOnClickListener(v -> {
            String username = usernameLogin.getText().toString();
            String password = passwordLogin.getText().toString();
            loginViewModel.login(username, password);
        });

        // Create account navigation
        createAccountTxtView.setOnClickListener(v -> {
            IntentUtil.startActivity(LoginActivity.this, RegisterActivity.class);
        });

        // Forgot password navigation
        forgotPasswordTxtView.setOnClickListener(v -> {
            IntentUtil.startActivity(LoginActivity.this, ForgotPasswordActivity.class);
        });

        // Observe login success
        loginViewModel.getIsLoggedIn().observe(this, isLoggedIn -> {
            if (isLoggedIn != null && isLoggedIn) {
                String userId = sharedPreferences.getString("user_id", "No Value");
                Log.d("LoginActivity", "Logged in with userId: " + userId);
                IntentUtil.startActivity(LoginActivity.this, BasicSummaryActivity.class);
                finish();
            }
        });

        // Observe success message
        loginViewModel.getLoginSuccessMessage().observe(this, successMessage -> {
            if (successMessage != null) {
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Observe error messages related to login
        loginViewModel.getLoginErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Observe error messages
        loginViewModel.getUsernameError().observe(this, error -> {
            if (error != null) {
                usernameLogin.setError(error);
            }
        });

        loginViewModel.getPasswordError().observe(this, error -> {
            if (error != null) {
                passwordLogin.setError(error);
            }
        });
    }

    // Retrieve login state from SharedPreferences
    private boolean getLoginState() {
        return getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getBoolean("is_logged_in", false);
    }
}