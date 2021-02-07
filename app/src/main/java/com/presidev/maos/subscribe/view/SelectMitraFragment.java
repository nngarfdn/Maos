package com.presidev.maos.subscribe.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.presidev.maos.R;
import com.presidev.maos.dashboard.view.DashboardMitraAdapter;
import com.presidev.maos.profile.mitra.Mitra;
import com.presidev.maos.search.model.MitraFilter;
import com.presidev.maos.search.viewmodel.SearchViewModel;

public class SelectMitraFragment extends BottomSheetDialogFragment {
    private DashboardMitraAdapter adapter;
    private SelectMitraListener listener;

    public SelectMitraFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_mitra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvMitra = view.findViewById(R.id.rv_mitra_sm);
        rvMitra.setHasFixedSize(true);
        rvMitra.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.query(new MitraFilter());
        searchViewModel.getMitraLiveData().observe(this, result -> {
            adapter = new DashboardMitraAdapter(result);
            rvMitra.setAdapter(adapter);
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SelectMitraFragment.SelectMitraListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement " + SelectMitraFragment.SelectMitraListener.class.getSimpleName());
        }
    }

    public interface SelectMitraListener{
        void receiveData(Mitra mitra);
    }
}