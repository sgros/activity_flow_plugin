// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import android.text.TextUtils;
import android.os.Looper;

public final class Assertions
{
    private Assertions() {
    }
    
    public static void checkArgument(final boolean b) {
        if (b) {
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public static void checkArgument(final boolean b, final Object obj) {
        if (b) {
            return;
        }
        throw new IllegalArgumentException(String.valueOf(obj));
    }
    
    public static int checkIndex(final int n, final int n2, final int n3) {
        if (n >= n2 && n < n3) {
            return n;
        }
        throw new IndexOutOfBoundsException();
    }
    
    public static void checkMainThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return;
        }
        throw new IllegalStateException("Not in applications main thread");
    }
    
    @EnsuresNonNull({ "#1" })
    public static String checkNotEmpty(final String s) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            return s;
        }
        throw new IllegalArgumentException();
    }
    
    @EnsuresNonNull({ "#1" })
    public static String checkNotEmpty(final String s, final Object obj) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            return s;
        }
        throw new IllegalArgumentException(String.valueOf(obj));
    }
    
    @EnsuresNonNull({ "#1" })
    public static <T> T checkNotNull(final T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException();
    }
    
    @EnsuresNonNull({ "#1" })
    public static <T> T checkNotNull(final T t, final Object obj) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(String.valueOf(obj));
    }
    
    public static void checkState(final boolean b) {
        if (b) {
            return;
        }
        throw new IllegalStateException();
    }
    
    public static void checkState(final boolean b, final Object obj) {
        if (b) {
            return;
        }
        throw new IllegalStateException(String.valueOf(obj));
    }
}
