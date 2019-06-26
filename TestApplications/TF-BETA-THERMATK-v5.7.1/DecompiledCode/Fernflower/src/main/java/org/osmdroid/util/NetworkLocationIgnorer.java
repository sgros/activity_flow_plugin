package org.osmdroid.util;

import org.osmdroid.config.Configuration;

public class NetworkLocationIgnorer {
   private long mLastGps = 0L;

   public boolean shouldIgnore(String var1, long var2) {
      if ("gps".equals(var1)) {
         this.mLastGps = var2;
      } else if (var2 < this.mLastGps + Configuration.getInstance().getGpsWaitTime()) {
         return true;
      }

      return false;
   }
}
