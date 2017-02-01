package com.example.mojiotest.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Andrei_Ortyashov on 2/1/2017.
 */

public class PrefManager {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public static final String PREFS_NAME = "Mojio_Test_Pref";
    public static final String IS_LOGIN = "IS_LOGIN";

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
