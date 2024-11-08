// LoginActivity.java
package com.eldroidfri730.extr.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView createAccountTxtView, forgotPasswordTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Setup UI components
        usernameEditText = findViewById(R.id.loginusername);
        passwordEditText = findViewById(R.id.loginpassword);
        createAccountTxtView = findViewById(R.id.createnewaccounttextview);
        forgotPasswordTxtView = findViewById(R.id.forgotpasswordtextview);
        loginButton = findViewById(R.id.loginuserbutton);

        createAccountTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(LoginActivity.this, RegisterActivity.class);
            }
        });
        forgotPasswordTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(LoginActivity.this, ForgotPasswordActivity.class);
            }
        });

        // Set up login button click listener
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            loginViewModel.login(username, password);
        });

        // Observe login status
        loginViewModel.getIsLoggedIn().observe(this, isLoggedIn -> {
            if (isLoggedIn != null && isLoggedIn) {
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                IntentUtil.startActivity(LoginActivity.this, BasicSummaryActivity.class);
            } else {
                Toast.makeText(this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
