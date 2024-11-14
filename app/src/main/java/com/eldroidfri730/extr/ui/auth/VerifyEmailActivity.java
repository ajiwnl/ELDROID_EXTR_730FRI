package com.eldroidfri730.extr.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eldroidfri730.extr.R;
import com.eldroidfri730.extr.utils.IntentUtil;

public class VerifyEmailActivity extends AppCompatActivity {

    private Button gobacktologin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);


        gobacktologin = findViewById(R.id.gobackbutton);

        gobacktologin.setOnClickListener( v-> {
            IntentUtil.startActivity(VerifyEmailActivity.this, LoginActivity.class);
        });
    }
}