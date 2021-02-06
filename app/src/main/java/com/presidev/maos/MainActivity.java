package com.presidev.maos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.presidev.maos.login.preference.UserPreference;
import com.presidev.maos.login.view.LoginActivity;
import com.presidev.maos.login.view.RegisterActivity;
import com.presidev.maos.login.viewmodel.AuthViewModel;
import com.presidev.maos.mitramanagement.view.KatalogMitraActivity;
import com.presidev.maos.search.view.SearchActivity;
import com.presidev.maos.utils.Constants;

import static com.presidev.maos.utils.AppUtils.showToast;
import static com.presidev.maos.utils.Constants.LEVEL_MITRA;

public class MainActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Commit", Toast.LENGTH_SHORT).show();

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        Button btnKatalogMitra = findViewById(R.id.btn_katalog_mitra);
        btnKatalogMitra.setOnClickListener(v -> {
            Intent intent = new Intent(this, KatalogMitraActivity.class);
            startActivity(intent);
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Button btnLogin = findViewById(R.id.btn_login_main);
        btnLogin.setOnClickListener(v -> {
            if (firebaseUser == null){
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else showToast(this, "Kamu sudah masuk");
        });

        Button btnRegisterMitra = findViewById(R.id.btn_register_mitra_main);
        btnRegisterMitra.setOnClickListener(v -> {
            if (firebaseUser == null){
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra(Constants.EXTRA_LEVEL, LEVEL_MITRA);
                startActivity(intent);
            } else showToast(this, "Kamu sudah masuk");
        });

        Button btnLogout = findViewById(R.id.btn_logout_main);
        btnLogout.setOnClickListener(view -> {
            authViewModel.logout();
            authViewModel.getUserLiveData().observe(this, firebaseUser -> {
                this.firebaseUser = firebaseUser;
                if (firebaseUser == null) showToast(this, "Berhasil keluar akun");
            });
        });

        Button btnLevel = findViewById(R.id.btn_level_main);
        btnLevel.setOnClickListener(view -> {
            UserPreference userPreference = new UserPreference(this);
            showToast(this, userPreference.getEmail() + ": " + userPreference.getLevel());
        });

        Button btnSearch = findViewById(R.id.btn_search_main);
        btnSearch.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });

    }
}