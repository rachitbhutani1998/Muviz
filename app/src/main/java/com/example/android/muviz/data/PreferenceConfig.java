package com.example.android.muviz.data;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceConfig {

    public static final String SORT_ORDER = "sort_order";
    public static final String SORT_BASIS = "sort_basis";

    private static SharedPreferences preferences;
    private static PreferenceConfig preferenceConfig;

    private PreferenceConfig(Context context) {
        preferences = context.getSharedPreferences("example.android.muviz.preference", Context.MODE_PRIVATE);
    }

    public static PreferenceConfig getInstance(Context context) {
        if (preferences == null)
            preferenceConfig = new PreferenceConfig(context);
        return preferenceConfig;
    }

    public void saveString(String key, String value) {
        preferences.edit().putString(key, value)
                .apply();
    }

    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }
}
