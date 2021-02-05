package com.presidev.maos.model;

public class User {
    private final String id;
    private final String name;
    private final String email;
    private final String level;

    public User(String id, String name, String email, String level) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.level = level;
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

    public String getLevel(){
        return level;
    }
}
