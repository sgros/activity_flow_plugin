package com.google.android.exoplayer2;

import android.util.Pair;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.google.android.exoplayer2.util.Assertions;

public abstract class Timeline {
   public static final Timeline EMPTY = new Timeline() {
      public int getIndexOfPeriod(Object var1) {
         return -1;
      }

      public Timeline.Period getPeriod(int var1, Timeline.Period var2, boolean var3) {
         throw new IndexOutOfBoundsException();
      }

      public int getPeriodCount() {
         return 0;
      }

      public Object getUidOfPeriod(int var1) {
         throw new IndexOutOfBoundsException();
      }

      public Timeline.Window getWindow(int var1, Timeline.Window var2, boolean var3, long var4) {
         throw new IndexOutOfBoundsException();
      }

      public int getWindowCount() {
         return 0;
      }
   };

   public int getFirstWindowIndex(boolean var1) {
      byte var2;
      if (this.isEmpty()) {
         var2 = -1;
      } else {
         var2 = 0;
      }

      return var2;
   }

   public abstract int getIndexOfPeriod(Object var1);

   public int getLastWindowIndex(boolean var1) {
      int var2;
      if (this.isEmpty()) {
         var2 = -1;
      } else {
         var2 = this.getWindowCount() - 1;
      }

      return var2;
   }

   public final int getNextPeriodIndex(int var1, Timeline.Period var2, Timeline.Window var3, int var4, boolean var5) {
      int var6 = this.getPeriod(var1, var2).windowIndex;
      if (this.getWindow(var6, var3).lastPeriodIndex == var1) {
         var1 = this.getNextWindowIndex(var6, var4, var5);
         return var1 == -1 ? -1 : this.getWindow(var1, var3).firstPeriodIndex;
      } else {
         return var1 + 1;
      }
   }

   public int getNextWindowIndex(int var1, int var2, boolean var3) {
      if (var2 != 0) {
         if (var2 != 1) {
            if (var2 == 2) {
               if (var1 == this.getLastWindowIndex(var3)) {
                  var1 = this.getFirstWindowIndex(var3);
               } else {
                  ++var1;
               }

               return var1;
            } else {
               throw new IllegalStateException();
            }
         } else {
            return var1;
         }
      } else {
         if (var1 == this.getLastWindowIndex(var3)) {
            var1 = -1;
         } else {
            ++var1;
         }

         return var1;
      }
   }

   public final Timeline.Period getPeriod(int var1, Timeline.Period var2) {
      return this.getPeriod(var1, var2, false);
   }

   public abstract Timeline.Period getPeriod(int var1, Timeline.Period var2, boolean var3);

   public Timeline.Period getPeriodByUid(Object var1, Timeline.Period var2) {
      return this.getPeriod(this.getIndexOfPeriod(var1), var2, true);
   }

   public abstract int getPeriodCount();

   public final Pair getPeriodPosition(Timeline.Window var1, Timeline.Period var2, int var3, long var4) {
      Pair var6 = this.getPeriodPosition(var1, var2, var3, var4, 0L);
      Assertions.checkNotNull(var6);
      return (Pair)var6;
   }

   public final Pair getPeriodPosition(Timeline.Window var1, Timeline.Period var2, int var3, long var4, long var6) {
      Assertions.checkIndex(var3, 0, this.getWindowCount());
      this.getWindow(var3, var1, false, var6);
      var6 = var4;
      if (var4 == -9223372036854775807L) {
         var4 = var1.getDefaultPositionUs();
         var6 = var4;
         if (var4 == -9223372036854775807L) {
            return null;
         }
      }

      var3 = var1.firstPeriodIndex;
      var6 += var1.getPositionInFirstPeriodUs();

      for(var4 = this.getPeriod(var3, var2, true).getDurationUs(); var4 != -9223372036854775807L && var6 >= var4 && var3 < var1.lastPeriodIndex; var4 = this.getPeriod(var3, var2, true).getDurationUs()) {
         var6 -= var4;
         ++var3;
      }

      Object var8 = var2.uid;
      Assertions.checkNotNull(var8);
      return Pair.create(var8, var6);
   }

   public abstract Object getUidOfPeriod(int var1);

   public final Timeline.Window getWindow(int var1, Timeline.Window var2) {
      return this.getWindow(var1, var2, false);
   }

   public final Timeline.Window getWindow(int var1, Timeline.Window var2, boolean var3) {
      return this.getWindow(var1, var2, var3, 0L);
   }

