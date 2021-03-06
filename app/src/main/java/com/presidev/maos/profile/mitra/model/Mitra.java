package com.presidev.maos.profile.mitra.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mitra implements Parcelable {
    private String id;
    private String logo;
    private String banner;
    private String name;
    private String email;
    private String description;
    private String whatsApp;
    private String address;
    private String province;
    private String regency;
    private String district;
    private String rules;
    private boolean COD;
    private boolean kirimLuarKota;
    private boolean membership;

    public Mitra() {}

    public Mitra(String id, String logo, String banner, String name, String email, String description, String whatsApp, String address, String province, String regency, String district, String rules, boolean COD, boolean kirimLuarKota, boolean membership) {
        this.id = id;
        this.logo = logo;
        this.banner = banner;
        this.name = name;
        this.email = email;
        this.description = description;
        this.whatsApp = whatsApp;
        this.address = address;
        this.province = province;
        this.regency = regency;
        this.district = district;
        this.rules = rules;
        this.COD = COD;
        this.kirimLuarKota = kirimLuarKota;
        this.membership = membership;
    }

    protected Mitra(Parcel in) {
        id = in.readString();
        logo = in.readString();
        banner = in.readString();
        name = in.readString();
        email = in.readString();
        description = in.readString();
        whatsApp = in.readString();
        address = in.readString();
        province = in.readString();
        regency = in.readString();
        district = in.readString();
        rules = in.readString();
        COD = in.readByte() != 0;
        kirimLuarKota = in.readByte() != 0;
        membership = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(logo);
        dest.writeString(banner);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(description);
        dest.writeString(whatsApp);
        dest.writeString(address);
        dest.writeString(province);
        dest.writeString(regency);
        dest.writeString(district);
        dest.writeString(rules);
        dest.writeByte((byte) (COD ? 1 : 0));
        dest.writeByte((byte) (kirimLuarKota ? 1 : 0));
        dest.writeByte((byte) (membership ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Mitra> CREATOR = new Creator<Mitra>() {
        @Override
        public Mitra createFromParcel(Parcel in) {
            return new Mitra(in);
        }

        @Override
        public Mitra[] newArray(int size) {
            return new Mitra[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWhatsApp() {
        return whatsApp;
    }

    public void setWhatsApp(String whatsApp) {
        this.whatsApp = whatsApp;
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

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public boolean isCOD() {
        return COD;
    }

    public void setCOD(boolean COD) {
        this.COD = COD;
    }

    public boolean isKirimLuarKota() {
        return kirimLuarKota;
    }

    public void setKirimLuarKota(boolean kirimLuarKota) {
        this.kirimLuarKota = kirimLuarKota;
    }

    public boolean isMembership() {
        return membership;
    }

    public void setMembership(boolean membership) {
        this.membership = membership;
    }
}
