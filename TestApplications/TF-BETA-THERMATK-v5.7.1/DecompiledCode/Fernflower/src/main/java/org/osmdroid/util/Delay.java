package org.osmdroid.util;

public class Delay {
   private long mDuration;
   private final long[] mDurations;
   private int mIndex;
   private long mNextTime;

   public Delay(long[] var1) {
      if (var1 != null && var1.length != 0) {
         this.mDurations = var1;
         this.next();
      } else {
         throw new IllegalArgumentException();
      }
   }

   private long now() {
      return System.nanoTime() / 1000000L;
   }

   public long next() {
      long[] var1 = this.mDurations;
      long var2;
      if (var1 == null) {
         var2 = this.mDuration;
      } else {
         int var4 = this.mIndex;
         var2 = var1[var4];
         if (var4 < var1.length - 1) {
            this.mIndex = var4 + 1;
         }
      }

      this.mNextTime = this.now() + var2;
      return var2;
   }

   public boolean shouldWait() {
      boolean var1;
      if (this.now() < this.mNextTime) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
