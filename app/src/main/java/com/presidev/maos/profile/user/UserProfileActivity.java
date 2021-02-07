package com.presidev.maos.profile.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.presidev.maos.R;
import com.presidev.maos.login.viewmodel.AuthViewModel;
import com.presidev.maos.subscribe.view.MembershipIntroActivity;
import com.presidev.maos.welcome.view.SplashActivity;

import static com.presidev.maos.utils.AppUtils.loadProfilePicFromUrl;
import static com.presidev.maos.utils.Constants.EXTRA_USER;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private AuthViewModel authViewModel;
    private User user;

    private ImageView imgPhoto;
    private TextView tvName, tvEmail, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Button btnUpdate = findViewById(R.id.btn_update_up);
        Button btnSubscribe = findViewById(R.id.btn_subscribe_up);
        Button btnResetPassword = findViewById(R.id.btn_reset_password_up);
        Button btnAbout = findViewById(R.id.btn_about_up);
        Button btnLogout = findViewById(R.id.btn_logout_up);
        btnUpdate.setOnClickListener(this);
        btnSubscribe.setOnClickListener(this);
        btnResetPassword.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        imgPhoto = findViewById(R.id.img_photo_up);
        tvName = findViewById(R.id.tv_name_up);
        tvEmail = findViewById(R.id.tv_email_up);
        tvAddress = findViewById(R.id.tv_address_up);

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserLiveData().observe(this, user -> {
            this.user = user;
            loadProfilePicFromUrl(imgPhoto, user.getPhoto());
            tvName.setText(user.getName());
            tvEmail.setText(user.getEmail());
            tvAddress.setText(user.getAddress());
        });
        userViewModel.query("C6EOiwB3MqfMYRDECIvgQMYK6SF2");

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update_up:
                if (user == null) return;
                Intent intent = new Intent(this, UpdateUserProfileActivity.class);
                intent.putExtra(EXTRA_USER, user);
                startActivity(intent);
                break;

            case R.id.btn_subscribe_up:
                Intent intentSubscribe = new Intent(this, MembershipIntroActivity.class);
                startActivity(intentSubscribe);
                break;

            case R.id.btn_reset_password_up:
                new AlertDialog.Builder(this)
                        .setTitle("Ganti kata sandi")
                        .setMessage("Kirim tautan ganti kata sandi ke email Anda?")
                        .setNegativeButton("Tidak", null)
                        .setPositiveButton("Ya", (dialogInterface, i) -> {
                            authViewModel.sendPasswordReset(user.getEmail());
                        }).create().show();
                break;

            case R.id.btn_about_up:
                break;

            case R.id.btn_logout_up:
                authViewModel.getUserLiveData().observe(this, firebaseUser -> {
                    if (firebaseUser == null){
                        Intent intentRestart = new Intent(this, SplashActivity.class);
                        startActivity(intentRestart);
                        finish();
                    }
                });
                authViewModel.logout();
                break;
        }
    }
}