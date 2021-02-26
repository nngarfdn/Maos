package com.presidev.maos.catatanku.quotes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.presidev.maos.R;
import com.presidev.maos.databinding.FragmentQuotesBinding;
import com.presidev.maos.databinding.FragmentTargetBinding;

public class QuotesFragment extends Fragment {

    private FragmentQuotesBinding binding;

    public QuotesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQuotesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fabAddQuote.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddQuoteActivity.class);
            startActivity(intent);
        });
    }
}