package com.stripe.android.util;

import com.stripe.android.time.Clock;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {
    public static boolean hasYearPassed(int i) {
        if (normalizeYear(i) < Clock.getCalendarInstance().get(1)) {
            return true;
        }
        return false;
    }

    public static boolean hasMonthPassed(int i, int i2) {
        boolean z = true;
        if (hasYearPassed(i)) {
            return true;
        }
        Calendar calendarInstance = Clock.getCalendarInstance();
        if (normalizeYear(i) != calendarInstance.get(1) || i2 >= calendarInstance.get(2) + 1) {
            z = false;
        }
        return z;
    }

    private static int normalizeYear(int i) {
        if (i >= 100 || i < 0) {
            return i;
        }
        String valueOf = String.valueOf(Clock.getCalendarInstance().get(1));
        valueOf = valueOf.substring(0, valueOf.length() - 2);
        return Integer.parseInt(String.format(Locale.US, "%s%02d", new Object[]{valueOf, Integer.valueOf(i)}));
    }
}
