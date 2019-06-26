// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser.util;

public class CastUtils
{
    public static int l2i(final long lng) {
        if (lng <= 2147483647L && lng >= -2147483648L) {
            return (int)lng;
        }
        final StringBuilder sb = new StringBuilder("A cast to int has gone wrong. Please contact the mp4parser discussion group (");
        sb.append(lng);
        sb.append(")");
        throw new RuntimeException(sb.toString());
    }
}
