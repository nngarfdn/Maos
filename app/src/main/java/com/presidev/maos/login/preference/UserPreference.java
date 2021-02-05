package com.presidev.maos.login.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.presidev.maos.login.model.Account;

public class UserPreference {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private static final String PREFERENCE_NAME = "user_preference";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String LEVEL = "level";

    @SuppressLint("CommitPrefEdits")
    public UserPreference(Context context){
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setData(Account account){
        editor.putString(ID, account.getId());
        editor.putString(NAME, account.getName());
        editor.putString(EMAIL, account.getEmail());
        editor.putString(LEVEL, account.getLevel());
        editor.apply();
    }

    public void resetData(){
        editor.clear().apply();
    }

    public String getId(){
        return sharedPreferences.getString(ID, null);
    }

    public String getName(){
        return sharedPreferences.getString(NAME, null);
    }

    public String getEmail(){
        return sharedPreferences.getString(EMAIL, null);
    }

    public String getLevel(){
        return sharedPreferences.getString(LEVEL, null);
    }
}
