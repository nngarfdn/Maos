package com.presidev.maos.developer.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.presidev.maos.R;
import com.presidev.maos.auth.view.AuthViewModel;
import com.presidev.maos.welcome.SplashActivity;

public class DeveloperFragment extends Fragment implements View.OnClickListener {
    private AuthViewModel authViewModel;
    private FirebaseUser firebaseUser;

    public DeveloperFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_developer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnRegisterMitra = view.findViewById(R.id.btn_register_mitra_dev);
        Button btnAddMemberCard = view.findViewById(R.id.btn_add_member_card_dev);
        Button btnLogout = view.findViewById(R.id.btn_logout_dev);
        btnRegisterMitra.setOnClickListener(this);
        btnAddMemberCard.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_register_mitra_dev) {
            Intent intentRegisterMitra = new Intent(requireActivity(), MitraRegistrationActivity.class);
            startActivity(intentRegisterMitra);
        } else if (id == R.id.btn_add_member_card_dev) {
            Intent intent = new Intent(requireActivity(), AddMemberCardActivity.class);
            startActivity(intent);
        } else if (id == R.id.btn_logout_dev) {
            authViewModel.logout();
            authViewModel.getUserLiveData().observe(this, account -> {
                this.firebaseUser = account.getFirebaseUser();
                if (firebaseUser == null) {
                    Intent intentRestart = new Intent(getContext(), SplashActivity.class);
                    startActivity(intentRestart);
                    requireActivity().finish();
                }
            });
        }
    }
}