package com.presidev.maos.profile.user;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.presidev.maos.callback.OnImageUploadCallback;

public class UserViewModel extends ViewModel {
    private final UserRepository repository = new UserRepository();

    public LiveData<User> getUserLiveData(){
        return repository.getUserLiveData();
    }

    public void query(String userId){
        repository.query(userId);
    }

    public void insert(User user){
        repository.insert(user);
    }

    public void update(User user){
        repository.update(user);
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
