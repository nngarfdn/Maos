package com.presidev.maos.catatanku.target;

import android.os.Parcel;
import android.os.Parcelable;

public class Target implements Parcelable {
    private String id;
    private String userId;
    private String bookTitle;
    private int totalPages;
    private int dailyPages;
    private int pagesRead;
    private int progress;

    public Target() {}

    public Target(String id, String userId, String bookTitle, int totalPages, int dailyPages, int pagesRead, int progress) {
        this.id = id;
        this.userId = userId;
        this.bookTitle = bookTitle;
        this.totalPages = totalPages;
        this.dailyPages = dailyPages;
        this.pagesRead = pagesRead;
        this.progress = progress;
    }

    protected Target(Parcel in) {
        id = in.readString();
        userId = in.readString();
        bookTitle = in.readString();
        totalPages = in.readInt();
        dailyPages = in.readInt();
        pagesRead = in.readInt();
        progress = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(bookTitle);
        dest.writeInt(totalPages);
        dest.writeInt(dailyPages);
        dest.writeInt(pagesRead);
        dest.writeInt(progress);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Target> CREATOR = new Creator<Target>() {
        @Override
        public Target createFromParcel(Parcel in) {
            return new Target(in);
        }

        @Override
        public Target[] newArray(int size) {
            return new Target[size];
        }
    };

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

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getDailyPages() {
        return dailyPages;
    }

    public void setDailyPages(int dailyPages) {
        this.dailyPages = dailyPages;
    }

    public int getPagesRead() {
        return pagesRead;
    }

    public void setPagesRead(int pagesRead) {
        this.pagesRead = pagesRead;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
