// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;

public final class RepeatModeUtil
{
    public static final int REPEAT_TOGGLE_MODE_ALL = 2;
    public static final int REPEAT_TOGGLE_MODE_NONE = 0;
    public static final int REPEAT_TOGGLE_MODE_ONE = 1;
    
    private RepeatModeUtil() {
    }
    
    public static int getNextRepeatMode(final int n, final int n2) {
        for (int i = 1; i <= 2; ++i) {
            final int n3 = (n + i) % 3;
            if (isRepeatModeEnabled(n3, n2)) {
                return n3;
            }
        }
        return n;
    }
    
    public static boolean isRepeatModeEnabled(final int n, final int n2) {
        final boolean b = true;
        final boolean b2 = true;
        boolean b3 = b;
        if (n != 0) {
            if (n != 1) {
                return n == 2 && (n2 & 0x2) != 0x0 && b2;
            }
            b3 = ((n2 & 0x1) != 0x0 && b);
        }
        return b3;
    }
    
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface RepeatToggleModes {
    }
}
