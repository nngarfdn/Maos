package com.presidev.maos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.presidev.maos.login.view.MitraRegistrationActivity;
import com.presidev.maos.login.viewmodel.AuthViewModel;
import com.presidev.maos.subscribe.view.AddMemberCardActivity;
import com.presidev.maos.welcome.view.SplashActivity;

import static com.presidev.maos.utils.AppUtils.showToast;

public class DeveloperActivity extends AppCompatActivity implements View.OnClickListener {
    private AuthViewModel authViewModel;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Button btnRegisterMitra = findViewById(R.id.btn_register_mitra_dev);
        Button btnAddMemberCard = findViewById(R.id.btn_add_member_card_dev);
        Button btnLogout = findViewById(R.id.btn_logout_dev);
        btnRegisterMitra.setOnClickListener(this);
        btnAddMemberCard.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        authViewModel.getUserLiveData().observe(this, account -> {
            this.firebaseUser = account.getFirebaseUser();
            if (firebaseUser == null) {
                showToast(this, "Berhasil keluar akun");
                Intent intent1 = new Intent(this, SplashActivity.class);
                startActivity(intent1);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register_mitra_dev:
                if (firebaseUser == null){
                    Intent intent = new Intent(this, MitraRegistrationActivity.class);
                    startActivity(intent);
                } else showToast(this, "Kamu sudah masuk");
                break;

            case R.id.btn_add_member_card_dev:
                Intent intent = new Intent(this, AddMemberCardActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_logout_dev:
                authViewModel.logout();
                Intent i = new Intent(this, SplashActivity.class);
                startActivity(i);
                authViewModel.getUserLiveData().observe(this, account -> {
                    this.firebaseUser = account.getFirebaseUser();
                    if (firebaseUser == null) {
                        showToast(this, "Berhasil keluar akun");
                        Intent intent1 = new Intent(this, SplashActivity.class);
                        startActivity(intent1);
                    }
                });


                break;
        }
    }
}