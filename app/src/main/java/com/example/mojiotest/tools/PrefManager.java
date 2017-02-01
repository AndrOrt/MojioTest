package com.example.mojiotest.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private static final String PREFS_NAME = "Mojio_Test_Pref";
    private static final String IS_LOGIN = "IS_LOGIN";

    private Context context;

    public PrefManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession() {
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void LoginOut() {
        editor.clear().commit();
    }
}
