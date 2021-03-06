package com.presidev.maos.auth.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.presidev.maos.R;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;

    private EditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        edtEmail = findViewById(R.id.edt_email_rp);
        Button btnSend = findViewById(R.id.btn_email_rp);

        btnSend.setOnClickListener(view -> sendResetPassword(edtEmail.getText().toString()));
    }

    private void sendResetPassword(String email){
        if (!validateForm(email)) return;
        authViewModel.sendPasswordReset(email);
    }

    private boolean validateForm(String email){
        boolean valid = true;

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Masukkan email yang valid");
            valid = false;
        }

        return valid;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}