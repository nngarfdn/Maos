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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.presidev.maos.R;
import com.presidev.maos.about.AboutActivity;
import com.presidev.maos.login.preference.AccountPreference;
import com.presidev.maos.login.viewmodel.AuthViewModel;
import com.presidev.maos.mitrabookcatalog.view.MitraBookCatalogActivity;
import com.presidev.maos.mitramanagement.view.KatalogMitraActivity;
import com.presidev.maos.welcome.view.SplashActivity;

import static com.presidev.maos.utils.AppUtils.loadProfilePicFromUrl;
import static com.presidev.maos.utils.Constants.EXTRA_MITRA;

public class MitraProfileFragment extends Fragment implements View.OnClickListener {
    private AuthViewModel authViewModel;
    private Mitra mitra;

    private ImageView imgLogo;
    private TextView tvName, tvEmail;

    public MitraProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mitra_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AccountPreference accountPreference = new AccountPreference(getContext());

        ImageButton ibView = view.findViewById(R.id.ib_view_mp);
        Button btnManage = view.findViewById(R.id.btn_manage_mp);
        Button btnUpdate = view.findViewById(R.id.btn_update_mp);
        Button btnSubscriber = view.findViewById(R.id.btn_subscriber_mp);
        Button btnResetPassword = view.findViewById(R.id.btn_reset_password_mp);
        Button btnAbout = view.findViewById(R.id.btn_about_mp);
        Button btnLogout = view.findViewById(R.id.btn_logout_mp);
        ibView.setOnClickListener(this);
        btnManage.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnSubscriber.setOnClickListener(this);
        btnResetPassword.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        imgLogo = view.findViewById(R.id.img_logo_mp);
        tvName = view.findViewById(R.id.tv_name_mp);
        tvEmail = view.findViewById(R.id.tv_email_mp);

        MitraViewModel mitraViewModel = new ViewModelProvider(this).get(MitraViewModel.class);
        mitraViewModel.getMitraLiveData().observe(getViewLifecycleOwner(), mitra -> {
            this.mitra = mitra;
            loadProfilePicFromUrl(imgLogo, mitra.getLogo());
            tvName.setText(mitra.getName());
            tvEmail.setText(mitra.getEmail());
        });
        mitraViewModel.query(accountPreference.getId());
        mitraViewModel.addSnapshotListener(accountPreference.getId());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_view_mp:
                Intent intentView = new Intent(getContext(), MitraBookCatalogActivity.class);
                intentView.putExtra(EXTRA_MITRA, mitra);
                startActivity(intentView);
                break;

            case R.id.btn_manage_mp:
                Intent intentManage = new Intent(getContext(), KatalogMitraActivity.class);
                startActivity(intentManage);
                break;

            case R.id.btn_update_mp:
                if (mitra == null) return;
                Intent intent = new Intent(getContext(), UpdateMitraProfileActivity.class);
                intent.putExtra(EXTRA_MITRA, mitra);
                startActivity(intent);
                break;

            case R.id.btn_subscriber_mp:
                Intent intentMemberList = new Intent(getContext(), MemberListActivity.class);
                intentMemberList.putExtra(EXTRA_MITRA, mitra);
                startActivity(intentMemberList);
                break;

            case R.id.btn_reset_password_mp:
                if (mitra == null) return;
                new AlertDialog.Builder(getContext())
                        .setTitle("Ganti kata sandi")
                        .setMessage("Kirim tautan ganti kata sandi ke email-mu?")
                        .setNegativeButton("Tidak", null)
                        .setPositiveButton("Ya", (dialogInterface, i) ->
                                authViewModel.sendPasswordReset(mitra.getEmail())).create().show();
                break;

            case R.id.btn_about_mp:
                Intent intentAbout = new Intent(getContext(), AboutActivity.class);
                startActivity(intentAbout);
                break;

            case R.id.btn_logout_mp:
                authViewModel.getUserLiveData().observe(this, account -> {
                    if (account.getFirebaseUser() == null){
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