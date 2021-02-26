package com.presidev.maos.auth.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Exclude;

public class Account {
    @Exclude private final FirebaseUser firebaseUser;
    private final String id;
    private final String email;
    private final String level;
    @Exclude private final boolean isNewAccount;

    public Account(FirebaseUser firebaseUser, String id, String email, String level, boolean isNewAccount) {
        this.firebaseUser = firebaseUser;
        this.id = id;
        this.email = email;
        this.level = level;
        this.isNewAccount = isNewAccount;
    }

    @Exclude
    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLevel(){
        return level;
    }

    @Exclude
    public boolean isNewAccount() {
        return isNewAccount;
    }
}
