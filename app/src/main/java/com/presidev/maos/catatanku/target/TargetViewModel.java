package com.presidev.maos.catatanku.target;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

public class TargetViewModel extends ViewModel {
    private final TargetRepository repository = new TargetRepository();

    public LiveData<ArrayList<Target>> getTargetListLiveData(){
        return repository.getTargetListLiveData();
    }

    public LiveData<Target> getTargetLiveData(){
        return repository.getTargetLiveData();
    }

    public void query(String userId){
        repository.query(userId);
    }

    public void queryById(String userId, String targetId){
        repository.queryById(userId, targetId);
    }

    public void insert(Target target){
        repository.insert(target);
    }

    public void update(Target target){
        repository.update(target);
    }

    public void delete(Target target){
        repository.delete(target);
    }

    public CollectionReference getReference(){
        return repository.reference;
    }

    public void addSnapshotListener(String userId){
        repository.addSnapshotListener(userId);
    }
}
