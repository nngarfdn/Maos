package com.presidev.maos.search.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.presidev.maos.R;
import com.presidev.maos.customview.LoadingDialog;
import com.presidev.maos.dashboard.view.DashboardMitraAdapter;
import com.presidev.maos.search.model.MitraFilter;
import com.presidev.maos.search.viewmodel.SearchViewModel;

import static com.presidev.maos.utils.Constants.EXTRA_MITRA_FILTER;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, MitraFilterFragment.MitraFilterListener {
    private LoadingDialog loadingDialog;
    private MitraFilter mitraFilter;
    private SearchViewModel searchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        loadingDialog = new LoadingDialog(this, true);

        RecyclerView recyclerView = findViewById(R.id.rv_mitra_search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.getMitraLiveData().observe(this, mitraList -> {
            DashboardMitraAdapter adapter = new DashboardMitraAdapter(mitraList);
            recyclerView.setAdapter(adapter);
            loadingDialog.dismiss();
        });

        mitraFilter = new MitraFilter();
        searchViewModel.query(mitraFilter);

        SearchView svMitra = findViewById(R.id.sv_mitra_search);
        svMitra.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) loadingDialog.show();
                mitraFilter.setKeyword(query);
                searchViewModel.query(mitraFilter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) onQueryTextSubmit("");
                return false;
            }
        });

        ImageButton ibMitraFilter = findViewById(R.id.ib_mitra_filter_search);
        ibMitraFilter.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ib_mitra_filter_search) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_MITRA_FILTER, mitraFilter);
            MitraFilterFragment bottomSheet = new MitraFilterFragment();
            bottomSheet.setArguments(bundle);
            bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
        }
    }

    @Override
    public void receiveData(MitraFilter filter) {
        loadingDialog.show();
        mitraFilter = filter;
        searchViewModel.query(mitraFilter);
    }
}