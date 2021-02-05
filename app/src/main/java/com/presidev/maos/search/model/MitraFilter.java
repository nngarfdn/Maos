package com.presidev.maos.search.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MitraFilter implements Parcelable {
    private String keyword;
    private boolean isByProvince;
    private boolean isByRegency;
    private boolean isByDistrict;
    private String province;
    private String regency;
    private String district;
    private boolean onlyCOD;
    private boolean onlyKirimLuarKota;

    public MitraFilter() {
        this.keyword = "";
        this.isByProvince = false;
        this.isByRegency = false;
        this.isByDistrict = false;
        this.province = "";
        this.regency = "";
        this.district = "";
        this.onlyCOD = false;
        this.onlyKirimLuarKota = false;
    }

    public MitraFilter(String keyword, boolean isByProvince, boolean isByRegency, boolean isByDistrict, String province, String regency, String district, boolean onlyCOD, boolean onlyKirimLuarKota) {
        this.keyword = keyword;
        this.isByProvince = isByProvince;
        this.isByRegency = isByRegency;
        this.isByDistrict = isByDistrict;
        this.province = province;
        this.regency = regency;
        this.district = district;
        this.onlyCOD = onlyCOD;
        this.onlyKirimLuarKota = onlyKirimLuarKota;
    }

    protected MitraFilter(Parcel in) {
        keyword = in.readString();
        isByProvince = in.readByte() != 0;
        isByRegency = in.readByte() != 0;
        isByDistrict = in.readByte() != 0;
        province = in.readString();
        regency = in.readString();
        district = in.readString();
        onlyCOD = in.readByte() != 0;
        onlyKirimLuarKota = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(keyword);
        dest.writeByte((byte) (isByProvince ? 1 : 0));
        dest.writeByte((byte) (isByRegency ? 1 : 0));
        dest.writeByte((byte) (isByDistrict ? 1 : 0));
        dest.writeString(province);
        dest.writeString(regency);
        dest.writeString(district);
        dest.writeByte((byte) (onlyCOD ? 1 : 0));
        dest.writeByte((byte) (onlyKirimLuarKota ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MitraFilter> CREATOR = new Creator<MitraFilter>() {
        @Override
        public MitraFilter createFromParcel(Parcel in) {
            return new MitraFilter(in);
        }

        @Override
        public MitraFilter[] newArray(int size) {
            return new MitraFilter[size];
        }
    };

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isByProvince() {
        return isByProvince;
    }

    public void setByProvince(boolean byProvince) {
        isByProvince = byProvince;
    }

    public boolean isByRegency() {
        return isByRegency;
    }

    public void setByRegency(boolean byRegency) {
        isByRegency = byRegency;
    }

    public boolean isByDistrict() {
        return isByDistrict;
    }

    public void setByDistrict(boolean byDistrict) {
        isByDistrict = byDistrict;
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

    public boolean isOnlyCOD() {
        return onlyCOD;
    }

    public void setOnlyCOD(boolean onlyCOD) {
        this.onlyCOD = onlyCOD;
    }

    public boolean isOnlyKirimLuarKota() {
        return onlyKirimLuarKota;
    }

    public void setOnlyKirimLuarKota(boolean onlyKirimLuarKota) {
        this.onlyKirimLuarKota = onlyKirimLuarKota;
    }
}
