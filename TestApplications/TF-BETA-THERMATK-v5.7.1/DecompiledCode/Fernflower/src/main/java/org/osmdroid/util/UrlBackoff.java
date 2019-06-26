package org.osmdroid.util;

import java.util.HashMap;
import java.util.Map;

public class UrlBackoff {
   private static final long[] mExponentialBackoffDurationInMillisDefault = new long[]{5000L, 15000L, 60000L, 120000L, 300000L};
   private final Map mDelays;
   private long[] mExponentialBackoffDurationInMillis;

   public UrlBackoff() {
      this.mExponentialBackoffDurationInMillis = mExponentialBackoffDurationInMillisDefault;
      this.mDelays = new HashMap();
   }

   public void next(String param1) {
      // $FF: Couldn't be decompiled
   }

   public Delay remove(String param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean shouldWait(String param1) {
      // $FF: Couldn't be decompiled
   }
}
