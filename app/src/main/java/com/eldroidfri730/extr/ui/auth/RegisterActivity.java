package com.eldroidfri730.extr.ui.auth;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.viewmodel.auth.RegisterViewModel;
import com.eldroidfri730.extr.utils.TextUtil;

public class RegisterActivity extends AppCompatActivity {
    private TextView existingAccountTextView;
    private Button registerAccountButton;
    private EditText registerEmail;
    private EditText registerUsername;
    private EditText registerPassword;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Initialize ViewModel
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // UI setup
        existingAccountTextView = findViewById(R.id.existingaccounttextview);
        registerAccountButton = findViewById(R.id.registeraccountbutton);
        registerEmail = findViewById(R.id.registeremail);
        registerUsername = findViewById(R.id.loginusername);
        registerPassword = findViewById(R.id.loginpassword);

        // Set underlined text
        existingAccountTextView.setText(TextUtil.getUnderlinedText("Already Have An Account?"));

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
        existingAccountTextView.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        registerAccountButton.setOnClickListener(v -> {
            String email = registerEmail.getText().toString();
            String username = registerUsername.getText().toString();
            String password = registerPassword.getText().toString();

            // Validate inputs
            if (registerViewModel.validateInputs(email, password, username)) {
                // Proceed with registration (next step)
/*                startActivity(new Intent(this, VerifyEmailActivity.class));*/
            }
        });
    }
}