   public abstract Timeline.Window getWindow(int var1, Timeline.Window var2, boolean var3, long var4);

   public abstract int getWindowCount();

   public final boolean isEmpty() {
      boolean var1;
      if (this.getWindowCount() == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final boolean isLastPeriod(int var1, Timeline.Period var2, Timeline.Window var3, int var4, boolean var5) {
      if (this.getNextPeriodIndex(var1, var2, var3, var4, var5) == -1) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   public static final class Period {
      private AdPlaybackState adPlaybackState;
      public long durationUs;
      public Object id;
      private long positionInWindowUs;
      public Object uid;
      public int windowIndex;

      public Period() {
         this.adPlaybackState = AdPlaybackState.NONE;
      }

      public int getAdCountInAdGroup(int var1) {
         return this.adPlaybackState.adGroups[var1].count;
      }

      public long getAdDurationUs(int var1, int var2) {
         AdPlaybackState.AdGroup var3 = this.adPlaybackState.adGroups[var1];
         long var4;
         if (var3.count != -1) {
            var4 = var3.durationsUs[var2];
         } else {
            var4 = -9223372036854775807L;
         }

         return var4;
      }

      public int getAdGroupCount() {
         return this.adPlaybackState.adGroupCount;
      }

      public int getAdGroupIndexAfterPositionUs(long var1) {
         return this.adPlaybackState.getAdGroupIndexAfterPositionUs(var1, this.durationUs);
      }

      public int getAdGroupIndexForPositionUs(long var1) {
         return this.adPlaybackState.getAdGroupIndexForPositionUs(var1);
      }

      public long getAdGroupTimeUs(int var1) {
         return this.adPlaybackState.adGroupTimesUs[var1];
      }

      public long getAdResumePositionUs() {
         return this.adPlaybackState.adResumePositionUs;
      }

      public long getDurationMs() {
         return C.usToMs(this.durationUs);
      }

      public long getDurationUs() {
         return this.durationUs;
      }

      public int getFirstAdIndexToPlay(int var1) {
         return this.adPlaybackState.adGroups[var1].getFirstAdIndexToPlay();
      }

      public int getNextAdIndexToPlay(int var1, int var2) {
         return this.adPlaybackState.adGroups[var1].getNextAdIndexToPlay(var2);
      }

      public long getPositionInWindowMs() {
         return C.usToMs(this.positionInWindowUs);
      }

      public boolean isAdAvailable(int var1, int var2) {
         AdPlaybackState.AdGroup var3 = this.adPlaybackState.adGroups[var1];
         boolean var4;
         if (var3.count != -1 && var3.states[var2] != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         return var4;
      }

      public Timeline.Period set(Object var1, Object var2, int var3, long var4, long var6) {
         this.set(var1, var2, var3, var4, var6, AdPlaybackState.NONE);
         return this;
      }

      public Timeline.Period set(Object var1, Object var2, int var3, long var4, long var6, AdPlaybackState var8) {
         this.id = var1;
         this.uid = var2;
         this.windowIndex = var3;
         this.durationUs = var4;
         this.positionInWindowUs = var6;
         this.adPlaybackState = var8;
         return this;
      }
   }

   public static final class Window {
      public long defaultPositionUs;
      public long durationUs;
      public int firstPeriodIndex;
      public boolean isDynamic;
      public boolean isSeekable;
      public int lastPeriodIndex;
      public long positionInFirstPeriodUs;
      public long presentationStartTimeMs;
      public Object tag;
      public long windowStartTimeMs;

      public long getDefaultPositionMs() {
         return C.usToMs(this.defaultPositionUs);
      }

      public long getDefaultPositionUs() {
         return this.defaultPositionUs;
      }

      public long getDurationMs() {
         return C.usToMs(this.durationUs);
      }

      public long getPositionInFirstPeriodUs() {
         return this.positionInFirstPeriodUs;
      }

      public Timeline.Window set(Object var1, long var2, long var4, boolean var6, boolean var7, long var8, long var10, int var12, int var13, long var14) {
         this.tag = var1;
         this.presentationStartTimeMs = var2;
         this.windowStartTimeMs = var4;
         this.isSeekable = var6;
         this.isDynamic = var7;
         this.defaultPositionUs = var8;
         this.durationUs = var10;
         this.firstPeriodIndex = var12;
         this.lastPeriodIndex = var13;
         this.positionInFirstPeriodUs = var14;
         return this;
      }
   }
}
