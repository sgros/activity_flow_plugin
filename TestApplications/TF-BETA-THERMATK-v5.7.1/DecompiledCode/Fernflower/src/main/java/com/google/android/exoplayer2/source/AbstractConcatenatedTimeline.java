package com.google.android.exoplayer2.source;

import android.util.Pair;
import com.google.android.exoplayer2.Timeline;

abstract class AbstractConcatenatedTimeline extends Timeline {
   private final int childCount;
   private final boolean isAtomic;
   private final ShuffleOrder shuffleOrder;

   public AbstractConcatenatedTimeline(boolean var1, ShuffleOrder var2) {
      this.isAtomic = var1;
      this.shuffleOrder = var2;
      this.childCount = var2.getLength();
   }

   public static Object getChildPeriodUidFromConcatenatedUid(Object var0) {
      return ((Pair)var0).second;
   }

   public static Object getChildTimelineUidFromConcatenatedUid(Object var0) {
      return ((Pair)var0).first;
   }

   public static Object getConcatenatedUid(Object var0, Object var1) {
      return Pair.create(var0, var1);
   }

   private int getNextChildIndex(int var1, boolean var2) {
      if (var2) {
         var1 = this.shuffleOrder.getNextIndex(var1);
      } else if (var1 < this.childCount - 1) {
         ++var1;
      } else {
         var1 = -1;
      }

      return var1;
   }

   private int getPreviousChildIndex(int var1, boolean var2) {
      if (var2) {
         var1 = this.shuffleOrder.getPreviousIndex(var1);
      } else if (var1 > 0) {
         --var1;
      } else {
         var1 = -1;
      }

      return var1;
   }

   protected abstract int getChildIndexByChildUid(Object var1);

   protected abstract int getChildIndexByPeriodIndex(int var1);

   protected abstract int getChildIndexByWindowIndex(int var1);

   protected abstract Object getChildUidByChildIndex(int var1);

   protected abstract int getFirstPeriodIndexByChildIndex(int var1);

   public int getFirstWindowIndex(boolean var1) {
      if (this.childCount == 0) {
         return -1;
      } else {
         boolean var2 = this.isAtomic;
         int var3 = 0;
         if (var2) {
            var1 = false;
         }

         if (var1) {
            var3 = this.shuffleOrder.getFirstIndex();
         }

         int var4;
         do {
            if (!this.getTimelineByChildIndex(var3).isEmpty()) {
               return this.getFirstWindowIndexByChildIndex(var3) + this.getTimelineByChildIndex(var3).getFirstWindowIndex(var1);
            }

            var4 = this.getNextChildIndex(var3, var1);
            var3 = var4;
         } while(var4 != -1);

         return -1;
      }
   }

   protected abstract int getFirstWindowIndexByChildIndex(int var1);

   public final int getIndexOfPeriod(Object var1) {
      boolean var2 = var1 instanceof Pair;
      int var3 = -1;
      if (!var2) {
         return -1;
      } else {
         Object var4 = getChildTimelineUidFromConcatenatedUid(var1);
         var1 = getChildPeriodUidFromConcatenatedUid(var1);
         int var5 = this.getChildIndexByChildUid(var4);
         if (var5 == -1) {
            return -1;
         } else {
            int var6 = this.getTimelineByChildIndex(var5).getIndexOfPeriod(var1);
            if (var6 != -1) {
               var3 = this.getFirstPeriodIndexByChildIndex(var5) + var6;
            }

            return var3;
         }
      }
   }

   public int getLastWindowIndex(boolean var1) {
      if (this.childCount == 0) {
         return -1;
      } else {
         if (this.isAtomic) {
            var1 = false;
         }

         int var2;
         if (var1) {
            var2 = this.shuffleOrder.getLastIndex();
         } else {
            var2 = this.childCount - 1;
         }

         int var3;
         do {
            if (!this.getTimelineByChildIndex(var2).isEmpty()) {
               return this.getFirstWindowIndexByChildIndex(var2) + this.getTimelineByChildIndex(var2).getLastWindowIndex(var1);
            }

            var3 = this.getPreviousChildIndex(var2, var1);
            var2 = var3;
         } while(var3 != -1);

         return -1;
      }
   }

   public int getNextWindowIndex(int var1, int var2, boolean var3) {
      boolean var4 = this.isAtomic;
      byte var5 = 0;
      int var6 = var2;
      if (var4) {
         var6 = var2;
         if (var2 == 1) {
            var6 = 2;
         }

         var3 = false;
      }

      int var7 = this.getChildIndexByWindowIndex(var1);
      int var8 = this.getFirstWindowIndexByChildIndex(var7);
      Timeline var9 = this.getTimelineByChildIndex(var7);
      if (var6 == 2) {
         var2 = var5;
      } else {
         var2 = var6;
      }

      var1 = var9.getNextWindowIndex(var1 - var8, var2, var3);
      if (var1 != -1) {
         return var8 + var1;
      } else {
         for(var1 = this.getNextChildIndex(var7, var3); var1 != -1 && this.getTimelineByChildIndex(var1).isEmpty(); var1 = this.getNextChildIndex(var1, var3)) {
         }

         if (var1 != -1) {
            return this.getFirstWindowIndexByChildIndex(var1) + this.getTimelineByChildIndex(var1).getFirstWindowIndex(var3);
         } else {
            return var6 == 2 ? this.getFirstWindowIndex(var3) : -1;
         }
      }
   }

   public final Timeline.Period getPeriod(int var1, Timeline.Period var2, boolean var3) {
      int var4 = this.getChildIndexByPeriodIndex(var1);
      int var5 = this.getFirstWindowIndexByChildIndex(var4);
      int var6 = this.getFirstPeriodIndexByChildIndex(var4);
      this.getTimelineByChildIndex(var4).getPeriod(var1 - var6, var2, var3);
      var2.windowIndex += var5;
      if (var3) {
         var2.uid = getConcatenatedUid(this.getChildUidByChildIndex(var4), var2.uid);
      }

      return var2;
   }

   public final Timeline.Period getPeriodByUid(Object var1, Timeline.Period var2) {
      Object var3 = getChildTimelineUidFromConcatenatedUid(var1);
      Object var4 = getChildPeriodUidFromConcatenatedUid(var1);
      int var5 = this.getChildIndexByChildUid(var3);
      int var6 = this.getFirstWindowIndexByChildIndex(var5);
      this.getTimelineByChildIndex(var5).getPeriodByUid(var4, var2);
      var2.windowIndex += var6;
      var2.uid = var1;
      return var2;
   }

   protected abstract Timeline getTimelineByChildIndex(int var1);

   public final Object getUidOfPeriod(int var1) {
      int var2 = this.getChildIndexByPeriodIndex(var1);
      int var3 = this.getFirstPeriodIndexByChildIndex(var2);
      Object var4 = this.getTimelineByChildIndex(var2).getUidOfPeriod(var1 - var3);
      return getConcatenatedUid(this.getChildUidByChildIndex(var2), var4);
   }

   public final Timeline.Window getWindow(int var1, Timeline.Window var2, boolean var3, long var4) {
      int var6 = this.getChildIndexByWindowIndex(var1);
      int var7 = this.getFirstWindowIndexByChildIndex(var6);
      int var8 = this.getFirstPeriodIndexByChildIndex(var6);
      this.getTimelineByChildIndex(var6).getWindow(var1 - var7, var2, var3, var4);
      var2.firstPeriodIndex += var8;
      var2.lastPeriodIndex += var8;
      return var2;
   }
}
