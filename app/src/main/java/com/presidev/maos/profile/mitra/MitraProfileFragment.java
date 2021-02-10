package com.presidev.maos.profile.mitra;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.presidev.maos.R;
import com.presidev.maos.login.preference.AccountPreference;
import com.presidev.maos.login.viewmodel.AuthViewModel;
import com.presidev.maos.welcome.view.SplashActivity;

import static com.presidev.maos.utils.AppUtils.loadProfilePicFromUrl;
import static com.presidev.maos.utils.Constants.EXTRA_MITRA;

public class MitraProfileFragment extends Fragment implements View.OnClickListener {
    private AuthViewModel authViewModel;
    private Mitra mitra;

    private ImageView imgLogo;
    private TextView tvName, tvEmail, tvWhatsApp;

    public MitraProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mitra_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AccountPreference accountPreference = new AccountPreference(getContext());

        Button btnUpdate = view.findViewById(R.id.btn_update_mp);
        Button btnSubscribe = view.findViewById(R.id.btn_subscriber_mp);
        Button btnResetPassword = view.findViewById(R.id.btn_reset_password_mp);
        Button btnAbout = view.findViewById(R.id.btn_about_mp);
        Button btnLogout = view.findViewById(R.id.btn_logout_mp);
        btnUpdate.setOnClickListener(this);
        btnSubscribe.setOnClickListener(this);
        btnResetPassword.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        imgLogo = view.findViewById(R.id.img_logo_mp);
        tvName = view.findViewById(R.id.tv_name_mp);
        tvEmail = view.findViewById(R.id.tv_email_mp);
        tvWhatsApp = view.findViewById(R.id.tv_whatsapp_mp);

        MitraViewModel mitraViewModel = new ViewModelProvider(this).get(MitraViewModel.class);
        mitraViewModel.getMitraLiveData().observe(getViewLifecycleOwner(), mitra -> {
            this.mitra = mitra;
            loadProfilePicFromUrl(imgLogo, mitra.getLogo());
            tvName.setText(mitra.getName());
            tvEmail.setText(mitra.getEmail());
            tvWhatsApp.setText(mitra.getWhatsApp());
        });
        mitraViewModel.query(accountPreference.getId());
        mitraViewModel.addSnapshotListener(accountPreference.getId());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update_mp:
                if (mitra == null) return;
                Intent intent = new Intent(getContext(), UpdateMitraProfileActivity.class);
                intent.putExtra(EXTRA_MITRA, mitra);
                startActivity(intent);
                break;

            case R.id.btn_subscriber_mp:
                Intent intentMemberList = new Intent(getContext(), MemberListActivity.class);
                startActivity(intentMemberList);
                break;

            case R.id.btn_reset_password_mp:
                new AlertDialog.Builder(getContext())
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
                        Intent intentRestart = new Intent(getContext(), SplashActivity.class);
                        startActivity(intentRestart);
                        getActivity().finish();
                    }
                });
                new AlertDialog.Builder(getActivity())
                        .setTitle("Keluar akun")
                        .setMessage("Apakah kamu yakin ingin keluar?")
                        .setNegativeButton("Tidak", null)
                        .setPositiveButton("Ya", (dialogInterface, i) -> authViewModel.logout())
                        .create().show();
                break;
        }
    }
}