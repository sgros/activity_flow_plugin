package com.google.android.exoplayer2;

import com.google.android.exoplayer2.util.Assertions;

public final class SeekParameters {
   public static final SeekParameters CLOSEST_SYNC = new SeekParameters(Long.MAX_VALUE, Long.MAX_VALUE);
   public static final SeekParameters DEFAULT;
   public static final SeekParameters EXACT = new SeekParameters(0L, 0L);
   public static final SeekParameters NEXT_SYNC = new SeekParameters(0L, Long.MAX_VALUE);
   public static final SeekParameters PREVIOUS_SYNC = new SeekParameters(Long.MAX_VALUE, 0L);
   public final long toleranceAfterUs;
   public final long toleranceBeforeUs;

   static {
      DEFAULT = EXACT;
   }

   public SeekParameters(long var1, long var3) {
      boolean var5 = true;
      boolean var6;
      if (var1 >= 0L) {
         var6 = true;
      } else {
         var6 = false;
      }

      Assertions.checkArgument(var6);
      if (var3 >= 0L) {
         var6 = var5;
      } else {
         var6 = false;
      }

      Assertions.checkArgument(var6);
      this.toleranceBeforeUs = var1;
      this.toleranceAfterUs = var3;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && SeekParameters.class == var1.getClass()) {
         SeekParameters var3 = (SeekParameters)var1;
         if (this.toleranceBeforeUs != var3.toleranceBeforeUs || this.toleranceAfterUs != var3.toleranceAfterUs) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (int)this.toleranceBeforeUs * 31 + (int)this.toleranceAfterUs;
   }
}
