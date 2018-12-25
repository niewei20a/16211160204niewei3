package com.example.a18199.a16211160204niewei.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SPUtils {
    private static SPUtils util;
    private static SharedPreferences sp;

    private SPUtils(Context context, String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * 初始化SharedPreferencesUtil,只需要初始化一次，建议在Application中初始化
     *
     * @param context 上下文对象
     * @param name    SharedPreferences Name
     */
    public static void getInstance(Context context, String name) {
        if (util == null) {
            util = new SPUtils(context, name);
        }
    }

    /**
     * 保存数据到SharedPreferences
     *
     * @param key   键
     * @param value 需要保存的数据
     * @return 保存结果
     */
    public static boolean putData(String key, String value) {
        boolean result;
        SharedPreferences.Editor editor = sp.edit();
        String type = value.getClass().getSimpleName();
        try {
            editor.putString(key, value);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }
    public static boolean putData(String key, Boolean value) {
        boolean result;
        SharedPreferences.Editor editor = sp.edit();
        String type = value.getClass().getSimpleName();
        try {
            editor.putBoolean(key, value);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }
    public static String getData(String key, String defaultValue) {
        String type = defaultValue.getClass().getSimpleName();
        String result;
        result = sp.getString(key, defaultValue);
        return result;
    }
    public static boolean getData(String key, Boolean defaultValue) {
        boolean result = false;
        String type = defaultValue.getClass().getSimpleName();
        try {
            result = sp.getBoolean(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

