package com.presidev.maos.login.model;

import com.google.firebase.firestore.Exclude;

public class User {
    private final String id;
    private final String name;
    private final String email;
    private boolean isNew = false;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Exclude
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
