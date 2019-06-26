// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;
import android.text.TextUtils;

public final class Log
{
    public static final int LOG_LEVEL_ALL = 0;
    public static final int LOG_LEVEL_ERROR = 3;
    public static final int LOG_LEVEL_INFO = 1;
    public static final int LOG_LEVEL_OFF = Integer.MAX_VALUE;
    public static final int LOG_LEVEL_WARNING = 2;
    private static int logLevel = 0;
    private static boolean logStackTraces = true;
    
    private Log() {
    }
    
    private static String appendThrowableMessage(String string, final Throwable t) {
        if (t == null) {
            return string;
        }
        final String message = t.getMessage();
        if (!TextUtils.isEmpty((CharSequence)message)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(" - ");
            sb.append(message);
            string = sb.toString();
        }
        return string;
    }
    
    public static void d(final String s, final String s2) {
        if (Log.logLevel == 0) {
            android.util.Log.d(s, s2);
        }
    }
    
    public static void d(final String s, final String s2, final Throwable t) {
        if (!Log.logStackTraces) {
            d(s, appendThrowableMessage(s2, t));
        }
        if (Log.logLevel == 0) {
            android.util.Log.d(s, s2, t);
        }
    }
    
    public static void e(final String s, final String s2) {
        if (Log.logLevel <= 3) {
            android.util.Log.e(s, s2);
        }
    }
    
    public static void e(final String s, final String s2, final Throwable t) {
        if (!Log.logStackTraces) {
            e(s, appendThrowableMessage(s2, t));
        }
        if (Log.logLevel <= 3) {
            android.util.Log.e(s, s2, t);
        }
    }
    
    public static int getLogLevel() {
        return Log.logLevel;
    }
    
    public static void i(final String s, final String s2) {
        if (Log.logLevel <= 1) {
            android.util.Log.i(s, s2);
        }
    }
    
    public static void i(final String s, final String s2, final Throwable t) {
        if (!Log.logStackTraces) {
            i(s, appendThrowableMessage(s2, t));
        }
        if (Log.logLevel <= 1) {
            android.util.Log.i(s, s2, t);
        }
    }
    
    public static void setLogLevel(final int logLevel) {
        Log.logLevel = logLevel;
    }
    
    public static void setLogStackTraces(final boolean logStackTraces) {
        Log.logStackTraces = logStackTraces;
    }
    
    public static void w(final String s, final String s2) {
        if (Log.logLevel <= 2) {
            android.util.Log.w(s, s2);
        }
    }
    
    public static void w(final String s, final String s2, final Throwable t) {
        if (!Log.logStackTraces) {
            w(s, appendThrowableMessage(s2, t));
        }
        if (Log.logLevel <= 2) {
            android.util.Log.w(s, s2, t);
        }
    }
    
    public boolean getLogStackTraces() {
        return Log.logStackTraces;
    }
    
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @interface LogLevel {
    }
}
