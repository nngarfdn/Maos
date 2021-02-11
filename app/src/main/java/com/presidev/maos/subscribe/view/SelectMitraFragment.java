package com.presidev.maos.subscribe.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.presidev.maos.R;
import com.presidev.maos.customview.LoadingDialog;
import com.presidev.maos.profile.mitra.Mitra;
import com.presidev.maos.search.model.MitraFilter;
import com.presidev.maos.search.viewmodel.SearchViewModel;
import com.presidev.maos.subscribe.adapter.SelectMitraAdapter;

public class SelectMitraFragment extends BottomSheetDialogFragment {
    private SelectMitraAdapter adapter;
    private SelectMitraListener listener;

    public SelectMitraFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_mitra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoadingDialog loadingDialog = new LoadingDialog(getActivity(), true);

        RecyclerView recyclerView = view.findViewById(R.id.rv_mitra_sm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new SelectMitraAdapter(this, listener);
        recyclerView.setAdapter(adapter);

        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.getMitraLiveData().observe(this, result -> {
            adapter.setData(result);
            loadingDialog.dismiss();
        });

        MitraFilter filter = new MitraFilter();
        searchViewModel.query(filter);

        SearchView searchView = view.findViewById(R.id.sv_mitra_sm);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) loadingDialog.show();
                filter.setKeyword(query);
                searchViewModel.query(filter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) onQueryTextSubmit("");
                return false;
            }
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