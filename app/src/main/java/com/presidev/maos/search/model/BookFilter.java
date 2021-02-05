package com.presidev.maos.search.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BookFilter implements Parcelable {
    private String mitraId;
    private String keyword;
    private boolean onlyAvailable;

    public BookFilter() {
        this.mitraId = "";
        this.keyword = "";
        this.onlyAvailable = false;
    }

    public BookFilter(String mitraId, String keyword, boolean onlyAvailable) {
        this.mitraId = mitraId;
        this.keyword = keyword;
        this.onlyAvailable = onlyAvailable;
    }

    protected BookFilter(Parcel in) {
        mitraId = in.readString();
        keyword = in.readString();
        onlyAvailable = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mitraId);
        dest.writeString(keyword);
        dest.writeByte((byte) (onlyAvailable ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookFilter> CREATOR = new Creator<BookFilter>() {
        @Override
        public BookFilter createFromParcel(Parcel in) {
            return new BookFilter(in);
        }

        @Override
        public BookFilter[] newArray(int size) {
            return new BookFilter[size];
        }
    };

    public String getMitraId() {
        return mitraId;
    }

    public void setMitraId(String mitraId) {
        this.mitraId = mitraId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isOnlyAvailable() {
        return onlyAvailable;
    }

    public void setOnlyAvailable(boolean onlyAvailable) {
        this.onlyAvailable = onlyAvailable;
    }
}
