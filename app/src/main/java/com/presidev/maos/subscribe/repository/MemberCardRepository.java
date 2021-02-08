package com.presidev.maos.subscribe.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.presidev.maos.subscribe.model.MemberCard;

import java.util.ArrayList;

import static com.presidev.maos.utils.DateUtils.getCurrentDate;

public class MemberCardRepository {
    private final String TAG = getClass().getSimpleName();

    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    public final CollectionReference reference = database.collection("memberCards");

    private final MutableLiveData<MemberCard> memberCardLiveData = new MutableLiveData<>();
    public MutableLiveData<MemberCard> getMemberCardLiveData(){
        return memberCardLiveData;
    }

    private final MutableLiveData<ArrayList<MemberCard>> memberCardListLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<MemberCard>> getMemberCardListLiveData(){
        return memberCardListLiveData;
    }

    public void queryByCardId(String cardId){
        reference.document(cardId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        MemberCard memberCard = task.getResult().toObject(MemberCard.class);
                        memberCardLiveData.postValue(memberCard);
                        if (memberCard != null) Log.d(TAG, "query: " + memberCard.getId());
                        Log.d(TAG, "Document was queried");
                    } else Log.w(TAG, "Error querying document", task.getException());
                });
    }

    public void queryByUserId(String userId){
        reference.whereEqualTo("userId", userId).whereGreaterThanOrEqualTo("expDate", getCurrentDate())
                .orderBy("expDate", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        ArrayList<MemberCard> result = new ArrayList<>();
                        Log.d(TAG, "Size result: " + task.getResult().size());
                        for (DocumentSnapshot document : task.getResult()){
                            MemberCard memberCard = document.toObject(MemberCard.class);
                            result.add(memberCard);
                            Log.d(TAG, "query: " + memberCard.getId());
                        }
                        memberCardListLiveData.postValue(result);
                        Log.d(TAG, "Document was queried");
                    } else Log.w(TAG, "Error querying document", task.getException());
                });
    }

    public void queryByMitraId(String mitraId){
        reference.whereEqualTo("mitraId", mitraId).whereGreaterThanOrEqualTo("expDate", getCurrentDate())
                .orderBy("expDate", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        ArrayList<MemberCard> result = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()){
                            MemberCard memberCard = document.toObject(MemberCard.class);
                            result.add(memberCard);
                            Log.d(TAG, "query: " + memberCard.getId());
                        }
                        memberCardListLiveData.postValue(result);
                        Log.d(TAG, "Document was queried");
                    } else Log.w(TAG, "Error querying document", task.getException());
                });
    }

    public void insert(MemberCard memberCard){
        reference.document(memberCard.getId())
                .set(memberCard)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was added");
                    else Log.w(TAG, "Error adding document", task.getException());
                });
    }

    public void update(MemberCard memberCard){
        reference.document(memberCard.getId())
                .update("startDate", memberCard.getStartDate(),
                        "expDate", memberCard.getExpDate())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Document was updated");
                    else Log.w(TAG, "Error updating document", task.getException());
                });
    }
}
