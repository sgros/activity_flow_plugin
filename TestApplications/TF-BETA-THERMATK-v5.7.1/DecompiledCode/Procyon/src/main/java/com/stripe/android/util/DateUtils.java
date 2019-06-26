// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.util;

import java.util.Locale;
import java.util.Calendar;
import com.stripe.android.time.Clock;

public class DateUtils
{
    public static boolean hasMonthPassed(final int n, final int n2) {
        final boolean hasYearPassed = hasYearPassed(n);
        boolean b = true;
        if (hasYearPassed) {
            return true;
        }
        final Calendar calendarInstance = Clock.getCalendarInstance();
        if (normalizeYear(n) != calendarInstance.get(1) || n2 >= calendarInstance.get(2) + 1) {
            b = false;
        }
        return b;
    }
    
    public static boolean hasYearPassed(int normalizeYear) {
        normalizeYear = normalizeYear(normalizeYear);
        final Calendar calendarInstance = Clock.getCalendarInstance();
        boolean b = true;
        if (normalizeYear >= calendarInstance.get(1)) {
            b = false;
        }
        return b;
    }
    
    private static int normalizeYear(final int i) {
        int int1 = i;
        if (i < 100 && (int1 = i) >= 0) {
            final String value = String.valueOf(Clock.getCalendarInstance().get(1));
            int1 = Integer.parseInt(String.format(Locale.US, "%s%02d", value.substring(0, value.length() - 2), i));
        }
        return int1;
    }
}
