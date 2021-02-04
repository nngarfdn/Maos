package com.presidev.maos.login.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.presidev.maos.MainActivity;
import com.presidev.maos.R;
import com.presidev.maos.customview.LoadingDialog;

import java.util.regex.Pattern;

import static com.presidev.maos.utils.AppUtils.showToast;

public class RegisterActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private FirebaseAuth firebaseAuth;
    private LoadingDialog loadingDialog;

    private EditText edtName, edtEmail, edtPassword, edtPasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initalize
        loadingDialog = new LoadingDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize view
        edtName = findViewById(R.id.edt_name_register);
        edtEmail = findViewById(R.id.edt_email_register);
        edtPassword = findViewById(R.id.edt_password_register);
        edtPasswordConfirmation = findViewById(R.id.edt_password_confirmation_register);

        Button btnRegister = findViewById(R.id.btn_email_register);
        btnRegister.setOnClickListener(view -> registerWithEmail(edtName.getText().toString(),
                edtEmail.getText().toString(), edtPassword.getText().toString(), edtPasswordConfirmation.getText().toString()));
    }

    private void registerWithEmail(String name, String email, String password, String passwordConfirmation){
        if (!validateForm(name, email, password, passwordConfirmation)) return;
        Log.d(TAG, "createAccount: " + email);

        loadingDialog.show();

        // Start create user with email
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        // Profile updates
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        firebaseUser.updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> Log.d(TAG, "User profile updated."));

                        Log.d(TAG, "createUserWithEmail: success");
                        launchMain();
                    } else {
                        // If sign in fails, display a message to the user
                        Log.w(TAG, "createUserWithEmail: failure", task.getException());
                        showToast(getApplicationContext(), "Email sudah terdaftar.");
                    }
                    loadingDialog.dismiss();
                });
    }

    private boolean validateForm(String name, String email, String password, String konfirm){
        boolean valid = true;

        if (TextUtils.isEmpty(name)){
            edtName.setError("Masukkan nama lengkap");
            valid = false;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Masukkan email yang valid");
            valid = false;
        }

        if (TextUtils.isEmpty(password) || !Pattern.compile("^(?=.*\\d).{8,}$").matcher(password).matches()) {
            edtPassword.setError("Masukkan kata sandi minimal 8 karakter dengan kombinasi huruf dan angka");
            valid = false;
        }

        if (!password.equals(konfirm)) {
            edtPasswordConfirmation.setError("Konfirmasi kata sandi tidak sama");
            valid = false;
        }

        return valid;
    }

    private void launchMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  // Clear all previous activities
        startActivity(intent);
    }
}