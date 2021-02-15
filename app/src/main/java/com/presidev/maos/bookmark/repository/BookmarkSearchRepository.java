package com.presidev.maos.bookmark.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.presidev.maos.mitramanagement.model.Book;
import com.presidev.maos.search.model.BookFilter;

import java.util.ArrayList;

public class BookmarkSearchRepository {

    private final String TAG = getClass().getSimpleName();

    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    private final MutableLiveData<ArrayList<Book>> bookLiveData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Book>> getBookLiveData() {
        return bookLiveData;
    }



}
