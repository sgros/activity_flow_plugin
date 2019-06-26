package com.google.android.exoplayer2.util;

public final class TimestampAdjuster {
   public static final long DO_NOT_OFFSET = Long.MAX_VALUE;
   private static final long MAX_PTS_PLUS_ONE = 8589934592L;
   private long firstSampleTimestampUs;
   private volatile long lastSampleTimestampUs = -9223372036854775807L;
   private long timestampOffsetUs;

   public TimestampAdjuster(long var1) {
      this.setFirstSampleTimestampUs(var1);
   }

   public static long ptsToUs(long var0) {
      return var0 * 1000000L / 90000L;
   }

   public static long usToPts(long var0) {
      return var0 * 90000L / 1000000L;
   }

   public long adjustSampleTimestamp(long param1) {
      // $FF: Couldn't be decompiled
   }

   public long adjustTsTimestamp(long var1) {
      if (var1 == -9223372036854775807L) {
         return -9223372036854775807L;
      } else {
         long var3 = var1;
         if (this.lastSampleTimestampUs != -9223372036854775807L) {
            long var5 = usToPts(this.lastSampleTimestampUs);
            var3 = (4294967296L + var5) / 8589934592L;
            long var7 = (var3 - 1L) * 8589934592L + var1;
            var1 += var3 * 8589934592L;
            var3 = var1;
            if (Math.abs(var7 - var5) < Math.abs(var1 - var5)) {
               var3 = var7;
            }
         }

         return this.adjustSampleTimestamp(ptsToUs(var3));
      }
   }

   public long getFirstSampleTimestampUs() {
      return this.firstSampleTimestampUs;
   }

   public long getLastAdjustedTimestampUs() {
      long var1 = this.lastSampleTimestampUs;
      long var3 = -9223372036854775807L;
      if (var1 != -9223372036854775807L) {
         var3 = this.lastSampleTimestampUs;
         var3 += this.timestampOffsetUs;
      } else {
         var1 = this.firstSampleTimestampUs;
         if (var1 != Long.MAX_VALUE) {
            var3 = var1;
         }
      }

      return var3;
   }

   public long getTimestampOffsetUs() {
      long var1 = this.firstSampleTimestampUs;
      long var3 = -9223372036854775807L;
      if (var1 == Long.MAX_VALUE) {
         var3 = 0L;
      } else if (this.lastSampleTimestampUs != -9223372036854775807L) {
         var3 = this.timestampOffsetUs;
      }

      return var3;
   }

   public void reset() {
      this.lastSampleTimestampUs = -9223372036854775807L;
   }

   public void setFirstSampleTimestampUs(long var1) {
      synchronized(this){}

      Throwable var10000;
      label80: {
         boolean var10001;
         boolean var3;
         label79: {
            label78: {
               try {
                  if (this.lastSampleTimestampUs == -9223372036854775807L) {
                     break label78;
                  }
               } catch (Throwable var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label80;
               }

               var3 = false;
               break label79;
            }

            var3 = true;
         }

         label72:
         try {
            Assertions.checkState(var3);
            this.firstSampleTimestampUs = var1;
            return;
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label72;
         }
      }

      Throwable var4 = var10000;
      throw var4;
   }

   public void waitUntilInitialized() throws InterruptedException {
      synchronized(this){}

      while(true) {
         boolean var3 = false;

         try {
            var3 = true;
            if (this.lastSampleTimestampUs != -9223372036854775807L) {
               var3 = false;
               return;
            }

            this.wait();
            var3 = false;
         } finally {
            if (var3) {
               ;
            }
         }
      }
   }
}
