package com.stripe.android.util;

import com.stripe.android.time.Clock;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {
   public static boolean hasMonthPassed(int var0, int var1) {
      boolean var2 = hasYearPassed(var0);
      boolean var3 = true;
      if (var2) {
         return true;
      } else {
         Calendar var4 = Clock.getCalendarInstance();
         if (normalizeYear(var0) != var4.get(1) || var1 >= var4.get(2) + 1) {
            var3 = false;
         }

         return var3;
      }
   }

   public static boolean hasYearPassed(int var0) {
      var0 = normalizeYear(var0);
      Calendar var1 = Clock.getCalendarInstance();
      boolean var2 = true;
      if (var0 >= var1.get(1)) {
         var2 = false;
      }

      return var2;
   }

   private static int normalizeYear(int var0) {
      int var1 = var0;
      if (var0 < 100) {
         var1 = var0;
         if (var0 >= 0) {
            String var2 = String.valueOf(Clock.getCalendarInstance().get(1));
            var2 = var2.substring(0, var2.length() - 2);
            var1 = Integer.parseInt(String.format(Locale.US, "%s%02d", var2, var0));
         }
      }

      return var1;
   }
}
