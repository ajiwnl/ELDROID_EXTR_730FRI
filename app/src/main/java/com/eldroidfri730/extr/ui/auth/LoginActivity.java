package com.eldroidfri730.extr.ui.auth;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize ViewModel
        LoginViewModelFactory factory = new LoginViewModelFactory(getApplication());
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        // Setup UI components
        usernameLogin = findViewById(R.id.loginusername);
        passwordLogin = findViewById(R.id.loginpassword);
        loginButton = findViewById(R.id.loginuserbutton);

        // Set up login button click listener
        loginButton.setOnClickListener(v -> {
            String username = usernameLogin.getText().toString();
            String password = passwordLogin.getText().toString();
            loginViewModel.login(username, password);
        });

        // Observe username validation error
        loginViewModel.getUsernameError().observe(this, error -> {
            if (error != null) {
                usernameLogin.setError(error);
            }
        });

        // Observe password validation error
        loginViewModel.getPasswordError().observe(this, error -> {
            if (error != null) {
                passwordLogin.setError(error);
            }
        });

        // Observe login success
        loginViewModel.getIsLoggedIn().observe(this, isLoggedIn -> {
            if (isLoggedIn != null && isLoggedIn) {
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                IntentUtil.startActivity(LoginActivity.this, BasicSummaryActivity.class);
            }
        });
    }
}
