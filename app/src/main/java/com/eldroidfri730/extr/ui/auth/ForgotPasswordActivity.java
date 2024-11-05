package com.eldroidfri730.extr.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.viewmodel.auth.ForgotPasswordViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextView goback;
    private Button sendemail;
    private EditText forgotPasswordEmail;

    private ForgotPasswordViewModel forgotPasswordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sendemail = findViewById(R.id.sendemailforgotpassbutton);
        goback = findViewById(R.id.gobacktextview);

        forgotPasswordViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);

        forgotPasswordEmail = findViewById(R.id.forgotpasswordemail);

        forgotPasswordViewModel.getEmailError().observe(this, error -> {
            if (error != null) {
                forgotPasswordEmail.setError(error);
            }
        });

        String text = "Go Back";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        goback.setText(spannableString);

        goback.setOnClickListener( v -> {
            Intent toLogin = new Intent(this, LoginActivity.class);
            startActivity(toLogin);
        });

        String email = sendemail.getText().toString();

        sendemail.setOnClickListener( v -> {
            if (forgotPasswordViewModel.validateEmail(email)) {
                // Proceed with registration (next step)
                Toast.makeText(this, "Email Sent!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}