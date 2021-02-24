package com.presidev.maos.catatanku.reminder.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Reminder implements Parcelable {
    String id;
    String bookTitle;
    String tempatPeminjam;
    String returnDate;
    String isKembali = "false";

    public Reminder(String id, String bookTitle, String tempatPeminjam, String returnDate, String isKembali) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.tempatPeminjam = tempatPeminjam;
        this.returnDate = returnDate;
        this.isKembali = isKembali;
    }

    public Reminder() {
    }

    protected Reminder(Parcel in) {
        id = in.readString();
        bookTitle = in.readString();
        tempatPeminjam = in.readString();
        returnDate = in.readString();
        isKembali = in.readString();
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

    @Override
    public String toString() {
        return "Reminder{" +
                "id='" + id + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", tempatPeminjam='" + tempatPeminjam + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", isKembali='" + isKembali + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(bookTitle);
        dest.writeString(tempatPeminjam);
        dest.writeString(returnDate);
        dest.writeString(isKembali);
    }
}
