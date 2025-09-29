package com.example.lasttryprojectactivity.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "restaurant_session";
    private static final String IS_LOGGED_IN = "is_logged_in";
    private static final String USER_ID = "user_id";
    private static final String USERNAME = "username";
    private static final String IS_FIRST_LAUNCH = "is_first_launch";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(int userId, String username) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putInt(USER_ID, userId);
        editor.putString(USERNAME, username);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return pref.getInt(USER_ID, -1);
    }

    public String getUsername() {
        return pref.getString(USERNAME, "");
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public void setFirstLaunch(boolean isFirstLaunch) {
        editor.putBoolean(IS_FIRST_LAUNCH, isFirstLaunch);
        editor.commit();
    }

    public boolean isFirstLaunch() {
        return pref.getBoolean(IS_FIRST_LAUNCH, true);
    }
}
