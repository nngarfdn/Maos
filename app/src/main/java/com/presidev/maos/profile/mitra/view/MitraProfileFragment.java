package com.presidev.maos.profile.mitra.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.presidev.maos.R;
import com.presidev.maos.about.AboutActivity;
import com.presidev.maos.auth.preference.AuthPreference;
import com.presidev.maos.auth.view.AuthViewModel;
import com.presidev.maos.mitrabookcatalog.view.MitraBookCatalogActivity;
import com.presidev.maos.mitramanagement.view.KatalogMitraActivity;
import com.presidev.maos.profile.mitra.model.Mitra;
import com.presidev.maos.welcome.SplashActivity;

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

        AuthPreference authPreference = new AuthPreference(requireContext());

        ImageButton ibView = view.findViewById(R.id.ib_view_mp);
        LinearLayout layoutProfile = view.findViewById(R.id.layout_profile_mp);
        Button btnManage = view.findViewById(R.id.btn_manage_mp);
        Button btnUpdate = view.findViewById(R.id.btn_update_mp);
        Button btnSubscriber = view.findViewById(R.id.btn_subscriber_mp);
        Button btnResetPassword = view.findViewById(R.id.btn_reset_password_mp);
        Button btnAbout = view.findViewById(R.id.btn_about_mp);
        Button btnLogout = view.findViewById(R.id.btn_logout_mp);
        ibView.setOnClickListener(this);
        layoutProfile.setOnClickListener(this);
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
        mitraViewModel.query(authPreference.getId());
        mitraViewModel.addSnapshotListener(authPreference.getId());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ib_view_mp || id == R.id.layout_profile_mp) {
            Intent intentView = new Intent(getContext(), MitraBookCatalogActivity.class);
            intentView.putExtra(EXTRA_MITRA, mitra);
            startActivity(intentView);
        } else if (id == R.id.btn_manage_mp) {
            Intent intentManage = new Intent(getContext(), KatalogMitraActivity.class);
            startActivity(intentManage);
        } else if (id == R.id.btn_update_mp) {
            if (mitra == null) return;
            Intent intent = new Intent(getContext(), UpdateMitraProfileActivity.class);
            intent.putExtra(EXTRA_MITRA, mitra);
            startActivity(intent);
        } else if (id == R.id.btn_subscriber_mp) {
            Intent intentMemberList = new Intent(getContext(), MemberListActivity.class);
            intentMemberList.putExtra(EXTRA_MITRA, mitra);
            startActivity(intentMemberList);
        } else if (id == R.id.btn_reset_password_mp) {
            if (mitra == null) return;
            new AlertDialog.Builder(getContext())
                    .setTitle("Ganti kata sandi")
                    .setMessage("Kirim tautan ganti kata sandi ke email-mu?")
                    .setNegativeButton("Tidak", null)
                    .setPositiveButton("Ya", (dialogInterface, i) ->
                            authViewModel.sendPasswordReset(mitra.getEmail())).create().show();
        } else if (id == R.id.btn_about_mp) {
            Intent intentAbout = new Intent(getContext(), AboutActivity.class);
            startActivity(intentAbout);
        } else if (id == R.id.btn_logout_mp) {
            authViewModel.getUserLiveData().observe(this, account -> {
                if (account.getFirebaseUser() == null) {
                    Intent intentRestart = new Intent(getContext(), SplashActivity.class);
                    startActivity(intentRestart);
                    requireActivity().finish();
                }
            });
            new AlertDialog.Builder(getActivity())
                    .setTitle("Keluar akun")
                    .setMessage("Apakah kamu yakin ingin keluar?")
                    .setNegativeButton("Tidak", null)
                    .setPositiveButton("Ya", (dialogInterface, i) -> authViewModel.logout())
                    .create().show();
        }
    }
}