package com.presidev.maos.login.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.presidev.maos.R;

import static com.presidev.maos.utils.AppUtils.showToast;

public class ResetPasswordActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private FirebaseAuth firebaseAuth;
    private EditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        firebaseAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edt_email_rp);
        Button btnSend = findViewById(R.id.btn_email_rp);

        btnSend.setOnClickListener(view -> sendResetPassword(edtEmail.getText().toString()));
    }

    private void sendResetPassword(String email){
        if (!validateForm(email)) return;

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.d(TAG, "Email sent.");
                        showToast(getApplicationContext(), "Permintaan setel ulang kata sandi telah dikirim ke email.");
                        finish();
                    } else {
                        Log.d(TAG, "Email sent failed.");
                        showToast(getApplicationContext(), "Email belum terdaftar.");
                    }
                });
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