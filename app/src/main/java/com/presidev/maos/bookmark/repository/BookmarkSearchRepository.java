package com.presidev.maos.bookmark.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.presidev.maos.mitramanagement.model.Book;

import java.util.ArrayList;

public class BookmarkSearchRepository {

    private final String TAG = getClass().getSimpleName();

    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    private final MutableLiveData<ArrayList<Book>> bookLiveData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Book>> getBookLiveData() {
        return bookLiveData;
    }



}
