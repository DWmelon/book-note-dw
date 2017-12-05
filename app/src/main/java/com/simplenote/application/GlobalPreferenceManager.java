package com.simplenote.application;

import android.content.Context;

/**
 * Created by melon on 2017/7/20.
 */

public class GlobalPreferenceManager {

    private static final String PREFE_NAME = "global_prefe";


    private static final String KEY_IS_LOGIN = "new_version";

    public static final String KEY_ACCESS_KEY_ID = "access_key_id";
    public static final String KEY_SECRET_KEY_ID = "secret_key_id";
    public static final String KEY_SECURITY_TOKEN = "security_token";

    public static void setLogin(Context context, boolean isLogin) {
        context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).edit().putBoolean(KEY_IS_LOGIN, isLogin).apply();
    }

    public static boolean isLogin(Context context) {
        return context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).getBoolean(KEY_IS_LOGIN, false);
    }

    public static void setString(Context context,String key, String value) {
        context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public static String getString(Context context,String key) {
        return context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).getString(key, "");
    }

}
