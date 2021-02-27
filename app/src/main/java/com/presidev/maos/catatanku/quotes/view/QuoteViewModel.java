package com.presidev.maos.catatanku.quotes.view;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.presidev.maos.callback.OnImageUploadCallback;
import com.presidev.maos.catatanku.quotes.model.Quote;
import com.presidev.maos.catatanku.quotes.repository.QuoteRepository;

import java.util.ArrayList;

public class QuoteViewModel extends ViewModel {
    private final QuoteRepository repository = new QuoteRepository();

    public LiveData<ArrayList<Quote>> getQuoteListLiveData(){
        return repository.getQuoteListLiveData();
    }

    public LiveData<Quote> getQuoteLiveData(){
        return repository.getQuoteLiveData();
    }

    public void query(String userId){
        repository.query(userId);
    }

    public void queryById(String userId, String quoteId){
        repository.queryById(userId, quoteId);
    }

    public void insert(Quote quote){
        repository.insert(quote);
    }

    public void delete(Quote quote){
        repository.delete(quote);
    }

    public void uploadImage(Context context, Bitmap bitmap, String userId, String fileName, OnImageUploadCallback callback){
        repository.uploadImage(context, bitmap, userId, fileName, callback);
    }

    public void deleteImage(String imageUrl){
        repository.deleteImage(imageUrl);
    }

    public CollectionReference getReference(){
        return repository.reference;
    }

    public void addSnapshotListener(String userId){
        repository.addSnapshotListener(userId);
    }
}
