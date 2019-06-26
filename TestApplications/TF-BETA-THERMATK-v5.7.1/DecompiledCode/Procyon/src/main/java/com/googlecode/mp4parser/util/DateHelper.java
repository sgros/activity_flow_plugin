// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser.util;

import java.util.Date;

public class DateHelper
{
    public static long convert(final Date date) {
        return date.getTime() / 1000L + 2082844800L;
    }
    
    public static Date convert(final long n) {
        return new Date((n - 2082844800L) * 1000L);
    }
}
