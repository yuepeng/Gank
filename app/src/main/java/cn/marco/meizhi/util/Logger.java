package cn.marco.meizhi.util;

import android.util.Log;

public final class Logger {

    public static final String TAG = "meizhi";

    public static void i(String message) {
        Log.i(TAG, message);
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void w(String message) {
        Log.w(TAG, message);
    }

}
