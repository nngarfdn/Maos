package com.presidev.maos.catatanku.quotes.view;

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
import com.presidev.maos.databinding.FragmentQuotesBinding;

import org.jetbrains.annotations.NotNull;

public class QuotesFragment extends Fragment {
    private FragmentQuotesBinding binding;

    public QuotesFragment() {}

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQuotesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        binding.rvQuotes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvQuotes.setHasFixedSize(true);
        QuoteAdapter adapter = new QuoteAdapter();
        binding.rvQuotes.setAdapter(adapter);

        QuoteViewModel quoteViewModel = new ViewModelProvider(this).get(QuoteViewModel.class);
        quoteViewModel.getQuoteListLiveData().observe(getViewLifecycleOwner(), adapter::setData);
        quoteViewModel.query(firebaseUser.getUid());
        quoteViewModel.addSnapshotListener(firebaseUser.getUid());

        binding.fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddQuoteActivity.class);
            startActivity(intent);
        });
    }
}