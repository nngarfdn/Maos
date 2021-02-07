package com.presidev.maos.subscribe;

public class MemberCard {
    private String id;
    private String idUser;
    private String idMitra;
    private String startDate;
    private String expDate;

    public MemberCard(String id, String idUser, String idMitra, String startDate, String expDate) {
        this.id = id;
        this.idUser = idUser;
        this.idMitra = idMitra;
        this.startDate = startDate;
        this.expDate = expDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdMitra() {
        return idMitra;
    }

    public void setIdMitra(String idMitra) {
        this.idMitra = idMitra;
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
