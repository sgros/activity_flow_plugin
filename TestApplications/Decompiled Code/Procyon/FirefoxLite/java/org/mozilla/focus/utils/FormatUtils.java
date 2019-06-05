// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import java.text.DecimalFormat;

public class FormatUtils
{
    private static final DecimalFormat DF;
    
    static {
        DF = new DecimalFormat("0.0");
    }
    
    public static String getReadableStringFromFileSize(final long n) {
        if (n < 1048576L) {
            final StringBuilder sb = new StringBuilder();
            sb.append(FormatUtils.DF.format(n / 1024L));
            sb.append(" KB");
            return sb.toString();
        }
        if (n < 1073741824L) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(FormatUtils.DF.format(n / 1048576L));
            sb2.append(" MB");
            return sb2.toString();
        }
        if (n < 1099511627776L) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(FormatUtils.DF.format(n / 1073741824L));
            sb3.append(" GB");
            return sb3.toString();
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(FormatUtils.DF.format(n / 1099511627776L));
        sb4.append(" TB");
        return sb4.toString();
    }
}
