package com.presidev.maos.search.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.presidev.maos.mitramanagement.model.Book;
import com.presidev.maos.profile.mitra.model.Mitra;
import com.presidev.maos.search.model.BookFilter;
import com.presidev.maos.search.model.MitraFilter;
import com.presidev.maos.search.repository.SearchRepository;

import java.util.ArrayList;

public class SearchViewModel extends ViewModel {
    private final SearchRepository repository = new SearchRepository();

    public LiveData<ArrayList<Mitra>> getMitraLiveData(){
        return repository.getMitraLiveData();
    }

    public LiveData<ArrayList<Book>> getBookLiveData(){
        return repository.getBookLiveData();
    }

    public void query(MitraFilter filter){
        repository.query(filter);
    }

    public void query(MitraFilter filter, boolean isMembership){
        repository.query(filter, isMembership);
    }

    public void query(BookFilter filter){
        repository.query(filter);
    }
}
