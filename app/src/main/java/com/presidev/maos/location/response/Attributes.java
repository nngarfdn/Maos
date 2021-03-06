package com.presidev.maos.location.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("nama")
    @Expose
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
