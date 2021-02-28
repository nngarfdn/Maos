package com.presidev.maos.catatanku.quotes.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.presidev.maos.callback.OnImageUploadCallback;
import com.presidev.maos.catatanku.quotes.model.Quote;

import java.util.ArrayList;

import static com.presidev.maos.utils.Constants.FOLDER_QUOTE;
import static com.presidev.maos.utils.ImageUtils.convertBitmapToByteArray;
import static com.presidev.maos.utils.ImageUtils.getCompressedByteArray;

public class QuoteRepository {
    private final String TAG = getClass().getSimpleName();

    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public final CollectionReference reference = database.collection("catatanKu");

    private final MutableLiveData<ArrayList<Quote>> quoteListLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Quote>> getQuoteListLiveData(){
        return quoteListLiveData;
    }

    private final MutableLiveData<Quote> quoteLiveData = new MutableLiveData<>();
    public MutableLiveData<Quote> getQuoteLiveData(){
        return quoteLiveData;
    }

    public void query(String userId){
        reference.document(userId).collection("quotes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        ArrayList<Quote> quoteList = new ArrayList<>();
                        if (task.getResult() != null){
                            for (DocumentSnapshot document : task.getResult()){
                                Quote quote = document.toObject(Quote.class);
                                quoteList.add(quote);
                                if (quote != null) Log.d(TAG, "query: " + quote.getUrl());
                            }
                        }
                        quoteListLiveData.postValue(quoteList);
                        Log.d(TAG, "Document was queried");
                    } else Log.w(TAG, "Error querying document", task.getException());
                });
    }

    public void queryById(String userId, String quoteId){
        reference.document(userId).collection("quotes").document(quoteId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if (task.getResult() != null){
                            Quote quote = task.getResult().toObject(Quote.class);
                            quoteLiveData.postValue(quote);
                            if (quote != null) Log.d(TAG, "query: " + quote.getUrl());
                        }
                        Log.d(TAG, "Document was queried");
                    } else Log.w(TAG, "Error querying document", task.getException());
                });
    }

    public void insert(Quote quote){
        reference.document(quote.getUserId()).collection("quotes").document(quote.getId())
                .set(quote)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was added");
                    else Log.w(TAG, "Error adding document", task.getException());
                });
    }

    public void delete(Quote quote){
        reference.document(quote.getUserId()).collection("quotes").document(quote.getId())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was updated");
                    else Log.w(TAG, "Error updating document", task.getException());
                });
    }

    public void uploadImage(Context context, Bitmap bitmap, String userId, String fileName, OnImageUploadCallback callback){
        byte[] image = convertBitmapToByteArray(context, bitmap);
        image = getCompressedByteArray(image, false);

        StorageReference reference = storage.getReference().child(FOLDER_QUOTE + "/" + userId + "/" + fileName);
        UploadTask uploadTask = reference.putBytes(image);
        uploadTask.addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uriResult -> {
            callback.onSuccess(uriResult.toString());
            Log.d(TAG, "Image was uploaded");
        })).addOnFailureListener(e -> Log.w(TAG, "Error uploading image", e));
    }

    public void deleteImage(String imageUrl){
        storage.getReferenceFromUrl(imageUrl).delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Image was deleted"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting image", e));
    }

    public void addSnapshotListener(String userId){
        reference.document(userId).collection("quotes")
                .addSnapshotListener((value, error) -> {
                if (error != null) Log.w(TAG, "Listen failed", error);
                else if (value != null){
                    query(userId);
                    Log.d(TAG, "Changes detected");
                }
            });
    }
}
