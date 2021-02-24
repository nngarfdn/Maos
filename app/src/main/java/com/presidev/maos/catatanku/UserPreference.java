package com.presidev.maos.catatanku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static com.presidev.maos.utils.DateUtils.getCurrentDate;

public class UserPreference {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private static final String PREFERENCE_NAME = "user_preference";
    private static final String IS_FIRST_TIME_LOGIN = "is_first_time_login";
    private static final String LAST_UPDATE_PAGES_READ = "last_update_pages_read";
    private static final String TODAY_PAGES_READ = "today_pages_read";

    @SuppressLint("CommitPrefEdits")
    public UserPreference(Context context){
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setIsFirstTimeLogin(boolean IsFirstTimeLogin){
        editor.putBoolean(IS_FIRST_TIME_LOGIN, IsFirstTimeLogin);
        editor.apply();
    }

    public boolean getIsFirstTimeLogin(){
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LOGIN, true);
    }

    public void setData(String targetId, String lastUpdatePagesRead, int todayPagesRead){
        editor.putString(LAST_UPDATE_PAGES_READ +  "_" + targetId, lastUpdatePagesRead);
        editor.putInt(TODAY_PAGES_READ +  "_" + targetId, todayPagesRead);
        editor.apply();
    }

    public void removeData(String targetId){
        editor.remove(LAST_UPDATE_PAGES_READ +  "_" + targetId);
        editor.remove(TODAY_PAGES_READ +  "_" + targetId);
        editor.apply();
    }

    public String getLastUpdatePagesRead(String targetId){
        return sharedPreferences.getString(LAST_UPDATE_PAGES_READ +  "_" + targetId, getCurrentDate());
    }

    public int getTodayPagesRead(String targetId){
        return sharedPreferences.getInt(TODAY_PAGES_READ +  "_" + targetId, 0);
    }

    public void resetData(){
        editor.clear().apply();
    }
}
