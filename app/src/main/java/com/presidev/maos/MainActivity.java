package com.presidev.maos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.presidev.maos.login.preference.AccountPreference;
import com.presidev.maos.login.view.LoginActivity;
import com.presidev.maos.login.viewmodel.AuthViewModel;
import com.presidev.maos.mitramanagement.view.KatalogMitraActivity;
import com.presidev.maos.profile.mitra.MitraProfileActivity;
import com.presidev.maos.profile.user.UserProfileActivity;
import com.presidev.maos.search.view.SearchActivity;

import static com.presidev.maos.utils.AppUtils.showToast;

public class MainActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Button btnDeveloper = findViewById(R.id.btn_developer_main);
        btnDeveloper.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeveloperActivity.class);
            startActivity(intent);
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
            AccountPreference accountPreference = new AccountPreference(this);
            showToast(this, accountPreference.getEmail() + ": " + accountPreference.getLevel());
        });

        Button btnSearch = findViewById(R.id.btn_search_main);
        btnSearch.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });

        Button btnUserMitra = findViewById(R.id.btn_mitra_profile_main);
        btnUserMitra.setOnClickListener(view -> {
            Intent intent = new Intent(this, MitraProfileActivity.class);
            startActivity(intent);
        });

        Button btnUserProfile = findViewById(R.id.btn_user_profile_main);
        btnUserProfile.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        });

        Button btnAppMain = findViewById(R.id.btn_app);
        btnAppMain.setOnClickListener(view -> {
            Intent intent = new Intent(this, App.class);
            startActivity(intent);
        });
    }
}