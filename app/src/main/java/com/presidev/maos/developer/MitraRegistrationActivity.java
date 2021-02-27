package com.presidev.maos.developer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.presidev.maos.R;
import com.presidev.maos.customview.LoadingDialog;
import com.presidev.maos.auth.view.AuthViewModel;
import com.presidev.maos.profile.mitra.model.Mitra;
import com.presidev.maos.profile.mitra.view.MitraViewModel;

import java.util.Objects;
import java.util.regex.Pattern;

import static com.presidev.maos.utils.AppUtils.showToast;
import static com.presidev.maos.utils.Constants.LEVEL_MITRA;

public class MitraRegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    private AuthViewModel authViewModel;
    private LoadingDialog loadingDialog;

    private EditText edtName, edtEmail, edtPassword, edtPasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mitra_registration);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        loadingDialog = new LoadingDialog(this, true);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Initialize view
        edtName = findViewById(R.id.edt_name_register);
        edtEmail = findViewById(R.id.edt_email_register);
        edtPassword = findViewById(R.id.edt_password_register);
        edtPasswordConfirmation = findViewById(R.id.edt_password_confirmation_register);

        Button btnEmail = findViewById(R.id.btn_email_register);
        btnEmail.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_email_register) {
            registerWithEmail(edtName.getText().toString(),
                    edtEmail.getText().toString(),
                    edtPassword.getText().toString(),
                    edtPasswordConfirmation.getText().toString());
        }
    }

    private void registerWithEmail(String name, String email, String password, String passwordConfirmation) {
        if (!validateForm(name, email, password, passwordConfirmation)) return;
        Log.d(TAG, "createAccount: " + email);

        loadingDialog.show();
        authViewModel.registerWithEmail(name, email, password, LEVEL_MITRA);
        authViewModel.getUserLiveData().observe(this, account -> {
            Mitra mitra = new Mitra();
            mitra.setId(account.getId());
            mitra.setName(name);
            mitra.setEmail(account.getEmail());
            mitra.setMembership(true);
            MitraViewModel mitraViewModel = new ViewModelProvider(this).get(MitraViewModel.class);
            mitraViewModel.insert(mitra);

            loadingDialog.dismiss();
            showToast(this, "Akun mitra berhasil dibuat");
        });
    }

    private boolean validateForm(String name, String email, String password, String konfirm) {
        boolean valid = true;

        if (name.isEmpty()) {
            edtName.setError("Masukkan nama lengkap");
            valid = false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Masukkan email yang valid");
            valid = false;
        }

        if (password.isEmpty() || !Pattern.compile("^(?=.*\\d).{8,}$").matcher(password).matches()) {
            edtPassword.setError("Masukkan kata sandi minimal 8 karakter dengan kombinasi huruf dan angka");
            valid = false;
        }

        if (!password.equals(konfirm)) {
            edtPasswordConfirmation.setError("Konfirmasi kata sandi tidak sama");
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