package com.presidev.maos.login.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.presidev.maos.MainActivity;
import com.presidev.maos.R;
import com.presidev.maos.customview.LoadingDialog;
import com.presidev.maos.login.viewmodel.AuthViewModel;
import com.presidev.maos.profile.mitra.Mitra;
import com.presidev.maos.profile.mitra.MitraViewModel;
import com.presidev.maos.profile.user.User;
import com.presidev.maos.profile.user.UserViewModel;

import java.util.regex.Pattern;

import static com.presidev.maos.utils.Constants.EXTRA_LEVEL;
import static com.presidev.maos.utils.Constants.LEVEL_MITRA;
import static com.presidev.maos.utils.Constants.LEVEL_USER;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    private AuthViewModel authViewModel;
    private LoadingDialog loadingDialog;

    private EditText edtName, edtEmail, edtPassword, edtPasswordConfirmation;

    private String userLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadingDialog = new LoadingDialog(this, true);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Initialize view
        edtName = findViewById(R.id.edt_name_register);
        edtEmail = findViewById(R.id.edt_email_register);
        edtPassword = findViewById(R.id.edt_password_register);
        edtPasswordConfirmation = findViewById(R.id.edt_password_confirmation_register);

        Button btnEmail = findViewById(R.id.btn_email_register);
        Button btnGoogle = findViewById(R.id.btn_google_register);

        btnEmail.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_LEVEL)){
            userLevel = intent.getStringExtra(EXTRA_LEVEL);
            Log.d(getClass().getSimpleName(), "Account level: " + userLevel);
        }
        Log.d(getClass().getSimpleName(), "Account level: " + userLevel);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_email_register:
                registerWithEmail(edtName.getText().toString(),
                        edtEmail.getText().toString(),
                        edtPassword.getText().toString(),
                        edtPasswordConfirmation.getText().toString());
                break;

            case R.id.btn_google_register:
                break;
        }
    }

    private void registerWithEmail(String name, String email, String password, String passwordConfirmation){
        if (!validateForm(name, email, password, passwordConfirmation)) return;
        Log.d(TAG, "createAccount: " + email);

        loadingDialog.show();
        authViewModel.registerWithEmail(name, email, password, userLevel);
        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            // Insert to database
            if (userLevel.equals(LEVEL_MITRA)){
                Mitra mitra = new Mitra();
                mitra.setId(firebaseUser.getUid());
                mitra.setName(name);;
                mitra.setEmail(firebaseUser.getEmail());
                mitra.setLogo(firebaseUser.getPhotoUrl().toString());
                MitraViewModel mitraViewModel = new ViewModelProvider(this).get(MitraViewModel.class);
                mitraViewModel.insert(mitra);
            } else if (userLevel.equals(LEVEL_USER)){
                User user = new User();
                user.setId(firebaseUser.getUid());
                user.setName(name);
                user.setEmail(firebaseUser.getEmail());
                user.setPhoto(firebaseUser.getPhotoUrl().toString());
                UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
                userViewModel.insert(user);
            }

            loadingDialog.dismiss();
            launchMain();
        });
    }

    private void launchMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
}