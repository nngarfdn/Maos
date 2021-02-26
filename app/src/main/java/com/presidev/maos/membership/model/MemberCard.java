package com.presidev.maos.membership.model;

public class MemberCard {
    private String id;
    private String userId;
    private String mitraId;
    private String startDate;
    private String expDate;

    public MemberCard() {}

    public MemberCard(String id, String userId, String mitraId, String startDate, String expDate) {
        this.id = id;
        this.userId = userId;
        this.mitraId = mitraId;
        this.startDate = startDate;
        this.expDate = expDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMitraId() {
        return mitraId;
    }

    public void setMitraId(String mitraId) {
        this.mitraId = mitraId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }
}
