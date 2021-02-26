package com.presidev.maos.profile.user.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.presidev.maos.R;
import com.presidev.maos.about.AboutActivity;
import com.presidev.maos.catatanku.CatatanKuActivity;
import com.presidev.maos.auth.preference.AuthPreference;
import com.presidev.maos.auth.view.AuthViewModel;
import com.presidev.maos.membership.view.MembershipIntroActivity;
import com.presidev.maos.membership.view.MemberCardViewModel;
import com.presidev.maos.profile.user.model.User;
import com.presidev.maos.welcome.SplashActivity;

import static com.presidev.maos.utils.AppUtils.loadProfilePicFromUrl;
import static com.presidev.maos.utils.Constants.EXTRA_USER;

public class UserProfileFragment extends Fragment implements View.OnClickListener {
    private AuthViewModel authViewModel;
    private MemberCardAdapter adapter;
    private User user;

    private ImageView imgPhoto;
    private TextView tvName, tvEmail;

    public UserProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AuthPreference authPreference = new AuthPreference(getContext());

        RecyclerView rvMemberCard = view.findViewById(R.id.rv_member_card_up);
        rvMemberCard.setHasFixedSize(true);
        rvMemberCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new MemberCardAdapter(this);
        rvMemberCard.setAdapter(adapter);

        Button btnCatatanku = view.findViewById(R.id.btn_catatanku_up);
        Button btnUpdate = view.findViewById(R.id.btn_update_up);
        Button btnSubscribe = view.findViewById(R.id.btn_subscribe_up);
        Button btnResetPassword = view.findViewById(R.id.btn_reset_password_up);
        Button btnAbout = view.findViewById(R.id.btn_about_up);
        Button btnLogout = view.findViewById(R.id.btn_logout_up);
        btnCatatanku.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnSubscribe.setOnClickListener(this);
        btnResetPassword.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        imgPhoto = view.findViewById(R.id.img_photo_up);
        tvName = view.findViewById(R.id.tv_name_up);
        tvEmail = view.findViewById(R.id.tv_email_up);

        MemberCardViewModel memberCardViewModel = new ViewModelProvider(this).get(MemberCardViewModel.class);
        memberCardViewModel.getMemberCardListLiveData().observe(getViewLifecycleOwner(), result -> adapter.setData(result));

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
            loadProfilePicFromUrl(imgPhoto, user.getPhoto());
            tvName.setText(user.getName());
            tvEmail.setText(user.getEmail());
            memberCardViewModel.queryByUserId(user.getId());
        });
        userViewModel.query(authPreference.getId());
        userViewModel.addSnapshotListener(authPreference.getId());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_catatanku_up:
                Intent intentCatatanKu = new Intent(getContext(), CatatanKuActivity.class);
                startActivity(intentCatatanKu);
                break;

            case R.id.btn_update_up:
                if (user == null) return;
                Intent intent = new Intent(getContext(), UpdateUserProfileActivity.class);
                intent.putExtra(EXTRA_USER, user);
                startActivity(intent);
                break;

            case R.id.btn_subscribe_up:
                Intent intentSubscribe = new Intent(getContext(), MembershipIntroActivity.class);
                startActivity(intentSubscribe);
                break;

            case R.id.btn_reset_password_up:
                if (user == null) return;
                new AlertDialog.Builder(getContext())
                        .setTitle("Ganti kata sandi")
                        .setMessage("Kirim tautan ganti kata sandi ke email-mu?")
                        .setNegativeButton("Tidak", null)
                        .setPositiveButton("Ya", (dialogInterface, i) ->
                                authViewModel.sendPasswordReset(user.getEmail())).create().show();
                break;

            case R.id.btn_about_up:
                Intent intentAbout = new Intent(getContext(), AboutActivity.class);
                startActivity(intentAbout);
                break;

            case R.id.btn_logout_up:
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