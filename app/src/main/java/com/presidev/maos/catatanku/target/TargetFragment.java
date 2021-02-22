package com.presidev.maos.catatanku.target;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.presidev.maos.databinding.FragmentTargetBinding;

import org.jetbrains.annotations.NotNull;

public class TargetFragment extends Fragment {

    private FragmentTargetBinding binding;

    public TargetFragment() {}

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTargetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        binding.rvTarget.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTarget.setHasFixedSize(true);
        TargetAdapter adapter = new TargetAdapter();
        binding.rvTarget.setAdapter(adapter);

        TargetViewModel targetViewModel = new ViewModelProvider(this).get(TargetViewModel.class);
        targetViewModel.getTargetListLiveData().observe(getViewLifecycleOwner(), targetList -> {
            adapter.setData(targetList);
        });
        targetViewModel.query(firebaseUser.getUid());
        targetViewModel.addSnapshotListener(firebaseUser.getUid());

        binding.fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddUpdateTargetActivity.class);
            startActivity(intent);
        });
    }
}