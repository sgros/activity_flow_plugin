// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.util;

public class StringUtils
{
    public static String safeSubstring(final String s, final int b, final int a) {
        return s.substring(Math.max(0, b), Math.min(a, s.length()));
    }
}
