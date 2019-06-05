package org.greenrobot.greendao;

import android.util.Log;

public class DaoLog {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    private static final String TAG = "greenDAO";
    public static final int VERBOSE = 2;
    public static final int WARN = 5;

    public static boolean isLoggable(int i) {
        return Log.isLoggable(TAG, i);
    }

    public static String getStackTraceString(Throwable th) {
        return Log.getStackTraceString(th);
    }

    public static int println(int i, String str) {
        return Log.println(i, TAG, str);
    }

    /* renamed from: v */
    public static int m17v(String str) {
        return Log.v(TAG, str);
    }

    /* renamed from: v */
    public static int m18v(String str, Throwable th) {
        return Log.v(TAG, str, th);
    }

    /* renamed from: d */
    public static int m11d(String str) {
        return Log.d(TAG, str);
    }

    /* renamed from: d */
    public static int m12d(String str, Throwable th) {
        return Log.d(TAG, str, th);
    }

    /* renamed from: i */
    public static int m15i(String str) {
        return Log.i(TAG, str);
    }

    /* renamed from: i */
    public static int m16i(String str, Throwable th) {
        return Log.i(TAG, str, th);
    }

    /* renamed from: w */
    public static int m19w(String str) {
        return Log.w(TAG, str);
    }

    /* renamed from: w */
    public static int m20w(String str, Throwable th) {
        return Log.w(TAG, str, th);
    }

    /* renamed from: w */
    public static int m21w(Throwable th) {
        return Log.w(TAG, th);
    }

    /* renamed from: e */
    public static int m13e(String str) {
        return Log.w(TAG, str);
    }

    /* renamed from: e */
    public static int m14e(String str, Throwable th) {
        return Log.e(TAG, str, th);
    }
}
