package com.presidev.maos.search.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.presidev.maos.R;
import com.presidev.maos.dashboard.view.DashboardMitraAdapter;
import com.presidev.maos.search.model.MitraFilter;
import com.presidev.maos.search.viewmodel.SearchViewModel;
import com.presidev.maos.utils.ShimmerHelper;

import static com.presidev.maos.utils.Constants.EXTRA_MITRA_FILTER;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, MitraFilterFragment.MitraFilterListener {
    private MitraFilter filter;
    private RecyclerView recyclerView;
    private SearchViewModel searchViewModel;
    private ShimmerHelper shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.rv_mitra_search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ImageView imgEmpty = findViewById(R.id.img_empty_search);
        imgEmpty.setVisibility(View.GONE);

        shimmer = new ShimmerHelper(findViewById(R.id.shimmer_mitra_search), recyclerView);
        shimmer.show();

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.getMitraLiveData().observe(this, mitraList -> {
            DashboardMitraAdapter adapter = new DashboardMitraAdapter(mitraList);
            recyclerView.setAdapter(adapter);

            if (mitraList.isEmpty()) imgEmpty.setVisibility(View.VISIBLE);
            else imgEmpty.setVisibility(View.GONE);

            shimmer.hide();
        });

        filter = new MitraFilter();
        searchViewModel.query(filter);

        SearchView searchView = findViewById(R.id.sv_mitra_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) shimmer.show();
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

        ImageButton ibMitraFilter = findViewById(R.id.ib_mitra_filter_search);
        ibMitraFilter.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ib_mitra_filter_search) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_MITRA_FILTER, filter);
            MitraFilterFragment bottomSheet = new MitraFilterFragment();
            bottomSheet.setArguments(bundle);
            bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
        }
    }

    @Override
    public void receiveData(MitraFilter filter) {
        shimmer.show();
        this.filter = filter;
        searchViewModel.query(filter);
    }
}