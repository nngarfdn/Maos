package com.presidev.maos.search.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.presidev.maos.R;
import com.presidev.maos.dashboard.view.DashboardMitraAdapter;
import com.presidev.maos.auth.preference.AuthPreference;
import com.presidev.maos.profile.mitra.view.MitraViewModel;
import com.presidev.maos.profile.user.view.UserViewModel;
import com.presidev.maos.search.model.MitraFilter;
import com.presidev.maos.utils.ShimmerHelper;

import static com.presidev.maos.utils.Constants.EXTRA_MITRA_FILTER;
import static com.presidev.maos.utils.Constants.LEVEL_MITRA;
import static com.presidev.maos.utils.Constants.LEVEL_USER;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, MitraFilterFragment.MitraFilterListener {
    private MitraFilter filter;
    private RecyclerView recyclerView;
    private SearchViewModel searchViewModel;
    private ShimmerHelper shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SwipeRefreshLayout swipeMitra = findViewById(R.id.swipe_mitra_search);
        recyclerView = findViewById(R.id.rv_mitra_search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ImageView imgEmpty = findViewById(R.id.img_empty_search);
        shimmer = new ShimmerHelper(findViewById(R.id.shimmer_mitra_search), recyclerView, imgEmpty);
        shimmer.show();

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.getMitraLiveData().observe(this, mitraList -> {
            DashboardMitraAdapter adapter = new DashboardMitraAdapter(mitraList);
            recyclerView.setAdapter(adapter);
            if (mitraList.isEmpty()) imgEmpty.setVisibility(View.VISIBLE);
            else imgEmpty.setVisibility(View.GONE);
            shimmer.hide();
            swipeMitra.setRefreshing(false);
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

        swipeMitra.setOnRefreshListener(() -> searchViewModel.query(filter));

        // Atur nilai default lokasi filter sama dengan lokasi pengguna
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserLiveData().observe(this, user -> {
            filter.setDistrict(user.getDistrict());
            filter.setRegency(user.getRegency());
            filter.setProvince(user.getProvince());
        });
        MitraViewModel mitraViewModel = new ViewModelProvider(this).get(MitraViewModel.class);
        mitraViewModel.getMitraLiveData().observe(this, mitra -> {
            filter.setDistrict(mitra.getDistrict());
            filter.setRegency(mitra.getRegency());
            filter.setProvince(mitra.getProvince());
        });
        AuthPreference authPreference = new AuthPreference(this);
        if (authPreference.getId() != null) {
            if (authPreference.getLevel().equals(LEVEL_USER)) userViewModel.query(authPreference.getId());
            else if (authPreference.getLevel().equals(LEVEL_MITRA)) mitraViewModel.query(authPreference.getId());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ib_mitra_filter_search) {
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