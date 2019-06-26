package com.stripe.android.time;

import java.util.Calendar;

public class Clock {
   private static Clock instance;
   protected Calendar calendarInstance;

   private Calendar _calendarInstance() {
      Calendar var1 = this.calendarInstance;
      if (var1 != null) {
         var1 = (Calendar)var1.clone();
      } else {
         var1 = Calendar.getInstance();
      }

      return var1;
   }

   public static Calendar getCalendarInstance() {
      return getInstance()._calendarInstance();
   }

   protected static Clock getInstance() {
      if (instance == null) {
         instance = new Clock();
      }

      return instance;
   }
}
