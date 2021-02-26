package com.presidev.maos.bookmark.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.presidev.maos.bookmark.model.Bookmark;

import java.util.ArrayList;

public class BookmarkRepository {
    private final String TAG = getClass().getSimpleName();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    private final MutableLiveData<Bookmark> resultData = new MutableLiveData<>();
    public LiveData<Bookmark> getData(){
        return resultData;
    }

    public void query(String userId){
        database.collection("bookmark").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Bookmark favorite = task.getResult().toObject(Bookmark.class);

                        // Pengguna yang belum pernah tambah favorit
                        if (favorite == null) {
                            favorite = new Bookmark(new ArrayList<>());
                            insert(userId, favorite);  // Sekalian bikin dokumennya
                        }

                        resultData.postValue(favorite);
                    } else Log.w(TAG, "Error querying document", task.getException());
                });
    }

    private void insert(String userId, Bookmark favorite){
        database.collection("bookmark").document(userId)
                .set(favorite)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was added");
                    else Log.w(TAG, "Error adding document", task.getException());
                });
    }

    public void add(String userId, String productId){
        database.collection("bookmark").document(userId)
                .update("listBookId", FieldValue.arrayUnion(productId))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was updated");
                    else Log.w(TAG, "Error updating document", task.getException());
                });
    }

    public void remove(String userId, String productId){
        database.collection("bookmark").document(userId)
                .update("listBookId", FieldValue.arrayRemove(productId))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was updated");
                    else Log.w(TAG, "Error updating document", task.getException());
                });
    }

}
