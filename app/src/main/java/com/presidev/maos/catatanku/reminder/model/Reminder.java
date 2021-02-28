package com.presidev.maos.catatanku.reminder.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Reminder implements Parcelable {
    String id;
    String uuid;
    String bookTitle;
    String tempatPeminjam;
    String returnDate;
    String isKembali = "false";

    public Reminder() {
    }

    public Reminder(String id, String uuid, String bookTitle, String tempatPeminjam, String returnDate, String isKembali) {
        this.id = id;
        this.uuid = uuid;
        this.bookTitle = bookTitle;
        this.tempatPeminjam = tempatPeminjam;
        this.returnDate = returnDate;
        this.isKembali = isKembali;
    }

    protected Reminder(Parcel in) {
        id = in.readString();
        uuid = in.readString();
        bookTitle = in.readString();
        tempatPeminjam = in.readString();
        returnDate = in.readString();
        isKembali = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(uuid);
        dest.writeString(bookTitle);
        dest.writeString(tempatPeminjam);
        dest.writeString(returnDate);
        dest.writeString(isKembali);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getTempatPeminjam() {
        return tempatPeminjam;
    }

    public void setTempatPeminjam(String tempatPeminjam) {
        this.tempatPeminjam = tempatPeminjam;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getIsKembali() {
        return isKembali;
    }

    public void setIsKembali(String isKembali) {
        this.isKembali = isKembali;
    }
}
