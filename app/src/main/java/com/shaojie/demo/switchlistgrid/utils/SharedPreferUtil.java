package com.shaojie.demo.switchlistgrid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.shaojie.demo.switchlistgrid.MyApp;

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

    public static int get(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public static void put(String key, int value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(key, value);
        editor.apply();
    }

}
