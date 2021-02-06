package com.presidev.maos.login.model;

public class Account {
    private final String id;
    private final String email;
    private final String level;

    public Account(String id, String email, String level) {
        this.id = id;
        this.email = email;
        this.level = level;
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
}
