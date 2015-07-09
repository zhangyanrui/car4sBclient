package cn.car4s.app.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import cn.car4s.app.AppContext;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/5/12.
 */
public class PreferencesUtil {
    public static <T> void putPreferences(String key, T value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AppContext.getInstance()).edit();
        if (value instanceof String) {
            editor.putString(key, value.toString());
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            editor.putInt(key, ((Integer) value).intValue());
        } else if (value instanceof Float) {
            editor.putFloat(key, ((Float) value).floatValue());
        } else if (value instanceof Long) {
            editor.putLong(key, ((Long) value).longValue());
        }
        editor.commit();
    }

    public static <T> T getPreferences(String key, T value) {
        Object o = null;
        if (value instanceof String) {
            o = PreferenceManager.getDefaultSharedPreferences(AppContext.getInstance()).getString(key, value.toString());
        } else if (value instanceof Boolean) {
            o = PreferenceManager.getDefaultSharedPreferences(AppContext.getInstance()).getBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            o = PreferenceManager.getDefaultSharedPreferences(AppContext.getInstance()).getInt(key, ((Integer) value).intValue());
        } else if (value instanceof Float) {
            o = PreferenceManager.getDefaultSharedPreferences(AppContext.getInstance()).getFloat(key, ((Float) value).floatValue());
        } else if (value instanceof Long) {
            o = PreferenceManager.getDefaultSharedPreferences(AppContext.getInstance()).getLong(key, ((Long) value).longValue());
        }
        T t = (T) o;
        return t;
    }

}
