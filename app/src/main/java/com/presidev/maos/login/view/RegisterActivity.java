package com.presidev.maos.login.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.presidev.maos.App;
import com.presidev.maos.R;
import com.presidev.maos.bookdetail.BookDetailActivity;
import com.presidev.maos.borrowbook.PeminjamanActivity;
import com.presidev.maos.customview.LoadingDialog;
import com.presidev.maos.login.viewmodel.AuthViewModel;
import com.presidev.maos.mitramanagement.model.Book;
import com.presidev.maos.profile.user.User;
import com.presidev.maos.profile.user.UserViewModel;

import java.util.regex.Pattern;

import static com.presidev.maos.bookdetail.BookDetailActivity.EXTRA_BOOK;
import static com.presidev.maos.utils.AppUtils.hideStatusBar;
import static com.presidev.maos.utils.Constants.EXTRA_LEVEL;
import static com.presidev.maos.utils.Constants.LEVEL_USER;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    private AuthViewModel authViewModel;
    private Book book;
    private LoadingDialog loadingDialog;

    private EditText edtName, edtEmail, edtPassword, edtPasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(this, getSupportActionBar());
        setContentView(R.layout.activity_register);

        loadingDialog = new LoadingDialog(this, true);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Initialize view
        edtName = findViewById(R.id.edt_name_register);
        edtEmail = findViewById(R.id.edt_email_register);
        edtPassword = findViewById(R.id.edt_password_register);
        edtPasswordConfirmation = findViewById(R.id.edt_password_confirmation_register);

        Button btnEmail = findViewById(R.id.btn_email_register);
        TextView tvLogin = findViewById(R.id.tv_login_register);
        btnEmail.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_BOOK)) book = getIntent().getParcelableExtra(BookDetailActivity.EXTRA_BOOK);
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

            case R.id.tv_login_register:
                onBackPressed();
                break;
        }
    }

    private void registerWithEmail(String name, String email, String password, String passwordConfirmation){
        if (!validateForm(name, email, password, passwordConfirmation)) return;
        Log.d(TAG, "createAccount: " + email);

        loadingDialog.show();
        authViewModel.registerWithEmail(name, email, password, LEVEL_USER);
        authViewModel.getUserLiveData().observe(this, account -> {
            User user = new User();
            user.setId(account.getId());
            user.setName(name);
            user.setEmail(account.getEmail());
            UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
            userViewModel.insert(user);

            loadingDialog.dismiss();
            launchMain();
        });
    }

    private void launchMain(){
        Intent intent;
        if (book != null) {
            intent = new Intent(this, PeminjamanActivity.class);
            intent.putExtra(EXTRA_BOOK, book);
        } else {
            intent = new Intent(this, App.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(EXTRA_LEVEL, LEVEL_USER);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(EXTRA_BOOK, book);
        startActivity(intent);
        finish();
    }

    private boolean validateForm(String name, String email, String password, String konfirm){
        boolean valid = true;

        if (name.isEmpty()){
            edtName.setError("Masukkan nama lengkap");
            valid = false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
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
}