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

        forgotPasswordViewModel.getEmailError().observe(this, error -> {
            if (error != null) {
                forgotPasswordEmail.setError(error);
            }
        });

        // Use the TextUtil to underline the text
        String text = getString(R.string.go_back);
        goback.setText(TextUtil.getUnderlinedText(text));

        goback.setOnClickListener(v -> {
            Intent toLogin = new Intent(this, LoginActivity.class);
            startActivity(toLogin);
        });

        sendemail.setOnClickListener(v -> {
            String email = forgotPasswordEmail.getText().toString();

            if (forgotPasswordViewModel.validateEmail(email)) {
                // Proceed with email sending or notification
                Toast.makeText(this, getString(R.string.email_notif), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
