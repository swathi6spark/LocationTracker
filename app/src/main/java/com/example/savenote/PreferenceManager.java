package com.example.savenote;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContext;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "notes_prefs";


    public PreferenceManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void writeToPreferences(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String readFromPrefs(String key) {
        return pref.getString(key, "0");
    }


}
