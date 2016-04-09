package com.shaojie.demo.switchlistgrid;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharedPreferences辅助类
 */
public class SharedPreferUtil {

    private static SharedPreferences getSharedPreferences() {
        Context ctx = MyApp.getInstance();
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    private static SharedPreferences.Editor getEditor() {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.edit();
    }

    public static boolean get(String key) {
        return get(key, true);
    }

    public static boolean get(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    public static String get(String key, String defValue) {
        return getSharedPreferences().getString(key, defValue);
    }

    public static int get(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public static void put(String key, boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void put(String key, long value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void put(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.apply();
    }

    public static void put(String key, int value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(key, value);
        editor.apply();
    }

}
