package com.presidev.maos.profile.mitra;

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
import com.presidev.maos.welcome.view.SplashActivity;

import static com.presidev.maos.utils.AppUtils.loadBlurImageFromUrl;
import static com.presidev.maos.utils.AppUtils.loadProfilePicFromUrl;
import static com.presidev.maos.utils.Constants.EXTRA_MITRA;

public class MitraProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private AuthViewModel authViewModel;
    private Mitra mitra;

    private ImageView imgLogo, imgBanner;
    private TextView tvName, tvEmail, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mitra_profile);

        Button btnUpdate = findViewById(R.id.btn_update_mp);
        Button btnSubscribe = findViewById(R.id.btn_subscriber_mp);
        Button btnResetPassword = findViewById(R.id.btn_reset_password_mp);
        Button btnAbout = findViewById(R.id.btn_about_mp);
        Button btnLogout = findViewById(R.id.btn_logout_mp);
        btnUpdate.setOnClickListener(this);
        btnSubscribe.setOnClickListener(this);
        btnResetPassword.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        imgLogo = findViewById(R.id.img_logo_mp);
        imgBanner = findViewById(R.id.img_banner_mp);
        tvName = findViewById(R.id.tv_name_mp);
        tvEmail = findViewById(R.id.tv_email_mp);
        tvAddress = findViewById(R.id.tv_address_mp);

        MitraViewModel mitraViewModel = new ViewModelProvider(this).get(MitraViewModel.class);
        mitraViewModel.getMitraLiveData().observe(this, mitra -> {
            this.mitra = mitra;
            loadProfilePicFromUrl(imgLogo, mitra.getLogo());
            loadBlurImageFromUrl(this, imgBanner, mitra.getBanner());
            tvName.setText(mitra.getName());
            tvEmail.setText(mitra.getEmail());
            tvAddress.setText(mitra.getAddress());
        });
        mitraViewModel.query("rSAszlHMDjgarYuvZgbQmzZnrM52");

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update_mp:
                if (mitra == null) return;
                Intent intent = new Intent(this, UpdateMitraProfileActivity.class);
                intent.putExtra(EXTRA_MITRA, mitra);
                startActivity(intent);
                break;

            case R.id.btn_subscriber_mp:
                break;

            case R.id.btn_reset_password_mp:
                new AlertDialog.Builder(this)
                        .setTitle("Ganti kata sandi")
                        .setMessage("Kirim tautan ganti kata sandi ke email Anda?")
                        .setNegativeButton("Tidak", null)
                        .setPositiveButton("Ya", (dialogInterface, i) ->
                                authViewModel.sendPasswordReset(mitra.getEmail())).create().show();
                break;

            case R.id.btn_about_mp:
                break;

            case R.id.btn_logout_mp:
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