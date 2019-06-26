package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.util.Assertions;

public final class SinglePeriodTimeline extends Timeline {
   private static final Object UID = new Object();
   private final boolean isDynamic;
   private final boolean isSeekable;
   private final long periodDurationUs;
   private final long presentationStartTimeMs;
   private final Object tag;
   private final long windowDefaultStartPositionUs;
   private final long windowDurationUs;
   private final long windowPositionInPeriodUs;
   private final long windowStartTimeMs;

   public SinglePeriodTimeline(long var1, long var3, long var5, long var7, long var9, long var11, boolean var13, boolean var14, Object var15) {
      this.presentationStartTimeMs = var1;
      this.windowStartTimeMs = var3;
      this.periodDurationUs = var5;
      this.windowDurationUs = var7;
      this.windowPositionInPeriodUs = var9;
      this.windowDefaultStartPositionUs = var11;
      this.isSeekable = var13;
      this.isDynamic = var14;
      this.tag = var15;
   }

   public SinglePeriodTimeline(long var1, long var3, long var5, long var7, boolean var9, boolean var10, Object var11) {
      this(-9223372036854775807L, -9223372036854775807L, var1, var3, var5, var7, var9, var10, var11);
   }

   public SinglePeriodTimeline(long var1, boolean var3, boolean var4, Object var5) {
      this(var1, var1, 0L, 0L, var3, var4, var5);
   }

   public int getIndexOfPeriod(Object var1) {
      byte var2;
      if (UID.equals(var1)) {
         var2 = 0;
      } else {
         var2 = -1;
      }

      return var2;
   }

   public Timeline.Period getPeriod(int var1, Timeline.Period var2, boolean var3) {
      Assertions.checkIndex(var1, 0, 1);
      Object var4;
      if (var3) {
         var4 = UID;
      } else {
         var4 = null;
      }

      var2.set((Object)null, var4, 0, this.periodDurationUs, -this.windowPositionInPeriodUs);
      return var2;
   }

   public int getPeriodCount() {
      return 1;
   }

   public Object getUidOfPeriod(int var1) {
      Assertions.checkIndex(var1, 0, 1);
      return UID;
   }

   public Timeline.Window getWindow(int var1, Timeline.Window var2, boolean var3, long var4) {
      Assertions.checkIndex(var1, 0, 1);
      Object var6;
      if (var3) {
         var6 = this.tag;
      } else {
         var6 = null;
      }

      label24: {
         label23: {
            long var7 = this.windowDefaultStartPositionUs;
            long var9 = var7;
            if (this.isDynamic) {
               var9 = var7;
               if (var4 != 0L) {
                  long var11 = this.windowDurationUs;
                  if (var11 == -9223372036854775807L) {
                     break label23;
                  }

                  var4 += var7;
                  var9 = var4;
                  if (var4 > var11) {
                     break label23;
                  }
               }
            }

            var4 = var9;
            break label24;
         }

         var4 = -9223372036854775807L;
      }

      var2.set(var6, this.presentationStartTimeMs, this.windowStartTimeMs, this.isSeekable, this.isDynamic, var4, this.windowDurationUs, 0, 0, this.windowPositionInPeriodUs);
      return var2;
   }

   public int getWindowCount() {
      return 1;
   }
}
