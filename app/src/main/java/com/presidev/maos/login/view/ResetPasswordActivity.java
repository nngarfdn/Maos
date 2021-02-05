package com.presidev.maos.login.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.presidev.maos.R;
import com.presidev.maos.login.viewmodel.AuthViewModel;

public class ResetPasswordActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;

    private EditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

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

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Masukkan email yang valid");
            valid = false;
        }

        return valid;
    }
}