// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.util;

import java.util.Collection;
import android.text.TextUtils;

public final class Preconditions
{
    public static void checkArgument(final boolean b, final String s) {
        if (b) {
            return;
        }
        throw new IllegalArgumentException(s);
    }
    
    public static String checkNotEmpty(final String s) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            return s;
        }
        throw new IllegalArgumentException("Must not be null or empty");
    }
    
    public static <T extends Collection<Y>, Y> T checkNotEmpty(final T t) {
        if (!t.isEmpty()) {
            return t;
        }
        throw new IllegalArgumentException("Must not be empty.");
    }
    
    public static <T> T checkNotNull(final T t) {
        return checkNotNull(t, "Argument must not be null");
    }
    
    public static <T> T checkNotNull(final T t, final String s) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(s);
    }
}
