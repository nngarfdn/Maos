package com.presidev.maos.bookmark.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.presidev.maos.bookmark.model.Bookmark;
import com.presidev.maos.bookmark.repository.BookmarkRepository;

public class BookmarkViewModel extends ViewModel {
    private final BookmarkRepository repository = new BookmarkRepository();

    public LiveData<Bookmark> getData(){
        return repository.getData();
    }

    public void loadData(String userId){
        repository.query(userId);
    }

    public void add(String userId, String productId){
        repository.add(userId, productId);
    }

    public void remove(String userId, String productId){
        repository.remove(userId, productId);
    }
}
