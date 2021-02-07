package com.presidev.maos.profile.mitra;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.presidev.maos.callback.OnImageUploadCallback;

public class MitraViewModel extends ViewModel {
    private final MitraRepository repository = new MitraRepository();

    public LiveData<Mitra> getMitraLiveData(){
        return repository.getMitraLiveData();
    }

    public void query(String userId){
        repository.query(userId);
    }

    public void queryByEmail(String email){
        repository.queryByEmail(email);
    }

    public void insert(Mitra mitra){
        repository.insert(mitra);
    }

    public void update(Mitra mitra){
        repository.update(mitra);
    }

    public CollectionReference getReference(){
        return repository.reference;
    }

    public void uploadImage(Context context, Uri uri, String folderName, String fileName, OnImageUploadCallback callback){
        repository.uploadImage(context, uri, folderName, fileName, callback);
    }

    public void deleteImage(String imageUrl){
        repository.deleteImage(imageUrl);
    }
}
