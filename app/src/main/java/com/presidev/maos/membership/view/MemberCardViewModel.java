package com.presidev.maos.membership.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.presidev.maos.membership.model.MemberCard;
import com.presidev.maos.membership.repository.MemberCardRepository;

import java.util.ArrayList;

public class MemberCardViewModel extends ViewModel {
    private final MemberCardRepository repository = new MemberCardRepository();

    public LiveData<MemberCard> getMemberCardLiveData(){
        return repository.getMemberCardLiveData();
    }

    public LiveData<ArrayList<MemberCard>> getMemberCardListLiveData(){
        return repository.getMemberCardListLiveData();
    }

    public void queryByCardId(String id){
        repository.queryByCardId(id);
    }

    public void queryByMitraId(String id){
        repository.queryByMitraId(id);
    }

    public void queryByUserId(String id){
        repository.queryByUserId(id);
    }

    public void insert(MemberCard memberCard){
        repository.insert(memberCard);
    }

    public void update(MemberCard memberCard){
        repository.update(memberCard);
    }

    public CollectionReference getReference(){
        return repository.reference;
    }
}
