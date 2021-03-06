package com.presidev.maos.catatanku.target.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Target implements Parcelable {
    private String id;
    private String userId;
    private String bookTitle;
    private int totalPages;
    private int dailyPages;
    private int pagesRead;
    private double progress;
    private boolean isReminderEnabled;
    private String reminderTime;
    private List<String> reminderDayOfWeeks;

    public Target() {}

    protected Target(Parcel in) {
        id = in.readString();
        userId = in.readString();
        bookTitle = in.readString();
        totalPages = in.readInt();
        dailyPages = in.readInt();
        pagesRead = in.readInt();
        progress = in.readDouble();
        isReminderEnabled = in.readByte() != 0;
        reminderTime = in.readString();
        reminderDayOfWeeks = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(bookTitle);
        dest.writeInt(totalPages);
        dest.writeInt(dailyPages);
        dest.writeInt(pagesRead);
        dest.writeDouble(progress);
        dest.writeByte((byte) (isReminderEnabled ? 1 : 0));
        dest.writeString(reminderTime);
        dest.writeStringList(reminderDayOfWeeks);
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

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public boolean getIsReminderEnabled() {
        return isReminderEnabled;
    }

    public void setIsReminderEnabled(boolean isReminderEnabled) {
        this.isReminderEnabled = isReminderEnabled;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public List<Integer> getReminderDayOfWeeks() {
        List<Integer> result = new ArrayList<>();
        for(String dayOfWeek : reminderDayOfWeeks) {
            result.add(Integer.parseInt(dayOfWeek));
        }
        return result;
    }

    public void setReminderDayOfWeeks(List<Integer> reminderDayOfWeeks) {
        List<String> result = new ArrayList<>();
        for(int dayOfWeek : reminderDayOfWeeks) {
            result.add(String.valueOf(dayOfWeek));
        }
        this.reminderDayOfWeeks = result;
    }
}
