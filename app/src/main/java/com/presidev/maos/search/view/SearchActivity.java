package com.presidev.maos.search.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.presidev.maos.R;
import com.presidev.maos.mitramanagement.model.Book;
import com.presidev.maos.profile.mitra.Mitra;
import com.presidev.maos.search.model.BookFilter;
import com.presidev.maos.search.model.MitraFilter;
import com.presidev.maos.search.viewmodel.SearchViewModel;

import static com.presidev.maos.utils.Constants.EXTRA_BOOK_FILTER;
import static com.presidev.maos.utils.Constants.EXTRA_MITRA_FILTER;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, MitraFilterFragment.MitraFilterListener, BookFilterFragment.BookFilterListener {
    private final String TAG = getClass().getSimpleName();

    private BookFilter bookFilter;
    private MitraFilter mitraFilter;
    private SearchViewModel searchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.getMitraLiveData().observe(this, mitraList -> {
            for (Mitra mitra : mitraList) Log.d(TAG, mitra.getName());
        });
        searchViewModel.getBookLiveData().observe(this, bookList -> {
            for (Book book : bookList) Log.d(TAG, book.getTitle());
        });

        mitraFilter = new MitraFilter();
        bookFilter = new BookFilter();

        SearchView svMitra = findViewById(R.id.sv_mitra_search);
        SearchView svBook = findViewById(R.id.sv_book_search);

        svMitra.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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

        svBook.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                bookFilter.setKeyword(query);
                searchViewModel.query(bookFilter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) onQueryTextSubmit("");
                return false;
            }
        });

        Button btnMitraFilter = findViewById(R.id.btn_mitra_filter_search);
        Button btnBookFilter = findViewById(R.id.btn_book_filter_search);
        btnMitraFilter.setOnClickListener(this);
        btnBookFilter.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_mitra_filter_search:
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_MITRA_FILTER, mitraFilter);
                MitraFilterFragment bottomSheet = new MitraFilterFragment();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
                break;

            case R.id.btn_book_filter_search:
                Bundle bundleBook = new Bundle();
                bundleBook.putParcelable(EXTRA_BOOK_FILTER, bookFilter);
                BookFilterFragment bottomSheetBook = new BookFilterFragment();
                bottomSheetBook.setArguments(bundleBook);
                bottomSheetBook.show(getSupportFragmentManager(), bottomSheetBook.getTag());
                break;
        }
    }

    @Override
    public void receiveData(BookFilter filter) {
        bookFilter = filter;
        searchViewModel.query(bookFilter);
    }

    @Override
    public void receiveData(MitraFilter filter) {
        mitraFilter = filter;
        searchViewModel.query(mitraFilter);
    }
}