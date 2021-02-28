package com.presidev.maos.developer.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DeveloperRepository {
    private final String TAG = getClass().getSimpleName();

    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    public final CollectionReference reference = database.collection("developer");

    private final MutableLiveData<ArrayList<String>> quoteBackgroundListLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> getQuoteBackgroundListLiveData(){
        return quoteBackgroundListLiveData;
    }

    @SuppressWarnings("unchecked")
    public void queryQuoteBackground(){
        reference.document("quote")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if (task.getResult() != null){
                            ArrayList<String> quoteBackgrounds =
                                    (ArrayList<String>) task.getResult().get("backgrounds");
                            quoteBackgroundListLiveData.postValue(quoteBackgrounds);
                        }
                        Log.d(TAG, "Document was queried");
                    } else Log.w(TAG, "Error querying document", task.getException());
                });
    }
}
