package com.presidev.maos.profile.user.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String id;
    private String photo;
    private String name;
    private String email;
    private String whatsApp;
    private String idCard;
    private String address;
    private String province;
    private String regency;
    private String district;

    public User(){}

    public User(String id, String photo, String name, String email, String whatsApp, String idCard, String address, String province, String regency, String district) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.email = email;
        this.whatsApp = whatsApp;
        this.idCard = idCard;
        this.address = address;
        this.province = province;
        this.regency = regency;
        this.district = district;
    }

    protected User(Parcel in) {
        id = in.readString();
        photo = in.readString();
        name = in.readString();
        email = in.readString();
        whatsApp = in.readString();
        idCard = in.readString();
        address = in.readString();
        province = in.readString();
        regency = in.readString();
        district = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(photo);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(whatsApp);
        dest.writeString(idCard);
        dest.writeString(address);
        dest.writeString(province);
        dest.writeString(regency);
        dest.writeString(district);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWhatsApp() {
        return whatsApp;
    }

    public void setWhatsApp(String whatsApp) {
        this.whatsApp = whatsApp;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRegency() {
        return regency;
    }

    public void setRegency(String regency) {
        this.regency = regency;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
