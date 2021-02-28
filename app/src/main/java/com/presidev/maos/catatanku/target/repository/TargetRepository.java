package com.presidev.maos.catatanku.target.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.presidev.maos.catatanku.target.model.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TargetRepository {
    private final String TAG = getClass().getSimpleName();

    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    public final CollectionReference reference = database.collection("catatanKu");

    private final MutableLiveData<ArrayList<Target>> targetListLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Target>> getTargetListLiveData(){
        return targetListLiveData;
    }

    private final MutableLiveData<Target> targetLiveData = new MutableLiveData<>();
    public MutableLiveData<Target> getTargetLiveData(){
        return targetLiveData;
    }

    public void query(String userId){
        reference.document(userId).collection("target")
                .orderBy("progress", Query.Direction.ASCENDING).orderBy("bookTitle", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        ArrayList<Target> targetList = new ArrayList<>();
                        if (task.getResult() != null){
                            for (DocumentSnapshot document : task.getResult()){
                                Target target = document.toObject(Target.class);
                                targetList.add(target);
                                if (target != null) Log.d(TAG, "query: " + target.getBookTitle());
                            }
                        }
                        targetListLiveData.postValue(targetList);
                        Log.d(TAG, "Document was queried");
                    } else Log.w(TAG, "Error querying document", task.getException());
                });
    }

    public void queryById(String userId, String targetId){
        reference.document(userId).collection("target").document(targetId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if (task.getResult() != null){
                            Target target = task.getResult().toObject(Target.class);
                            targetLiveData.postValue(target);
                            if (target != null) Log.d(TAG, "query: " + target.getBookTitle());
                            Log.d(TAG, "Document was queried");
                        }
                    } else Log.w(TAG, "Error querying document", task.getException());
                });
    }

    public void insert(Target target){
        reference.document(target.getUserId()).collection("target").document(target.getId())
                .set(target)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was added");
                    else Log.w(TAG, "Error adding document", task.getException());
                });
    }

    public void update(Target target){
        reference.document(target.getUserId()).collection("target").document(target.getId())
                .update(objectToHashMap(target))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was updated");
                    else Log.w(TAG, "Error updating document", task.getException());
                });
    }

    public void delete(Target target){
        reference.document(target.getUserId()).collection("target").document(target.getId())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was updated");
                    else Log.w(TAG, "Error updating document", task.getException());
                });
    }

    public void addSnapshotListener(String userId){
        reference.document(userId).collection("target")
                .addSnapshotListener((value, error) -> {
                if (error != null) Log.w(TAG, "Listen failed", error);
                else if (value != null){
                    query(userId);
                    Log.d(TAG, "Changes detected");
                }
            });
    }

    private Map<String, Object> objectToHashMap(Target target){
        Map<String, Object> document = new HashMap<>();
        document.put("id", target.getId());
        document.put("userId", target.getUserId());
        document.put("bookTitle", target.getBookTitle());
        document.put("totalPages", target.getTotalPages());
        document.put("dailyPages", target.getDailyPages());
        document.put("pagesRead", target.getPagesRead());
        document.put("progress", target.getProgress());
        document.put("isReminderEnabled", target.getIsReminderEnabled());
        document.put("reminderTime", target.getReminderTime());
        document.put("reminderDayOfWeeks", target.getReminderDayOfWeeks());
        return document;
    }
}
