package com.googlecode.mp4parser.util;

import java.util.Date;

public class DateHelper {
   public static long convert(Date var0) {
      return var0.getTime() / 1000L + 2082844800L;
   }

   public static Date convert(long var0) {
      return new Date((var0 - 2082844800L) * 1000L);
   }
}
